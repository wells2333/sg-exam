package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.MaterialSubject;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialSubjectMapper extends CrudMapper<MaterialSubject> {

	int deleteBySubjectId(MaterialSubject MaterialSubject);
	
	int deleteByMaterialId(MaterialSubject MaterialSubject);

	List<MaterialSubject> findListBySubjectId(MaterialSubject MaterialSubject);

	List<MaterialSubject> findListByMaterialId(Long materialId);

	List<MaterialSubject> findListByMaterialIdAndMaxSort(@Param("examinationId") Long examinationId,
			@Param("maxSort") Integer maxSort);

	MaterialSubject findByMaterialIdAndSubjectId(MaterialSubject MaterialSubject);

	/**
	 * 查询序号最小的题目
	 */
	MaterialSubject findMinSortByMaterialId(Long examinationId);

	/**
	 * 根据考试 ID 和序号查询
	 */
	MaterialSubject findByMaterialIdAndSort(MaterialSubject MaterialSubject);

	/**
	 * 根据考试 ID 查询最大的序号
	 */
	Integer findMaxSortByMaterialId(MaterialSubject MaterialSubject);

	/**
	 * 查询考试的题目数量
	 */
	Integer findSubjectCount(Long examinationId);

	/**
	 * 根据上一题 ID 查询下一题
	 */
	MaterialSubject getByPreviousId(MaterialSubject MaterialSubject);

	/**
	 * 根据当前题目 ID 查询上一题
	 */
	MaterialSubject getPreviousByCurrentId(MaterialSubject MaterialSubject);

	/**
	 * 根据分类 ID 查询
	 */
	List<MaterialSubject> findListByCategoryId(Long categoryId);
}
