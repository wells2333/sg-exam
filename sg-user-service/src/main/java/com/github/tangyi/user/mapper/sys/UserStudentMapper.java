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

package com.github.tangyi.user.mapper.sys;

import com.github.tangyi.api.user.model.UserStudent;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStudentMapper extends CrudMapper<UserStudent> {

    /**
     * 根据 userId 查询
     */
    List<UserStudent> getByUserId(String userId);

    /**
     * 根据 studentId 查询
     */
    UserStudent getByStudentId(String studentId);

    /**
     * 根据用户 ID 删除
     */
    int deleteByUserId(String userId);

    /**
     * 根据学生 ID 删除
     */
    int deleteByStudentId(String studentId);
}
