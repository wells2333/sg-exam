package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.exam.api.dto.SubjectDto;

import java.util.List;

/**
 * 题目通用接口
 *
 * @author tangyi
 * @date 2019/6/16 17:30
 */
public interface BaseSubjectService {

    /**
     * 根据ID查询
     *
     * @param id id
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 17:35
     */
    SubjectDto getSubject(String id);

    /**
     * 根据题目序号查找
     *
     * @param serialNumber serialNumber
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 17:46
     */
    SubjectDto getSubjectBySerialNumber(Integer serialNumber);

    /**
     * 查询题目列表
     *
     * @param subjectDto subjectDto
     * @return List
     * @author tangyi
     * @date 2019/06/16 18:10
     */
    List<SubjectDto> findSubjectList(SubjectDto subjectDto);

    /**
     * 查询题目分页列表
     *
     * @param pageInfo   pageInfo
     * @param subjectDto subjectDto
     * @return List
     * @author tangyi
     * @date 2019/06/16 18:10
     */
    PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto);

    /**
     * 根据ID批量查询
     *
     * @param subjectDto subjectDto
     * @return List
     * @author tangyi
     * @date 2019/06/16 18:10
     */
    List<SubjectDto> findSubjectListById(SubjectDto subjectDto);

    /**
     * 保存
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 17:47
     */
    int insertSubject(SubjectDto subjectDto);

    /**
     * 更新
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 17:47
     */
    int updateSubject(SubjectDto subjectDto);

    /**
     * 删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 17:48
     */
    int deleteSubject(SubjectDto subjectDto);

    /**
     * 批量删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 17:49
     */
    int deleteAllSubject(SubjectDto subjectDto);

    /**
     * 物理删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:48
     */
    int physicalDeleteSubject(SubjectDto subjectDto);

    /**
     * 物理批量删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:49
     */
    int physicalDeleteAllSubject(SubjectDto subjectDto);
}
