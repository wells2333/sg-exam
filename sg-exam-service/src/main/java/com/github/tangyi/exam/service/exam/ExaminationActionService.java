package com.github.tangyi.exam.service.exam;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NetUtil;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.*;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.api.exam.service.IExaminationActionService;
import com.github.tangyi.api.exam.thread.IExecutorHolder;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.service.IIdentifyService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.exam.enums.ExaminationType;
import com.github.tangyi.exam.handler.HandlerFactory;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.RankInfoService;
import com.github.tangyi.exam.service.answer.AnswerService;
import com.github.tangyi.exam.service.fav.ExamFavoritesService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExamUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExaminationActionService implements IExaminationActionService {

	private final PlatformTransactionManager txManager;
	private final IIdentifyService identifyService;
	private final ExaminationService examinationService;
	private final ExaminationSubjectService examinationSubjectService;
	private final ExamRecordService examRecordService;
	private final SubjectsService subjectsService;
	private final AnswerService answerService;
	private final RankInfoService rankInfoService;
	private final IExecutorHolder executorHolder;
	private final ExamFavoritesService examFavoritesService;

	public ExaminationActionService(PlatformTransactionManager txManager, IIdentifyService identifyService,
			ExaminationService examinationService, ExaminationSubjectService examinationSubjectService,
			ExamRecordService examRecordService, SubjectsService subjectsService, AnswerService answerService,
			RankInfoService rankInfoService, IExecutorHolder executorHolder,
			ExamFavoritesService examFavoritesService) {
		this.txManager = txManager;
		this.identifyService = identifyService;
		this.examinationService = examinationService;
		this.examinationSubjectService = examinationSubjectService;
		this.examRecordService = examRecordService;
		this.subjectsService = subjectsService;
		this.answerService = answerService;
		this.rankInfoService = rankInfoService;
		this.executorHolder = executorHolder;
		this.examFavoritesService = examFavoritesService;
	}

	@Override
	@Transactional
	public StartExamDto start(ExaminationRecord examRecord) {
		SgPreconditions.checkNull(examRecord.getExaminationId(), "The examination id cannot be null.");
		SgPreconditions.checkNull(examRecord.getUserId(), "The user id cannot be null.");
		return this.start(examRecord.getUserId(), SysUtil.getUser(), examRecord.getExaminationId(),
				SysUtil.getTenantCode());
	}

	@Override
	public StartExamDto start(Long userId, String identifier, Long examinationId, String tenantCode) {
		StartExamDto dto = this.prepareStart(userId, examinationId);
		SubjectDto sDto = dto.getSubjectDto();
		Preconditions.checkNotNull(sDto);
		dto.setTotal(sDto.getTotal());

		ExaminationRecord record = new ExaminationRecord();
		record.setCommonValue(identifier, tenantCode);
		record.setUserId(userId);
		record.setType(dto.getExamination().getType());
		record.setExaminationId(examinationId);
		record.setStartTime(record.getCreateTime());
		// 默认未提交状态
		record.setSubmitStatus(SubmitStatusEnum.NOT_SUBMITTED.getValue());
		dto.setExamRecord(record);

		// 手动开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = txManager.getTransaction(def);
		ListeningExecutorService exec = executorHolder.getExamExecutor();
		try {
			long startNs = System.nanoTime();
			examRecordService.insert(record);
			// 创建第一题的答题
			answerService.save(this.initAnswer(identifier, tenantCode, sDto, record.getId()));
			// 提交事务
			txManager.commit(status);
			// 异步增加考试次数
			CompletableFuture.runAsync(() -> examFavoritesService.incrStartCount(examinationId), exec);
			long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
			log.info("Start examination id: {}, identifier: {}, userId: {}, recordId: {}, took: {}ms", examinationId,
					identifier, userId, record.getId(), tookMs);
			return dto;
		} catch (Exception e) {
			log.error("Failed to commit start exam transaction.", e);
			// 回滚事务
			txManager.rollback(status);
		}
		return null;
	}

	@Override
	@Transactional
	public StartExamDto anonymousUserStart(Long examinationId, String identifier) {
		String tenantCode = SysUtil.getTenantCode();
		SgPreconditions.checkNull(tenantCode, "tenantCode must not be null");
		SgPreconditions.checkNull(examinationId, "examinationId must not be null");
		SgPreconditions.checkNull(identifier, "identifier must not be null");
		// 查询用户信息
		Long userId = null;
		if (Validator.isIpv4(identifier)) {
			// ipv4 直接转成 long
			userId = NetUtil.ipv4ToLong(identifier);
			log.info("Anonymous user start exam, examinationId: {}, ipv4: {}, userId: {}", examinationId, identifier,
					userId);
		} else if (Validator.isIpv6(identifier)) {
			// ipv6 使用 SnowFlakeId
			userId = SnowFlakeId.newId();
			log.info("Anonymous user start exam, examinationId: {}, ipv6: {}, userId: {}", examinationId, identifier,
					userId);
		} else {
			UserVo vo = this.identifyService.findUserByIdentifier(IdentityType.PASSWORD.getValue(), identifier,
					tenantCode);
			if (vo != null) {
				userId = vo.getId();
			}
		}
		return this.start(userId, identifier, examinationId, tenantCode);
	}

	@Override
	@Transactional
	public Boolean submit(Long recordId, String operator, String tenantCode) {
		List<Answer> answerList = this.answerService.findListByExamRecordId(recordId);
		if (CollectionUtils.isEmpty(answerList)) {
			return Boolean.FALSE;
		}

		ExaminationRecord record = this.examRecordService.get(recordId);
		Long[] ids = answerList.stream().map(Answer::getSubjectId).toArray(Long[]::new);
		Map<Integer, List<Answer>> distinct = this.distinctAnswer(ids, answerList);
		distinct.remove(5);// 去掉材料题
		HandlerFactory.Result result = HandlerFactory.handleAll(distinct);
		// 记录总分、正确题目数、错误题目数
		record.setScore(result.getScore());
		record.setCorrectNumber(result.getCorrectNum());
		record.setInCorrectNumber(result.getInCorrectNum());
		// 更新答题状态
		List<Answer> updates = distinct.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
		this.answerService.batchUpdate(updates);
		// 更新状态为统计完成，否则需要阅卷完成后才更改统计状态
		SubmitStatusEnum submitStatus = result.isHasHumanJudgeSubject() ?
				SubmitStatusEnum.SUBMITTED :
				SubmitStatusEnum.CALCULATED;
		record.setSubmitStatus(submitStatus.getValue());
		// 保存成绩
		record.setCommonValue(operator, tenantCode);
		record.setId(recordId);
		record.setEndTime(record.getCreateTime());
		this.examRecordService.update(record);
		// 更新排名数据
		this.rankInfoService.updateRank(record);
		return Boolean.TRUE;
	}

	@Override
	@Transactional
	public boolean submitAsync(Answer answer) {
		long startNs = System.nanoTime();
		String currentUsername = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		answer.setOperator(currentUsername);
		answer.setTenantCode(tenantCode);

		ExaminationRecord record = new ExaminationRecord();
		record.setCommonValue(currentUsername, tenantCode);
		record.setId(answer.getExamRecordId());
		// 提交时间
		record.setEndTime(record.getCreateTime());
		record.setSubmitStatus(SubmitStatusEnum.SUBMITTED.getValue());
		// 更新考试状态
		boolean success = examRecordService.update(record) > 0;
		this.submitAsync(record.getId(), currentUsername, tenantCode);
		long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
		log.debug("Async submit examination, username: {}, time consuming: {}ms", currentUsername, tookMs);
		return success;
	}

	@Override
	@Transactional
	public Boolean submitAll(List<AnswerDto> answers) {
		if (CollectionUtils.isEmpty(answers)) {
			return Boolean.FALSE;
		}

		String userCode = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		Long recordId = answers.get(0).getExamRecordId();
		Long[] ids = answers.stream().map(AnswerDto::getSubjectId).toArray(Long[]::new);
		List<Answer> dbAnswers = answerService.batchFindByRecordIdAndSubjectId(recordId, ids);
		Map<Long, Answer> answerMap = dbAnswers.stream().collect(Collectors.toMap(Answer::getSubjectId, s -> s));
		// 区分更新和插入
		List<Answer> inserts = Lists.newArrayList();
		List<Answer> updates = Lists.newArrayList();
		Date endTime = new Date();
		for (AnswerDto answer : answers) {
			Answer newAnswer = new Answer();
			BeanUtils.copyProperties(answer, newAnswer);
			newAnswer.setEndTime(endTime);
			Answer dbAnswer = answerMap.get(answer.getSubjectId());
			if (dbAnswer != null) {
				newAnswer.setCommonValue(userCode, tenantCode);
				newAnswer.setAnswer(answer.getAnswer());
				updates.add(newAnswer);
			} else {
				newAnswer.setNewRecord(true);
				newAnswer.setCommonValue(userCode, tenantCode);
				newAnswer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
				newAnswer.setAnswerType(AnswerConstant.WRONG);
				inserts.add(newAnswer);
			}
		}
		if (CollectionUtils.isNotEmpty(inserts)) {
			int update = answerService.batchInsert(inserts);
			log.info("Batch insert success, recordId: {}, size: {}", recordId, update);
		}
		if (CollectionUtils.isNotEmpty(updates)) {
			int update = answerService.batchUpdate(updates);
			log.info("Batch update success, recordId: {}, size: {}", recordId, update);
		}
		this.submit(recordId, userCode, tenantCode);
		return Boolean.TRUE;
	}

	@Override
	@Transactional
	public boolean anonymousUserSubmit(Long examinationId, String identifier, List<SubjectDto> dtos) {
		long startMs = System.currentTimeMillis();
		if (StringUtils.isBlank(identifier) || CollectionUtils.isEmpty(dtos)) {
			return false;
		}

		Examination examination = examinationService.get(examinationId);
		if (examination == null) {
			return false;
		}

		String tenantCode = SysUtil.getTenantCode();
		Date now = DateUtils.asDate(LocalDateTime.now());
		// 判断用户是否存在，不存在则自动创建
		R<UserVo> r = null;
		if (!RUtil.isSuccess(r) || r.getResult() == null) {
			return false;
		}

		// TODO 自动注册账号
		UserVo user = r.getResult();
		// 保存考试记录
		ExaminationRecord record = new ExaminationRecord();
		record.setCommonValue(identifier, tenantCode);
		record.setUserId(user.getUserId());

		// 初始化 Answer
		List<Answer> answers = new ArrayList<>(dtos.size());
		List<Long> subjectIds = Lists.newArrayListWithExpectedSize(dtos.size());
		dtos.forEach(dto -> {
			Answer a = new Answer();
			a.setCommonValue(identifier, tenantCode);
			a.setAnswer(dto.getAnswer().getAnswer());
			a.setExamRecordId(record.getId());
			a.setEndTime(now);
			a.setSubjectId(dto.getId());
			a.setType(dto.getType());
			a.setAnswerType(AnswerConstant.WRONG);
			subjectIds.add(dto.getId());
			answers.add(a);
		});
		Map<Integer, List<Answer>> distinct = distinctAnswer(subjectIds.toArray(new Long[0]), answers);
		HandlerFactory.Result result = HandlerFactory.handleAll(distinct);
		// 记录总分、正确题目数、错误题目数
		record.setScore(result.getScore());
		record.setCorrectNumber(result.getCorrectNum());
		record.setInCorrectNumber(result.getInCorrectNum());
		// 更新状态为统计完成，否则需要阅卷完成后才更改统计状态
		record.setType(examination.getType());
		record.setExaminationId(examinationId);
		record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
		record.setStartTime(now);
		record.setEndTime(now);
		examRecordService.insert(record);
		answers.forEach(answerService::insert);
		log.info("anonymousUser submit, examinationId:{}, identifier: {}, time consuming: {}ms", examinationId,
				identifier, System.currentTimeMillis() - startMs);
		return true;
	}

	@Override
	public ExamRecordDetailsDto details(Long id) {
		ExaminationRecord record = examRecordService.get(id);
		SgPreconditions.checkNull(record, "record is not exist");
		Examination examination = examinationService.get(record.getExaminationId());
		SgPreconditions.checkNull(examination, "examination is not exist");
		ExamRecordDetailsDto result = new ExamRecordDetailsDto();
		ExaminationRecordDto dto = new ExaminationRecordDto();
		// 答题卡
		List<CardDto> cards = Lists.newArrayList();
		result.setRecord(dto);
		result.setCards(cards);
		BeanUtils.copyProperties(examination, dto);
		dto.setId(record.getId());
		dto.setTypeLabel(ExaminationType.matchByValue(examination.getType()).getName());
		dto.setStartTime(record.getStartTime());
		dto.setEndTime(record.getEndTime());
		dto.setScore(ObjectUtil.getDouble(record.getScore()));
		dto.setUserId(record.getUserId());
		dto.setExaminationId(record.getExaminationId());
		dto.setDuration(DateUtils.durationNoNeedMillis(record.getStartTime(), record.getEndTime()));
		// 正确题目数
		dto.setCorrectNumber(ObjectUtil.getInt(record.getCorrectNumber()));
		dto.setInCorrectNumber(ObjectUtil.getInt(record.getInCorrectNumber()));
		// 提交状态
		dto.setSubmitStatus(record.getSubmitStatus());
		SubmitStatusEnum status = SubmitStatusEnum.match(record.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED);
		dto.setSubmitStatusName(status.getName());
		// 答题列表
		dto.setAnswers(getDetailAnswers(record, cards));
		examRecordService.fillExamUserInfo(Collections.singletonList(dto), new Long[]{record.getUserId()});
		return result;
	}

	private List<AnswerDto> getDetailAnswers(ExaminationRecord examRecord, List<CardDto> cards) {
		List<AnswerDto> list = Lists.newArrayList();
		List<Answer> answers = answerService.findListByExamRecordId(examRecord.getId());
		if (CollectionUtils.isEmpty(answers)) {
			return list;
		}

		Map<Long, Answer> answerMap = answers.stream().collect(Collectors.toMap(Answer::getSubjectId, e -> e));
		List<ExaminationSubject> esList = examinationService.findListByExaminationId(examRecord.getExaminationId());
		if (CollectionUtils.isEmpty(esList)) {
			return list;
		}

		List<Long> ids = esList.stream().map(ExaminationSubject::getSubjectId).collect(Collectors.toList());
		Collection<SubjectDto> dtoList = subjectsService.getSubjects(ids);
		Map<Long, SubjectDto> map = dtoList.stream().collect(Collectors.toMap(SubjectDto::getId, e -> e));
		for (ExaminationSubject es : esList) {
			CardDto card = new CardDto();
			card.setSort(es.getSort());
			card.setSubjectId(es.getSubjectId());
			cards.add(card);
			Answer answer = answerMap.get(es.getSubjectId());
			if (answer != null) {
				AnswerDto dto = new AnswerDto();
				BeanUtils.copyProperties(answer, dto);
				SubjectDto subjectDto = map.get(answer.getSubjectId());
				dto.setSubject(subjectDto);
				String duration = DateUtils.duration(answer.getStartTime(), answer.getEndTime());
				if (StringUtils.isEmpty(duration)) {
					duration = "1ms";
				}
				dto.setDuration(duration);
				dto.setSpeechPlayCnt(answer.getSpeechPlayCnt());
				list.add(dto);
				if (subjectDto.getChildSubjects() != null){
					List<SubjectDto> childSubjects = subjectDto.getChildSubjects();
					for (SubjectDto childSubject : childSubjects) {
						card = new CardDto();
						card.setSort(childSubject.getSort());
						card.setSubjectId(childSubject.getId());
						cards.add(card);
						answer = answerMap.get(childSubject.getId());
						if (answer != null) {
							dto = new AnswerDto();
							BeanUtils.copyProperties(answer, dto);
							dto.setSubject(childSubject);
							duration = DateUtils.duration(answer.getStartTime(), answer.getEndTime());
							if (StringUtils.isEmpty(duration)) {
								duration = "1ms";
							}
							dto.setDuration(duration);
							dto.setSpeechPlayCnt(answer.getSpeechPlayCnt());
							list.add(dto);
						}
					}
				}
			}
		}
		return list;
	}

	private void submitAsync(Long recordId, String userCode, String tenantCode) {
		StopWatch watch = StopWatchUtil.start();
		ListenableFuture<Boolean> future = executorHolder.getSubmitExecutor()
				.submit(() -> submit(recordId, userCode, tenantCode));
		Futures.addCallback(future, new FutureCallback<>() {
			@Override
			public void onSuccess(@Nullable Boolean result) {
				log.info("Submit future finished, recordId: {}, user: {}, took: {}", recordId, userCode,
						StopWatchUtil.stop(watch));
			}

			@Override
			public void onFailure(@Nullable Throwable e) {
				log.error("Submit future failed, recordId: {}, user: {}", recordId, userCode, e);
			}
		}, executorHolder.getSubmitExecutor());
	}

	private Map<Integer, List<Answer>> distinctAnswer(Long[] subjectIds, List<Answer> answers) {
		List<Subjects> subjects = subjectsService.findBySubjectIds(subjectIds);
		Map<Long, Integer> typeMap = ExamUtil.toMap(subjects);
		return ExamUtil.distinctAnswer(answers, typeMap);
	}

	private StartExamDto prepareStart(Long userId, Long examinationId) {
		long startNs = System.nanoTime();
		ListeningExecutorService exec = executorHolder.getExamExecutor();
		// 答题卡
		ListenableFuture<List<CardDto>> f1 = exec.submit(() -> {
			List<ExaminationSubject> ess = examinationSubjectService.findListByExaminationId(examinationId);
			if (CollectionUtils.isNotEmpty(ess)) {
				return ess.stream().map(es -> {
					CardDto c = new CardDto();
					c.setSubjectId(es.getSubjectId());
					c.setSort(es.getSort());
					return c;
				}).collect(Collectors.toList());
			}
			return Collections.emptyList();
		});

		// 获取第一题信息
		ListenableFuture<SubjectDto> f2 = exec.submit(
				() -> subjectsService.findFirstSubjectByExaminationId(examinationId));
		ListenableFuture<Examination> f3 = exec.submit(() -> examinationService.get(examinationId));

		StartExamDto dto = new StartExamDto();
		try {
			dto.setCards(f1.get());
			dto.setSubjectDto(f2.get());
			dto.setExamination(f3.get());
		} catch (Exception e) {
			log.error("Failed to get start exam futures result, userId: {}, examinationId: {}", userId, examinationId,
					e);
		}

		long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
		log.info("Prepare start finished, took: {}ms", tookMs);
		return dto;
	}

	private Answer initAnswer(String identifier, String tenantCode, SubjectDto sDto, Long recordId) {
		Answer answer = new Answer();
		answer.setSubjectId(sDto.getId());
		answer.setCommonValue(identifier, tenantCode);
		answer.setExamRecordId(recordId);
		// 默认待批改状态
		answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
		answer.setAnswerType(AnswerConstant.WRONG);
		answer.setStartTime(answer.getCreateTime());
		sDto.setAnswer(answer);
		return answer;
	}
}
