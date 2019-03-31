package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.Subject;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:15
 */
@Mapper
public interface SubjectMapper extends CrudMapper<Subject> {

    /**
     * 根据考试ID和序号查找
     *
     * @param subject subject
     * @return Subject
     * @author tangyi
     * @date 2019/01/20 12:19
     */
    Subject getByExaminationIdAndSerialNumber(Subject subject);

    /**
     * 根据考试ID查询题目数
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/01/23 20:14
     */
    int getExaminationTotalSubject(Subject subject);
}
