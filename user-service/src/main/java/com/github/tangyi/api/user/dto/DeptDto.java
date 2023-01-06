package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.user.model.Dept;
import com.github.tangyi.common.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeptDto extends TreeEntity<DeptDto> {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门描述
     */
    private String deptDesc;

    /**
     * 部门负责人
     */
    private String deptLeader;

    /**
     * 父部门ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    public DeptDto(Dept dept) {
		BeanUtils.copyProperties(dept, this);
    }
}
