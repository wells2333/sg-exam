package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Knowledge;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 知识库Mapper
 *
 * @author tangyi
 * @date 2019/1/1 15:03
 */
@Repository
public interface KnowledgeMapper extends CrudMapper<Knowledge> {
}
