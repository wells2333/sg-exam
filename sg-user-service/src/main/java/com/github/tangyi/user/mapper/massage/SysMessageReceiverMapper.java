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

package com.github.tangyi.user.mapper.massage;

import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysMessageReceiverMapper extends CrudMapper<SysMessageReceiver> {

	List<SysMessageReceiver> getByMessageId(@Param("messageId") Long messageId);

	List<SysMessageReceiver> getPublishedMessage(@Param("params") Map<String, Object> params);

	int insertBatch(List<SysMessageReceiver> receivers);

	int deleteByMessageId(@Param("messageId") Long messageId);
}
