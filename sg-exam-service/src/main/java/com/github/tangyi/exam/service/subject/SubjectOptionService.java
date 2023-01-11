package com.github.tangyi.exam.service.subject;

import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectOptionMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SubjectOptionService extends CrudService<SubjectOptionMapper, SubjectOption> {

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_CHOICES_OPTION, key = "#id")
	public SubjectOption get(Long id) {
		return super.get(id);
	}

	public List<SubjectOption> getBySubjectChoicesId(SubjectOption subjectOption) {
		return this.dao.getBySubjectChoicesId(subjectOption);
	}

	public List<SubjectOption> getBySubjectChoicesIds(List<Long> ids) {
		return this.dao.getBySubjectChoicesIds(ids);
	}

	@Override
	@Transactional
	public int insert(SubjectOption subjectOption) {
		return super.insert(subjectOption);
	}

	@Transactional
	public int insertBatch(List<SubjectOption> subjectOptionList) {
		return this.dao.insertBatch(subjectOptionList);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES_OPTION, key = "#subjectOption.id")
	public int update(SubjectOption subjectOption) {
		return super.update(subjectOption);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES_OPTION, key = "#subjectOption.id")
	public int delete(SubjectOption subjectOption) {
		return super.delete(subjectOption);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES_OPTION, allEntries = true)
	public int deleteBySubjectChoicesId(SubjectOption subjectOption) {
		return this.dao.deleteBySubjectChoicesId(subjectOption);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES_OPTION, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES_OPTION, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}
}
