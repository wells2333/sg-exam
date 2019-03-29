package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.Examination;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考试Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:11
 */
@Mapper
public interface ExaminationMapper extends CrudMapper<Examination> {

    /**
     * 查询考试数量
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/3/1 15:32
     */
    int findExaminationCount(Examination examination);
}
