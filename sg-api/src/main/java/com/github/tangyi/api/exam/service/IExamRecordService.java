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
