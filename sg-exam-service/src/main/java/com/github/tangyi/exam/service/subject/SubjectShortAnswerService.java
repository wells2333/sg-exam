package com.github.tangyi.exam.service.subject;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectShortAnswer;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectShortAnswerMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectShortAnswerConverter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SubjectShortAnswerService extends CrudService<SubjectShortAnswerMapper, SubjectShortAnswer>
		implements ISubjectService {

	private final SubjectShortAnswerConverter subjectShortAnswerConverter;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#id")
	public SubjectShortAnswer get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SubjectShortAnswer subjectShortAnswer) {
		return super.insert(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int update(SubjectShortAnswer subjectShortAnswer) {
		return super.update(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int delete(SubjectShortAnswer subjectShortAnswer) {
		return super.delete(subjectShortAnswer);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int physicalDelete(SubjectShortAnswer subjectShortAnswer) {
		return this.dao.physicalDelete(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		return subjectShortAnswerConverter.toDto(this.get(id));
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
	@Transactional
	public BaseEntity<SubjectShortAnswer> insertSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		subjectShortAnswer.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(subjectShortAnswer);
		return subjectShortAnswer;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		// 参考答案
		subjectShortAnswer.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		return this.delete(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		return this.physicalDelete(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.physicalDeleteAll(ids);
	}

	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		return subjectShortAnswerConverter.toDto(this.findList(subjectShortAnswer), true);
	}

	@Override
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		Map<String, Object> condition = Maps.newHashMap();
		PageInfo subjectShortAnswerPageInfo = this.findPage(condition, 0, 10);
		PageInfo<SubjectDto> subjectDtoPageInfo = new PageInfo<>();
		PageUtil.copyProperties(subjectShortAnswerPageInfo, subjectDtoPageInfo);
		subjectDtoPageInfo.setList(subjectShortAnswerConverter.toDto(subjectShortAnswerPageInfo.getList(), true));
		return subjectDtoPageInfo;
	}

	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return subjectShortAnswerConverter.toDto(this.findListById(ids), true);
	}
}
