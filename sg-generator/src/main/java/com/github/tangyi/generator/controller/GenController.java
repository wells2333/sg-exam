package com.github.tangyi.generator.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.generator.model.GenTable;
import com.github.tangyi.generator.model.GenTableColumn;
import com.github.tangyi.generator.service.IGenTableColumnService;
import com.github.tangyi.generator.service.impl.GenTableServiceImpl;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "代码生成Table管理")
@AllArgsConstructor
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController {

	private final GenTableServiceImpl genTableService;

	private final IGenTableColumnService genTableColumnService;

	@GetMapping("/list")
	@Operation(summary = "获取Table列表")
	public R<PageInfo<GenTable>> genList(@RequestParam Map<String, Object> params,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(genTableService.findPage(params, pageNum, pageSize));
	}

	@GetMapping(value = "/{talbleId}")
	@Operation(summary = "修改代码生成业务")
	public R<Map<String, Object>> getInfo(@PathVariable Long talbleId) {
		GenTable table = genTableService.selectGenTableById(talbleId);
		List<GenTable> tables = genTableService.selectGenTableAll();
		List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(talbleId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("info", table);
		map.put("rows", list);
		map.put("tables", tables);
		return R.success(map);
	}

	@GetMapping("/db/list")
	@Operation(summary = "查询数据库列表")
	public R<PageInfo<GenTable>> dataList(GenTable genTable) {
		List<GenTable> list = genTableService.selectDbTableList(genTable);
		return R.success(new PageInfo<>(list));
	}

	@Operation(summary = "查询数据表字段列表")
	@GetMapping(value = "/column/{talbleId}")
	public R<PageInfo<GenTableColumn>> columnList(Long tableId) {
		PageInfo<GenTableColumn> dataInfo = new PageInfo<>();
		List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
		dataInfo.setList(list);
		dataInfo.setTotal(list.size());
		return R.success(dataInfo);
	}

	@GetMapping("/importTable")
	@Operation(summary = "导入表结构")
	public R<Boolean> importTableSave(@RequestParam String table, @RequestParam String comment,
			@RequestParam(required = false) String packageName, @RequestParam(required = false) String tenantCode) {
		genTableService.importGenTable(table, comment, packageName, tenantCode);
		return R.success();
	}

	/**
	 * 修改保存代码生成业务
	 */
	@PutMapping
	public R<Boolean> editSave(HttpServletRequest request, @Validated @RequestBody GenTable genTable) {
		Map<String, String> optionMap = Maps.newHashMap();
		Map<String, String[]> map = request.getParameterMap();
		if (MapUtils.isNotEmpty(map)) {
			map.forEach((key, value) -> optionMap.put(key, value[0]));
		}
		String options = JSON.toJSONString(optionMap);
		genTableService.validateEdit(genTable, options);
		genTableService.updateGenTable(genTable, options);
		return R.success();
	}

	@DeleteMapping("/{tableId}")
	@Operation(summary = "删除代码生成", description = "根据ID删除代码生成")
	public R<Boolean> delete(@PathVariable Long tableId) {
		genTableService.deleteGenTableById(tableId);
		return R.success(Boolean.TRUE);
	}

	@GetMapping("/preview/{tableId}")
	@Operation(summary = "预览代码")
	public R<Map<String, String>> preview(@PathVariable("tableId") Long tableId) throws IOException {
		Map<String, String> dataMap = genTableService.previewCode(tableId);
		return R.success(dataMap);
	}

	@GetMapping("/download/{tableName}")
	@Operation(summary = "生成代码（下载方式）")
	public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
		byte[] data = genTableService.downloadCode(tableName);
		genCode(tableName, response, data);
	}

	/**
	 * 生成代码（自定义路径）
	 */
	@GetMapping("/genCode/{tableName}")
	public R<Boolean> genCode(@PathVariable("tableName") String tableName) {
		genTableService.generatorCode(tableName);
		return R.success();
	}

	/**
	 * 同步数据库
	 */
	@GetMapping("/synchDb/{tableName}")
	public R<Boolean> synchDb(@PathVariable("tableName") String tableName) {
		genTableService.synchDb(tableName);
		return R.success(Boolean.TRUE);
	}

	/**
	 * 生成zip文件
	 */
	private void genCode(String tableName, HttpServletResponse response, byte[] data) throws IOException {
		response.reset();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + tableName + ".zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
}
