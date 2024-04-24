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

package com.github.tangyi.exam.service.course;

import com.github.tangyi.api.exam.dto.KnowledgePointDto;
import com.github.tangyi.api.exam.model.ExamCourseKnowledgePoint;
import com.github.tangyi.api.exam.service.IExamCourseKnowledgePointService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseKnowledgePointMapper;
import com.github.tangyi.exam.service.media.ExamMediaService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseKnowledgePointService
		extends CrudService<ExamCourseKnowledgePointMapper, ExamCourseKnowledgePoint>
		implements IExamCourseKnowledgePointService, ExamCacheName {

	private final ExamMediaService mediaService;

	@Override
	@Cacheable(value = ExamCacheName.KNOWLEDGE_POINT, key = "#id")
	public ExamCourseKnowledgePoint get(Long id) {
		return super.get(id);
	}

	public KnowledgePointDto getDetail(Long id) {
		KnowledgePointDto dto = new KnowledgePointDto();
		ExamCourseKnowledgePoint point = this.get(id);
		if (point != null) {
			BeanUtils.copyProperties(point, dto);
			if (point.getVideoId() != null && point.getVideoUrl() == null) {
				dto.setVideoUrl(mediaService.videoUrl(point.getVideoId()));
			}
			if (point.getSectionId() != null && point.getSpeechUrl() == null) {
				dto.setSpeechUrl(mediaService.videoUrl(Long.valueOf(point.getSpeechId())));
			}
		}
		return dto;
	}

	@Override
	public List<KnowledgePointDto> getPoints(Long sectionId) {
		List<KnowledgePointDto> dtoList = Lists.newArrayList();
		List<ExamCourseKnowledgePoint> points = findListBySectionId(sectionId);
		if (CollectionUtils.isNotEmpty(points)) {
			for (ExamCourseKnowledgePoint point : points) {
				KnowledgePointDto dto = new KnowledgePointDto();
				BeanUtils.copyProperties(point, dto);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public List<ExamCourseKnowledgePoint> findListBySectionId(Long sectionId) {
		return this.dao.findListBySectionId(sectionId);
	}

	@Override
	@Transactional
	public int insert(ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		examCourseKnowledgePoint.setCommonValue();
		return super.insert(examCourseKnowledgePoint);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE_POINT, key = "#examCourseKnowledgePoint.id")
	public int update(ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		examCourseKnowledgePoint.setCommonValue();
		return super.update(examCourseKnowledgePoint);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE_POINT, key = "#examCourseKnowledgePoint.id")
	public int delete(ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		return super.delete(examCourseKnowledgePoint);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE_POINT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
