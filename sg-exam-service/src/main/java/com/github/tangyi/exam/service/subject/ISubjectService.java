package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.common.base.BaseEntity;

import java.util.List;

/**
 * 题目相关接口
 */
public interface ISubjectService {

	SubjectDto getSubject(Long id);

	List<SubjectDto> getSubjects(List<Long> ids);

	/**
	 * 根据ID查询上一题、下一题
	 *
	 * @param nextType      -1：当前题目，0：下一题，1：上一题
	 */
	SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType);

	List<SubjectDto> findSubjectList(SubjectDto subjectDto);

	PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto);

	List<SubjectDto> findSubjectListById(Long[] ids);

	BaseEntity<?> insertSubject(SubjectDto subjectDto);

	int updateSubject(SubjectDto subjectDto);

	int deleteSubject(SubjectDto subjectDto);

	int deleteAllSubject(Long[] ids);

	int physicalDeleteSubject(SubjectDto subjectDto);

	int physicalDeleteAllSubject(Long[] ids);
}
