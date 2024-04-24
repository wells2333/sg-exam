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

package com.github.tangyi.log;

import com.github.tangyi.common.log.SgLogEvent;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.user.service.sys.LogService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

@Configuration
public class SgLogListener {

	private final LogService logService;

	public SgLogListener(LogService logService) {
		this.logService = logService;
	}

	@Async
	@Order
	@EventListener(SgLogEvent.class)
	public void saveSysLog(SgLogEvent event) {
		Log log = (Log) event.getSource();
		logService.save(log);
	}
}
