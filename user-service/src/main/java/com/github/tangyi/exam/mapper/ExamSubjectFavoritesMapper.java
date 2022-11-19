package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamSubjectFavorites;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 收藏题目Mapper
 *
 * @author tangyi
 * @date 2022-08-18
 */
@Repository
public interface ExamSubjectFavoritesMapper extends CrudMapper<ExamSubjectFavorites> {
}