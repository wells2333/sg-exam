package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DashboardDto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DashboardDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 在线用户数量
     */
    private String onlineUserNumber;

    /**
     * 考试数量
     */
    private String examinationNumber;

	/**
	 * 考试记录数量
	 */
	private String examinationRecordNumber;


	/**
     * 参与人数
     */
    private String examUserNumber;

	/**
	 * 单位数量
	 */
	private String tenantCount;

	/**
	 * 课程数
	 */
	private String courseCount;

	/**
	 * 考试记录数量
	 */
	private List<String> examRecordData = new ArrayList<>();

	/**
	 * 考试记录日期
	 */
	private List<String> examRecordDate = new ArrayList<>();
}
