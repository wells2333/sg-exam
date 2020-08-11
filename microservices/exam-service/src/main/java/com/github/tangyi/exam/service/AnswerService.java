package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.RankInfoDto;
import com.github.tangyi.api.exam.dto.StartExamDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.module.Answer;
import com.github.tangyi.api.exam.module.Examination;
import com.github.tangyi.api.exam.module.ExaminationRecord;
import com.github.tangyi.api.exam.module.ExaminationSubject;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AnswerHandleResult;
import com.github.tangyi.exam.handler.impl.ChoicesAnswerHandler;
import com.github.tangyi.exam.handler.impl.JudgementAnswerHandler;
import com.github.tangyi.exam.handler.impl.MultipleChoicesAnswerHandler;
import com.github.tangyi.exam.handler.impl.ShortAnswerHandler;
import com.github.tangyi.exam.mapper.AnswerMapper;
import com.github.tangyi.exam.utils.AnswerHandlerUtil;
import com.github.tangyi.exam.utils.ExamRecordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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

    private final SubjectService subjectService;

    private final ExamRecordService examRecordService;

    private final ExaminationService examinationService;

    private final ExaminationSubjectService examinationSubjectService;

    private final ChoicesAnswerHandler choicesHandler;

	private final MultipleChoicesAnswerHandler multipleChoicesHandler;

	private final JudgementAnswerHandler judgementHandler;

	private final ShortAnswerHandler shortAnswerHandler;

	private final RedisTemplate<String, String> redisTemplate;

	/**
     * 查找答题
     *
     * @param answer answer
     * @return Answer
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Cacheable(value = "answer#" + CommonConstant.CACHE_EXPIRE, key = "#answer.id")
    public Answer get(Answer answer) {
        return super.get(answer);
    }

    /**
     * 根据用户ID、考试ID、考试记录ID、题目ID查找答题
     *
     * @param answer answer
     * @return Answer
     * @author tangyi
     * @date 2019/01/21 19:41
     */
    public Answer getAnswer(Answer answer) {
        return this.dao.getAnswer(answer);
    }

    /**
     * 更新答题
     *
     * @param answer answer
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Transactional
    @CacheEvict(value = "answer", key = "#answer.id")
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
    @CacheEvict(value = "answer", key = "#answer.id")
    public int updateScore(Answer answer) {
        answer.setAnswer(AnswerHandlerUtil.replaceComma(answer.getAnswer()));
        // 加分减分逻辑
        Answer oldAnswer = this.get(answer);
        if (!oldAnswer.getAnswerType().equals(answer.getAnswerType())) {
            ExaminationRecord record = new ExaminationRecord();
            record.setId(oldAnswer.getExamRecordId());
            record = examRecordService.get(record);
            if (record == null) {
                throw new CommonException("ExamRecord is null");
            }
            Double oldScore = record.getScore();
            if (AnswerConstant.RIGHT.equals(answer.getAnswerType())) {
                // 加分
                record.setCorrectNumber(record.getInCorrectNumber() + 1);
                record.setInCorrectNumber(record.getInCorrectNumber() - 1);
                record.setScore(record.getScore() + answer.getScore());
            } else if (AnswerConstant.WRONG.equals(answer.getAnswerType())) {
                // 减分
                record.setCorrectNumber(record.getInCorrectNumber() - 1);
                record.setInCorrectNumber(record.getInCorrectNumber() + 1);
                record.setScore(record.getScore() - answer.getScore());
            }
            if (examRecordService.update(record) > 0) {
                log.info("Update answer success, examRecordId: {}, oldScore: {}, newScore: {}", oldAnswer.getExamRecordId(), oldScore, record.getScore());
            }
        }
        return super.update(answer);
    }

    /**
     * 删除答题
     *
     * @param answer answer
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Transactional
    @CacheEvict(value = "answer", key = "#answer.id")
    public int delete(Answer answer) {
        return super.delete(answer);
    }

    /**
     * 批量删除答题
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Transactional
    @CacheEvict(value = "answer", allEntries = true)
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 保存
     *
     * @param answer answer
     * @return int
     * @author tangyi
     * @date 2019/04/30 18:03
     */
    @Transactional
    public int save(Answer answer) {
        answer.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
		answer.setAnswer(AnswerHandlerUtil.replaceComma(answer.getAnswer()));
		return super.save(answer);
    }

    /**
     * 保存答题，返回下一题信息
     *
     * @param answerDto       answerDto
     * @param type            0：下一题，1：上一题
     * @param nextSubjectId   nextSubjectId
     * @param nextSubjectType 下一题的类型，选择题、判断题
     * @return SubjectDto
     * @author tangyi
     * @date 2019/05/01 11:42
     */
    @Transactional
    public SubjectDto saveAndNext(AnswerDto answerDto, Integer type, Long nextSubjectId, Integer nextSubjectType) {
		String userCode = SysUtil.getUser();
		String sysCode = SysUtil.getSysCode();
		String tenantCode = SysUtil.getTenantCode();
		if (this.save(answerDto, userCode, sysCode, tenantCode) > 0) {
			// 查询下一题
			return this.subjectAnswer(answerDto.getSubjectId(), answerDto.getExamRecordId(),
					type, nextSubjectId, nextSubjectType);
		}
        return null;
    }

	/**
	 * 保存答题
	 *
	 * @param answerDto       answerDto
	 * @param userCode       userCode
	 * @param sysCode       sysCode
	 * @param tenantCode       tenantCode
	 * @return int
	 * @author tangyi
	 * @date 2019/05/01 11:42
	 */
	@Transactional
	public int save(AnswerDto answerDto, String userCode, String sysCode, String tenantCode) {
		Answer answer = new Answer();
		BeanUtils.copyProperties(answerDto, answer);
		Answer tempAnswer = this.getAnswer(answer);
		if (tempAnswer != null) {
			tempAnswer.setCommonValue(userCode, sysCode, tenantCode);
			tempAnswer.setAnswer(answer.getAnswer());
			tempAnswer.setType(answer.getType());
			tempAnswer.setEndTime(tempAnswer.getModifyDate());
			return this.update(tempAnswer);
		} else {
			answer.setCommonValue(userCode, sysCode, tenantCode);
			answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
			answer.setAnswerType(AnswerConstant.WRONG);
			answer.setEndTime(answer.getModifyDate());
			return this.insert(answer);
		}
	}

    /**
     * 提交答卷，自动统计选择题得分
     *
     * @param answer answer
     * @author tangyi
     * @date 2018/12/26 14:09
     */
    @Transactional
    public void submit(Answer answer) {
        long start = System.currentTimeMillis();
        String currentUsername = answer.getModifier();
        // 查找已提交的题目
        List<Answer> answerList = findList(answer);
        if (CollectionUtils.isEmpty(answerList))
            return;
        // 成绩
        ExaminationRecord record = new ExaminationRecord();
        // 分类题目
        Map<String, List<Answer>> distinctAnswer = this.distinctAnswer(answerList);
		AnswerHandleResult result = handleAll(distinctAnswer);
		// 记录总分、正确题目数、错误题目数
		record.setScore(result.getScore());
		record.setCorrectNumber(result.getCorrectNum());
		record.setInCorrectNumber(result.getInCorrectNum());
		// 更新答题状态
		distinctAnswer.values().forEach(answers -> answers.forEach(this::update));
        // 更新状态为统计完成，否则需要阅卷完成后才更改统计状态
        record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
        // 保存成绩
        record.setCommonValue(currentUsername, SysUtil.getSysCode(), SysUtil.getTenantCode());
        record.setId(answer.getExamRecordId());
        record.setEndTime(record.getCreateDate());
        examRecordService.update(record);
        // 更新排名数据
		updateRank(record);
        log.debug("Submit examination, username: {}，time consuming: {}ms", currentUsername, System.currentTimeMillis() - start);
    }

    /**
     * 更新排名信息
	 * 基于Redis的sort set数据结构
     * @param record record
     * @author tangyi
     * @date 2019/12/8 23:21
     */
    private void updateRank(ExaminationRecord record) {
		redisTemplate.opsForZSet().add(AnswerConstant.CACHE_PREFIX_RANK + record.getExaminationId(), JsonMapper.getInstance().toJson(record), record.getScore());
	}

    /**
     * 通过mq异步处理
     * 1. 先发送消息
     * 2. 发送消息成功，更新提交状态，发送失败，返回提交失败
     * 3. 消费消息，计算成绩
     *
     * @param answer answer
     * @return boolean
     * @author tangyi
     * @date 2019/05/03 14:35
     */
    @Transactional
    public boolean submitAsync(Answer answer) {
        long start = System.currentTimeMillis();
        String currentUsername = SysUtil.getUser();
        String applicationCode = SysUtil.getSysCode();
        String tenantCode = SysUtil.getTenantCode();
        answer.setModifier(currentUsername);
        answer.setApplicationCode(applicationCode);
        answer.setTenantCode(tenantCode);

        ExaminationRecord examRecord = new ExaminationRecord();
        examRecord.setCommonValue(currentUsername, applicationCode, tenantCode);
        examRecord.setId(answer.getExamRecordId());
        // 提交时间
        examRecord.setEndTime(examRecord.getCreateDate());
        examRecord.setSubmitStatus(SubmitStatusEnum.SUBMITTED.getValue());
        // 1. 发送消息
        //amqpTemplate.convertAndSend(MqConstant.SUBMIT_EXAMINATION_QUEUE, answer);
        // 2. 更新考试状态
        boolean success = examRecordService.update(examRecord) > 0;
		log.debug("Submit examination, username: {}，time consuming: {}ms", currentUsername, System.currentTimeMillis() - start);
		return success;
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
        String currentUsername = SysUtil.getUser();
        // 创建考试记录
        if (examRecord.getExaminationId() == null)
            throw new CommonException("参数校验失败，考试id为空！");
        if (examRecord.getUserId() == null)
            throw new CommonException("参数校验失败，用户id为空！");
		return this.start(examRecord.getUserId(), currentUsername, examRecord.getExaminationId(), SysUtil.getSysCode(), SysUtil.getTenantCode());
    }

    /**
     * 查询题目和答题
     *
     * @param subjectId       subjectId
     * @param examRecordId    examRecordId
     * @param nextType        -1：当前题目，0：下一题，1：上一题
     * @param nextSubjectId   nextSubjectId
     * @param nextSubjectType 下一题的类型，选择题、判断题
     * @return SubjectDto
     * @author tangyi
     * @date 2019/04/30 17:10
     */
    @Transactional
    public SubjectDto subjectAnswer(Long subjectId, Long examRecordId, Integer nextType, Long nextSubjectId, Integer nextSubjectType) {
		// 查找考试记录
    	ExaminationRecord examRecord = examRecordService.get(examRecordId);
        if (examRecord == null)
            throw new CommonException("考试记录不存在.");

        // 考试ID，题目ID查找关联关系
        ExaminationSubject examinationSubject = new ExaminationSubject();
        examinationSubject.setExaminationId(examRecord.getExaminationId());
        examinationSubject.setSubjectId(subjectId);
        PageInfo<ExaminationSubject> examinationSubjectPageInfo = examinationSubjectService.findPage(
                PageUtil.pageInfo(CommonConstant.PAGE_NUM_DEFAULT, CommonConstant.PAGE_SIZE_DEFAULT, "id",
                        CommonConstant.PAGE_ORDER_DEFAULT), examinationSubject);
        if (CollectionUtils.isEmpty(examinationSubjectPageInfo.getList()))
            throw new CommonException("序号为" + subjectId + "的题目不存在.");

        // 查询下一题
        SubjectDto subject;
        if (nextSubjectId != null) {
            subject = subjectService.get(nextSubjectId, nextSubjectType);
        } else {
            subject = subjectService.getNextByCurrentIdAndType(examRecord.getExaminationId(), subjectId, examinationSubjectPageInfo.getList().get(0).getType(), nextType);
        }
        if (subject == null) {
            log.error("Subject does not exist: {}", subjectId);
            return null;
        }

        // 查找答题
        Answer answer = new Answer();
        answer.setSubjectId(subject.getId());
        answer.setExamRecordId(examRecordId);
        Answer userAnswer = this.getAnswer(answer);
        userAnswer = userAnswer == null ? new Answer() : userAnswer;
        // 设置答题
        subject.setAnswer(userAnswer);
        subject.setExaminationRecordId(examRecordId);
        return subject;
    }

    /**
     * 分类答题
     *
     * @param answers answers
     * @return Map
     * @author tangyi
     * @date 2019/06/18 16:32
     */
    private Map<String, List<Answer>> distinctAnswer(List<Answer> answers) {
        Map<String, List<Answer>> distinctMap = new HashMap<>();
        answers.stream().collect(Collectors.groupingBy(Answer::getType, Collectors.toList())).forEach((type, temp) -> {
            // 匹配类型
            SubjectTypeEnum subjectType = SubjectTypeEnum.matchByValue(type);
            if (subjectType != null) {
                switch (subjectType) {
                    case CHOICES:
                        distinctMap.put(SubjectTypeEnum.CHOICES.name(), temp);
                        break;
					case MULTIPLE_CHOICES:
						distinctMap.put(SubjectTypeEnum.MULTIPLE_CHOICES.name(), temp);
						break;
                    case SHORT_ANSWER:
                        distinctMap.put(SubjectTypeEnum.SHORT_ANSWER.name(), temp);
                        break;
					case JUDGEMENT:
						distinctMap.put(SubjectTypeEnum.JUDGEMENT.name(), temp);
						break;
					default:
						break;
                }
            }
        });
        return distinctMap;
    }

    /**
     * 答题详情
     *
     * @param recordId         recordId
     * @param currentSubjectId currentSubjectId
     * @param nextSubjectType  nextSubjectType
     * @param nextType         nextType
     * @return AnswerDto
     * @author tangyi
     * @date 2019/06/18 23:05
     */
    public AnswerDto answerInfo(Long recordId, Long currentSubjectId, Integer nextSubjectType, Integer nextType) {
        ExaminationRecord record = examRecordService.get(recordId);
        SubjectDto subjectDto;
        // 题目为空，则加载第一题
        if (currentSubjectId == null) {
            subjectDto = subjectService.findFirstSubjectByExaminationId(record.getExaminationId());
        } else {
            ExaminationSubject examinationSubject = new ExaminationSubject();
            examinationSubject.setExaminationId(record.getExaminationId());
            examinationSubject.setSubjectId(currentSubjectId);

            // 查询该考试和指定序号的题目的关联信息
            // 下一题
            if (AnswerConstant.NEXT.equals(nextType)) {
                examinationSubject = examinationSubjectService.getByPreviousId(examinationSubject);
            } else if (AnswerConstant.PREVIOUS.equals(nextType)) {
                // 上一题
                examinationSubject = examinationSubjectService.getPreviousByCurrentId(examinationSubject);
            } else {
                examinationSubject = examinationSubjectService.findByExaminationIdAndSubjectId(examinationSubject);
            }
            if (examinationSubject == null)
                throw new CommonException("ID为" + currentSubjectId + "的题目不存在");
            // 查询题目的详细信息
            subjectDto = subjectService.get(examinationSubject.getSubjectId(), examinationSubject.getType());
        }
        AnswerDto answerDto = new AnswerDto();
        answerDto.setSubject(subjectDto);
        // 查询答题
        Answer answer = new Answer();
        answer.setSubjectId(subjectDto.getId());
        answer.setExamRecordId(recordId);
        Answer userAnswer = this.getAnswer(answer);
        if (userAnswer == null)
            userAnswer = answer;
        BeanUtils.copyProperties(userAnswer, answerDto);
        answerDto.setDuration(ExamRecordUtil.getExamDuration(userAnswer.getStartTime(), userAnswer.getEndTime()));
        // 判断正误
        SubjectTypeEnum subjectType = SubjectTypeEnum.matchByValue(subjectDto.getType());
        if (subjectType != null) {
            switch (subjectType) {
                case CHOICES:
                    choicesHandler.judgeOptionRight(userAnswer, subjectDto);
                    break;
                case MULTIPLE_CHOICES:
                    multipleChoicesHandler.judgeOptionRight(userAnswer, subjectDto);
                    break;
                case SHORT_ANSWER:
                    shortAnswerHandler.judgeRight(userAnswer, subjectDto);
                    break;
                case JUDGEMENT:
                    judgementHandler.judgeRight(userAnswer, subjectDto);
                    break;
                default:
                    break;
            }
        }
        ResponseBean<List<UserVo>> userVoResponseBean = null;
        if (ResponseUtil.isSuccess(userVoResponseBean) && CollectionUtils.isNotEmpty(userVoResponseBean.getData())) {
            UserVo userVo = userVoResponseBean.getData().get(0);
            answerDto.setUserName(userVo.getName());
        }
        return answerDto;
    }

    /**
     * 完成批改
     *
     * @param examRecord examRecord
     * @return Boolean
     * @author tangyi
     * @date 2019/06/19 14:44
     */
    public Boolean completeMarking(ExaminationRecord examRecord) {
        long start = System.currentTimeMillis();
        examRecord = examRecordService.get(examRecord);
        if (examRecord == null)
            throw new CommonException("考试记录不存在.");
        Answer answer = new Answer();
        answer.setExamRecordId(examRecord.getId());
        List<Answer> answers = this.findList(answer);
        if (CollectionUtils.isNotEmpty(answers)) {
            long correctNumber = answers.stream()
                    .filter(tempAnswer -> tempAnswer.getAnswerType().equals(AnswerConstant.RIGHT)).count();
            // 总分
            Double score = answers.stream().mapToDouble(Answer::getScore).sum();
            examRecord.setScore(score);
            examRecord.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
            examRecord.setCorrectNumber((int) correctNumber);
            examRecord.setInCorrectNumber(answers.size() - examRecord.getCorrectNumber());
            examRecordService.update(examRecord);
            log.debug("Submit done, username: {}, examinationId: {}, score: {}, time consuming: {}ms", examRecord.getCreator(), examRecord.getExaminationId(),
                    score, System.currentTimeMillis() - start);
        }
        return Boolean.TRUE;
    }

    /**
     * 获取排名数据
     * @param recordId recordId
     * @return List
     * @author tangyi
     * @date 2019/12/8 23:36
     */
	public List<RankInfoDto> getRankInfo(Long recordId) {
		List<RankInfoDto> rankInfos = new ArrayList<>();
		// 查询缓存
		Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet()
				.reverseRangeByScoreWithScores(AnswerConstant.CACHE_PREFIX_RANK + recordId, 0, Integer.MAX_VALUE);
		if (typedTuples != null) {
			// 用户ID列表
			Set<Long> userIds = new HashSet<>();
			typedTuples.forEach(typedTuple -> {
				ExaminationRecord record = JsonMapper.getInstance()
						.fromJson(typedTuple.getValue(), ExaminationRecord.class);
				if (record != null) {
					RankInfoDto rankInfo = new RankInfoDto();
					rankInfo.setUserId(record.getUserId());
					userIds.add(record.getUserId());
					rankInfo.setScore(typedTuple.getScore());
					rankInfos.add(rankInfo);
				}
			});
			if (!userIds.isEmpty()) {
				ResponseBean<List<UserVo>> userResponse = null;
				if (ResponseUtil.isSuccess(userResponse)) {
					rankInfos.forEach(rankInfo -> {
						userResponse.getData().stream().filter(user -> user.getId().equals(rankInfo.getUserId()))
								.findFirst().ifPresent(user -> {
							// 设置考生信息
							rankInfo.setName(user.getName());
							rankInfo.setAvatarUrl(user.getAvatarUrl());
						});
					});
				}
			}
		}
		return rankInfos;
	}

    /**
     * 获取错题列表
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param recordId recordId
     * @param answer   answer
     * @return List
     * @author tangyi
     * @date 2020/02/19 22:50
     */
	public PageInfo<AnswerDto> answerListInfo(String pageNum, String pageSize, String sort, String order, Long recordId, Answer answer) {
        List<AnswerDto> answerDtos = new ArrayList<>();
        answer.setExamRecordId(recordId);
        PageInfo<Answer> answerPageInfo = this.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), answer);
        if (CollectionUtils.isNotEmpty(answerPageInfo.getList())) {
            answerDtos = answerPageInfo.getList().stream().map(tempAnswer -> {
                AnswerDto answerDto = new AnswerDto();
                BeanUtils.copyProperties(tempAnswer, answerDto);
                SubjectDto subjectDto = subjectService.get(tempAnswer.getSubjectId(), tempAnswer.getType());
                answerDto.setSubject(subjectDto);
                // 判断正误
                SubjectTypeEnum subjectType = SubjectTypeEnum.matchByValue(subjectDto.getType());
                if (subjectType != null) {
                    switch (subjectType) {
                        case CHOICES:
                            choicesHandler.judgeOptionRight(tempAnswer, subjectDto);
                            break;
                        case MULTIPLE_CHOICES:
                            multipleChoicesHandler.judgeOptionRight(tempAnswer, subjectDto);
                            break;
                        case SHORT_ANSWER:
                            shortAnswerHandler.judgeRight(tempAnswer, subjectDto);
                            break;
                        case JUDGEMENT:
                            judgementHandler.judgeRight(tempAnswer, subjectDto);
                            break;
                        default:
                            break;
                    }
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

    /**
     * 移动端提交答题
     * @param examinationId examinationId
     * @return ResponseBean
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
        String sysCode = SysUtil.getSysCode();
        Date currentDate = DateUtils.asDate(LocalDateTime.now());
        // 判断用户是否存在，不存在则自动创建
        ResponseBean<UserVo> userVoResponseBean = null;
        if (!ResponseUtil.isSuccess(userVoResponseBean) || userVoResponseBean.getData() == null) {
            return false;
        }
        // TODO 自动注册账号
        UserVo user = userVoResponseBean.getData();
        // 保存考试记录
        ExaminationRecord record = new ExaminationRecord();
        record.setCommonValue(identifier, sysCode, tenantCode);
        record.setUserId(user.getUserId());

        // 初始化Answer
        List<Answer> answers = new ArrayList<>(subjectDtos.size());
        subjectDtos.forEach(subjectDto -> {
            Answer answer = new Answer();
            answer.setCommonValue(identifier, sysCode, tenantCode);
            answer.setAnswer(subjectDto.getAnswer().getAnswer());
            answer.setExamRecordId(record.getId());
            answer.setEndTime(currentDate);
            answer.setSubjectId(subjectDto.getId());
            answer.setType(subjectDto.getType());
            answer.setAnswerType(AnswerConstant.WRONG);
            answers.add(answer);
        });
        // 分类题目
        Map<String, List<Answer>> distinctAnswer = this.distinctAnswer(answers);
        AnswerHandleResult result = handleAll(distinctAnswer);
        // 记录总分、正确题目数、错误题目数
        record.setScore(result.getScore());
        record.setCorrectNumber(result.getCorrectNum());
        record.setInCorrectNumber(result.getInCorrectNum());
        // 更新状态为统计完成，否则需要阅卷完成后才更改统计状态
        record.setExaminationId(examinationId);
        record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
        record.setStartTime(currentDate);
        record.setEndTime(currentDate);
        examRecordService.insert(record);
        answers.forEach(this::insert);
        log.info("AnonymousUser submit, examinationId:{}, identifier: {}, time consuming: {}ms", examinationId, identifier, System.currentTimeMillis() - start);
        return true;
    }

    /**
     * 自动判分
     * @param distinctAnswer distinctAnswer
     * @return ResponseBean
     * @author tangyi
     * @date 2020/03/15 16:21
     */
    public AnswerHandleResult handleAll(Map<String, List<Answer>> distinctAnswer) {
        // 暂时只自动统计单选题、多选题、判断题，简答题由老师阅卷批改
        AnswerHandleResult choiceResult = choicesHandler.handle(distinctAnswer.get(SubjectTypeEnum.CHOICES.name()));
        AnswerHandleResult multipleResult = multipleChoicesHandler.handle(distinctAnswer.get(SubjectTypeEnum.MULTIPLE_CHOICES.name()));
        AnswerHandleResult judgementResult = judgementHandler.handle(distinctAnswer.get(SubjectTypeEnum.JUDGEMENT.name()));
        AnswerHandleResult shortAnswerResult = shortAnswerHandler.handle(distinctAnswer.get(SubjectTypeEnum.SHORT_ANSWER.name()));
        return AnswerHandlerUtil.addAll(Arrays.asList(choiceResult, multipleResult, judgementResult, shortAnswerResult));
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
        String applicationCode = SysUtil.getSysCode();
        String tenantCode = SysUtil.getTenantCode();
        // 创建考试记录
        if (examinationId == null)
            throw new CommonException("参数校验失败，考试id为空！");
        if (identifier == null)
            throw new CommonException("参数校验失败，用户identifier为空！");
        // 查询用户信息
        ResponseBean<UserVo> userVoResponseBean = null;
        if (!ResponseUtil.isSuccess(userVoResponseBean)) {
            throw new CommonException("获取用户" + identifier + "信息失败！");
        }
        return this.start(userVoResponseBean.getData().getUserId(), identifier, examinationId, applicationCode, tenantCode);
    }

    /**
     * 开始考试
     *
     * @param userId userId
     * @param identifier identifier
     * @param examinationId examinationId
     * @param applicationCode applicationCode
     * @param tenantCode tenantCode
     * @return StartExamDto
     * @author tangyi
     * @date 2019/04/30 23:06
     */
    @Transactional
    public StartExamDto start(Long userId, String identifier, Long examinationId, String applicationCode, String tenantCode) {
        StartExamDto startExamDto = new StartExamDto();
        // 查找考试信息
        Examination examination = examinationService.get(examinationId);
        ExaminationRecord examRecord = new ExaminationRecord();
        examRecord.setCommonValue(identifier, applicationCode, tenantCode);
        examRecord.setUserId(userId);
        examRecord.setExaminationId(examinationId);
        examRecord.setStartTime(examRecord.getCreateDate());

        // 默认未提交状态
        examRecord.setSubmitStatus(SubmitStatusEnum.NOT_SUBMITTED.getValue());
        // 保存考试记录
        if (examRecordService.insert(examRecord) > 0) {
            startExamDto.setExamination(examination);
            startExamDto.setExamRecord(examRecord);
            // 根据题目ID，类型获取第一题的详细信息
            SubjectDto subjectDto = subjectService.findFirstSubjectByExaminationId(examRecord.getExaminationId());
            startExamDto.setSubjectDto(subjectDto);
            // 创建第一题的答题
            Answer answer = new Answer();
            answer.setCommonValue(identifier, applicationCode, tenantCode);
            answer.setExamRecordId(examRecord.getId());
            answer.setSubjectId(subjectDto.getId());
            // 默认待批改状态
            answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
            answer.setAnswerType(AnswerConstant.WRONG);
            answer.setStartTime(answer.getCreateDate());
            // 保存答题
            this.save(answer);
            subjectDto.setAnswer(answer);
        }
        return startExamDto;
    }
}
