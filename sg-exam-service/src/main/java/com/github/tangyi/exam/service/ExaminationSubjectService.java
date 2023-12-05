package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.ExaminationSubjectMapper;
import com.github.tangyi.exam.utils.ExamUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class ExaminationSubjectService extends CrudService<ExaminationSubjectMapper, ExaminationSubject> {

	@Override
	public PageInfo<ExaminationSubject> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		return super.findPage(params, pageNum, pageSize);
	}

	@Transactional
	public int deleteBySubjectId(ExaminationSubject examinationSubject) {
		return this.dao.deleteBySubjectId(examinationSubject);
	}

	public List<ExaminationSubject> findListBySubjectId(ExaminationSubject examinationSubject) {
		return this.dao.findListBySubjectId(examinationSubject);
	}

	public List<ExaminationSubject> findListByExaminationId(Long examinationId) {
		return this.dao.findListByExaminationId(examinationId);
	}

	public List<ExaminationSubject> findListByExaminationIdAndMaxSort(Long examinationId, Integer maxSort) {
		return this.dao.findListByExaminationIdAndMaxSort(examinationId, maxSort);
	}

	public ExaminationSubject findByExaminationIdAndSubjectId(ExaminationSubject examinationSubject) {
		return this.dao.findByExaminationIdAndSubjectId(examinationSubject);
	}

	public ExaminationSubject findMinSortByExaminationId(Long examinationId) {
		return this.dao.findMinSortByExaminationId(examinationId);
	}

	public ExaminationSubject findByExaminationIdAndSort(ExaminationSubject examinationSubject) {
		return this.dao.findByExaminationIdAndSort(examinationSubject);
	}

	/**
	 * 根据上一题 ID 查询下一题
	 */
	public ExaminationSubject getByPreviousId(ExaminationSubject examinationSubject) {
		return this.dao.getByPreviousId(examinationSubject);
	}

	/**
	 * 根据当前题目 ID 查询上一题
	 */
	public ExaminationSubject getPreviousByCurrentId(ExaminationSubject examinationSubject) {
		return this.dao.getPreviousByCurrentId(examinationSubject);
	}

	/**
	 * 根据分类 ID 查询
	 */
	public List<ExaminationSubject> findListByCategoryId(Long categoryId) {
		return this.dao.findListByCategoryId(categoryId);
	}

	/**
	 * 查询下一题的序号
	 */
	public Integer nextSubjectNo(Long examinationId) {
		Integer no = this.dao.findMaxSortByExaminationId(ExamUtil.createEs(examinationId, null));
		return no == null ? 1 : no + 1;
	}

	public Integer findSubjectCount(Long examinationId) {
		return this.dao.findSubjectCount(examinationId);
	}

	@Transactional
	public int updateSort(Long examinationId, Long subjectId, Integer sort) {
		ExaminationSubject es = new ExaminationSubject();
		es.setExaminationId(examinationId);
		es.setSubjectId(subjectId);
		es = findByExaminationIdAndSubjectId(es);
		if (es != null) {
			es.setSort(sort);
			return update(es);
		}
		return -1;
	}

	@Transactional
	public void resetSubjectsSort(Long examinationId, Integer maxSort) {
		List<ExaminationSubject> subjects = this.findListByExaminationIdAndMaxSort(examinationId, maxSort);
		if (CollectionUtils.isEmpty(subjects)) {
			return;
		}
		for (ExaminationSubject subject : subjects) {
			subject.setSort(subject.getSort() - 1);
			this.update(subject);
		}
		log.info("Reset subject sort finished, examinationId: {}, maxSort: {}, update size: {}", examinationId, maxSort,
				subjects.size());
	}
}
