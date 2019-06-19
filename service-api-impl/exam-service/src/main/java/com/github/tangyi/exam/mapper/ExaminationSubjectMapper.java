package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.ExaminationSubject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 考试题目关联mapper
 *
 * @author tangyi
 * @date 2019/6/16 15:37
 */
@Mapper
public interface ExaminationSubjectMapper extends CrudMapper<ExaminationSubject> {

    /**
     * 根据题目ID删除
     *
     * @param examinationSubject examinationSubject
     * @return int
     * @author tangyi
     * @date 2019/06/16 21:58
     */
    int deleteBySubjectId(ExaminationSubject examinationSubject);

    /**
     * 根据题目ID查询
     *
     * @param examinationSubject examinationSubject
     * @return List
     * @author tangyi
     * @date 2019/06/17 12:18
     */
    List<ExaminationSubject> findListBySubjectId(ExaminationSubject examinationSubject);

    /**
     * 查询考试的题目数量
     *
     * @param examinationSubject examinationSubject
     * @return int
     * @author tangyi
     * @date 2019/06/18 14:37
     */
    int findSubjectCount(ExaminationSubject examinationSubject);

    /**
     * 根据考试ID和题目序号查询
     *
     * @param examinationSubject examinationSubject
     * @return ExaminationSubject
     * @author tangyi
     * @date 2019/06/18 23:17
     */
    ExaminationSubject findByExaminationIdAndSerialNumber(ExaminationSubject examinationSubject);
}
