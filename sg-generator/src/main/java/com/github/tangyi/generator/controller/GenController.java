package com.github.tangyi.generator.controller;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.generator.model.GenTable;
import com.github.tangyi.generator.model.GenTableColumn;
import com.github.tangyi.generator.service.IGenTableColumnService;
import com.github.tangyi.generator.service.IGenTableService;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author tangyi
 * @date 2022/7/18 4:01 下午
 */
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController {

	@Autowired
	private IGenTableService genTableService;

	@Autowired
	private IGenTableColumnService genTableColumnService;

	/**
	 * 查询代码生成列表
	 */
	@GetMapping("/list")
	public R<PageInfo<GenTable>> genList(GenTable genTable) {
		List<GenTable> list = genTableService.selectGenTableList(genTable);
		return R.success(new PageInfo<>(list));
	}

	/**
	 * 修改代码生成业务
	 */
	@GetMapping(value = "/{talbleId}")
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

	/**
	 * 查询数据库列表
	 */
	@GetMapping("/db/list")
	public R<PageInfo<GenTable>> dataList(GenTable genTable) {
		List<GenTable> list = genTableService.selectDbTableList(genTable);
		return R.success(new PageInfo<>(list));
	}

	/**
	 * 查询数据表字段列表
	 */
	@GetMapping(value = "/column/{talbleId}")
	public R<PageInfo<GenTableColumn>> columnList(Long tableId) {
		PageInfo<GenTableColumn> dataInfo = new PageInfo<>();
		List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
		dataInfo.setList(list);
		dataInfo.setTotal(list.size());
		return R.success(dataInfo);
	}

	/**
	 * 导入表结构（保存）
	 */
	@GetMapping("/importTable")
	public R<Boolean> importTableSave(@RequestParam String table, @RequestParam String comment) {
		genTableService.importGenTable(table, comment);
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

	/**
	 * 删除代码生成
	 */
	@DeleteMapping("/{tableIds}")
	public R<Boolean> remove(@PathVariable Long[] tableIds) {
		genTableService.deleteGenTableByIds(tableIds);
		return R.success(Boolean.TRUE);
	}

	/**
	 * 预览代码
	 */
	@GetMapping("/preview/{tableId}")
	public R<Map<String, String>> preview(@PathVariable("tableId") Long tableId) throws IOException {
		Map<String, String> dataMap = genTableService.previewCode(tableId);
		return R.success(dataMap);
	}

	/**
	 * 生成代码（下载方式）
	 */
	@GetMapping("/download/{tableName}")
	public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
		byte[] data = genTableService.downloadCode(tableName);
		genCode(response, data);
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
	 * 批量生成代码
	 */
	@GetMapping("/batchGenCode")
	public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
		String[] tableNames = Convert.toStrArray(tables);
		byte[] data = genTableService.downloadCode(tableNames);
		genCode(response, data);
	}

	/**
	 * 生成zip文件
	 */
	private void genCode(HttpServletResponse response, byte[] data) throws IOException {
		response.reset();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
}
