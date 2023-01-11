package com.github.tangyi.exam.service;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.user.service.IDeptService;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.ExaminationType;
import com.github.tangyi.exam.excel.model.ExamRecordExcelModel;
import com.github.tangyi.exam.mapper.ExamRecordMapper;
import com.github.tangyi.exam.service.exam.ExaminationService;
import com.github.tangyi.exam.utils.ExamUtil;
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

@Slf4j
@AllArgsConstructor
@Service
public class ExamRecordService extends CrudService<ExamRecordMapper, ExaminationRecord> {

	private final ExaminationService examinationService;

	private final IUserService userService;

	private final IDeptService deptService;

	@Override
	@Cacheable(value = ExamCacheName.EXAM_RECORD, key = "#id")
	public ExaminationRecord get(Long id) {
		return super.get(id);
	}

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
				dto.setTypeLabel(ExaminationType.matchByValue(examination.getType()).getName());
				list.add(dto);
			}
		});
		this.fillExamUserInfo(list, userIds.toArray(new Long[0]));
		return list;
	}

	public PageInfo<ExaminationRecordDto> getUserExamRecords(Long userId, Map<String, Object> condition, int pageNum,
			int pageSize) {
		this.commonPageParam(condition, pageNum, pageSize);
		return new PageInfo<>(toRecordDto(this.dao.getByUserId(userId, condition)));
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_RECORD, key = "#examRecord.id")
	public int update(ExaminationRecord examRecord) {
		return super.update(examRecord);
	}

	@Override
	@Transactional
	public int insert(ExaminationRecord examRecord) {
		return super.insert(examRecord);
	}

	public List<ExaminationRecord> getByUserIdAndExaminationId(ExaminationRecord examRecord) {
		return this.dao.getByUserIdAndExaminationId(examRecord);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_RECORD, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	public void fillExamUserInfo(List<ExaminationRecordDto> list, Long[] userIds) {
		Map<Long, UserVo> userMap = userService.findUserVoListById(userIds).stream()
				.collect(Collectors.toMap(UserVo::getId, e -> e));
		List<Long> deptIds = userMap.values().stream().map(UserVo::getDeptId).distinct().collect(Collectors.toList());
		List<DeptVo> deptVos = deptService.findById(deptIds.toArray(Long[]::new));
		Map<Long, DeptVo> deptMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(deptVos)) {
			for (DeptVo dept : deptVos) {
				deptMap.put(dept.getId(), dept);
			}
		}
		list.forEach(dto -> {
			UserVo user = userMap.get(dto.getUserId());
			if (user != null) {
				dto.setUserName(user.getName());
				DeptVo dept = deptMap.get(user.getDeptId());
				if (dept != null) {
					dto.setDeptName(dept.getDeptName());
				}
			}
		});
	}

	public int findExaminationRecordCount(ExaminationRecord examinationRecord) {
		return this.dao.findExaminationRecordCount(examinationRecord);
	}

	/**
	 * 根据时间范围查询考试记录数
	 */
	public Integer findExaminationRecordCountByDate(Date start, Date end) {
		return this.dao.findExaminationRecordCountByDate(start, end);
	}

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
				ExcelToolUtil.writeExcel(request, response, ExamUtil.convertExamRecord(examRecordDtoList),
						ExamRecordExcelModel.class);
			}
		} catch (Exception e) {
			log.error("Export examRecord failed", e);
		}
	}

	/**
	 * 查询参与考试人数
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
	 */
	public ExaminationDashboardDto findExamRecordTendency(String tenantCode, int pastDays) {
		ExaminationDashboardDto dto = new ExaminationDashboardDto();
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
		dto.setExamRecordDate(Lists.newArrayList(tendencyMap.keySet()));
		dto.setExamRecordData(Lists.newArrayList(tendencyMap.values()));
		return dto;
	}
}
