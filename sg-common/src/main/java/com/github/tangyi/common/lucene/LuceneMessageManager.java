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

package com.github.tangyi.common.lucene;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LuceneMessageManager {

	private static final String TOPIC = "redis.channel.topic.lucene";

	@Data
	private static final class DocMessage {
		private String type;
		private IndexDoc doc;
		private int operation;
	}

	private static final class MessageReceiver {

		private final LuceneMessageManager messageManager;

		public MessageReceiver(LuceneMessageManager messageManager) {
			this.messageManager = messageManager;
		}

		// 通过反射调用
		public void receiveMessage(String message, String channel) {
			log.info("receive message: {}, channel: {}", message, channel);
			try {
				this.messageManager.receiveMessage(message);
			} catch (Exception e) {
				log.error("Failed to handle receive message.", e);
			}
		}
	}

	private final RedisTemplate<String, String> redisTemplate;

	public LuceneMessageManager(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void sendMessage(IndexDoc indexDoc, DocType type, IndexDocOperation operation) {
		DocMessage message = new DocMessage();
		message.setDoc(indexDoc);
		message.setType(type.getType());
		message.setOperation(operation.getType());
		String json = JSON.toJSONString(message);
		this.redisTemplate.convertAndSend(TOPIC, json);
	}

	public void receiveMessage(String json) throws IOException {
		DocMessage message = JSON.parseObject(json, DocMessage.class);
		if (message != null) {
			DocType docType = DocType.matchByType(message.getType());
			if (docType != null) {
				if (message.getOperation() == IndexDocOperation.ADD.getType()) {
					LuceneIndexManager.getInstance().addDocument(message.getDoc(), docType);
				} else if (message.getOperation() == IndexDocOperation.UPDATE.getType()) {
					LuceneIndexManager.getInstance().updateDocument(message.getDoc(), docType);
				} else if (message.getOperation() == IndexDocOperation.DEL.getType()) {
					LuceneIndexManager.getInstance().deleteDocument(message.getDoc(), docType);
				}
			}
		}
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(LuceneMessageManager messageManager) {
		return new MessageListenerAdapter(new MessageReceiver(messageManager), "receiveMessage");
	}

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter adapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(factory);
		container.addMessageListener(adapter, new ChannelTopic(TOPIC));
		return container;
	}
}
