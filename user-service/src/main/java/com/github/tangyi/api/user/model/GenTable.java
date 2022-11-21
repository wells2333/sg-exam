package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 代码生成 gen_table
 *
 * @author tangyi
 * @date 2022-11-21
 */
@Data
@Table(name = "gen_table")
@EqualsAndHashCode(callSuper = true)
public class GenTable extends BaseEntity<GenTable> {

	/**
	 * 编号
	 */
	@Column(name = "table_id")
	private Long tableId;

	/**
	 * 表名称
	 */
	@Column(name = "table_name")
	private String tableName;

	/**
	 * 表描述
	 */
	@Column(name = "table_comment")
	private String tableComment;

	/**
	 * 关联子表的表名
	 */
	@Column(name = "sub_table_name")
	private String subTableName;

	/**
	 * 子表关联的外键名
	 */
	@Column(name = "sub_table_fk_name")
	private String subTableFkName;

	/**
	 * 实体类名称
	 */
	@Column(name = "class_name")
	private String className;

	/**
	 * 使用的模板（crud单表操作 tree树表操作）
	 */
	@Column(name = "tpl_category")
	private String tplCategory;

	/**
	 * 生成包路径
	 */
	@Column(name = "package_name")
	private String packageName;

	/**
	 * 生成模块名
	 */
	@Column(name = "module_name")
	private String moduleName;

	/**
	 * 生成业务名
	 */
	@Column(name = "business_name")
	private String businessName;

	/**
	 * 生成功能名
	 */
	@Column(name = "function_name")
	private String functionName;

	/**
	 * 生成功能作者
	 */
	@Column(name = "function_author")
	private String functionAuthor;

	/**
	 * 生成代码方式（0zip压缩包 1自定义路径）
	 */
	@Column(name = "gen_type")
	private String genType;

	/**
	 * 生成路径（不填默认项目路径）
	 */
	@Column(name = "gen_path")
	private String genPath;

	/**
	 * 其它生成选项
	 */
	@Column(name = "options")
	private String options;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
