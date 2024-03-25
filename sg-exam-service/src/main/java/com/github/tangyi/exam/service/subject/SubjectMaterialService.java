package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectMaterial;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectMaterialMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectMaterialConverter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 填空题
 */
@AllArgsConstructor
@Service
public class SubjectMaterialService extends CrudService<SubjectMaterialMapper, SubjectMaterial>
		implements ISubjectService {

	private final SubjectMaterialConverter converter;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#id")
	public SubjectMaterial get(Long id) {
		return super.get(id);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		return converter.convert(this.get(id));
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
		return converter.convert(this.findListById(ids), true);
	}

	@Override
	@Transactional
	public BaseEntity<?> insertSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		s.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(s);
		return s;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		s.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectId")
	public int updateSubjectSort(Long subjectId, Integer sort) {
		SubjectMaterial s = new SubjectMaterial();
		s.setId(subjectId);
		s.setSort(sort);
		return this.update(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		return this.delete(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		return this.physicalDelete(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#s.id")
	public int physicalDelete(SubjectMaterial s) {
		return this.dao.physicalDelete(s);
	}
}
