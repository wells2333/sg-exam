package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.module.Answer;
import com.github.tangyi.api.exam.module.Examination;
import com.github.tangyi.api.exam.module.ExaminationRecord;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.ResponseUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.exam.excel.model.ExamRecordExcelModel;
import com.github.tangyi.exam.mapper.AnswerMapper;
import com.github.tangyi.exam.mapper.ExamRecordMapper;
import com.github.tangyi.exam.utils.ExamRecordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考试记录service
 *
 * @author tangyi
 * @date 2018/11/8 21:20
 */
@Slf4j
@AllArgsConstructor
@Service
public class ExamRecordService extends CrudService<ExamRecordMapper, ExaminationRecord> {

	private final ExaminationService examinationService;

	private final AnswerMapper answerMapper;

	private final SubjectService subjectService;

	/**
     * 查询考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Cacheable(value = "record#" + CommonConstant.CACHE_EXPIRE, key = "#examRecord.id")
    public ExaminationRecord get(ExaminationRecord examRecord) {
        return super.get(examRecord);
    }

	/**
	 * 获取分页数据
	 *
	 * @param pageNum    pageNum
	 * @param pageSize   pageSize
	 * @param sort       sort
	 * @param order      order
	 * @param examRecord examRecord
	 * @return PageInfo
	 * @author tangyi
	 * @date 2018/11/10 21:33
	 */
    public PageInfo<ExaminationRecordDto> examRecordList(ExaminationRecord examRecord, String pageNum, String pageSize, String sort, String order) {
		examRecord.setTenantCode(SysUtil.getTenantCode());
		PageInfo<ExaminationRecordDto> examRecordDtoPageInfo = new PageInfo<>();
		List<ExaminationRecordDto> examRecordDtoList = new ArrayList<>();
		// 查询考试记录
		PageInfo<ExaminationRecord> examRecordPageInfo = this.findPage(
				PageUtil.pageInfo(pageNum, pageSize, sort, order), examRecord);
		if (CollectionUtils.isNotEmpty(examRecordPageInfo.getList())) {
			// 查询考试信息
			List<Examination> examinations = examinationService.findListById(examRecordPageInfo.getList().stream().map(ExaminationRecord::getExaminationId).distinct().toArray(Long[]::new));
			examRecordPageInfo.getList().forEach(tempExamRecord -> {
				// 找到考试记录所属的考试信息
				Examination examinationRecordExamination = examinations.stream()
						.filter(tempExamination -> tempExamRecord.getExaminationId().equals(tempExamination.getId()))
						.findFirst().orElse(null);
				// 转换成ExamRecordDto
				if (examinationRecordExamination != null) {
					ExaminationRecordDto examRecordDto = new ExaminationRecordDto();
					BeanUtils.copyProperties(examinationRecordExamination, examRecordDto);
					examRecordDto.setId(tempExamRecord.getId());
					examRecordDto.setStartTime(tempExamRecord.getStartTime());
					examRecordDto.setEndTime(tempExamRecord.getEndTime());
					examRecordDto.setScore(tempExamRecord.getScore());
					examRecordDto.setUserId(tempExamRecord.getUserId());
					examRecordDto.setExaminationId(tempExamRecord.getExaminationId());
					// 正确题目数
					examRecordDto.setCorrectNumber(tempExamRecord.getCorrectNumber());
					examRecordDto.setInCorrectNumber(tempExamRecord.getInCorrectNumber());
					// 提交状态
					examRecordDto.setSubmitStatus(tempExamRecord.getSubmitStatus());
					examRecordDtoList.add(examRecordDto);
				}
			});
			this.fillExamUserInfo(examRecordDtoList, examRecordPageInfo.getList().stream().map(ExaminationRecord::getUserId).distinct().toArray(Long[]::new));
		}
		examRecordDtoPageInfo.setTotal(examRecordPageInfo.getTotal());
		examRecordDtoPageInfo.setPages(examRecordPageInfo.getPages());
		examRecordDtoPageInfo.setPageSize(examRecordPageInfo.getPageSize());
		examRecordDtoPageInfo.setPageNum(examRecordPageInfo.getPageNum());
		examRecordDtoPageInfo.setList(examRecordDtoList);
		return examRecordDtoPageInfo;
	}

    /**
     * 更新考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#examRecord.id")
    public int update(ExaminationRecord examRecord) {
        return super.update(examRecord);
    }

    /**
     * 删除考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#examRecord.id")
    public int insert(ExaminationRecord examRecord) {
        return super.insert(examRecord);
    }

    /**
     * 根据用户id、考试id查找
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2018/12/26 13:58
     */
    public ExaminationRecord getByUserIdAndExaminationId(ExaminationRecord examRecord) {
        return this.dao.getByUserIdAndExaminationId(examRecord);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:11
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", allEntries = true)
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

	/**
	 * 获取用户、部门相关信息
	 * @param examRecordDtoList examRecordDtoList
	 * @param userIds userIds
	 */
    public void fillExamUserInfo(List<ExaminationRecordDto> examRecordDtoList, Long[] userIds) {
		// 查询用户信息
		ResponseBean<List<UserVo>> returnT =null;
		if (ResponseUtil.isSuccess(returnT)) {
			// 查询部门信息
			ResponseBean<List<DeptVo>> deptResponseBean = null;
			if (ResponseUtil.isSuccess(deptResponseBean)) {
				examRecordDtoList.forEach(tempExamRecordDto -> {
					// 查询、设置用户信息
					UserVo examRecordDtoUserVo = returnT.getData().stream()
							.filter(tempUserVo -> tempExamRecordDto.getUserId().equals(tempUserVo.getId()))
							.findFirst().orElse(null);
					if (examRecordDtoUserVo != null) {
						// 设置用户名
						tempExamRecordDto.setUserName(examRecordDtoUserVo.getName());
						// 查询、设置部门信息
						if (CollectionUtils.isNotEmpty(deptResponseBean.getData())) {
							DeptVo examRecordDtoDeptVo = deptResponseBean.getData().stream()
									// 根据部门ID过滤
									.filter(tempDept -> tempDept.getId().equals(examRecordDtoUserVo.getDeptId()))
									.findFirst().orElse(null);
							// 设置部门名称
							if (examRecordDtoDeptVo != null)
								tempExamRecordDto.setDeptName(examRecordDtoDeptVo.getDeptName());
						}
					}
				});
			}
		}
	}

	/**
	 * 查询考试记录数
	 * @param examinationRecord examinationRecord
	 * @return int
	 * @author tangyi
	 * @date 2020/1/31 5:17 下午
	 */
	public int findExaminationRecordCount(ExaminationRecord examinationRecord) {
		return this.dao.findExaminationRecordCount(examinationRecord);
	}

	/**
	 *
	 * 根据时间范围查询考试记录数
	 * @param start start
	 * @return List
	 * @author tangyi
	 * @date 2020/1/31 10:17 下午
	 */
	public List<ExaminationRecord> findExaminationRecordCountByDate(Date start) {
		return this.dao.findExaminationRecordCountByDate(start);
	}

	/**
	 * 导出
	 *
	 * @param ids ids
	 * @param request request
	 * @param response response
	 * @author tangyi
	 * @date 2018/12/31 22:28
	 */
	public void exportExamRecord(Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ExaminationRecord> examRecordList;
			if (ArrayUtils.isNotEmpty(ids)) {
				examRecordList = this.findListById(ids);
			} else {
				// 导出全部
				ExaminationRecord examRecord = new ExaminationRecord();
				examRecord.setTenantCode(SysUtil.getTenantCode());
				examRecordList = this.findList(examRecord);
			}
			// 查询考试、用户、部门数据
			if (CollectionUtils.isNotEmpty(examRecordList)) {
				List<ExaminationRecordDto> examRecordDtoList = new ArrayList<>();
				// 查询考试信息
				List<Examination> examinations = examinationService.findListById(examRecordList.stream().map(ExaminationRecord::getExaminationId).distinct().toArray(Long[]::new));
				// 用户id
				Set<Long> userIdSet = new HashSet<>();
				examRecordList.forEach(tempExamRecord -> {
					// 查找考试记录所属的考试信息
					Examination examRecordExamination = examinations.stream()
							.filter(tempExamination -> tempExamRecord.getExaminationId().equals(tempExamination.getId()))
							.findFirst().orElse(null);
					if (examRecordExamination != null) {
						ExaminationRecordDto recordDto = new ExaminationRecordDto();
						recordDto.setId(tempExamRecord.getId());
						recordDto.setExaminationName(examRecordExamination.getExaminationName());
						recordDto.setStartTime(tempExamRecord.getStartTime());
						recordDto.setEndTime(tempExamRecord.getEndTime());
						recordDto.setDuration(
								ExamRecordUtil.getExamDuration(tempExamRecord.getStartTime(), tempExamRecord.getEndTime()));
						recordDto.setScore(tempExamRecord.getScore());
						recordDto.setUserId(tempExamRecord.getUserId());
						recordDto.setCorrectNumber(tempExamRecord.getCorrectNumber());
						recordDto.setInCorrectNumber(tempExamRecord.getInCorrectNumber());
						recordDto.setSubmitStatusName(
								SubmitStatusEnum.match(tempExamRecord.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED).getName());
						userIdSet.add(tempExamRecord.getUserId());
						examRecordDtoList.add(recordDto);
					}
				});
				this.fillExamUserInfo(examRecordDtoList, userIdSet.toArray(new Long[0]));
				ExcelToolUtil.writeExcel(request, response, ExamRecordUtil.convertToExcelModel(examRecordDtoList), ExamRecordExcelModel.class);
			}
		} catch (Exception e) {
			log.error("Export examRecord failed", e);
		}
	}

	/**
	 * 查询参与考试人数
	 *
	 * @return ExaminationDashboardDto
	 * @author tangyi
	 * @date 2019/10/27 20:07:38
	 */
	public ExaminationDashboardDto findExamDashboardData(String tenantCode) {
		ExaminationDashboardDto dashboardDto = new ExaminationDashboardDto();
		Examination examination = new Examination();
		examination.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), tenantCode);
		// 考试数量
		dashboardDto.setExaminationCount(examinationService.findExaminationCount(examination));
		// 考生数量
		dashboardDto.setExamUserCount(examinationService.findExamUserCount(examination));
		// 考试记录数量
		ExaminationRecord examinationRecord = new ExaminationRecord();
		examinationRecord.setCommonValue(examination.getCreator(), examination.getApplicationCode(), examination.getTenantCode());
		dashboardDto.setExaminationRecordCount(this.findExaminationRecordCount(examinationRecord));
		return dashboardDto;
	}

	/**
	 * 查询过去n天的考试记录数据
	 * @param tenantCode tenantCode
	 * @param pastDays pastDays
	 * @return ExaminationDashboardDto
	 * @author tangyi
	 * @date 2020/1/31 5:46 下午
	 */
	public ExaminationDashboardDto findExamRecordTendency(String tenantCode, int pastDays) {
		ExaminationDashboardDto dashboardDto = new ExaminationDashboardDto();
		Examination examination = new Examination();
		examination.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), tenantCode);
		Map<String, String> tendencyMap = new LinkedHashMap<>();
		LocalDateTime start = null;
		pastDays = -pastDays;
		for (int i = pastDays; i <= 0; i++) {
			LocalDateTime localDateTime = DateUtils.plusDay(i);
			if (i == pastDays) {
				start = localDateTime;
			}
			tendencyMap.put(localDateTime.format(DateUtils.FORMATTER_DAY), "0");
		}
		List<ExaminationRecord> examinationRecords = this.findExaminationRecordCountByDate(DateUtils.asDate(start));
		if (CollectionUtils.isNotEmpty(examinationRecords)) {
			Map<String, List<ExaminationRecord>> examinationRecordsMap = examinationRecords.stream()
					.peek(examinationRecord -> examinationRecord
							.setExt(DateUtils.asLocalDateTime(examinationRecord.getCreateDate())
									.format(DateUtils.FORMATTER_DAY)))
					.collect(Collectors.groupingBy(ExaminationRecord::getExt));
			log.info("ExamRecordTendency map: {}", examinationRecordsMap);
			examinationRecordsMap.forEach((key, value) -> tendencyMap.replace(key, String.valueOf(value.size())));
		}
		dashboardDto.setExamRecordDate(new ArrayList<>(tendencyMap.keySet()));
		dashboardDto.setExamRecordData(new ArrayList<>(tendencyMap.values()));
		return dashboardDto;
	}

	/**
	 * 成绩详情
	 * @param id id
	 * @return ExaminationRecordDto
	 * @author tangyi
	 * @date 2020/2/21 9:26 上午
	 */
	public ExaminationRecordDto details(Long id) {
		ExaminationRecord examRecord = this.get(id);
		if (examRecord == null)
			throw new CommonException("ExamRecord is not exist");
		Examination examination = examinationService.get(examRecord.getExaminationId());
		if (examination == null)
			throw new CommonException("Examination is not exist");
		ExaminationRecordDto examRecordDto = new ExaminationRecordDto();
		BeanUtils.copyProperties(examination, examRecordDto);
		examRecordDto.setId(examRecord.getId());
		examRecordDto.setStartTime(examRecord.getStartTime());
		examRecordDto.setEndTime(examRecord.getEndTime());
		examRecordDto.setScore(examRecord.getScore());
		examRecordDto.setUserId(examRecord.getUserId());
		examRecordDto.setExaminationId(examRecord.getExaminationId());
		examRecordDto.setDuration(
				ExamRecordUtil.getExamDuration(examRecord.getStartTime(), examRecord.getEndTime()));
		// 正确题目数
		examRecordDto.setCorrectNumber(examRecord.getCorrectNumber());
		examRecordDto.setInCorrectNumber(examRecord.getInCorrectNumber());
		// 提交状态
		examRecordDto.setSubmitStatus(examRecord.getSubmitStatus());
		// 答题列表
		List<Answer> answers = answerMapper.findListByExamRecordId(examRecord.getId());
		if (CollectionUtils.isNotEmpty(answers)) {
			List<AnswerDto> answerDtos = answers.stream().map(answer -> {
				AnswerDto answerDto = new AnswerDto();
				BeanUtils.copyProperties(answer, answerDto);
				SubjectDto subjectDto = subjectService.get(answer.getSubjectId(), answer.getType());
				answerDto.setSubject(subjectDto);
				answerDto.setDuration(ExamRecordUtil.getExamDuration(answer.getStartTime(), answer.getEndTime()));
				return answerDto;
			}).collect(Collectors.toList());
			examRecordDto.setAnswers(answerDtos);
		}
		this.fillExamUserInfo(Collections.singletonList(examRecordDto), new Long[] {examRecord.getUserId()});
		return examRecordDto;
	}
}
