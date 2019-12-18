package com.github.tangyi.user.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DashboardDto
 *
 * @author tangyi
 * @date 2019-03-01 13:56
 */
@Data
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
}
