/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.MaterialSubject;
import com.github.tangyi.api.exam.model.MaterialSubject;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.MaterialSubjectMapper;
import com.github.tangyi.exam.mapper.MaterialSubjectMapper;
import com.github.tangyi.exam.utils.ExamUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class MaterialSubjectService extends CrudService<MaterialSubjectMapper, MaterialSubject> {

	@Override
	public PageInfo<MaterialSubject> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		return super.findPage(params, pageNum, pageSize);
	}

	@Transactional
	public int deleteBySubjectId(MaterialSubject examinationSubject) {
		return this.dao.deleteBySubjectId(examinationSubject);
	}
	@Transactional
	public int deleteByMaterialId(MaterialSubject examinationSubject) {
		return this.dao.deleteByMaterialId(examinationSubject);
	}


	public List<MaterialSubject> findListBySubjectId(MaterialSubject examinationSubject) {
		return this.dao.findListBySubjectId(examinationSubject);
	}

	public List<MaterialSubject> findListByMaterialId(Long materialId) {
		return this.dao.findListByMaterialId(materialId);
	}

	public List<MaterialSubject> findListByMaterialIdAndMaxSort(Long examinationId, Integer maxSort) {
		return this.dao.findListByMaterialIdAndMaxSort(examinationId, maxSort);
	}

	public MaterialSubject findByMaterialIdAndSubjectId(MaterialSubject examinationSubject) {
		return this.dao.findByMaterialIdAndSubjectId(examinationSubject);
	}

	public MaterialSubject findMinSortByMaterialId(Long examinationId) {
		return this.dao.findMinSortByMaterialId(examinationId);
	}

	public MaterialSubject findByMaterialIdAndSort(MaterialSubject examinationSubject) {
		return this.dao.findByMaterialIdAndSort(examinationSubject);
	}

	/**
	 * 根据上一题 ID 查询下一题
	 */
	public MaterialSubject getByPreviousId(MaterialSubject examinationSubject) {
		return this.dao.getByPreviousId(examinationSubject);
	}

	/**
	 * 根据当前题目 ID 查询上一题
	 */
	public MaterialSubject getPreviousByCurrentId(MaterialSubject examinationSubject) {
		return this.dao.getPreviousByCurrentId(examinationSubject);
	}

	/**
	 * 根据分类 ID 查询
	 */
	public List<MaterialSubject> findListByCategoryId(Long categoryId) {
		return this.dao.findListByCategoryId(categoryId);
	}

	public static MaterialSubject createMs(Long materialId, Long subjectId) {
		MaterialSubject ms = new MaterialSubject();
		ms.setMaterialId(materialId);
		ms.setSubjectId(subjectId);
		return ms;
	}

	/**
	 * 查询下一题的序号
	 */
	public Integer nextSubjectNo(Long examinationId) {
		Integer no = this.dao.findMaxSortByMaterialId((createMs(examinationId, null)));
		return no == null ? 1 : no + 1;
	}

	public Integer findSubjectCount(Long examinationId) {
		return this.dao.findSubjectCount(examinationId);
	}

	@Transactional
	public int updateSort(Long examinationId, Long subjectId, Integer sort) {
		MaterialSubject es = new MaterialSubject();
		es.setMaterialId(examinationId);
		es.setSubjectId(subjectId);
		es = findByMaterialIdAndSubjectId(es);
		if (es != null) {
			es.setSort(sort);
			return update(es);
		}
		return -1;
	}
}
