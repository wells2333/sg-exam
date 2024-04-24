/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

