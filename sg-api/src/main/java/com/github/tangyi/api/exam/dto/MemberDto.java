package com.github.tangyi.api.exam.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class MemberDto {

	private List<Long> userMembers = Lists.newArrayList();

	private String deptMember;
}
