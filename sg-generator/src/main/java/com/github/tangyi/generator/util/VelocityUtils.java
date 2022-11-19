package com.github.tangyi.generator.util;

import com.alibaba.fastjson.JSONObject;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.StringUtil;
import com.github.tangyi.generator.constants.GenConstants;
import com.github.tangyi.generator.model.GenTable;
import com.github.tangyi.generator.model.GenTableColumn;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.util.*;

/**
 * 模板处理工具类
 *
 * @author ruoyi
 */
public class VelocityUtils {

	/** 项目空间路径 */
	private static final String PROJECT_PATH = "main/java";

	/** mybatis空间路径 */
	private static final String MYBATIS_PATH = "main/resources/mapper";

	/** 默认上级菜单，系统工具 */
	private static final String DEFAULT_PARENT_MENU_ID = "3";

	/**
	 * 设置模板变量信息
	 *
	 * @return 模板列表
	 */
	public static VelocityContext prepareContext(GenTable genTable) {
		String moduleName = genTable.getModuleName();
		String businessName = genTable.getBusinessName();
		String packageName = genTable.getPackageName();
		String functionName = genTable.getFunctionName();

		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("basePackage", "com.github.tangyi");
		velocityContext.put("tplCategory", genTable.getTplCategory());
		velocityContext.put("tableName", genTable.getTableName());
		velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
		velocityContext.put("ClassName", genTable.getClassName());
		velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
		velocityContext.put("moduleName", genTable.getModuleName());
		velocityContext.put("ModuleName", StringUtils.capitalize(genTable.getModuleName()));
		velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
		velocityContext.put("businessName", genTable.getBusinessName());
		velocityContext.put("businessNameUpper", genTable.getBusinessName().toUpperCase(Locale.ROOT));
		velocityContext.put("basePackage", getPackagePrefix(packageName));
		velocityContext.put("packageName", packageName);
		velocityContext.put("author", genTable.getFunctionAuthor());
		velocityContext.put("datetime", DateUtils.getDate());
		velocityContext.put("pkColumn", genTable.getPkColumn());
		velocityContext.put("importList", getImportList(genTable));
		velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
		velocityContext.put("columns", genTable.getColumns());
		velocityContext.put("table", genTable);
		setMenuVelocityContext(velocityContext, genTable);
		return velocityContext;
	}

	public static void setMenuVelocityContext(VelocityContext context, GenTable genTable) {
		String options = genTable.getOptions();
		JSONObject paramsObj = JSONObject.parseObject(options);
		String parentMenuId = getParentMenuId(paramsObj);
		context.put("parentMenuId", parentMenuId);
	}

	/**
	 * 获取模板信息
	 *
	 * @return 模板列表
	 */
	public static List<String> getTemplateList(String tplCategory) {
		List<String> templates = new ArrayList<String>();
		templates.add("vm/java/model.java.vm");
		templates.add("vm/java/controller.java.vm");
		templates.add("vm/java/service.java.vm");
		templates.add("vm/java/mapper.java.vm");
		templates.add("vm/java/constants.java.vm");
		templates.add("vm/xml/mapper.xml.vm");
		return templates;
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, GenTable genTable) {
		// 文件名称
		String fileName = "";
		// 包路径
		String packageName = genTable.getPackageName();
		// 模块名
		String moduleName = genTable.getModuleName();
		// 大写类名
		String className = genTable.getClassName();
		// 业务名称
		String businessName = genTable.getBusinessName();

		String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
		String mybatisPath = MYBATIS_PATH + "/" + moduleName;

		if (template.contains("model.java.vm")) {
			fileName = StringUtil.format("{}/api/{}/model/{}.java", javaPath, moduleName, className);
		}
		if (template.contains("mapper.java.vm")) {
			fileName = StringUtil.format("{}/{}/mapper/{}Mapper.java", javaPath, moduleName, className);
		} else if (template.contains("service.java.vm")) {
			fileName = StringUtil.format("{}/{}/service/{}Service.java", javaPath, moduleName, className);
		} else if (template.contains("controller.java.vm")) {
			fileName = StringUtil.format("{}/{}/controller/{}Controller.java", javaPath, moduleName, className);
		} else if(template.contains("vm/java/constants.java.vm")) {
			fileName = StringUtil.format("{}/{}/constants/{}Constants.java", javaPath, moduleName, className);
		}else if (template.contains("mapper.xml.vm")) {
			fileName = StringUtil.format("{}/{}/{}Mapper.xml", mybatisPath, moduleName, className);
		}
		return fileName;
	}

	/**
	 * 获取包前缀
	 *
	 * @param packageName 包名称
	 * @return 包前缀名称
	 */
	public static String getPackagePrefix(String packageName) {
		int lastIndex = packageName.lastIndexOf(".");
		return StringUtils.substring(packageName, 0, lastIndex);
	}

	/**
	 * 根据列类型获取导入包
	 *
	 * @param genTable 业务表对象
	 * @return 返回需要导入的包列表
	 */
	public static HashSet<String> getImportList(GenTable genTable) {
		List<GenTableColumn> columns = genTable.getColumns();
		GenTable subGenTable = genTable.getSubTable();
		HashSet<String> importList = new HashSet<String>();
		if (!Objects.isNull(subGenTable)) {
			importList.add("java.util.List");
		}
		for (GenTableColumn column : columns) {
			if (!column.isSuperColumn() && GenConstants.TYPE_DATE.equals(column.getJavaType())) {
				importList.add("java.util.Date");
				importList.add("com.fasterxml.jackson.annotation.JsonFormat");
			} else if (!column.isSuperColumn() && GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
				importList.add("java.math.BigDecimal");
			}
		}
		return importList;
	}

	/**
	 * 获取权限前缀
	 *
	 * @param moduleName 模块名称
	 * @param businessName 业务名称
	 * @return 返回权限前缀
	 */
	public static String getPermissionPrefix(String moduleName, String businessName) {
		return StringUtil.format("{}:{}", moduleName, businessName);
	}

	/**
	 * 获取上级菜单ID字段
	 *
	 * @param paramsObj 生成其他选项
	 * @return 上级菜单ID字段
	 */
	public static String getParentMenuId(JSONObject paramsObj) {
		if (!Objects.isNull(paramsObj) && paramsObj.containsKey(GenConstants.PARENT_MENU_ID) && StringUtils.isNotEmpty(
				paramsObj.getString(GenConstants.PARENT_MENU_ID))) {
			return paramsObj.getString(GenConstants.PARENT_MENU_ID);
		}
		return DEFAULT_PARENT_MENU_ID;
	}
}
