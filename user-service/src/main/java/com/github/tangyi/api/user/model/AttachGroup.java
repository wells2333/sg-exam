package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 *
 * @author tangyi
 * @date 2022/4/16 3:17 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_attachment_group")
public class AttachGroup extends BaseEntity<AttachGroup> {

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "group_code")
	private String groupCode;

	@Column(name = "url_expire")
	private Integer urlExpire;

	@Column(name = "remark")
	private String remark;
}
