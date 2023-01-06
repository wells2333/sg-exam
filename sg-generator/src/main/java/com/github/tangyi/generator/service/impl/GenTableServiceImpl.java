package com.github.tangyi.generator.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.tangyi.common.exceptions.ServiceException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.generator.config.GenConfig;
import com.github.tangyi.generator.constants.GenConstants;
import com.github.tangyi.generator.mapper.GenTableColumnMapper;
import com.github.tangyi.generator.mapper.GenTableMapper;
import com.github.tangyi.generator.model.GenTable;
import com.github.tangyi.generator.model.GenTableColumn;
import com.github.tangyi.generator.service.IGenTableService;
import com.github.tangyi.generator.util.GenUtils;
import com.github.tangyi.generator.util.VelocityInitializer;
import com.github.tangyi.generator.util.VelocityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@AllArgsConstructor
public class GenTableServiceImpl extends CrudService<GenTableMapper, GenTable> implements IGenTableService {

	private final GenTableMapper genTableMapper;

	private final GenTableColumnMapper genTableColumnMapper;

	private final GenConfig genConfig;

	/**
	 * 查询业务信息
	 *
	 * @param id 业务ID
	 * @return 业务信息
	 */
	@Override
	public GenTable selectGenTableById(Long id) {
		GenTable genTable = genTableMapper.selectGenTableById(id);
		setTableFromOptions(genTable);
		return genTable;
	}

	/**
	 * 查询据库列表
	 *
	 * @param genTable 业务信息
	 * @return 数据库表集合
	 */
	@Override
	public List<GenTable> selectDbTableList(GenTable genTable) {
		return genTableMapper.selectDbTableList(genTable);
	}

	/**
	 * 查询据库列表
	 *
	 * @param tableNames 表名称组
	 * @return 数据库表集合
	 */
	@Override
	public List<GenTable> selectDbTableListByNames(String[] tableNames) {
		return genTableMapper.selectDbTableListByNames(tableNames);
	}

	/**
	 * 查询所有表信息
	 *
	 * @return 表信息集合
	 */
	@Override
	public List<GenTable> selectGenTableAll() {
		return genTableMapper.selectGenTableAll();
	}

	/**
	 * 修改业务
	 *
	 * @param genTable 业务信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public void updateGenTable(GenTable genTable, String options) {
		genTable.setOptions(options);
		int row = genTableMapper.updateGenTable(genTable);
		if (row > 0) {
			for (GenTableColumn cenTableColumn : genTable.getColumns()) {
				genTableColumnMapper.updateGenTableColumn(cenTableColumn);
			}
		}
	}

	@Override
	@Transactional
	public void deleteGenTableById(Long tableId) {
		Long[] tableIds = Collections.singletonList(tableId).toArray(Long[]::new);
		genTableMapper.deleteGenTableByIds(tableIds);
		genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
	}

	/**
	 * 导入表结构
	 *
	 * @param tableName 导入表列表
	 */
	@Override
	@Transactional
	public void importGenTable(String tableName, String comment, String packageName, String tenantCode) {
		try {
			GenTable table = new GenTable();
			table.setTableName(tableName);
			table.setTableComment(comment);
			if (StringUtils.isNotEmpty(tenantCode)) {
				table.setCommonValue(SysUtil.getUser(), tenantCode);
			} else {
				table.setCommonValue();
			}
			if (StringUtils.isEmpty(packageName)) {
				packageName = genConfig.getPackageName();
			}
			GenUtils.initTable(table, SysUtil.getUser(), genConfig, packageName);
			int row = genTableMapper.insertGenTable(table);
			if (row > 0) {
				// 保存列信息
				List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
				for (GenTableColumn column : genTableColumns) {
					GenUtils.initColumnField(column, table);
					genTableColumnMapper.insertGenTableColumn(column);
				}
			}
		} catch (Exception e) {
			throw new ServiceException("导入失败：" + e.getMessage());
		}
	}

	/**
	 * 预览代码
	 *
	 * @param tableId 表编号
	 * @return 预览数据列表
	 */
	@Override
	public Map<String, String> previewCode(Long tableId) {
		Map<String, String> dataMap = new LinkedHashMap<>();
		// 查询表信息
		GenTable table = genTableMapper.selectGenTableById(tableId);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);
		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);
			dataMap.put(template, sw.toString());
		}
		return dataMap;
	}

	/**
	 * 生成代码（下载方式）
	 *
	 * @param tableName 表名称
	 * @return 数据
	 */
	@Override
	public byte[] downloadCode(String tableName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		generatorCode(tableName, zip);
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 生成代码（自定义路径）
	 *
	 * @param tableName 表名称
	 */
	@Override
	public void generatorCode(String tableName) {
		// 查询表信息
		GenTable table = genTableMapper.selectGenTableByName(tableName);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);

		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
		for (String template : templates) {
			if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm")) {
				// 渲染模板
				StringWriter sw = new StringWriter();
				Template tpl = Velocity.getTemplate(template, "UTF-8");
				tpl.merge(context, sw);
				try {
					String path = getGenPath(table, template);
					FileUtils.writeStringToFile(new File(path), sw.toString(), "UTF-8");
					log.info("genCode success, tableName: {}, path: {}", tableName, path);
				} catch (IOException e) {
					throw new ServiceException("渲染模板失败，表名：" + table.getTableName());
				}
			}
		}
	}

	/**
	 * 同步数据库
	 *
	 * @param tableName 表名称
	 */
	@Override
	@Transactional
	public void synchDb(String tableName) {
		GenTable table = genTableMapper.selectGenTableByName(tableName);
		List<GenTableColumn> tableColumns = table.getColumns();
		List<String> tableColumnNames = tableColumns.stream().map(GenTableColumn::getColumnName)
				.collect(Collectors.toList());

		List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
		if (CollectionUtils.isEmpty(dbTableColumns)) {
			throw new ServiceException("同步数据失败，原表结构不存在");
		}
		List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName)
				.collect(Collectors.toList());

		dbTableColumns.forEach(column -> {
			if (!tableColumnNames.contains(column.getColumnName())) {
				GenUtils.initColumnField(column, table);
				genTableColumnMapper.insertGenTableColumn(column);
			}
		});

		List<GenTableColumn> delColumns = tableColumns.stream()
				.filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(delColumns)) {
			genTableColumnMapper.deleteGenTableColumns(delColumns);
		}
	}

	/**
	 * 批量生成代码（下载方式）
	 *
	 * @param tableNames 表数组
	 * @return 数据
	 */
	@Override
	public byte[] downloadCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String tableName : tableNames) {
			generatorCode(tableName, zip);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 查询表信息并生成代码
	 */
	private void generatorCode(String tableName, ZipOutputStream zip) {
		// 查询表信息
		GenTable table = genTableMapper.selectGenTableByName(tableName);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);

		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);
			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
				IOUtils.write(sw.toString(), zip, "UTF-8");
				IOUtils.closeQuietly(sw);
				zip.flush();
				zip.closeEntry();
			} catch (IOException e) {
				log.error("渲染模板失败，表名：" + table.getTableName(), e);
			}
		}
	}

	/**
	 * 修改保存参数校验
	 *
	 * @param genTable 业务信息
	 */
	@Override
	public void validateEdit(GenTable genTable, String options) {
		if (GenConstants.TPL_TREE.equals(genTable.getTplCategory())) {
			JSONObject paramsObj = JSONObject.parseObject(options);
			if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE))) {
				throw new ServiceException("树编码字段不能为空");
			} else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE))) {
				throw new ServiceException("树父编码字段不能为空");
			} else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME))) {
				throw new ServiceException("树名称字段不能为空");
			} else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory())) {
				if (StringUtils.isEmpty(genTable.getSubTableName())) {
					throw new ServiceException("关联子表的表名不能为空");
				} else if (StringUtils.isEmpty(genTable.getSubTableFkName())) {
					throw new ServiceException("子表关联的外键名不能为空");
				}
			}
		}
	}

	/**
	 * 设置主键列信息
	 *
	 * @param table 业务表信息
	 */
	public void setPkColumn(GenTable table) {
		for (GenTableColumn column : table.getColumns()) {
			if (column.isPk()) {
				table.setPkColumn(column);
				break;
			}
		}
		if (Objects.isNull(table.getPkColumn())) {
			table.setPkColumn(table.getColumns().get(0));
		}
		if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
			for (GenTableColumn column : table.getSubTable().getColumns()) {
				if (column.isPk()) {
					table.getSubTable().setPkColumn(column);
					break;
				}
			}
			if (Objects.isNull(table.getSubTable().getPkColumn())) {
				table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
			}
		}
	}

	/**
	 * 设置主子表信息
	 *
	 * @param table 业务表信息
	 */
	public void setSubTable(GenTable table) {
		String subTableName = table.getSubTableName();
		if (StringUtils.isNotEmpty(subTableName)) {
			table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
		}
	}

	/**
	 * 设置代码生成其他选项值
	 *
	 * @param genTable 设置后的生成对象
	 */
	public void setTableFromOptions(GenTable genTable) {
		JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
		if (!Objects.isNull(paramsObj)) {
			String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
			String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
			String treeName = paramsObj.getString(GenConstants.TREE_NAME);
			String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
			String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);

			genTable.setTreeCode(treeCode);
			genTable.setTreeParentCode(treeParentCode);
			genTable.setTreeName(treeName);
			genTable.setParentMenuId(parentMenuId);
			genTable.setParentMenuName(parentMenuName);
		}
	}

	/**
	 * 获取代码生成地址
	 *
	 * @param table 业务表信息
	 * @param template 模板文件路径
	 * @return 生成地址
	 */
	public String getGenPath(GenTable table, String template) {
		String genPath = table.getGenPath();
		if (StringUtils.equals(genPath, "/")) {
			return genConfig.getGenDir() + File.separator + VelocityUtils.getFileName(template, table);
		}
		return genPath + File.separator + VelocityUtils.getFileName(template, table);
	}
}
