package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.module.SubjectChoices;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 选择题Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:15
 */
@Mapper
public interface SubjectChoicesMapper extends CrudMapper<SubjectChoices> {

    /**
     * 物理删除
     *
     * @param subjectChoices subjectChoices
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:44
     */
    int physicalDelete(SubjectChoices subjectChoices);

    /**
     * 物理批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:44
     */
    int physicalDeleteAll(Long[] ids);
}
