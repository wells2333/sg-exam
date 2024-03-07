package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectFillBlank;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.SubjectFillBlankMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 填空题
 */
@AllArgsConstructor
@Service
public class SubjectFillBlankService extends CrudService<SubjectFillBlankMapper, SubjectFillBlank>
		implements ISubjectService {

	@Override
	public SubjectDto getSubject(Long id) {
		return null;
	}

	@Override
	public List<SubjectDto> getSubjects(List<Long> ids) {
		return null;
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
		return null;
	}

	@Override
	public BaseEntity<?> insertSubject(SubjectDto subjectDto) {
		return null;
	}

	@Override
	public int updateSubject(SubjectDto subjectDto) {
		return 0;
	}

	@Override
	public int updateSubjectSort(Long subjectId, Integer sort) {
		return 0;
	}

	@Override
	public int deleteSubject(SubjectDto subjectDto) {
		return 0;
	}

	@Override
	public int deleteAllSubject(Long[] ids) {
		return 0;
	}

	@Override
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		return 0;
	}

	@Override
	public int physicalDeleteAllSubject(Long[] ids) {
		return 0;
	}
}
