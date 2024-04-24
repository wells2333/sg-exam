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

package com.github.tangyi.exam.service;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.exam.service.IExamRecordService;
import com.github.tangyi.api.user.service.IDeptService;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.ExaminationType;
import com.github.tangyi.exam.excel.ExamRecordModel;
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
public class ExamRecordService extends CrudService<ExamRecordMapper, ExaminationRecord> implements IExamRecordService {

	private final ExaminationService examinationService;
	private final IUserService userService;
	private final IDeptService deptService;

	@Override
	@Cacheable(value = ExamCacheName.EXAM_RECORD, key = "#id")
	public ExaminationRecord get(Long id) {
		return super.get(id);
	}

	@Override
	public PageInfo<ExaminationRecordDto> examRecordList(Map<String, Object> condition, int pageNum, int pageSize) {
		PageInfo<ExaminationRecordDto> pageInfo = new PageInfo<>();
		PageInfo<ExaminationRecord> recordPageInfo = this.findPage(condition, pageNum, pageSize);
		List<ExaminationRecord> records = recordPageInfo.getList();
		PageUtil.copyProperties(recordPageInfo, pageInfo);
		pageInfo.setList(toRecordDto(records));
		return pageInfo;
	}

	@Override
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

	@Override
	public List<ExaminationRecord> getByUserIdAndExaminationId(ExaminationRecord examRecord) {
		return this.dao.getByUserIdAndExaminationId(examRecord);
	}

	@Override
	public List<ExaminationRecord> getByExaminationId(Long examinationId) {
		return this.dao.getByExaminationId(examinationId);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_RECORD, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Override
	public void fillExamUserInfo(List<ExaminationRecordDto> list, Long[] userIds) {
		try {
			Map<Long, UserVo> userMap = userService.findUserVoListById(userIds).stream()
					.collect(Collectors.toMap(UserVo::getId, e -> e));
			List<Long> deptIds = userMap.values().stream().map(UserVo::getDeptId).distinct().toList();
			List<DeptVo> deptVos = deptService.findById(deptIds.toArray(Long[]::new));
			Map<Long, DeptVo> deptMap = Maps.newHashMap();
			if (CollectionUtils.isNotEmpty(deptVos)) {
				for (DeptVo dept : deptVos) {
					deptMap.put(dept.getId(), dept);
				}
			}

			for (ExaminationRecordDto dto : list) {
				UserVo user = userMap.get(dto.getUserId());
				if (user != null) {
					dto.setUserName(user.getName());
					DeptVo dept = deptMap.get(user.getDeptId());
					if (dept != null) {
						dto.setDeptName(dept.getDeptName());
					}
				} else {
					dto.setUserName(String.valueOf(dto.getUserId()));
					dto.setDeptName("-");
				}
			}
		} catch (Exception e) {
			log.error("Failed to fill exam userInfo.", e);
		}
	}

	@Override
	public int findExaminationRecordCount(ExaminationRecord examinationRecord) {
		return this.dao.findExaminationRecordCount(examinationRecord);
	}

	/**
	 * 根据时间范围查询考试记录数
	 */
	@Override
	public Integer findExaminationRecordCountByDate(Date start, Date end) {
		return this.dao.findExaminationRecordCountByDate(start, end);
	}

	@Override
	public void exportExamRecord(Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ExaminationRecord> list;
			if (ArrayUtils.isNotEmpty(ids)) {
				list = this.getByExaminationId(ids[0]);
			} else {
				// 导出全部
				ExaminationRecord record = new ExaminationRecord();
				record.setTenantCode(SysUtil.getTenantCode());
				list = this.findList(record);
			}

			// 查询考试、用户、部门数据
			if (CollectionUtils.isNotEmpty(list)) {
				List<ExaminationRecordDto> dtoList = new ArrayList<>();
				// 查询考试信息
				List<Examination> examinations = examinationService.findListById(
						list.stream().map(ExaminationRecord::getExaminationId).distinct().toArray(Long[]::new));
				// 用户 ID
				Set<Long> userIdSet = new HashSet<>();
				list.forEach(temp -> {
					// 查找考试记录所属的考试信息
					Examination e = examinations.stream()    //
							.filter(t -> temp.getExaminationId().equals(t.getId()))    //
							.findFirst().orElse(null);    //
					if (e != null) {
						ExaminationRecordDto dto = new ExaminationRecordDto();
						dto.setId(temp.getId());
						dto.setExaminationId(e.getId());
						dto.setExaminationName(e.getExaminationName());
						dto.setStartTime(temp.getStartTime());
						dto.setEndTime(temp.getEndTime());
						dto.setDuration(DateUtils.formatDurationV2(DateUtils.calculateDuration(temp.getStartTime(), temp.getEndTime()), false));
						dto.setScore(temp.getScore());
						dto.setUserId(temp.getUserId());
						dto.setCorrectNumber(temp.getCorrectNumber());
						dto.setInCorrectNumber(temp.getInCorrectNumber());
						dto.setSubmitStatus(temp.getSubmitStatus());
						dto.setSubmitStatusName(
								SubmitStatusEnum.match(temp.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED)
										.getName());
						userIdSet.add(temp.getUserId());
						dtoList.add(dto);
					}
				});
				this.fillExamUserInfo(dtoList, userIdSet.toArray(new Long[0]));
				ExcelToolUtil.writeExcel(request, response, ExamUtil.convertExamRecord(dtoList), ExamRecordModel.class);
			}
		} catch (Exception e) {
			log.error("Export examRecord failed", e);
		}
	}

	/**
	 * 查询参与考试人数
	 */
	@Override
	public ExaminationDashboardDto findExamDashboardData(String tenantCode) {
		ExaminationDashboardDto dto = new ExaminationDashboardDto();
		Examination e = new Examination();
		e.setCommonValue(SysUtil.getUser(), tenantCode);
		// 考试数量
		dto.setExaminationCount(examinationService.findExaminationCount(e));
		// 考生数量
		dto.setExamUserCount(examinationService.findExamUserCount(e));
		// 考试记录数量
		ExaminationRecord examinationRecord = new ExaminationRecord();
		examinationRecord.setCommonValue(e.getCreator(), e.getTenantCode());
		dto.setExaminationRecordCount(this.findExaminationRecordCount(examinationRecord));
		return dto;
	}

	/**
	 * 查询过去 n 天的考试记录数据
	 */
	@Override
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

	private List<ExaminationRecordDto> toRecordDto(List<ExaminationRecord> records) {
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
		records.forEach(r -> {
			userIds.add(r.getUserId());
			Examination e = map.get(r.getExaminationId());
			if (e != null) {
				ExaminationRecordDto dto = new ExaminationRecordDto();
				BeanUtils.copyProperties(e, dto);
				dto.setId(r.getId());
				dto.setStartTime(r.getStartTime());
				dto.setEndTime(r.getEndTime());
				dto.setScore(r.getScore());
				dto.setUserId(r.getUserId());
				dto.setExaminationId(r.getExaminationId());
				// 正确题目数
				dto.setCorrectNumber(r.getCorrectNumber());
				dto.setInCorrectNumber(r.getInCorrectNumber());
				// 提交状态
				dto.setSubmitStatus(r.getSubmitStatus());
				dto.setSubmitStatusName(
						SubmitStatusEnum.match(r.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED).getName());
				dto.setTypeLabel(ExaminationType.matchByValue(e.getType()).getName());
				list.add(dto);
			}
		});
		this.fillExamUserInfo(list, userIds.toArray(new Long[0]));
		return list;
	}
}
