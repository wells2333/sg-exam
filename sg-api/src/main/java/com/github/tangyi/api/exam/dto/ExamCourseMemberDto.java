package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.ExamCourseMember;
import lombok.Data;

@Data
public class ExamCourseMemberDto extends ExamCourseMember {

	private String userName;

	private String email;

	private String phone;

	private Integer gender;

}
