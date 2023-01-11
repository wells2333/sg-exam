package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectVideo;
import com.github.tangyi.api.user.service.IQiNiuService;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectVideoMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectVideoConverter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectVideoService extends CrudService<SubjectVideoMapper, SubjectVideo> implements ISubjectService {

	private final SubjectVideoConverter subjectVideoConverter;

	private final IQiNiuService qiNiuService;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_VIDEO, key = "#id")
	public SubjectVideo get(Long id) {
		return super.get(id);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		SubjectDto dto = subjectVideoConverter.toDto(this.get(id));
		if (dto != null && dto.getVideoId() != null) {
			dto.setVideoUrl(qiNiuService.getPreviewUrl(dto.getVideoId()));
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
		return subjectVideoConverter.toDto(this.findListById(ids), true);
	}

	@Override
	public BaseEntity<?> insertSubject(SubjectDto subjectDto) {
		SubjectVideo subject = new SubjectVideo();
		BeanUtils.copyProperties(subjectDto, subject);
		subject.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(subject);
		return subject;
	}

	@Override
	@CacheEvict(value = ExamCacheName.SUBJECT_VIDEO, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectVideo subject = new SubjectVideo();
		BeanUtils.copyProperties(subjectDto, subject);
		subject.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(subject);
	}

	@Override
	@CacheEvict(value = ExamCacheName.SUBJECT_VIDEO, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectVideo subject = new SubjectVideo();
		BeanUtils.copyProperties(subjectDto, subject);
		return this.delete(subject);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_VIDEO, key = "#subject.id")
	public int physicalDelete(SubjectVideo subject) {
		return this.dao.physicalDelete(subject);
	}

	@Override
	@CacheEvict(value = ExamCacheName.SUBJECT_VIDEO, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@CacheEvict(value = ExamCacheName.SUBJECT_VIDEO, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectVideo subject = new SubjectVideo();
		BeanUtils.copyProperties(subjectDto, subject);
		return this.physicalDelete(subject);
	}

	@Override
	@CacheEvict(value = ExamCacheName.SUBJECT_VIDEO, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}
}
