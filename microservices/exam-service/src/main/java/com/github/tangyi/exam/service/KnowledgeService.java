package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.module.Knowledge;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.KnowledgeMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 知识库service
 *
 * @author tangyi
 * @date 2019/1/1 15:09
 */
@Service
public class KnowledgeService extends CrudService<KnowledgeMapper, Knowledge> {

    /**
     * 获取知识库信息
     *
     * @param knowledge knowledge
     * @return Knowledge
     * @author tangyi
     * @date 2019/1/1 15:09
     */
    @Override
    @Cacheable(value = "knowledge#" + CommonConstant.CACHE_EXPIRE, key = "#knowledge.id")
    public Knowledge get(Knowledge knowledge) {
        return super.get(knowledge);
    }

    /**
     * 更新知识库信息
     *
     * @param knowledge knowledge
     * @return int
     * @author tangyi
     * @date 2019/1/1 15:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "knowledge", key = "#knowledge.id")
    public int update(Knowledge knowledge) {
        return super.update(knowledge);
    }

    /**
     * 删除知识库信息
     *
     * @param knowledge knowledge
     * @return int
     * @author tangyi
     * @date 2019/1/1 15:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "knowledge", key = "#knowledge.id")
    public int delete(Knowledge knowledge) {
        return super.delete(knowledge);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:15
     */
    @Override
    @Transactional
    @CacheEvict(value = "knowledge", allEntries = true)
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }
}
