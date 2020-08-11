package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.module.Examination;
import com.github.tangyi.common.persistence.CrudMapper;
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

    /**
     * 查询参与考试人数
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/10/27 20:08:58
     */
    int findExamUserCount(Examination examination);
}
