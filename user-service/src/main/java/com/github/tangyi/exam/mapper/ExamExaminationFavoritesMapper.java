package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamExaminationFavorites;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收藏Mapper
 *
 * @author tangyi
 * @date 2022-08-18
 */
@Repository
public interface ExamExaminationFavoritesMapper extends CrudMapper<ExamExaminationFavorites> {

	List<ExamExaminationFavorites> findUserFavorites(@Param("userId") Long userId);

	ExamExaminationFavorites getByUserIdAndExaminationId(ExamExaminationFavorites favorites);

	int deleteByUserIdAndExaminationId(ExamExaminationFavorites favorites);
}