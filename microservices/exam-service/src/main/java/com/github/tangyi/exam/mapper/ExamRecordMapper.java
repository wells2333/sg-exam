package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.module.ExaminationRecord;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 考试记录Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:12
 */
@Mapper
public interface ExamRecordMapper extends CrudMapper<ExaminationRecord> {

    /**
     * 根据用户id、考试id查找
     *
     * @param examRecord examRecord
     * @return Score
     * @author tangyi
     * @date 2018/12/26 13:56
     */
    ExaminationRecord getByUserIdAndExaminationId(ExaminationRecord examRecord);

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
