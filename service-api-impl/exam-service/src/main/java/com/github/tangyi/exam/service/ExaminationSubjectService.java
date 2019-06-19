package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.api.module.ExaminationSubject;
import com.github.tangyi.exam.mapper.ExaminationSubjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考试题目关联service
 *
 * @author tangyi
 * @date 2019/6/16 15:38
 */
@AllArgsConstructor
@Service
public class ExaminationSubjectService extends CrudService<ExaminationSubjectMapper, ExaminationSubject> {

    @Override
    public PageInfo<ExaminationSubject> findPage(PageInfo<ExaminationSubject> page, ExaminationSubject entity) {
        return super.findPage(page, entity);
    }

    /**
     * 根据题目ID删除
     *
     * @param examinationSubject examinationSubject
     * @return int
     * @author tangyi
     * @date 2019/06/16 21:56
     */
    @Transactional
    public int deleteBySubjectId(ExaminationSubject examinationSubject) {
        return this.dao.deleteBySubjectId(examinationSubject);
    }

    /**
     * 根据题目ID查询
     *
     * @param examinationSubject examinationSubject
     * @return List
     * @author tangyi
     * @date 2019/06/17 12:18
     */
    public List<ExaminationSubject> findListBySubjectId(ExaminationSubject examinationSubject) {
        return this.dao.findListBySubjectId(examinationSubject);
    }

    /**
     * 查询考试的题目数量
     *
     * @param examinationSubject examinationSubject
     * @return int
     * @author tangyi
     * @date 2019/06/18 14:35
     */
    public int findSubjectCount(ExaminationSubject examinationSubject) {
        return this.dao.findSubjectCount(examinationSubject);
    }

    /**
     * 根据考试ID和题目序号查询
     *
     * @param examinationSubject examinationSubject
     * @return ExaminationSubject
     * @author tangyi
     * @date 2019/06/18 23:17
     */
    public ExaminationSubject findByExaminationIdAndSerialNumber(ExaminationSubject examinationSubject) {
        return this.dao.findByExaminationIdAndSerialNumber(examinationSubject);
    }
}
