package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.ExamRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考试记录Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:12
 */
@Mapper
public interface ExamRecordMapper extends CrudMapper<ExamRecord> {

    /**
     * 根据用户id、考试id查找
     *
     * @param examRecord examRecord
     * @return Score
     * @author tangyi
     * @date 2018/12/26 13:56
     */
    ExamRecord getByUserIdAndExaminationId(ExamRecord examRecord);
}
