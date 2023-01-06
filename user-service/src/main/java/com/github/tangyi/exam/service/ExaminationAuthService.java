package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.ExaminationAuth;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.ExaminationAuthMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ExaminationAuthService extends CrudService<ExaminationAuthMapper, ExaminationAuth> {

    public List<ExaminationAuth> finListByUserId(ExaminationAuth examinationAuth) {
        return null;
    }

    public List<ExaminationAuth> finListByExaminationId(ExaminationAuth examinationAuth) {
        return null;
    }

    @Transactional
    public int deleteByExaminationId(ExaminationAuth examinationAuth) {
        return -1;
    }

    @Transactional
    public int deleteByUserId(ExaminationAuth examinationAuth) {
        return -1;
    }
}

