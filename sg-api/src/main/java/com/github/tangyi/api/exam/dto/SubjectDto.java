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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectDto extends BaseEntity<SubjectDto> {

    /**
     *  材料题附属题目
     */
    private List<SubjectDto> childSubjects;
    /**
     *  材料题附属题数量
     */
    private Integer childLength;
    /**
     * 材料 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long materialId;
    /**
     * 考试 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long examinationId;

    /**
     * 考试记录 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long examinationRecordId;

    /**
     * 题目分类 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 题目类型
     */
    private Integer type;

    private String typeLabel;

    /**
     * 选择题类型
     */
    private Integer choicesType;

    /**
     * 分值
     */
    private Double score;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 难度等级
     */
    private Integer level;

    /**
     * 答题
     */
    private Answer answer;

    /**
     * 选项列表
     */
    private List<SubjectOption> options;

    private Integer sort;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long speechId;

    private String speechUrl;

    /**
     * 题目总数
     */
    private Integer total;

    /**
     * 是否还有题目
     */
    private boolean hasMore;

    /**
     * 视频 ID
     */
    private Long videoId;

    /**
     * 视频 URL
     */
    private String videoUrl;

    /**
     * 图片 ID
     */
    private Long imageId;

    /**
     * 判分类型
     */
    private Integer judgeType;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 是否已收藏
     */
    private boolean favorite;

    /**
     * 查看次数
     */
    private String views;

    private Long subjectVideoId;

    private String subjectVideoName;

    private String subjectVideoUrl;

    private Integer speechPlayLimit;

    private Integer autoPlaySpeech;

    /**
     * 考试题目对应的分类题目 ID
     */
    private Long parentId;
}
