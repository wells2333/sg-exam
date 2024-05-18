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

package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.KnowledgeDto;
import com.github.tangyi.api.exam.model.Knowledge;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.vo.AttachmentVo;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.exam.service.KnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Tag(name = "知识库信息管理")
@RestController
@RequestMapping("/v1/knowledge")
public class KnowledgeController extends BaseController {

	private final KnowledgeService knowledgeService;

	@GetMapping("/{id}")
	@Operation(summary = "获取知识信息", description = "根据知识 id 获取知识详细信息")
	public R<Knowledge> get(@PathVariable Long id) {
		return R.success(knowledgeService.get(id));
	}

	@GetMapping("knowledgeList")
	@Operation(summary = "获取知识列表")
	public R<PageInfo<KnowledgeDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<Knowledge> pageInfo = knowledgeService.findPage(condition, pageNum, pageSize);
		PageInfo<KnowledgeDto> dtoPageInfo = new PageInfo<>();
		List<KnowledgeDto> list = new ArrayList<>();
		// 查询附件
		Set<Long> attachmentIdSet = new HashSet<>();
		pageInfo.getList().forEach(tempKnowledge -> attachmentIdSet.add(tempKnowledge.getAttachmentId()));
		// 根据附件 ID 查询附件
		//R<List<AttachmentVo>> returnT = userServiceClient.findAttachmentById(attachmentIdSet.toArray(new Long[0]));
		R<List<AttachmentVo>> returnT = null;
		pageInfo.getList().stream()
				// 转成 Dto
				.map(tempKnowledge -> {
					KnowledgeDto dto = new KnowledgeDto();
					BeanUtils.copyProperties(tempKnowledge, dto);
					return dto;
				})
				// 遍历
				.forEach(tempKnowledgeDto -> {
					if (returnT != null && CollectionUtils.isNotEmpty(returnT.getResult())) {
						AttachmentVo vo = returnT.getResult().stream()
								// 根据 ID 过滤
								.filter(tempAttachmentVo -> tempAttachmentVo.getId()
										.equals(tempKnowledgeDto.getAttachmentId()))
								// 匹配第一个
								.findFirst().orElse(null);
						// 设置附件名称、附件大小
						if (vo != null) {
							tempKnowledgeDto.setAttachName(vo.getAttachName());
							tempKnowledgeDto.setAttachSize(vo.getAttachSize());
						}
					}
					list.add(tempKnowledgeDto);
				});
		dtoPageInfo.setList(list);
		dtoPageInfo.setTotal(pageInfo.getTotal());
		dtoPageInfo.setPageNum(pageInfo.getPageNum());
		dtoPageInfo.setPageSize(pageInfo.getPageSize());
		return R.success(dtoPageInfo);
	}

	@PostMapping
	@Operation(summary = "创建知识", description = "创建知识")
	public R<Boolean> add(@RequestBody @Valid Knowledge knowledge) {
		knowledge.setCommonValue();
		return R.success(knowledgeService.insert(knowledge) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新知识信息", description = "根据知识 id 更新知识的基本信息")
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Knowledge knowledge) {
		knowledge.setId(id);
		knowledge.setCommonValue();
		return R.success(knowledgeService.update(knowledge) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除知识", description = "根据 ID 删除知识")
	@SgLog(value = "删除知识", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		boolean success = false;
		Knowledge knowledge = knowledgeService.get(id);
		if (knowledge != null) {
			knowledge.setCommonValue();
			success = knowledgeService.delete(knowledge) > 0;
		}
		// 删除附件
		//if (knowledge != null && knowledge.getAttachmentId() != null)
		//success = userServiceClient.deleteAttachment(knowledge.getAttachmentId()).getData();
		return R.success(success);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除知识", description = "根据知识 id 批量删除知识")
	@SgLog(value = "批量删除知识", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		boolean success = false;
		if (ArrayUtils.isNotEmpty(ids)) {
			success = knowledgeService.deleteAll(ids) > 0;
		}
		return R.success(success);
	}
}
