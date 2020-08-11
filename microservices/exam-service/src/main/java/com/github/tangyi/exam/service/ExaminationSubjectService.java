package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.module.ExaminationSubject;
import com.github.tangyi.common.service.CrudService;
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
     * 根据考试id查询题目id列表
     *
     * @param examinationId examinationId
     * @return int
     * @author tangyi
     * @date 2019/06/18 14:35
     */
    public List<ExaminationSubject> findListByExaminationId(Long examinationId) {
        return this.dao.findListByExaminationId(examinationId);
    }

    /**
     * 根据考试ID和题目ID查询
     *
     * @param examinationSubject examinationSubject
     * @return ExaminationSubject
     * @author tangyi
     * @date 2019/06/18 23:17
     */
    public ExaminationSubject findByExaminationIdAndSubjectId(ExaminationSubject examinationSubject) {
        return this.dao.findByExaminationIdAndSubjectId(examinationSubject);
    }

    /**
     * 根据上一题ID查询下一题
     *
     * @param examinationSubject examinationSubject
     * @return ExaminationSubject
     * @author tangyi
     * @date 2019/10/07 20:59:43
     */
    public ExaminationSubject getByPreviousId(ExaminationSubject examinationSubject) {
        return this.dao.getByPreviousId(examinationSubject);
    }

    /**
     * 根据当前题目ID查询上一题
     *
     * @param examinationSubject examinationSubject
     * @return ExaminationSubject
     * @author tangyi
     * @date 2019/10/07 20:59:43
     */
    public ExaminationSubject getPreviousByCurrentId(ExaminationSubject examinationSubject) {
        return this.dao.getPreviousByCurrentId(examinationSubject);
    }

    /**
     * 根据分类id查询
     *
     * @param examinationSubject examinationSubject
     * @return List
     * @author tangyi
     * @date 2019/10/24 21:47:24
     */
    public List<ExaminationSubject> findListByCategoryId(ExaminationSubject examinationSubject) {
        return this.dao.findListByCategoryId(examinationSubject);
    }
}
