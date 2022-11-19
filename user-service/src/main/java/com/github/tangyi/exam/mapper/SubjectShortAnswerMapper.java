package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectShortAnswer;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 简答题Mapper
 *
 * @author tangyi
 * @date 2016/6/16 14:52
 */
@Repository
public interface SubjectShortAnswerMapper extends CrudMapper<SubjectShortAnswer> {

    /**
     * 物理删除
     *
     * @param subjectShortAnswer subjectShortAnswer
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:54
     */
    int physicalDelete(SubjectShortAnswer subjectShortAnswer);

    /**
     * 物理批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:54
     */
    int physicalDeleteAll(Long[] ids);
}
