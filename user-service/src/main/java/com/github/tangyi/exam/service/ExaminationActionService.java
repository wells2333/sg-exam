package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.*;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.ExaminationTypeEnum;
import com.github.tangyi.exam.handler.AnswerHandleResult;
import com.github.tangyi.exam.service.answer.AnswerHandleService;
import com.github.tangyi.exam.service.answer.AnswerService;
import com.github.tangyi.exam.service.data.RedisCounterService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.AnswerHandlerUtil;
import com.github.tangyi.exam.utils.SubjectUtil;
import com.github.tangyi.user.service.CommonExecutorService;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author tangyi
 * @date 2022/4/14 6:43 下午
 */
@Slf4j
@Service
public class ExaminationActionService {

	private final ExaminationService examinationService;

	private final ExaminationSubjectService examinationSubjectService;

	private final ExamRecordService examRecordService;

	private final SubjectsService subjectsService;

	private final AnswerService answerService;

	private final AnswerHandleService answerHandleService;

	private final RankInfoService rankInfoService;

	private final CommonExecutorService commonExecutorService;

	private final RedisCounterService redisCounterService;

	public ExaminationActionService(ExaminationService examinationService,
			ExaminationSubjectService examinationSubjectService, ExamRecordService examRecordService,
			SubjectsService subjectsService, AnswerService answerService, AnswerHandleService answerHandleService,
			RankInfoService rankInfoService, CommonExecutorService commonExecutorService,
			RedisCounterService redisCounterService) {
		this.examinationService = examinationService;
		this.examinationSubjectService = examinationSubjectService;
		this.examRecordService = examRecordService;
		this.subjectsService = subjectsService;
		this.answerService = answerService;
		this.answerHandleService = answerHandleService;
		this.rankInfoService = rankInfoService;
		this.commonExecutorService = commonExecutorService;
		this.redisCounterService = redisCounterService;
	}

	/**
	 * 开始考试
	 *
	 * @param examRecord examRecord
	 * @return StartExamDto
	 * @author tangyi
	 * @date 2019/04/30 23:06
	 */
	@Transactional
	public StartExamDto start(ExaminationRecord examRecord) {
		SgPreconditions.checkNull(examRecord.getExaminationId(), "参数校验失败，考试id为空");
		SgPreconditions.checkNull(examRecord.getUserId(), "参数校验失败，用户id为空");
		return this.start(examRecord.getUserId(), SysUtil.getUser(), examRecord.getExaminationId(),
				SysUtil.getTenantCode());
	}

	/**
	 * 开始考试
	 *
	 * @param userId userId
	 * @param identifier identifier
	 * @param examinationId examinationId
	 * @param tenantCode tenantCode
	 * @return StartExamDto
	 * @author tangyi
	 * @date 2019/04/30 23:06
	 */
	@Transactional
	public StartExamDto start(Long userId, String identifier, Long examinationId, String tenantCode) {
		StartExamDto dto = new StartExamDto();
		Examination examination = examinationService.get(examinationId);

		dto.setExamination(examination);

		ExaminationRecord record = new ExaminationRecord();
		record.setCommonValue(identifier, tenantCode);
		record.setUserId(userId);
		record.setType(examination.getType());
		record.setExaminationId(examinationId);
		record.setStartTime(record.getCreateTime());
		// 默认未提交状态
		record.setSubmitStatus(SubmitStatusEnum.NOT_SUBMITTED.getValue());
		// 保存考试记录
		examRecordService.insert(record);
		dto.setExamRecord(record);

		// 根据题目ID，类型获取第一题的详细信息
		SubjectDto subjectDto = subjectsService.findFirstSubjectByExaminationId(examinationId);
		dto.setSubjectDto(subjectDto);
		dto.setTotal(subjectDto.getTotal());

		// 创建第一题的答题
		Answer answer = new Answer();
		answer.setCommonValue(identifier, tenantCode);
		answer.setExamRecordId(record.getId());
		answer.setSubjectId(subjectDto.getId());
		// 默认待批改状态
		answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
		answer.setAnswerType(AnswerConstant.WRONG);
		answer.setStartTime(answer.getCreateTime());
		// 保存答题
		answerService.save(answer);
		subjectDto.setAnswer(answer);

		// 答题卡
		List<ExaminationSubject> ess = examinationSubjectService.findListByExaminationId(examinationId);
		if (CollectionUtils.isNotEmpty(ess)) {
			List<CardDto> cards = ess.stream().map(es -> {
				CardDto card = new CardDto();
				card.setSubjectId(es.getSubjectId());
				card.setSort(es.getSort());
				return card;
			}).collect(Collectors.toList());
			dto.setCards(cards);
		}
		redisCounterService.incrCount(ExamCacheName.EXAMINATION_START_COUNT, examinationId);
		return dto;
	}

	/**
	 * 开始考试
	 *
	 * @param examinationId examinationId
	 * @param identifier identifier
	 * @return StartExamDto
	 * @author tangyi
	 * @date 2020/3/21 5:51 下午
	 */
	@Transactional
	public StartExamDto anonymousUserStart(Long examinationId, String identifier) {
		String tenantCode = SysUtil.getTenantCode();
		// 创建考试记录
		SgPreconditions.checkNull(examinationId, "参数校验失败，考试id为空");
		SgPreconditions.checkNull(identifier, "参数校验失败，用户identifier为空");
		// 查询用户信息
		R<UserVo> r = null;
		SgPreconditions.checkBoolean(!RUtil.isSuccess(r), "获取用户" + identifier + "信息失败！");
		return this.start(r.getResult().getUserId(), identifier, examinationId, tenantCode);
	}

	/**
	 * 提交答卷，自动统计选择题得分
	 *
	 * @param recordId recordId
	 * @param operator operator
	 * @author tangyi
	 * @date 2018/12/26 14:09
	 */
	@Transactional
	public Boolean submit(Long recordId, String operator, String tenantCode) {
		// 已提交的题目
		List<Answer> answerList = answerService.findListByExamRecordId(recordId);
		if (CollectionUtils.isEmpty(answerList)) {
			return Boolean.FALSE;
		}
		// 成绩
		ExaminationRecord record = new ExaminationRecord();
		Long[] subjectIds = answerList.stream().map(Answer::getSubjectId).toArray(Long[]::new);
		Map<Integer, List<Answer>> distinct = distinctAnswer(subjectIds, answerList);
		AnswerHandleResult result = answerHandleService.handleAll(distinct);
		// 记录总分、正确题目数、错误题目数
		record.setScore(result.getScore());
		record.setCorrectNumber(result.getCorrectNum());
		record.setInCorrectNumber(result.getInCorrectNum());
		// 更新答题状态
		List<Answer> updates = distinct.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
		answerService.batchUpdate(updates);
		// 更新状态为统计完成，否则需要阅卷完成后才更改统计状态
		record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
		// 保存成绩
		record.setCommonValue(operator, tenantCode);
		record.setId(recordId);
		record.setEndTime(record.getCreateTime());
		examRecordService.update(record);
		// 更新排名数据
		rankInfoService.updateRank(record);
		return Boolean.TRUE;
	}

	/**
	 * 异步提交
	 * @param answer answer
	 * @return boolean
	 * @author tangyi
	 * @date 2019/05/03 14:35
	 */
	@Transactional
	public boolean submitAsync(Answer answer) {
		long start = System.currentTimeMillis();
		String currentUsername = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		answer.setOperator(currentUsername);
		answer.setTenantCode(tenantCode);

		ExaminationRecord examRecord = new ExaminationRecord();
		examRecord.setCommonValue(currentUsername, tenantCode);
		examRecord.setId(answer.getExamRecordId());
		// 提交时间
		examRecord.setEndTime(examRecord.getCreateTime());
		examRecord.setSubmitStatus(SubmitStatusEnum.SUBMITTED.getValue());
		// 更新考试状态
		boolean success = examRecordService.update(examRecord) > 0;
		submitAsync(examRecord.getId(), currentUsername, tenantCode);
		log.debug("async submit examination, username: {}，time consuming: {}ms", currentUsername,
				System.currentTimeMillis() - start);
		return success;
	}

	@Transactional
	public Boolean submitAll(List<AnswerDto> answers) {
		if (CollectionUtils.isEmpty(answers)) {
			return Boolean.FALSE;
		}
		String userCode = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		Long recordId = answers.get(0).getExamRecordId();
		Long[] subjectIds = answers.stream().map(AnswerDto::getSubjectId).toArray(Long[]::new);
		List<Answer> dbAnswers = answerService.batchFindByRecordIdAndSubjectId(recordId, subjectIds);
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
			log.info("batch insert success, recordId: {}, size: {}", recordId, update);
		}
		if (CollectionUtils.isNotEmpty(updates)) {
			int update = answerService.batchUpdate(updates);
			log.info("batch update success, recordId: {}, size: {}", recordId, update);
		}
		submitAsync(recordId, userCode, tenantCode);
		return Boolean.TRUE;
	}

	/**
	 * 移动端提交答题
	 * @param examinationId examinationId
	 * @return R
	 * @author tangyi
	 * @date 2020/03/15 16:08
	 */
	@Transactional
	public boolean anonymousUserSubmit(Long examinationId, String identifier, List<SubjectDto> subjectDtos) {
		long start = System.currentTimeMillis();
		if (StringUtils.isBlank(identifier) || CollectionUtils.isEmpty(subjectDtos)) {
			return false;
		}
		Examination examination = examinationService.get(examinationId);
		if (examination == null) {
			return false;
		}
		String tenantCode = SysUtil.getTenantCode();
		Date currentDate = DateUtils.asDate(LocalDateTime.now());
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

		// 初始化Answer
		List<Answer> answers = new ArrayList<>(subjectDtos.size());
		List<Long> subjectIds = Lists.newArrayListWithExpectedSize(subjectDtos.size());
		subjectDtos.forEach(subjectDto -> {
			Answer answer = new Answer();
			answer.setCommonValue(identifier, tenantCode);
			answer.setAnswer(subjectDto.getAnswer().getAnswer());
			answer.setExamRecordId(record.getId());
			answer.setEndTime(currentDate);
			answer.setSubjectId(subjectDto.getId());
			answer.setType(subjectDto.getType());
			answer.setAnswerType(AnswerConstant.WRONG);
			subjectIds.add(subjectDto.getId());
			answers.add(answer);
		});
		Map<Integer, List<Answer>> distinct = distinctAnswer(subjectIds.toArray(new Long[0]), answers);
		AnswerHandleResult result = answerHandleService.handleAll(distinct);
		// 记录总分、正确题目数、错误题目数
		record.setScore(result.getScore());
		record.setCorrectNumber(result.getCorrectNum());
		record.setInCorrectNumber(result.getInCorrectNum());
		// 更新状态为统计完成，否则需要阅卷完成后才更改统计状态
		record.setType(examination.getType());
		record.setExaminationId(examinationId);
		record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
		record.setStartTime(currentDate);
		record.setEndTime(currentDate);
		examRecordService.insert(record);
		answers.forEach(answerService::insert);
		log.info("anonymousUser submit, examinationId:{}, identifier: {}, time consuming: {}ms", examinationId,
				identifier, System.currentTimeMillis() - start);
		return true;
	}

	/**
	 *  分类题目
	 * @param subjectIds subjectIds
	 * @param answers answers
	 * @return Map
	 */
	public Map<Integer, List<Answer>> distinctAnswer(Long[] subjectIds, List<Answer> answers) {
		List<Subjects> subjects = subjectsService.findBySubjectIds(subjectIds);
		Map<Long, Integer> typeMap = SubjectUtil.toMap(subjects);
		return AnswerHandlerUtil.distinctAnswer(answers, typeMap);
	}

	/**
	 * 成绩详情
	 * @param id id
	 * @return ExaminationRecordDto
	 * @author tangyi
	 * @date 2020/2/21 9:26 上午
	 */
	public ExamRecordDetailsDto details(Long id) {
		ExaminationRecord record = examRecordService.get(id);
		SgPreconditions.checkNull(record, "record is not exist");
		Examination examination = examinationService.get(record.getExaminationId());
		SgPreconditions.checkNull(examination, "examination is not exist");
		ExamRecordDetailsDto result = new ExamRecordDetailsDto();
		ExaminationRecordDto recordDto = new ExaminationRecordDto();
		// 答题卡
		List<CardDto> cards = Lists.newArrayList();
		result.setRecord(recordDto);
		result.setCards(cards);
		BeanUtils.copyProperties(examination, recordDto);
		recordDto.setId(record.getId());
		recordDto.setTypeLabel(ExaminationTypeEnum.matchByValue(examination.getType()).getName());
		recordDto.setStartTime(record.getStartTime());
		recordDto.setEndTime(record.getEndTime());
		recordDto.setScore(ObjectUtil.getDouble(record.getScore()));
		recordDto.setUserId(record.getUserId());
		recordDto.setExaminationId(record.getExaminationId());
		recordDto.setDuration(DateUtils.durationNoNeedMillis(record.getStartTime(), record.getEndTime()));
		// 正确题目数
		recordDto.setCorrectNumber(ObjectUtil.getInt(record.getCorrectNumber()));
		recordDto.setInCorrectNumber(ObjectUtil.getInt(record.getInCorrectNumber()));
		// 提交状态
		recordDto.setSubmitStatus(record.getSubmitStatus());
		SubmitStatusEnum status = SubmitStatusEnum.match(record.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED);
		recordDto.setSubmitStatusName(status.getName());
		// 答题列表
		recordDto.setAnswers(getDetailAnswers(record, cards));
		examRecordService.fillExamUserInfo(Collections.singletonList(recordDto), new Long[]{record.getUserId()});
		return result;
	}

	public List<AnswerDto> getDetailAnswers(ExaminationRecord examRecord, List<CardDto> cards) {
		List<AnswerDto> list = Lists.newArrayList();
		List<Answer> answers = answerService.findListByExamRecordId(examRecord.getId());
		if (CollectionUtils.isEmpty(answers)) {
			return list;
		}
		List<ExaminationSubject> ess = examinationService.findListByExaminationId(examRecord.getExaminationId());
		if (CollectionUtils.isEmpty(ess)) {
			return list;
		}
		for (ExaminationSubject es : ess) {
			CardDto card = new CardDto();
			card.setSort(es.getSort());
			card.setSubjectId(es.getSubjectId());
			cards.add(card);
			for (Answer answer : answers) {
				if (answer.getSubjectId().equals(es.getSubjectId())) {
					AnswerDto dto = new AnswerDto();
					BeanUtils.copyProperties(answer, dto);
					dto.setSubject(subjectsService.getSubject(answer.getSubjectId()));
					dto.setDuration(DateUtils.duration(answer.getStartTime(), answer.getEndTime()));
					list.add(dto);
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 异步提交
	 * @param recordId recordId
	 * @param userCode userCode
	 * @param tenantCode tenantCode
	 */
	public void submitAsync(Long recordId, String userCode, String tenantCode) {
		StopWatch watch = StopWatchUtil.start();
		ListenableFuture<Boolean> future = commonExecutorService.getSubmitExecutor()
				.submit(() -> submit(recordId, userCode, tenantCode));
		Futures.addCallback(future, new FutureCallback<>() {
			@Override
			public void onSuccess(@Nullable Boolean result) {
				log.info("submit future finished, recordId: {}, user: {}, took: {}", recordId, userCode,
						StopWatchUtil.stop(watch));
			}

			@Override
			public void onFailure(@Nullable Throwable e) {
				log.error("submit future failed, recordId: {}, user: {}", recordId, userCode, e);
			}
		}, commonExecutorService.getSubmitExecutor());
	}
}
