package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.Knowledge;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知识库Mapper
 *
 * @author tangyi
 * @date 2019/1/1 15:03
 */
@Mapper
public interface KnowledgeMapper extends CrudMapper<Knowledge> {
}
