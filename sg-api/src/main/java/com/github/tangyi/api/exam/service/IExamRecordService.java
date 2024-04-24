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

package com.github.tangyi.api.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.common.service.ICrudService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IExamRecordService extends ICrudService<ExaminationRecord> {

	PageInfo<ExaminationRecordDto> getUserExamRecords(Long userId, Map<String, Object> condition, int pageNum,
			int pageSize);

	PageInfo<ExaminationRecordDto> examRecordList(Map<String, Object> condition, int pageNum, int pageSize);

	List<ExaminationRecord> getByUserIdAndExaminationId(ExaminationRecord examRecord);

	List<ExaminationRecord> getByExaminationId(Long examinationId);

	ExaminationDashboardDto findExamRecordTendency(String tenantCode, int pastDays);

	ExaminationDashboardDto findExamDashboardData(String tenantCode);

	void exportExamRecord(Long[] ids, HttpServletRequest request, HttpServletResponse response);

	Integer findExaminationRecordCountByDate(Date start, Date end);

	int findExaminationRecordCount(ExaminationRecord examinationRecord);

	void fillExamUserInfo(List<ExaminationRecordDto> list, Long[] userIds);

}
