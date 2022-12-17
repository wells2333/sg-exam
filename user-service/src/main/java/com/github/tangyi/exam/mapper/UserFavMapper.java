package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户收藏Mapper
 *
 * @author tangyi
 * @date 2022-12-17
 */
@Repository
public interface UserFavMapper extends CrudMapper<ExamUserFav> {

	ExamUserFav getByUserIdAndTarget(ExamUserFav favorites);

	int deleteByUserIdAndTarget(ExamUserFav favorites);
}