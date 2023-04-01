package com.github.tangyi.api.user.model;

import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_attachment_group")
public class AttachGroup extends BaseEntity<AttachGroup> {

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "group_code")
	private String groupCode;

	@Column(name = "storage_type")
	private Integer storageType;

	@Column(name = "url_expire")
	private Long urlExpire;

	@Column(name = "remark")
	private String remark;

	public static AttachGroup of(String groupCode, Integer storageType) {
		AttachGroup group = new AttachGroup();
		group.setGroupCode(groupCode);
		group.setStorageType(storageType);
		return group;
	}

	public static AttachGroup of(AttachTypeEnum typeEnum) {
		AttachGroup group = new AttachGroup();
		group.setGroupCode(typeEnum.getValue());
		group.setStorageType(typeEnum.getStorageType());
		return group;
	}
}
