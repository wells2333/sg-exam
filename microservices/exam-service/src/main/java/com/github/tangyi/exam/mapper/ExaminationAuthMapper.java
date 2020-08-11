package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.module.ExaminationAuth;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考试权限Mapper
 *
 * @author tangyi
 * @date 2019-07-16 14:03
 */
@Mapper
public interface ExaminationAuthMapper extends CrudMapper<ExaminationAuth> {
}
