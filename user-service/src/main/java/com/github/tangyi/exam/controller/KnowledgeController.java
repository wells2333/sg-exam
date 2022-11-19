package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.KnowledgeDto;
import com.github.tangyi.api.exam.model.Knowledge;
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

/**
 * 知识库controller
 *
 * @author tangyi
 * @date 2019/1/1 15:11
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "知识库信息管理")
@RestController
@RequestMapping("/v1/knowledge")
public class KnowledgeController extends BaseController {

	private final KnowledgeService knowledgeService;

	@GetMapping("/{id}")
	@Operation(summary = "获取知识信息", description = "根据知识id获取知识详细信息")
	public R<Knowledge> get(@PathVariable Long id) {
		return R.success(knowledgeService.get(id));
	}

	@GetMapping("knowledgeList")
	@Operation(summary = "获取知识列表")
	public R<PageInfo<KnowledgeDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<Knowledge> knowledgePageInfo = knowledgeService.findPage(condition, pageNum, pageSize);
		PageInfo<KnowledgeDto> knowledgeDtoPageInfo = new PageInfo<>();
		List<KnowledgeDto> knowledgeDtoList = new ArrayList<>();
		// 查询附件
		Set<Long> attachmentIdSet = new HashSet<>();
		knowledgePageInfo.getList().forEach(tempKnowledge -> attachmentIdSet.add(tempKnowledge.getAttachmentId()));
		// 根据附件ID查询附件
		//R<List<AttachmentVo>> returnT = userServiceClient.findAttachmentById(attachmentIdSet.toArray(new Long[0]));
		R<List<AttachmentVo>> returnT = null;
		knowledgePageInfo.getList().stream()
				// 转成Dto
				.map(tempKnowledge -> {
					KnowledgeDto knowledgeDto = new KnowledgeDto();
					BeanUtils.copyProperties(tempKnowledge, knowledgeDto);
					return knowledgeDto;
				})
				// 遍历
				.forEach(tempKnowledgeDto -> {
					if (returnT != null && CollectionUtils.isNotEmpty(returnT.getResult())) {
						AttachmentVo tempKnowledgeDtoAttachmentVo = returnT.getResult().stream()
								// 根据ID过滤
								.filter(tempAttachmentVo -> tempAttachmentVo.getId()
										.equals(tempKnowledgeDto.getAttachmentId()))
								// 匹配第一个
								.findFirst().orElse(null);
						// 设置附件名称、附件大小
						if (tempKnowledgeDtoAttachmentVo != null) {
							tempKnowledgeDto.setAttachName(tempKnowledgeDtoAttachmentVo.getAttachName());
							tempKnowledgeDto.setAttachSize(tempKnowledgeDtoAttachmentVo.getAttachSize());
						}
					}
					knowledgeDtoList.add(tempKnowledgeDto);
				});
		knowledgeDtoPageInfo.setList(knowledgeDtoList);
		knowledgeDtoPageInfo.setTotal(knowledgePageInfo.getTotal());
		knowledgeDtoPageInfo.setPageNum(knowledgePageInfo.getPageNum());
		knowledgeDtoPageInfo.setPageSize(knowledgePageInfo.getPageSize());
		return R.success(knowledgeDtoPageInfo);
	}

	@PostMapping
	@Operation(summary = "创建知识", description = "创建知识")
	public R<Boolean> add(@RequestBody @Valid Knowledge knowledge) {
		knowledge.setCommonValue();
		return R.success(knowledgeService.insert(knowledge) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新知识信息", description = "根据知识id更新知识的基本信息")
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Knowledge knowledge) {
		knowledge.setId(id);
		knowledge.setCommonValue();
		return R.success(knowledgeService.update(knowledge) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除知识", description = "根据ID删除知识")
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
	@Operation(summary = "批量删除知识", description = "根据知识id批量删除知识")
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		boolean success = false;
		if (ArrayUtils.isNotEmpty(ids)) {
			success = knowledgeService.deleteAll(ids) > 0;
		}
		return R.success(success);
	}
}
