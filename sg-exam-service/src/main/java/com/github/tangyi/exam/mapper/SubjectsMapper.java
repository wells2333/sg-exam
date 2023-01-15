package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectsMapper extends CrudMapper<Subjects> {

	Subjects findBySubjectId(Long subjectId);

	Subjects findByCategoryIdAndSort(@Param("categoryId") Long categoryId, @Param("sort") Integer sort);

	List<Subjects> findBySubjectIds(@Param("subjectIds") Long[] subjectIds);

	List<Subjects> findByCategoryId(Long categoryId);

	List<Subjects> findIdAndTypeByCategoryId(@Param("categoryId") Long categoryId);

	List<Subjects> findByCategoryIds(@Param("categoryIds") Long[] categoryIds);

	List<Subjects> findByType(Integer type);

	Integer findMaxSortByCategoryId(Long categoryId);

	Integer findSubjectCountByCategoryId(Long categoryId);
}
