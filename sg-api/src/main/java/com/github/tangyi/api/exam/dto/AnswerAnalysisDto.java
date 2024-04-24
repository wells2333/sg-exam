/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
