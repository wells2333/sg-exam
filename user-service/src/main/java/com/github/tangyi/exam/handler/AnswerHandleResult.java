package com.github.tangyi.exam.handler;

import lombok.Data;

@Data
public class AnswerHandleResult {

	/**
	 * 总分
	 */
	private double score;

	/**
	 * 正确题目数
	 */
	private int correctNum;

	/**
	 * 错误题目数
	 */
	private int inCorrectNum;
}
