package com.github.tangyi.exam.service;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.ExaminationTypeEnum;
import com.github.tangyi.exam.excel.model.ExamRecordExcelModel;
import com.github.tangyi.exam.mapper.ExamRecordMapper;
import com.github.tangyi.exam.utils.ExamRecordUtil;
import com.github.tangyi.user.service.DeptService;
import com.github.tangyi.user.service.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

	private final UserService userService;

	private final DeptService deptService;

	/**
	 * 查询考试记录
	 *
	 * @param id id
	 * @return ExamRecord
	 * @author tangyi
	 * @date 2019/1/3 14:10
	 */
	@Override
	@Cacheable(value = ExamCacheName.EXAM_RECORD, key = "#id")
	public ExaminationRecord get(Long id) {
		return super.get(id);
	}

	/**
	 * 获取分页数据
	 *
	 * @param pageNum    pageNum
	 * @param pageSize   pageSize
	 * @return PageInfo
	 * @author tangyi
	 * @date 2018/11/10 21:33
	 */
	public PageInfo<ExaminationRecordDto> examRecordList(Map<String, Object> condition, int pageNum, int pageSize) {
		PageInfo<ExaminationRecordDto> pageInfo = new PageInfo<>();
		PageInfo<ExaminationRecord> recordPageInfo = this.findPage(condition, pageNum, pageSize);
		List<ExaminationRecord> records = recordPageInfo.getList();
		pageInfo.setList(toRecordDto(records));
		pageInfo.setTotal(recordPageInfo.getTotal());
		pageInfo.setPages(recordPageInfo.getPages());
		pageInfo.setPageSize(recordPageInfo.getPageSize());
		pageInfo.setPageNum(recordPageInfo.getPageNum());
		return pageInfo;
	}

	public List<ExaminationRecordDto> toRecordDto(List<ExaminationRecord> records) {
		if (CollectionUtils.isEmpty(records)) {
			return Collections.emptyList();
		}
		List<ExaminationRecordDto> list = Lists.newArrayListWithExpectedSize(records.size());
		List<Examination> examinations = examinationService.findListById(
				records.stream().map(ExaminationRecord::getExaminationId).distinct().toArray(Long[]::new));
		final Map<Long, Examination> map = CollectionUtils.isEmpty(examinations) ?
				Maps.newHashMap() :
				examinations.stream().collect(Collectors.toMap(Examination::getId, e -> e));
		Set<Long> userIds = Sets.newHashSetWithExpectedSize(records.size());
		records.forEach(record -> {
			userIds.add(record.getUserId());
			Examination examination = map.get(record.getExaminationId());
			if (examination != null) {
				ExaminationRecordDto dto = new ExaminationRecordDto();
				BeanUtils.copyProperties(examination, dto);
				dto.setId(record.getId());
				dto.setStartTime(record.getStartTime());
				dto.setEndTime(record.getEndTime());
				dto.setScore(record.getScore());
				dto.setUserId(record.getUserId());
				dto.setExaminationId(record.getExaminationId());
				// 正确题目数
				dto.setCorrectNumber(record.getCorrectNumber());
				dto.setInCorrectNumber(record.getInCorrectNumber());
				// 提交状态
				dto.setSubmitStatus(record.getSubmitStatus());
				dto.setSubmitStatusName(
						SubmitStatusEnum.match(record.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED).getName());
				dto.setTypeLabel(ExaminationTypeEnum.matchByValue(examination.getType()).getName());
				list.add(dto);
			}
		});
		this.fillExamUserInfo(list, userIds.toArray(new Long[0]));
		return list;
	}

	/**
	 * 根据用户ID获取考试记录
	 * @param userId userId
	 * @param condition condition
	 * @return List
	 */
	public PageInfo<ExaminationRecordDto> getUserExamRecords(Long userId, Map<String, Object> condition, int pageNum,
			int pageSize) {
		this.commonPageParam(condition, pageNum, pageSize);
		return new PageInfo<>(toRecordDto(this.dao.getByUserId(userId, condition)));
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
	@CacheEvict(value = ExamCacheName.EXAM_RECORD, key = "#examRecord.id")
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
	public int insert(ExaminationRecord examRecord) {
		return super.insert(examRecord);
	}

	/**
	 * 根据用户id、考试id查找
	 *
	 * @param examRecord examRecord
	 * @return List
	 * @author tangyi
	 * @date 2018/12/26 13:58
	 */
	public List<ExaminationRecord> getByUserIdAndExaminationId(ExaminationRecord examRecord) {
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
	@CacheEvict(value = ExamCacheName.EXAM_RECORD, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	/**
	 * 获取用户、部门相关信息
	 * @param examRecordDtoList examRecordDtoList
	 * @param userIds userIds
	 */
	public void fillExamUserInfo(List<ExaminationRecordDto> examRecordDtoList, Long[] userIds) {
		List<UserVo> userVos = userService.findUserVoListById(userIds);
		// 查询部门信息
		List<DeptVo> deptVos = deptService.findById(
				userVos.stream().map(UserVo::getDeptId).distinct().toArray(Long[]::new));
		examRecordDtoList.forEach(tempExamRecordDto -> {
			// 查询、设置用户信息
			userVos.stream().filter(tempUserVo -> tempExamRecordDto.getUserId().equals(tempUserVo.getId())).findFirst()
					.ifPresent(vo -> {
						tempExamRecordDto.setUserName(vo.getName());
						// 查询、设置部门信息
						if (CollectionUtils.isNotEmpty(deptVos)) {
							deptVos.stream().filter(tempDept -> tempDept.getId().equals(vo.getDeptId())).findFirst()
									.ifPresent(examRecordDtoDeptVo -> tempExamRecordDto.setDeptName(
											examRecordDtoDeptVo.getDeptName()));
						}
					});
		});
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
	public Integer findExaminationRecordCountByDate(Date start, Date end) {
		return this.dao.findExaminationRecordCountByDate(start, end);
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
				List<Examination> examinations = examinationService.findListById(
						examRecordList.stream().map(ExaminationRecord::getExaminationId).distinct()
								.toArray(Long[]::new));
				// 用户id
				Set<Long> userIdSet = new HashSet<>();
				examRecordList.forEach(tempExamRecord -> {
					// 查找考试记录所属的考试信息
					Examination examRecordExamination = examinations.stream()
							.filter(tempExamination -> tempExamRecord.getExaminationId()
									.equals(tempExamination.getId())).findFirst().orElse(null);
					if (examRecordExamination != null) {
						ExaminationRecordDto recordDto = new ExaminationRecordDto();
						recordDto.setId(tempExamRecord.getId());
						recordDto.setExaminationName(examRecordExamination.getExaminationName());
						recordDto.setStartTime(tempExamRecord.getStartTime());
						recordDto.setEndTime(tempExamRecord.getEndTime());
						recordDto.setDuration(DateUtils.durationNoNeedMillis(tempExamRecord.getStartTime(),
								tempExamRecord.getEndTime()));
						recordDto.setScore(tempExamRecord.getScore());
						recordDto.setUserId(tempExamRecord.getUserId());
						recordDto.setCorrectNumber(tempExamRecord.getCorrectNumber());
						recordDto.setInCorrectNumber(tempExamRecord.getInCorrectNumber());
						recordDto.setSubmitStatusName(
								SubmitStatusEnum.match(tempExamRecord.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED)
										.getName());
						userIdSet.add(tempExamRecord.getUserId());
						examRecordDtoList.add(recordDto);
					}
				});
				this.fillExamUserInfo(examRecordDtoList, userIdSet.toArray(new Long[0]));
				ExcelToolUtil.writeExcel(request, response, ExamRecordUtil.convertToExcelModel(examRecordDtoList),
						ExamRecordExcelModel.class);
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
		examination.setCommonValue(SysUtil.getUser(), tenantCode);
		// 考试数量
		dashboardDto.setExaminationCount(examinationService.findExaminationCount(examination));
		// 考生数量
		dashboardDto.setExamUserCount(examinationService.findExamUserCount(examination));
		// 考试记录数量
		ExaminationRecord examinationRecord = new ExaminationRecord();
		examinationRecord.setCommonValue(examination.getCreator(), examination.getTenantCode());
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
		examination.setCommonValue(SysUtil.getUser(), tenantCode);
		Map<String, String> tendencyMap = new LinkedHashMap<>();
		pastDays = -pastDays;
		for (int i = pastDays; i <= 0; i++) {
			LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).plusDays(i);
			LocalDateTime end = LocalDateTime.now().plusDays(i);
			Date startDate = DateUtils.asDate(start);
			Date endDate = DateUtils.asDate(end);
			Integer count = findExaminationRecordCountByDate(startDate, endDate);
			tendencyMap.put(start.format(DateUtils.FORMATTER_DAY), count == null ? "0" : String.valueOf(count));
		}
		dashboardDto.setExamRecordDate(Lists.newArrayList(tendencyMap.keySet()));
		dashboardDto.setExamRecordData(Lists.newArrayList(tendencyMap.values()));
		return dashboardDto;
	}
}
