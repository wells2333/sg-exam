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

package com.github.tangyi.user.controller.attach;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.service.attach.SysAttachmentChunkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "附件分块管理")
@RequestMapping("/v1/attachment/chunk")
public class AttachmentChunkController extends BaseController {

    private final SysAttachmentChunkService sysAttachmentChunkService;

    @GetMapping("/list")
    @Operation(summary = "查询附件分块列表")
    public R<PageInfo<SysAttachmentChunk>> list(@RequestParam Map<String, Object> condition,
                                                @RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
                                                @RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
        return R.success(sysAttachmentChunkService.findPage(condition, pageNum, pageSize));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "获取附件分块详细信息")
    public R<SysAttachmentChunk> get(@PathVariable("id") Long id) {
        return R.success(sysAttachmentChunkService.get(id));
    }

    @PostMapping
    @Operation(summary = "新增附件分块")
    @SgLog(value = "新增附件分块", operationType = OperationType.INSERT)
    public R<Boolean> add(@RequestBody @Valid SysAttachmentChunk sysAttachmentChunk) {
        sysAttachmentChunk.setCommonValue();
        return R.success(sysAttachmentChunkService.insert(sysAttachmentChunk) > 0);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改附件分块")
    @SgLog(value = "修改附件分块", operationType = OperationType.UPDATE)
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysAttachmentChunk sysAttachmentChunk) {
        sysAttachmentChunk.setId(id);
        sysAttachmentChunk.setCommonValue(SysUtil.getUser(), sysAttachmentChunk.getTenantCode());
        return R.success(sysAttachmentChunkService.update(sysAttachmentChunk) > 0);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除附件分块")
    @SgLog(value = "删除附件分块", operationType = OperationType.DELETE)
    public R<Boolean> delete(@PathVariable("id") Long id) {
        SysAttachmentChunk sysAttachmentChunk = sysAttachmentChunkService.get(id);
        sysAttachmentChunk.setCommonValue();
        return R.success(sysAttachmentChunkService.delete(sysAttachmentChunk) > 0);
    }
}
