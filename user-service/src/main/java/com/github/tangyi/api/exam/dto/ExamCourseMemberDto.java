package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.ExamCourseMember;
import lombok.Data;

/**
 *
 * @author tangyi
 * @date 2022/11/27 4:02 下午
 */
@Data
public class ExamCourseMemberDto extends ExamCourseMember {

	private String userName;

	private String email;

	private String phone;

	private Integer gender;

}
