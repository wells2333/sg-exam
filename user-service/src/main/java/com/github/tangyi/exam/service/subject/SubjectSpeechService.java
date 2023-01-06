package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectSpeech;
import com.github.tangyi.api.user.model.SpeechSynthesis;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectSpeechMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectSpeechConverter;
import com.github.tangyi.user.service.QiNiuService;
import com.github.tangyi.user.service.SpeechSynthesisService;
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
 * 语音题目
 * @author tangyi
 * @date 2022/6/23 1:31 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class SubjectSpeechService extends CrudService<SubjectSpeechMapper, SubjectSpeech> implements ISubjectService {

	private final SubjectSpeechConverter subjectSpeechConverter;

	private final SpeechSynthesisService speechSynthesisService;

	private final QiNiuService qiNiuService;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_SPEECH, key = "#id")
	public SubjectSpeech get(Long id) {
		return super.get(id);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		SubjectSpeech subjectSpeech = this.get(id);
		SubjectDto dto = subjectSpeechConverter.toDto(subjectSpeech);
		if (dto != null && dto.getSpeechId() != null) {
			SpeechSynthesis synthesis = speechSynthesisService.get(dto.getSpeechId());
			if (synthesis != null) {
				dto.setSpeechUrl(qiNiuService.getPreviewUrl(synthesis.getAttachId()));
			}
		}
		return dto;
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
		return subjectSpeechConverter.toDto(this.findListById(ids), true);
	}

	@Override
	@Transactional
	public BaseEntity<?> insertSubject(SubjectDto subjectDto) {
		SubjectSpeech subject = new SubjectSpeech();
		BeanUtils.copyProperties(subjectDto, subject);
		subject.setAnswer(subjectDto.getAnswer().getAnswer());
		addSynthesis(subject);
		if (subject.getSpeechId() != null) {
			this.insert(subject);
		}
		return subject;
	}

	@Transactional
	public void addSynthesis(SubjectSpeech subject) {
		try {
			SpeechSynthesis synthesis = new SpeechSynthesis();
			synthesis.setName(subject.getSubjectName());
			synthesis.setText(subject.getSubjectName());
			speechSynthesisService.addSynthesis(synthesis);
			subject.setSpeechId(synthesis.getId());
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SPEECH, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectSpeech subject = new SubjectSpeech();
		BeanUtils.copyProperties(subjectDto, subject);
		subject.setAnswer(subjectDto.getAnswer().getAnswer());
		if (subjectDto.getSpeechId() != null) {
			updateSynthesis(subject);
		} else {
			addSynthesis(subject);
		}
		if (subject.getSpeechId() != null) {
			return this.update(subject);
		}
		return -1;
	}

	@Transactional
	public void updateSynthesis(SubjectSpeech subject) {
		try {
			SpeechSynthesis synthesis = new SpeechSynthesis();
			synthesis.setId(subject.getSpeechId());
			synthesis.setName(subject.getSubjectName());
			synthesis.setText(subject.getSubjectName());
			speechSynthesisService.updateSynthesis(synthesis);
			subject.setSpeechId(synthesis.getId());
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SPEECH, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectSpeech subject = new SubjectSpeech();
		BeanUtils.copyProperties(subjectDto, subject);
		return this.delete(subject);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SPEECH, key = "#subjectSpeech.id")
	public int physicalDelete(SubjectSpeech subjectSpeech) {
		return this.dao.physicalDelete(subjectSpeech);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SPEECH, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SPEECH, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectSpeech subject = new SubjectSpeech();
		BeanUtils.copyProperties(subjectDto, subject);
		return this.physicalDelete(subject);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SPEECH, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}
}
