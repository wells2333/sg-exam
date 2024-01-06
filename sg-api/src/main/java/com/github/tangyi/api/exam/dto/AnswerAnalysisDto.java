package com.github.tangyi.api.exam.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 成绩分析结果
 */
@Data
public class AnswerAnalysisDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Data
	public static final class ScoreItem {
		private int rankNum;
		private String userName;
		private double score;
	}

	private String examinationName;
	private long userCnt;
	private double avgScore;
	private double highestScore;
	private double lowestScore;
	private List<ScoreItem> scoreItems;
	private Map<String, Integer> scoreDistribution;
	private Map<String, Float> scoreDistributionPercent;
}
