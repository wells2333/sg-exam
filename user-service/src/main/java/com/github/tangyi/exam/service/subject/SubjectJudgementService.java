package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectJudgement;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectJudgementMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectJudgementConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 判断题Service
 *
 * @author tangyi
 * @date 2019-07-16 13:02
 */
@AllArgsConstructor
@Slf4j
@Service
public class SubjectJudgementService extends CrudService<SubjectJudgementMapper, SubjectJudgement>
		implements ISubjectService {

	private final SubjectJudgementConverter subjectJudgementConverter;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#id")
	public SubjectJudgement get(Long id) {
		return super.get(id);
	}

	/**
	 * 根据ID查询
	 *
	 * @param id id
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019-07-16 13:06
	 */
	@Override
	public SubjectDto getSubject(Long id) {
		return subjectJudgementConverter.toDto(this.get(id));
	}

	/**
	 * 根据上一题ID查询下一题
	 *
	 * @param examinationId examinationId
	 * @param previousId    previousId
	 * @param nextType      0：下一题，1：上一题
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019-09-14 17:03
	 */
	@Override
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		return null;
	}

	/**
	 * 查询列表
	 *
	 * @param subjectDto subjectDto
	 * @return List<SubjectDto>
	 * @author tangyi
	 * @date 2019-07-16 13:08
	 */
	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		return null;
	}

	/**
	 * 查询分页
	 *
	 * @param pageInfo   pageInfo
	 * @param subjectDto subjectDto
	 * @return PageInfo<SubjectDto>
	 * @author tangyi
	 * @date 2019-07-16 13:08
	 */
	@Override
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		return null;
	}

	/**
	 * 根据ID批量查询
	 *
	 * @param ids ids
	 * @return List<SubjectDto>
	 * @author tangyi
	 * @date 2019-07-16 13:09
	 */
	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return subjectJudgementConverter.toDto(this.findListById(ids), true);
	}

	/**
	 * 保存
	 *
	 * @param subjectDto subjectDto
	 * @return int
	 * @author tangyi
	 * @date 2019-07-16 13:10
	 */
	@Override
	@Transactional
	public BaseEntity<SubjectJudgement> insertSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		subjectJudgement.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(subjectJudgement);
		return subjectJudgement;
	}

	/**
	 * 更新
	 *
	 * @param subjectDto subjectDto
	 * @return int
	 * @author tangyi
	 * @date 2019-07-16 13:10
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		subjectJudgement.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(subjectJudgement);
	}

	/**
	 * 删除
	 *
	 * @param subjectDto subjectDto
	 * @return int
	 * @author tangyi
	 * @date 2019-07-16 13:10
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		return this.delete(subjectJudgement);
	}

	/**
	 * 物理删除题目
	 *
	 * @param subjectJudgement subjectJudgement
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 22:58
	 */
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectJudgement.id")
	public int physicalDelete(SubjectJudgement subjectJudgement) {
		return this.dao.physicalDelete(subjectJudgement);
	}

	/**
	 * 批量删除
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019-07-16 13:10
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	/**
	 * 批量删除题目
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 22:58
	 */
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	/**
	 * 物理删除
	 *
	 * @param subjectDto subjectDto
	 * @return int
	 * @author tangyi
	 * @date 2019-07-16 13:10
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		return this.physicalDelete(subjectJudgement);
	}

	/**
	 * 批量物理删除
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019-07-16 813:11
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.physicalDeleteAll(ids);
	}
}
