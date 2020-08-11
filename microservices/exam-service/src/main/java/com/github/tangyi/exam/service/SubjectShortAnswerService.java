package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.module.SubjectShortAnswer;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.exam.mapper.SubjectShortAnswerMapper;
import com.github.tangyi.exam.utils.SubjectUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 简答题service
 *
 * @author tangyi
 * @date 2019/6/16 14:58
 */
@Service
public class SubjectShortAnswerService extends CrudService<SubjectShortAnswerMapper, SubjectShortAnswer>
        implements ISubjectService {

    /**
     * 查找题目
     *
     * @param subjectShortAnswer subjectShortAnswer
     * @return SubjectShortAnswer
     * @author tangyi
     * @date 2019/6/16 14:58
     */
    @Override
    @Cacheable(value = "subjectShortAnswer#" + CommonConstant.CACHE_EXPIRE, key = "#subjectShortAnswer.id")
    public SubjectShortAnswer get(SubjectShortAnswer subjectShortAnswer) {
        return super.get(subjectShortAnswer);
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
    @CacheEvict(value = "subjectShortAnswer", key = "#subjectShortAnswer.id")
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
    @CacheEvict(value = "subjectShortAnswer", key = "#subjectShortAnswer.id")
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
    @CacheEvict(value = "subjectShortAnswer", key = "#subjectShortAnswer.id")
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
    @CacheEvict(value = "subjectShortAnswer", allEntries = true)
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
    @CacheEvict(value = "subjectShortAnswer", allEntries = true)
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
        return SubjectUtil.subjectShortAnswerToDto(this.get(id), true);
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
	@CacheEvict(value = "subjectShortAnswer", key = "#subjectDto.id")
	public int insertSubject(SubjectDto subjectDto) {
        SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
        BeanUtils.copyProperties(subjectDto, subjectShortAnswer);
        subjectShortAnswer.setAnswer(subjectDto.getAnswer().getAnswer());
        return this.insert(subjectShortAnswer);
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
	@CacheEvict(value = "subjectShortAnswer", key = "#subjectDto.id")
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
	@CacheEvict(value = "subjectShortAnswer", key = "#subjectDto.id")
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
	@CacheEvict(value = "subjectShortAnswer", key = "#subjectDto.id")
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
	@CacheEvict(value = "subjectShortAnswer", allEntries = true)
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
	@CacheEvict(value = "subjectShortAnswer", allEntries = true)
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
        return SubjectUtil.subjectShortAnswerToDto(this.findList(subjectShortAnswer), true);
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
        PageInfo subjectShortAnswerPageInfo = this.findPage(pageInfo, subjectShortAnswer);
        PageInfo<SubjectDto> subjectDtoPageInfo = new PageInfo<>();
		PageUtil.copyProperties(subjectShortAnswerPageInfo, subjectDtoPageInfo);
        subjectDtoPageInfo.setList(SubjectUtil.subjectShortAnswerToDto(subjectShortAnswerPageInfo.getList(), true));
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
        return SubjectUtil.subjectShortAnswerToDto(this.findListById(ids), true);
    }
}
