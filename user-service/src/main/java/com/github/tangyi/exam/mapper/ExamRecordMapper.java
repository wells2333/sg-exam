package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 考试记录Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:12
 */
@Repository
public interface ExamRecordMapper extends CrudMapper<ExaminationRecord> {

	/**
	 * 根据用户id、考试id查找
	 *
	 * @param examRecord examRecord
	 * @return List
	 * @author tangyi
	 * @date 2018/12/26 13:56
	 */
	List<ExaminationRecord> getByUserIdAndExaminationId(ExaminationRecord examRecord);

	/**
	 * 根据用户id查询
	 *
	 * @param userId userId
	 * @return List
	 * @author tangyi
	 * @date 2018/12/26 13:56
	 */
	List<ExaminationRecord> getByUserId(@Param("userId") Long userId, @Param("params") Map<String, Object> params);

	/**
	 * 根据考试id查询
	 *
	 * @param examinationId examinationId
	 * @return List
	 * @author tangyi
	 * @date 2018/12/26 13:56
	 */
	List<ExaminationRecord> getByExaminationId(@Param("examinationId") Long examinationId);

	/**
	 * 查询考试记录数量
	 *
	 * @param examinationRecord examinationRecord
	 * @return int
	 * @author tangyi
	 * @date 2020/1/31 5:17 下午
	 */
	int findExaminationRecordCount(ExaminationRecord examinationRecord);

	/**
	 * 时间范围条件查询
	 * @param start start
	 * @return List
	 * @author tangyi
	 * @date 2020/2/1 11:57 上午
	 */
	List<ExaminationRecord> findExaminationRecordCountByDate(Date start);
}
