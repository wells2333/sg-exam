package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/4/13 1:57 下午
 */
@Repository
public interface SubjectsMapper extends CrudMapper<Subjects> {

	Subjects findBySubjectId(Long subjectId);

	List<Subjects> findBySubjectIds(@Param("subjectIds") Long[] subjectIds);

	List<Subjects> findByCategoryId(Long categoryId);

	List<Subjects> findByCategoryIds(@Param("categoryIds") Long[] categoryIds);

	List<Subjects> findByType(Integer type);

	Integer findMaxSortByCategoryId(Long categoryId);

	Integer findSubjectCountByCategoryId(Long categoryId);
}
