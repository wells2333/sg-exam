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
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 简答题service
 *
 * @author tangyi
 * @date 2019/6/16 14:58
 */
@Service
@AllArgsConstructor
public class SubjectShortAnswerService extends CrudService<SubjectShortAnswerMapper, SubjectShortAnswer>
		implements ISubjectService {

	private final SubjectShortAnswerConverter subjectShortAnswerConverter;

	/**
	 * 查找题目
	 *
	 * @param id id
	 * @return SubjectShortAnswer
	 * @author tangyi
	 * @date 2019/6/16 14:58
	 */
	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#id")
	public SubjectShortAnswer get(Long id) {
		return super.get(id);
	}

	/**
	 * 新增
	 *
	 * @param subjectShortAnswer subjectShortAnswer
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 14:58
	 */
	@Override
	@Transactional
	public int insert(SubjectShortAnswer subjectShortAnswer) {
		return super.insert(subjectShortAnswer);
	}

	/**
	 * 更新题目
	 *
	 * @param subjectShortAnswer subjectShortAnswer
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 14:58
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int update(SubjectShortAnswer subjectShortAnswer) {
		return super.update(subjectShortAnswer);
	}

	/**
	 * 删除题目
	 *
	 * @param subjectShortAnswer subjectShortAnswer
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 14:58
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int delete(SubjectShortAnswer subjectShortAnswer) {
		return super.delete(subjectShortAnswer);
	}

	/**
	 * 物理删除题目
	 *
	 * @param subjectShortAnswer subjectShortAnswer
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 22:58
	 */
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int physicalDelete(SubjectShortAnswer subjectShortAnswer) {
		return this.dao.physicalDelete(subjectShortAnswer);
	}

	/**
	 * 批量删除题目
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019/6/16 14:58
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
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
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	/**
	 * 根据ID查询
	 *
	 * @param id id
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 17:36
	 */
	@Override
	public SubjectDto getSubject(Long id) {
		return subjectShortAnswerConverter.toDto(this.get(id));
	}

	/**
	 * 根据上一题ID查询下一题
	 *
	 * @param examinationId examinationId
	 * @param previousId    previousId
	 * @param nextType      0：下一题，1：上一题
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/09/14 17:05
	 */
	@Override
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		return null;
	}

	/**
	 * 保存
	 *
	 * @param subjectDto subjectDto
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 17:54
	 */
	@Override
	@Transactional
	public BaseEntity<SubjectShortAnswer> insertSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		subjectShortAnswer.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(subjectShortAnswer);
		return subjectShortAnswer;
	}

	/**
	 * 更新
	 *
	 * @param subjectDto subjectDto
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 17:54
	 */
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

	/**
	 * 删除
	 *
	 * @param subjectDto subjectDto
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 17:54
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		return this.delete(subjectShortAnswer);
	}

	/**
	 * 物理删除
	 *
	 * @param subjectDto subjectDto
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 22:59
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		return this.physicalDelete(subjectShortAnswer);
	}

	/**
	 * 批量删除
	 *
	 * @param ids ids
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 17:54
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	/**
	 * 物理删除
	 *
	 * @param ids ids
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/16 22:59
	 */
	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.physicalDeleteAll(ids);
	}

	/**
	 * 查询列表
	 *
	 * @param subjectDto subjectDto
	 * @return List
	 * @author tangyi
	 * @date 2019/06/16 18:17
	 */
	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
		return subjectShortAnswerConverter.toDto(this.findList(subjectShortAnswer), true);
	}

	/**
	 * 查询分页列表
	 *
	 * @param pageInfo   pageInfo
	 * @param subjectDto subjectDto
	 * @return PageInfo
	 * @author tangyi
	 * @date 2019/06/16 18:17
	 */
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

	/**
	 * 根据ID查询列表
	 *
	 * @param ids ids
	 * @return List
	 * @author tangyi
	 * @date 2019/06/16 18:17
	 */
	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return subjectShortAnswerConverter.toDto(this.findListById(ids), true);
	}
}
