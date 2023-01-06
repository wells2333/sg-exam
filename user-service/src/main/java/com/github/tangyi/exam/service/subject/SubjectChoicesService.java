package com.github.tangyi.exam.service.subject;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.SubjectChoices;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectChoicesMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.subject.converter.SubjectChoicesConverter;
import com.github.tangyi.exam.utils.AnswerHandlerUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 选择题service
 */
@AllArgsConstructor
@Service
public class SubjectChoicesService extends CrudService<SubjectChoicesMapper, SubjectChoices>
		implements ISubjectService {

	private final SubjectOptionService subjectOptionService;

	private final ExaminationSubjectService examinationSubjectService;

	private final SubjectChoicesConverter subjectChoicesConverter;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_CHOICES, key = "#id")
	public SubjectChoices get(Long id) {
		SubjectChoices subject = super.get(id);
		if (subject != null) {
			SubjectOption subjectOption = new SubjectOption();
			subjectOption.setSubjectChoicesId(subject.getId());
			List<SubjectOption> options = subjectOptionService.getBySubjectChoicesId(subjectOption);
			subject.setOptions(options);
		}
		return subject;
	}

	int getExaminationTotalSubject(SubjectChoices subjectChoices) {
		return 0;
	}

	@Override
	@Transactional
	public int insert(SubjectChoices subjectChoices) {
		return super.insert(subjectChoices);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, key = "#subjectChoices.id")
	public int update(SubjectChoices subjectChoices) {
		this.insertOptions(subjectChoices);
		return super.update(subjectChoices);
	}

	public SubjectChoices getByCurrentId(Long examinationId, SubjectChoices subjectChoices) {
		ExaminationSubject examinationSubject = new ExaminationSubject();
		examinationSubject.setExaminationId(examinationId);
		examinationSubject.setSubjectId(subjectChoices.getId());
		examinationSubject = examinationSubjectService.findByExaminationIdAndSubjectId(examinationSubject);
		if (examinationSubject == null) {
			return null;
		}
		return this.getSubjectChoicesById(examinationSubject.getSubjectId());
	}

	/**
	 * 根据上一题ID查询下一题
	 */
	public SubjectChoices getByPreviousId(Long examinationId, SubjectChoices subjectChoices) {
		ExaminationSubject examinationSubject = new ExaminationSubject();
		examinationSubject.setExaminationId(examinationId);
		examinationSubject.setSubjectId(subjectChoices.getId());
		examinationSubject = examinationSubjectService.getByPreviousId(examinationSubject);
		if (examinationSubject == null) {
			return null;
		}
		return this.getSubjectChoicesById(examinationSubject.getSubjectId());
	}

	/**
	 * 根据当前题目ID查询上一题
	 */
	public SubjectChoices getPreviousByCurrentId(Long examinationId, SubjectChoices subjectChoices) {
		ExaminationSubject examinationSubject = new ExaminationSubject();
		examinationSubject.setExaminationId(examinationId);
		examinationSubject.setSubjectId(subjectChoices.getId());
		examinationSubject = examinationSubjectService.getPreviousByCurrentId(examinationSubject);
		if (examinationSubject == null) {
			return null;
		}
		return this.getSubjectChoicesById(examinationSubject.getSubjectId());
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, key = "#subjectChoices.id")
	public int delete(SubjectChoices subjectChoices) {
		int update;
		if ((update = super.delete(subjectChoices)) > 0) {
			this.deleteOptionAndRelation(subjectChoices.getId());
		}
		return update;
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, key = "#subjectChoices.id")
	public int physicalDelete(SubjectChoices subjectChoices) {
		int update;
		if ((update = this.dao.physicalDelete(subjectChoices)) > 0) {
			this.deleteOptionAndRelation(subjectChoices.getId());
		}
		return update;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, allEntries = true)
	public int deleteAll(Long[] ids) {
		int update;
		if ((update = super.deleteAll(ids)) > 0) {
			for (Long id : ids) {
				this.deleteOptionAndRelation(id);
			}
		}
		return update;
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		int update;
		if ((update = this.dao.physicalDeleteAll(ids)) > 0) {
			for (Long id : ids) {
				this.deleteOptionAndRelation(id);
			}
		}
		return update;
	}

	@Transactional
	public void deleteOptionAndRelation(Long subjectId) {
		// 删除选项
		SubjectOption option = new SubjectOption();
		option.setSubjectChoicesId(subjectId);
		subjectOptionService.deleteBySubjectChoicesId(option);
		// 删除关联关系
		ExaminationSubject es = new ExaminationSubject();
		es.setSubjectId(subjectId);
		examinationSubjectService.deleteBySubjectId(es);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		SubjectChoices subject = this.get(id);
		if (subject != null) {
			SubjectOption subjectOption = new SubjectOption();
			subjectOption.setSubjectChoicesId(subject.getId());
			List<SubjectOption> options = subjectOptionService.getBySubjectChoicesId(subjectOption);
			subject.setOptions(options);
		}
		return subjectChoicesConverter.toDto(subject);
	}

	@Override
	public List<SubjectDto> getSubjects(List<Long> ids) {
		List<SubjectChoices> choices = Lists.newArrayListWithExpectedSize(ids.size());
		for (Long id : ids) {
			SubjectChoices choice = this.get(id);
			if (choice != null) {
				choices.add(choice);
			}
		}
		List<Long> idList = choices.stream().map(SubjectChoices::getId).collect(Collectors.toList());
		List<SubjectOption> optionList = subjectOptionService.getBySubjectChoicesIds(idList);
		Map<Long, List<SubjectOption>> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(optionList)) {
			for (SubjectOption option : optionList) {
				map.computeIfAbsent(option.getSubjectChoicesId(), s -> Lists.newArrayListWithExpectedSize(5))
						.add(option);
			}
		}
		List<SubjectDto> list = Lists.newArrayListWithExpectedSize(choices.size());
		for (SubjectChoices choice : choices) {
			choice.setOptions(map.get(choice.getId()));
			list.add(subjectChoicesConverter.toDto(choice));
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
	@Transactional
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		SubjectChoices subjectChoices = new SubjectChoices();
		subjectChoices.setId(previousId);
		if (AnswerConstant.CURRENT.equals(nextType)) {
			subjectChoices = this.getByCurrentId(examinationId, subjectChoices);
		} else if (AnswerConstant.NEXT.equals(nextType)) {
			subjectChoices = this.getByPreviousId(examinationId, subjectChoices);
		} else {
			subjectChoices = this.getPreviousByCurrentId(examinationId, subjectChoices);
		}
		return subjectChoicesConverter.toDto(subjectChoices);
	}

	@Override
	@Transactional
	public BaseEntity<SubjectChoices> insertSubject(SubjectDto subjectDto) {
		SubjectChoices subjectChoices = new SubjectChoices();
		BeanUtils.copyProperties(subjectDto, subjectChoices);
		subjectChoices.setAnswer(subjectDto.getAnswer().getAnswer());
		subjectChoices.setChoicesType(subjectDto.getType());
		if (insert(subjectChoices) > 0) {
			insertOptions(subjectChoices);
		}
		return subjectChoices;
	}

	@Transactional
	public void insertOptions(SubjectChoices subjectChoices) {
		if (CollectionUtils.isNotEmpty(subjectChoices.getOptions())) {
			SubjectOption subjectOption = new SubjectOption();
			subjectOption.setSubjectChoicesId(subjectChoices.getId());
			subjectOptionService.deleteBySubjectChoicesId(subjectOption);
			subjectChoices.getOptions().forEach(option -> {
				option.setId(null);
				option.setNewRecord(true);
				option.setCommonValue(subjectChoices.getOperator(), subjectChoices.getTenantCode());
				option.setSubjectChoicesId(subjectChoices.getId());
			});
			// 批量插入
			subjectOptionService.insertBatch(subjectChoices.getOptions());
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectChoices subjectChoices = new SubjectChoices();
		BeanUtils.copyProperties(subjectDto, subjectChoices);
		subjectChoices.setCommonValue();
		subjectChoices.setAnswer(AnswerHandlerUtil.replaceComma(subjectDto.getAnswer().getAnswer()));
		return this.update(subjectChoices);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectChoices subjectChoices = new SubjectChoices();
		BeanUtils.copyProperties(subjectDto, subjectChoices);
		return this.delete(subjectChoices);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectChoices subjectChoices = new SubjectChoices();
		BeanUtils.copyProperties(subjectDto, subjectChoices);
		return this.physicalDelete(subjectChoices);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_CHOICES, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.physicalDeleteAll(ids);
	}

	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		SubjectChoices subjectChoices = new SubjectChoices();
		BeanUtils.copyProperties(subjectDto, subjectChoices);
		return subjectChoicesConverter.toDto(this.findList(subjectChoices), true);
	}

	@Override
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		SubjectChoices subjectChoices = new SubjectChoices();
		BeanUtils.copyProperties(subjectDto, subjectChoices);
		// 选择题类型：单选或多选
		if (subjectDto.getType() != null) {
			subjectChoices.setChoicesType(subjectDto.getType());
		}
		Map<String, Object> params = Maps.newHashMap();
		// TODO
		PageInfo<SubjectChoices> subjectChoicesPageInfo = this.findPage(params, 1, 10);
		List<SubjectDto> subjectDtos = subjectChoicesConverter.toDto(subjectChoicesPageInfo.getList(), true);
		PageInfo<SubjectDto> subjectDtoPageInfo = new PageInfo<>();
		subjectDtoPageInfo.setList(subjectDtos);
		subjectDtoPageInfo.setTotal(subjectChoicesPageInfo.getTotal());
		subjectDtoPageInfo.setPageSize(subjectChoicesPageInfo.getPageSize());
		subjectDtoPageInfo.setPageNum(subjectChoicesPageInfo.getPageNum());
		return subjectDtoPageInfo;
	}

	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return subjectChoicesConverter.toDto(this.findListById(ids), true);
	}

	/**
	 * 根据题目ID查询题目信息和选项
	 */
	private SubjectChoices getSubjectChoicesById(Long subjectId) {
		SubjectChoices subjectChoices = this.get(subjectId);
		SubjectOption subjectOption = new SubjectOption();
		subjectOption.setSubjectChoicesId(subjectChoices.getId());
		List<SubjectOption> options = subjectOptionService.getBySubjectChoicesId(subjectOption);
		subjectChoices.setOptions(options);
		return subjectChoices;
	}
}
