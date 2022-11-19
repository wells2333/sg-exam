package com.github.tangyi.exam.service.answer;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.ObjectUtil;
import com.github.tangyi.common.utils.RUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.handler.AnswerHandlerFactory;
import com.github.tangyi.exam.handler.IAnswerHandler;
import com.github.tangyi.exam.mapper.AnswerMapper;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.AnswerHandlerUtil;
import com.github.tangyi.exam.utils.ExaminationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 答题service
 *
 * @author tangyi
 * @date 2018/11/8 21:17
 */
@Slf4j
@AllArgsConstructor
@Service
public class AnswerService extends CrudService<AnswerMapper, Answer> {

	private final SubjectsService subjectsService;

	private final ExamRecordService examRecordService;

	private final ExaminationSubjectService esService;

	private final AnswerHandlerFactory handlerFactory;

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
	 * 根据考试记录ID、题目ID查找答题
	 *
	 * @param answer answer
	 * @return Answer
	 * @author tangyi
	 * @date 2019/01/21 19:41
	 */
	public Answer findByRecordIdAndSubjectId(Answer answer) {
		return this.dao.findByRecordIdAndSubjectId(answer);
	}

	/**
	 * 根据考试记录ID、题目ID批量查找答题
	 *
	 * @return Answer
	 * @author tangyi
	 * @date 2019/01/21 19:41
	 */
	public List<Answer> batchFindByRecordIdAndSubjectId(Long recordId, Long[] subjectIds) {
		return this.dao.batchFindByRecordIdAndSubjectId(recordId, subjectIds);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.ANSWER, key = "#answer.id")
	public int update(Answer answer) {
		answer.setAnswer(AnswerHandlerUtil.replaceComma(answer.getAnswer()));
		return super.update(answer);
	}

	/**
	 * 更新答题总分
	 *
	 * @param answer answer
	 * @return int
	 * @author tangyi
	 * @date 2019/1/3 14:27
	 */
	@Transactional
	@CacheEvict(value = ExamCacheName.ANSWER, key = "#answer.id")
	public int updateScore(Answer answer) {
		answer.setAnswer(AnswerHandlerUtil.replaceComma(answer.getAnswer()));
		// 加分减分逻辑
		Answer oldAnswer = this.get(answer.getId());
		String user = SysUtil.getUser();
		answer.setUpdateTime(new Date());
		answer.setOperator(user);
		answer.setMarkStatus(AnswerConstant.MARKED);
		answer.setMarkOperator(user);
		Long recordId = oldAnswer.getExamRecordId();
		if (!oldAnswer.getAnswerType().equals(answer.getAnswerType())) {
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
			record.setScore(totalScore.doubleValue());
			// 正确、错误题目数量
			Integer correctNumber = countByAnswerType(recordId, AnswerConstant.RIGHT);
			Integer inCorrectNumber = countByAnswerType(recordId, AnswerConstant.WRONG);
			record.setCorrectNumber(correctNumber);
			record.setInCorrectNumber(inCorrectNumber);
			if (examRecordService.update(record) > 0) {
				log.info("Update answer success, examRecordId: {}, oldScore: {}, newScore: {}",
						oldAnswer.getExamRecordId(), oldScore, record.getScore());
			}
		}
		return 1;
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
		answer.setAnswer(AnswerHandlerUtil.replaceComma(answer.getAnswer()));
		return super.save(answer);
	}

	/**
	 * 保存答题，返回下一题/上一题的信息
	 *
	 * @param answerDto       answerDto
	 * @param type            0：下一题，1：上一题
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/05/01 11:42
	 */
	@Transactional
	public SubjectDto saveAndNext(AnswerDto answerDto, Integer type, Integer nextSubjectSortNo) {
		String userCode = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		if (this.save(answerDto, userCode, tenantCode) > 0) {
			return this.subjectAnswer(answerDto.getSubjectId(), answerDto.getExamRecordId(), type, nextSubjectSortNo);
		}
		return null;
	}

	/**
	 * 保存答题
	 *
	 * @param answerDto       answerDto
	 * @param userCode       userCode
	 * @param tenantCode       tenantCode
	 * @return int
	 * @author tangyi
	 * @date 2019/05/01 11:42
	 */
	@Transactional
	public int save(AnswerDto answerDto, String userCode, String tenantCode) {
		Answer answer = new Answer();
		BeanUtils.copyProperties(answerDto, answer);
		Answer tempAnswer = this.findByRecordIdAndSubjectId(answer);
		if (tempAnswer != null) {
			tempAnswer.setCommonValue(userCode, tenantCode);
			tempAnswer.setAnswer(answer.getAnswer());
			tempAnswer.setType(answer.getType());
			tempAnswer.setEndTime(tempAnswer.getUpdateTime());
			return this.update(tempAnswer);
		} else {
			answer.setNewRecord(true);
			answer.setCommonValue(userCode, tenantCode);
			answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
			answer.setAnswerType(AnswerConstant.WRONG);
			answer.setEndTime(answer.getUpdateTime());
			return this.insert(answer);
		}
	}


	/**
	 * 查询题目和答题
	 *
	 * @param subjectId       subjectId
	 * @param recordId    recordId
	 * @param type        -1：当前题目，0：下一题，1：上一题
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/04/30 17:10
	 */
	@Transactional
	public SubjectDto subjectAnswer(Long subjectId, Long recordId, Integer type, Integer nextSubjectSortNo) {
		ExaminationRecord record = examRecordService.get(recordId);
		SgPreconditions.checkNull(record, "考试记录不存在");
		Long examinationId = record.getExaminationId();
		ExaminationSubject es = esService.findByExaminationIdAndSubjectId(
				ExaminationUtil.createEs(examinationId, subjectId));
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
	 *
	 * @param recordId         recordId
	 * @param currentSubjectId currentSubjectId
	 * @param nextType         nextType
	 * @return AnswerDto
	 * @author tangyi
	 * @date 2019/06/18 23:05
	 */
	public AnswerDto answerInfo(Long recordId, Long currentSubjectId, Integer nextType) {
		ExaminationRecord record = examRecordService.get(recordId);
		SubjectDto subjectDto = null;
		// 题目为空，则加载第一题
		if (currentSubjectId == null) {
			subjectDto = subjectsService.findFirstSubjectByExaminationId(record.getExaminationId());
		} else {
			ExaminationSubject examinationSubject = new ExaminationSubject();
			examinationSubject.setExaminationId(record.getExaminationId());
			examinationSubject.setSubjectId(currentSubjectId);
			// 查询该考试和指定序号的题目的关联信息
			// 下一题
			if (AnswerConstant.NEXT.equals(nextType)) {
				examinationSubject = esService.getByPreviousId(examinationSubject);
			} else if (AnswerConstant.PREVIOUS.equals(nextType)) {
				// 上一题
				examinationSubject = esService.getPreviousByCurrentId(examinationSubject);
			} else {
				examinationSubject = esService.findByExaminationIdAndSubjectId(examinationSubject);
			}
			SgPreconditions.checkNull(examinationSubject, "ID为" + currentSubjectId + "的题目不存在");
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
		IAnswerHandler handler = handlerFactory.getHandler(subjectDto.getType());
		if (handler.hasOption()) {
			handler.judgeOptionRight(userAnswer, subjectDto);
		} else {
			handler.judgeRight(userAnswer, subjectDto);
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
	 *
	 * @param pageNum  pageNum
	 * @param pageSize pageSize
	 * @param recordId recordId
	 * @param answer   answer
	 * @return List
	 * @author tangyi
	 * @date 2020/02/19 22:50
	 */
	public PageInfo<AnswerDto> answerListInfo(String pageNum, String pageSize, Long recordId, Answer answer) {
		List<AnswerDto> answerDtos = new ArrayList<>();
		answer.setExamRecordId(recordId);
		Map<String, Object> condition = Maps.newHashMap();
		super.tenantParams(condition);
		if (recordId != null) {
			condition.put("examRecordId", recordId);
		}
		if (answer.getAnswerType() != null) {
			condition.put("answerType", answer.getAnswerType());
		}
		PageInfo<Answer> answerPageInfo = this.findPage(condition, Integer.parseInt(pageNum),
				Integer.parseInt(pageSize));
		if (CollectionUtils.isNotEmpty(answerPageInfo.getList())) {
			answerDtos = answerPageInfo.getList().stream().map(tempAnswer -> {
				AnswerDto answerDto = new AnswerDto();
				BeanUtils.copyProperties(tempAnswer, answerDto);
				SubjectDto subjectDto = subjectsService.getSubject(tempAnswer.getSubjectId());
				answerDto.setSubject(subjectDto);
				// 判断正误
				IAnswerHandler handler = handlerFactory.getHandler(subjectDto.getType());
				if (handler.hasOption()) {
					handler.judgeOptionRight(tempAnswer, subjectDto);
				} else {
					handler.judgeRight(tempAnswer, subjectDto);
				}
				return answerDto;
			}).collect(Collectors.toList());
		}
		PageInfo<AnswerDto> answerDtoPageInfo = new PageInfo<>();
		answerDtoPageInfo.setList(answerDtos);
		answerDtoPageInfo.setTotal(answerPageInfo.getTotal());
		answerDtoPageInfo.setPageNum(answerPageInfo.getPageNum());
		answerDtoPageInfo.setPageSize(answerPageInfo.getPageSize());
		return answerDtoPageInfo;
	}

	/**
	 * 根据examRecordId查询
	 * @param examRecordId examRecordId
	 * @return List
	 * @author tangyi
	 * @date 2020/2/21 1:08 下午
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
}
