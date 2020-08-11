package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 考试监控数据
 * @author tangyi
 * @date 2020/1/31 5:04 下午
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExaminationDashboardDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 考试数量
	 */
	private Integer examinationCount;

	/**
	 * 考生数量
	 */
	private Integer examUserCount;

	/**
	 * 考试记录总数量
	 */
	private Integer examinationRecordCount;

	/**
	 * 考试记录数量
	 */
	private List<String> examRecordData;

	/**
	 * 考试记录日期
	 */
	private List<String> examRecordDate;

}
