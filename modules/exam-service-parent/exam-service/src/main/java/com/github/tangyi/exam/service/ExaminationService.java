package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.api.module.ExaminationSubject;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 考试service
 *
 * @author tangyi
 * @date 2018/11/8 21:19
 */
@Slf4j
@AllArgsConstructor
@Service
public class ExaminationService extends CrudService<ExaminationMapper, Examination> {

    private final SubjectChoicesService subjectChoicesService;

    private final SubjectService subjectService;

    private final ExaminationSubjectService examinationSubjectService;

    /**
     * 查询考试
     *
     * @param examination examination
     * @return Examination
     * @author tangyi
     * @date 2019/1/3 14:06
     */
    @Override
    @Cacheable(value = "examination#" + CommonConstant.CACHE_EXPIRE, key = "#examination.id")
    public Examination get(Examination examination) {
        return super.get(examination);
    }

    /**
     * 更新考试
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:07
     */
    @Override
    @Transactional
    @CacheEvict(value = "examination", key = "#examination.id")
    public int update(Examination examination) {
        return super.update(examination);
    }

    /**
     * 删除考试
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:07
     */
    @Override
    @Transactional
    @CacheEvict(value = "examination", key = "#examination.id")
    public int delete(Examination examination) {
        return super.delete(examination);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2018/12/4 9:51
     */
    @Override
    @Transactional
    @CacheEvict(value = "examination", allEntries = true)
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 查询考试数量
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/3/1 15:32
     */
    public int findExaminationCount(Examination examination) {
        return this.dao.findExaminationCount(examination);
    }

    /**
     * 查询参与考试人数
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/10/27 20:07:38
     */
    public int findExamUserCount(Examination examination) {
        return this.dao.findExamUserCount(examination);
    }

    /**
     * 根据考试ID获取题目分页数据
     *
     * @param subjectDto subjectDto
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param sort       sort
     * @param order      order
     * @return PageInfo
     * @author tangyi
     * @date 2019/06/16 16:00
     */
    public PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, String pageNum, String pageSize, String sort, String order) {
        // 返回结果
        PageInfo<SubjectDto> subjectDtoPageInfo = new PageInfo<>();
        // 查询考试题目关联表
        ExaminationSubject examinationSubject = new ExaminationSubject();
        examinationSubject.setTenantCode(SysUtil.getTenantCode());
        examinationSubject.setExaminationId(subjectDto.getExaminationId());
        PageInfo<ExaminationSubject> examinationSubjects = examinationSubjectService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examinationSubject);
        // 根据题目ID查询题目信息
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examinationSubjects.getList())) {
            // 按题目类型分组，获取对应的ID集合
            subjectDtoList = subjectService.findSubjectDtoList(examinationSubjects.getList());
        }
        subjectDtoPageInfo.setList(subjectDtoList);
        subjectDtoPageInfo.setTotal(examinationSubjects.getTotal());
        subjectDtoPageInfo.setPageNum(examinationSubjects.getPageNum());
        subjectDtoPageInfo.setPageSize(examinationSubjects.getPageSize());
        return subjectDtoPageInfo;
    }

    /**
     * 根据考试ID查询题目id列表
     *
     * @param examinationId examinationId
     * @return ExaminationSubject
     * @author tangyi
     * @date 2019/06/18 14:34
     */
    public List<ExaminationSubject> findListByExaminationId(Long examinationId) {
        return examinationSubjectService.findListByExaminationId(examinationId);
    }
}
