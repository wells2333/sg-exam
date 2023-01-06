package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ExamRecordMapper extends CrudMapper<ExaminationRecord> {

	List<ExaminationRecord> getByUserIdAndExaminationId(ExaminationRecord examRecord);

	List<ExaminationRecord> getByUserId(@Param("userId") Long userId, @Param("params") Map<String, Object> params);

	List<ExaminationRecord> getByExaminationId(@Param("examinationId") Long examinationId);

	/**
	 * 查询考试记录数量
	 */
	int findExaminationRecordCount(ExaminationRecord examinationRecord);

	/**
	 * 时间范围条件查询考试记录数量
	 */
	Integer findExaminationRecordCountByDate(@Param("start") Date start, @Param("end") Date end);
}
