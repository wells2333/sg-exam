/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.exam.service.answer;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.AnswerAnalysisDto;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.service.IAnswerService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.MarkConstant;
import com.github.tangyi.exam.handler.HandlerFactory;
import com.github.tangyi.exam.handler.IAnswerHandler;
import com.github.tangyi.exam.mapper.AnswerMapper;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.RankInfoService;
import com.github.tangyi.exam.service.media.ExamMediaService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExamUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AnswerService extends CrudService<AnswerMapper, Answer> implements IAnswerService {

	private final SubjectsService subjectsService;
	private final ExamRecordService examRecordService;
	private final ExaminationSubjectService esService;
	private final ExamMediaService examMediaService;
	private final RankInfoService rankInfoService;
	private final IUserService userService;
	private final IExaminationService examinationService;

	@Override
	@Cacheable(value = ExamCacheName.ANSWER, key = "#id")
	public Answer get(Long id) {
		return super.get(id);
	}

	@Transactional
	public int batchInsert(List<Answer> answers) {
		return this.dao.batchInsert(answers);
	}

	@Transactional
	public int batchUpdate(List<Answer> answers) {
		return this.dao.batchUpdate(answers);
	}

	/**
	 * 根据考试记录 ID、题目 ID 查找答题
	 */
	public Answer findByRecordIdAndSubjectId(Answer answer) {
		return this.dao.findByRecordIdAndSubjectId(answer);
	}

	public List<Answer> batchFindByRecordIdAndSubjectId(Long recordId, Long[] subjectIds) {
		return this.dao.batchFindByRecordIdAndSubjectId(recordId, subjectIds);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.ANSWER, key = "#answer.id")
	public int update(Answer answer) {
		answer.setAnswer(ExamUtil.replaceComma(answer.getAnswer()));
		return super.update(answer);
	}

	/**
	 * 更新答题总分
	 */
	@Transactional
	@CacheEvict(value = ExamCacheName.ANSWER, key = "#answer.id")
	public int updateScore(Answer answer) {
		answer.setAnswer(ExamUtil.replaceComma(answer.getAnswer()));
		// 加分减分逻辑
		Answer oldAnswer = this.get(answer.getId());
		String user = SysUtil.getUser();
		answer.setUpdateTime(new Date());
		answer.setOperator(user);
		answer.setMarkStatus(AnswerConstant.MARKED);
		answer.setMarkOperator(user);
		Long recordId = oldAnswer.getExamRecordId();
		ExaminationRecord record = new ExaminationRecord();
		record.setId(recordId);
		record = examRecordService.get(record.getId());
		SgPreconditions.checkNull(record, "examRecord is null");
		Double oldScore = record.getScore();
		SubjectDto subject = subjectsService.getSubject(oldAnswer.getSubjectId());
		SgPreconditions.checkNull(subject, "subject is null");
		double score = ObjectUtil.doubleValue(subject.getScore());
		// 题目得分
		answer.setScore(AnswerConstant.RIGHT.equals(answer.getAnswerType()) ? score : 0d);
		// 更新答题
		update(answer);
		// 重新计算总分
		Integer totalScore = sumScoreByAnswerType(recordId, AnswerConstant.RIGHT);
		if (totalScore == null)
			totalScore = 0;
		record.setScore(totalScore.doubleValue());
		// 正确、错误题目数量
		Integer correctNumber = countByAnswerType(recordId, AnswerConstant.RIGHT);
		Integer inCorrectNumber = countByAnswerType(recordId, AnswerConstant.WRONG);
		record.setCorrectNumber(correctNumber);
		record.setInCorrectNumber(inCorrectNumber);
		if (examRecordService.update(record) > 0) {
			log.info("Update answer success, examRecordId: {}, oldScore: {}, newScore: {}", oldAnswer.getExamRecordId(),
					oldScore, record.getScore());
		}
		// 判断是否全部批改
		if (isOK(recordId))
			markOk(recordId); // 批改
		return 1;
	}

	@Override
	@Transactional
	public int markOk(Long recordId) {
		ExaminationRecord record = examRecordService.get(recordId);
		if (record != null) {
			record.setCommonValue();
			record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
			this.rankInfoService.updateRank(record);
			return examRecordService.update(record);
		}
		return -1;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.ANSWER, key = "#answer.id")
	public int delete(Answer answer) {
		return super.delete(answer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.ANSWER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Transactional
	public int save(Answer answer) {
		answer.setCommonValue();
		answer.setAnswer(ExamUtil.replaceComma(answer.getAnswer()));
		return super.save(answer);
	}

	/**
	 * 保存答题，返回下一题/上一题的信息
	 *
	 * @param type 0：下一题，1：上一题
	 */
	@Transactional
	public SubjectDto saveAndNext(AnswerDto answerDto, Integer type, Integer nextSubjectSortNo) {
		Long userId = SysUtil.getUserId();
		String userCode = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		if (this.save(answerDto, userId, userCode, tenantCode) > 0) {
			return this.subjectAnswer(answerDto.getSubjectId(), answerDto.getExamRecordId(), type, nextSubjectSortNo);
		}
		return null;
	}

	@Override
	@Transactional
	public int save(AnswerDto answerDto, Long userId, String userCode, String tenantCode) {
		Answer answer = new Answer();
		BeanUtils.copyProperties(answerDto, answer);
		Answer temp = this.findByRecordIdAndSubjectId(answer);
		Long speechPlayCnt = examMediaService.getSpeechPlayCnt(userId, answerDto.getSubjectId());
		if (temp != null) {
			temp.setCommonValue(userCode, tenantCode);
			temp.setAnswer(answer.getAnswer());
			temp.setType(answer.getType());
			temp.setEndTime(temp.getUpdateTime());
			temp.setSpeechPlayCnt(speechPlayCnt);
			return this.update(temp);
		} else {
			answer.setNewRecord(true);
			answer.setCommonValue(userCode, tenantCode);
			answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
			answer.setAnswerType(AnswerConstant.WRONG);
			answer.setEndTime(answer.getUpdateTime());
			answer.setSpeechPlayCnt(speechPlayCnt);
			return this.insert(answer);
		}
	}

	/**
	 * 查询题目和答题
	 *
	 * @param type -1：当前题目，0：下一题，1：上一题
	 */
	@Transactional
	public SubjectDto subjectAnswer(Long subjectId, Long recordId, Integer type, Integer nextSubjectSortNo) {
		ExaminationRecord record = examRecordService.get(recordId);
		SgPreconditions.checkNull(record, "The examination record does not exists.");
		Long examinationId = record.getExaminationId();
		ExaminationSubject es = esService.findByExaminationIdAndSubjectId(ExamUtil.createEs(examinationId, subjectId));
		if (es != null) {
			es.setSort(getSort(es, nextSubjectSortNo, type));
			es = esService.findByExaminationIdAndSort(es);
		}
		SubjectDto subject = null;
		if (es != null) {
			subject = subjectsService.getSubject(es.getSubjectId());
			if (subject != null) {
				// 题目数量
				Integer subjectCount = esService.findSubjectCount(examinationId);
				Answer answer = new Answer();
				answer.setSubjectId(subject.getId());
				answer.setExamRecordId(recordId);
				Answer userAnswer = this.findByRecordIdAndSubjectId(answer);
				subject.setAnswer(userAnswer == null ? new Answer() : userAnswer);
				subject.setExaminationRecordId(recordId);
				subject.setTotal(subjectCount);
				subject.setHasMore(subjectCount != null && es.getSort() < subjectCount);

				// TODO 收藏
			}
		}
		return subject;
	}

	public int getSort(ExaminationSubject es, Integer nextSubjectSortNo, Integer type) {
		int sort = 1;
		if (nextSubjectSortNo != null) {
			sort = nextSubjectSortNo;
		} else {
			if (type == 0) {
				sort = es.getSort() + 1;
			} else if (type == 1) {
				sort = Math.max(1, es.getSort() - 1);
			} else if (type == -1) {
				sort = es.getSort();
			}
		}
		return sort;
	}

	/**
	 * 答题详情
	 */
	public AnswerDto answerInfo(Long recordId, Long currentSubjectId, Integer nextType) {
		ExaminationRecord record = examRecordService.get(recordId);
		SubjectDto subjectDto = null;
		// 题目为空，则加载第一题
		if (currentSubjectId == null) {
			subjectDto = subjectsService.findFirstSubjectByExaminationId(record.getExaminationId());
		} else {
			ExaminationSubject es = new ExaminationSubject();
			es.setExaminationId(record.getExaminationId());
			es.setSubjectId(currentSubjectId);
			// 查询该考试和指定序号的题目的关联信息
			// 下一题
			if (AnswerConstant.NEXT.equals(nextType)) {
				es = esService.getByPreviousId(es);
			} else if (AnswerConstant.PREVIOUS.equals(nextType)) {
				// 上一题
				es = esService.getPreviousByCurrentId(es);
			} else {
				es = esService.findByExaminationIdAndSubjectId(es);
			}
			if (es == null) {
				throw new IllegalStateException("The subject id " + currentSubjectId + " does not exists.");
			}

			// 查询题目的详细信息
			//subjectDto = subjectService.get(examinationSubject.getSubjectId(), examinationSubject.getType());
		}
		AnswerDto answerDto = new AnswerDto();
		answerDto.setSubject(subjectDto);
		// 查询答题
		Answer answer = new Answer();
		answer.setSubjectId(subjectDto.getId());
		answer.setExamRecordId(recordId);
		Answer userAnswer = this.findByRecordIdAndSubjectId(answer);
		if (userAnswer == null) {
			userAnswer = answer;
		}
		BeanUtils.copyProperties(userAnswer, answerDto);
		answerDto.setDuration(DateUtils.duration(userAnswer.getStartTime(), userAnswer.getEndTime()));
		// 判断正误
		IAnswerHandler handler = HandlerFactory.getHandler(subjectDto.getType());
		if (handler.hasOption()) {
			handler.judgeOptionRight(userAnswer, subjectDto);
		} else {
			IAnswerHandler.JudgeContext judgeContext = new IAnswerHandler.JudgeContext(
					new IAnswerHandler.HandleContext(), subjectDto, userAnswer);
			handler.judgeRight(judgeContext);
		}
		R<List<UserVo>> r = null;
		if (RUtil.isSuccess(r) && CollectionUtils.isNotEmpty(r.getResult())) {
			UserVo userVo = r.getResult().get(0);
			answerDto.setUserName(userVo.getName());
		}
		return answerDto;
	}

	/**
	 * 获取错题列表
	 */
	public PageInfo<AnswerDto> answerListInfo(String pageNum, String pageSize, Long recordId, Answer answer) {
		List<AnswerDto> list = new ArrayList<>();
		answer.setExamRecordId(recordId);
		Map<String, Object> condition = Maps.newHashMap();
		super.tenantParams(condition);
		if (recordId != null) {
			condition.put("examRecordId", recordId);
		}
		if (answer.getAnswerType() != null) {
			condition.put("answerType", answer.getAnswerType());
		}
		PageInfo<Answer> pageInfo = this.findPage(condition, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
			list = pageInfo.getList().stream().map(tempAnswer -> {
				AnswerDto dto = new AnswerDto();
				BeanUtils.copyProperties(tempAnswer, dto);
				SubjectDto subjectDto = subjectsService.getSubject(tempAnswer.getSubjectId());
				dto.setSubject(subjectDto);
				// 判断正误
				IAnswerHandler handler = HandlerFactory.getHandler(subjectDto.getType());
				if (handler.hasOption()) {
					handler.judgeOptionRight(tempAnswer, subjectDto);
				} else {
					handler.judgeRight(new IAnswerHandler.JudgeContext(new IAnswerHandler.HandleContext(), subjectDto,
							tempAnswer));
				}
				return dto;
			}).collect(Collectors.toList());
		}
		PageInfo<AnswerDto> page = new PageInfo<>();
		PageUtil.copyProperties(pageInfo, page);
		page.setList(list);
		return page;
	}

	/**
	 * 根据 examRecordId 查询
	 */
	public List<Answer> findListByExamRecordId(Long examRecordId) {
		return this.dao.findListByExamRecordId(examRecordId);
	}

	public Integer countByAnswerType(Long examRecordId, Integer answerType) {
		return this.dao.countByAnswerType(examRecordId, answerType);
	}

	public Integer sumScoreByAnswerType(Long examRecordId, Integer answerType) {
		return this.dao.sumScoreByAnswerType(examRecordId, answerType);
	}

	@Override
	public AnswerAnalysisDto analysis(Long examinationId) {
		AnswerAnalysisDto dto = new AnswerAnalysisDto();
		Examination examination = this.examinationService.get(examinationId);
		Optional.ofNullable(examination).ifPresent(e -> dto.setExaminationName(e.getExaminationName()));
		// 查询这个考试所有的已提交考试记录
		List<ExaminationRecord> records = this.examRecordService.getByExaminationId(examinationId);
		dto.setUserCnt(CollectionUtils.size(records));
		if (CollectionUtils.isNotEmpty(records)) {
			// 按分数排序
			List<ExaminationRecord> sorted = records.stream().filter(record -> record.getScore() != null)
					.sorted(Comparator.comparing(ExaminationRecord::getScore).reversed()).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(sorted)) {
				Map<Long, User> userMap = this.findUserList(sorted);
				this.fillAnswerAnalysisDto(sorted, dto, userMap);
			}
		}
		return dto;
	}

	@Override
	public Boolean isOK(Long recordId) {
		Map<String, Object> condition = Maps.newHashMap();
		super.tenantParams(condition);
		if (recordId != null) {
			condition.put("examRecordId", recordId);
		}
		PageInfo<Answer> pageInfo = this.findPage(condition, 1,10);
		List<Answer> list = pageInfo.getList();
		Boolean isOk = true;
		for (Answer answer : list) {
			if (answer.getMarkStatus() == 0){
				isOk = false;
				break;
			}

		}
		return isOk;
	}

	private Map<Long, User> findUserList(List<ExaminationRecord> sorted) {
		List<Long> userIds = sorted.stream().map(ExaminationRecord::getUserId).toList();
		List<User> userList = this.userService.findListById(userIds.toArray(Long[]::new));
		Map<Long, User> result = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				result.put(user.getId(), user);
			}
		}
		return result;
	}

	private void fillAnswerAnalysisDto(List<ExaminationRecord> records, AnswerAnalysisDto dto,
			Map<Long, User> userMap) {
		double highestScore = 0;
		double lowestScore = 200;
		double sumScore = 0;
		List<AnswerAnalysisDto.ScoreItem> scoreItems = Lists.newArrayListWithExpectedSize(records.size());
		Map<Integer, AtomicInteger> distribution = Maps.newHashMap();
		for (int i = 0; i < records.size(); i++) {
			ExaminationRecord record = records.get(i);
			// 最高分、最低分、平均分
			double score = record.getScore();
			highestScore = Math.max(highestScore, score);
			lowestScore = Math.min(lowestScore, score);
			sumScore += score;

			// 排名列表
			AnswerAnalysisDto.ScoreItem scoreItem = new AnswerAnalysisDto.ScoreItem();
			scoreItem.setRankNum(i + 1);
			scoreItem.setScore(score);
			Optional.ofNullable(userMap.get(record.getUserId()))
					.ifPresent(user -> scoreItem.setUserName(user.getName()));
			scoreItems.add(scoreItem);

			// 分数分布
			int level = this.getScoreLevel(score);
			distribution.computeIfAbsent(level, s -> new AtomicInteger(0)).incrementAndGet();
		}
		dto.setHighestScore(highestScore);
		dto.setLowestScore(lowestScore);
		dto.setAvgScore(sumScore / records.size());
		dto.setScoreItems(scoreItems);
		this.fillScoreDistribution(dto, distribution);
	}

	private void fillScoreDistribution(AnswerAnalysisDto dto, Map<Integer, AtomicInteger> distribution) {
		AtomicInteger defaultCount = new AtomicInteger(0);
		// 计算成绩分布数量
		Map<String, Integer> data = Maps.newLinkedHashMap();
		data.put("60 以下", distribution.getOrDefault(1, defaultCount).get());
		data.put("60-70", distribution.getOrDefault(2, defaultCount).get());
		data.put("70-80", distribution.getOrDefault(3, defaultCount).get());
		data.put("80-90", distribution.getOrDefault(4, defaultCount).get());
		data.put("90-100", distribution.getOrDefault(5, defaultCount).get());
		data.put("100-110", distribution.getOrDefault(6, defaultCount).get());
		data.put("110-120", distribution.getOrDefault(7, defaultCount).get());
		data.put("120-130", distribution.getOrDefault(8, defaultCount).get());
		data.put("130 以上", distribution.getOrDefault(9, defaultCount).get());
		dto.setScoreDistribution(data);

		// 计算成绩分布百分比
		Map<String, Float> percentMap = Maps.newLinkedHashMap();
		int total = data.values().stream().mapToInt(Integer::intValue).sum();
		for (Map.Entry<String, Integer> entry : data.entrySet()) {
			float percent = (float) entry.getValue() / total;
			percentMap.put(entry.getKey(), percent);
		}
		dto.setScoreDistributionPercent(percentMap);
	}

	private int getScoreLevel(double score) {
		int level = -1;
		if (score < 60) {
			level = 1;
		} else if (score >= 60 && score < 70) {
			level = 2;
		} else if (score >= 70 && score < 80) {
			level = 3;
		} else if (score >= 80 && score < 90) {
			level = 4;
		} else if (score >= 90 && score < 100) {
			level = 5;
		} else if (score >= 100 && score < 110) {
			level = 6;
		} else if (score >= 110 && score < 120) {
			level = 7;
		} else if (score >= 120 && score < 130) {
			level = 8;
		} else if (score >= 130) {
			level = 9;
		}
		return level;
	}
}
