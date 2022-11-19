package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectJudgement;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 判断题Mapper
 *
 * @author tangyi
 * @date 2019-07-16 13:00
 */
@Repository
public interface SubjectJudgementMapper extends CrudMapper<SubjectJudgement> {

    /**
     * 物理删除
     *
     * @param subjectJudgement subjectJudgement
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:54
     */
    int physicalDelete(SubjectJudgement subjectJudgement);

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
