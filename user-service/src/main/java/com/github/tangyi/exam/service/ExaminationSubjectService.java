package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.ExaminationSubjectMapper;
import com.github.tangyi.exam.utils.ExaminationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

	/**
	 * 根据考试id查询题目id列表
	 *
	 * @param examinationId examinationId
	 * @return int
	 * @author tangyi
	 * @date 2019/06/18 14:35
	 */
	public List<ExaminationSubject> findListByExaminationId(Long examinationId) {
		return this.dao.findListByExaminationId(examinationId);
	}

	/**
	 * 根据考试ID和题目ID查询
	 *
	 * @param examinationSubject examinationSubject
	 * @return ExaminationSubject
	 * @author tangyi
	 * @date 2019/06/18 23:17
	 */
	public ExaminationSubject findByExaminationIdAndSubjectId(ExaminationSubject examinationSubject) {
		return this.dao.findByExaminationIdAndSubjectId(examinationSubject);
	}

	public ExaminationSubject findMinSortByExaminationId(Long examinationId) {
		return this.dao.findMinSortByExaminationId(examinationId);
	}

	/**
	 * 根据考试ID和题目序号查询
	 *
	 * @param examinationSubject examinationSubject
	 * @return ExaminationSubject
	 * @author tangyi
	 * @date 2019/06/18 23:17
	 */
	public ExaminationSubject findByExaminationIdAndSort(ExaminationSubject examinationSubject) {
		return this.dao.findByExaminationIdAndSort(examinationSubject);
	}

	/**
	 * 根据上一题ID查询下一题
	 *
	 * @param examinationSubject examinationSubject
	 * @return ExaminationSubject
	 * @author tangyi
	 * @date 2019/10/07 20:59:43
	 */
	public ExaminationSubject getByPreviousId(ExaminationSubject examinationSubject) {
		return this.dao.getByPreviousId(examinationSubject);
	}

	/**
	 * 根据当前题目ID查询上一题
	 *
	 * @param examinationSubject examinationSubject
	 * @return ExaminationSubject
	 * @author tangyi
	 * @date 2019/10/07 20:59:43
	 */
	public ExaminationSubject getPreviousByCurrentId(ExaminationSubject examinationSubject) {
		return this.dao.getPreviousByCurrentId(examinationSubject);
	}

	/**
	 * 根据分类id查询
	 *
	 * @param examinationSubject examinationSubject
	 * @return List
	 * @author tangyi
	 * @date 2019/10/24 21:47:24
	 */
	public List<ExaminationSubject> findListByCategoryId(ExaminationSubject examinationSubject) {
		return this.dao.findListByCategoryId(examinationSubject);
	}

	/**
	 * 查询下一题的序号
	 * @param examinationId examinationId
	 * @return Integer
	 */
	public Integer nextSubjectNo(Long examinationId) {
		Integer no = this.dao.findMaxSortByExaminationId(ExaminationUtil.createEs(examinationId, null));
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
}
