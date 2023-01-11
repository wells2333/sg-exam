package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectJudgement;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectJudgementMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectJudgementConverter;
import com.google.common.collect.Lists;
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

	@Override
	public SubjectDto getSubject(Long id) {
		return subjectJudgementConverter.toDto(this.get(id));
	}

	@Override
	public List<SubjectDto> getSubjects(List<Long> ids) {
		List<SubjectDto> list = Lists.newArrayListWithExpectedSize(ids.size());
		for (Long id : ids) {
			SubjectDto dto = getSubject(id);
			if (dto != null) {
				list.add(dto);
			}
		}
		return list;
	}

	/**
	 * 根据上一题ID查询下一题
	 *
	 * @param examinationId examinationId
	 * @param previousId    previousId
	 * @param nextType      0：下一题，1：上一题
	 */
	@Override
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		return null;
	}

	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		return null;
	}

	@Override
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		return null;
	}

	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return subjectJudgementConverter.toDto(this.findListById(ids), true);
	}

	@Override
	@Transactional
	public BaseEntity<SubjectJudgement> insertSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		subjectJudgement.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(subjectJudgement);
		return subjectJudgement;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		subjectJudgement.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(subjectJudgement);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		return this.delete(subjectJudgement);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectJudgement.id")
	public int physicalDelete(SubjectJudgement subjectJudgement) {
		return this.dao.physicalDelete(subjectJudgement);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectJudgement subjectJudgement = new SubjectJudgement();
		BeanUtils.copyProperties(subjectDto, subjectJudgement);
		return this.physicalDelete(subjectJudgement);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_JUDGEMENT, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.physicalDeleteAll(ids);
	}
}
