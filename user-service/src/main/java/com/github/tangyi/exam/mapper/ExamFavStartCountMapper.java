package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamFavStartCount;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收藏、开始数量Mapper
 *
 * @author tangyi
 * @date 2022-12-17
 */
@Repository
public interface ExamFavStartCountMapper extends CrudMapper<ExamFavStartCount> {

	ExamFavStartCount findByTarget(@Param("targetId") Long targetId, @Param("targetType") Integer targetType);

	int batchInsert(List<ExamFavStartCount> list);

	int bathUpdate(List<ExamFavStartCount> list);
}