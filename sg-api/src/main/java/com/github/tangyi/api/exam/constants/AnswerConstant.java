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

package com.github.tangyi.api.exam.constants;

public class AnswerConstant {
    /**
     * 不计算对错
     */
    public static final Integer NotCount = -1;
    /**
     * 正确
     */
    public static final Integer RIGHT = 0;

    /**
     * 错误
     */
    public static final Integer WRONG = 1;

    /**
     * 待批改
     */
    public static final Integer TO_BE_MARKED = 0;

    /**
     * 已经批改
     */
    public static final Integer MARKED = 1;

    /**
     * 当前题目
     */
    public static final Integer CURRENT = -1;

    /**
     * 下一题
     */
    public static final Integer NEXT = 0;

    /**
     * 上一题
     */
    public static final Integer PREVIOUS = 1;

	/**
	 * 排名缓存前缀
	 */
	public static final String CACHE_PREFIX_RANK = "rank:";
}
