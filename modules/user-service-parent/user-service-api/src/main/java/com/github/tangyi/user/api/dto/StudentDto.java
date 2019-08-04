package com.github.tangyi.user.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangyi
 * @date 2019/07/10 18:07
 */
@Data
public class StudentDto implements Serializable {

    private static final long serialVersionUID = 2602249526687821147L;

    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id")
    private String studentId;

    /**
     * 关系类型
     */
    @ApiModelProperty(value = "关系类型")
    private Integer relationshipType;

    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期", dataType = "Date")
    private Date born;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话", example = "15521089184")
    private String phone;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    private String wechat;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别，0：男，1：女", dataType = "Integer", example = "0")
    private Integer sex;

    /**
     * 家庭住址
     */
    @ApiModelProperty(value = "家庭住址")
    private String address;

    /**
     * 就读年级
     */
    @ApiModelProperty(value = "就读年级ID")
    private String grade;


    /**
     * 就读年级
     */
    @ApiModelProperty(value = "就读年级名称")
    private String gradeName;

    /**
     * 就读学校
     */
    @ApiModelProperty(value = "就读学校")
    private String school;

    /**
     * 就读城市
     */
    @ApiModelProperty(value = "就读城市ID")
    private String cityId;

    /**
     * 县id
     */
    @ApiModelProperty(value = "就读区县ID")
    private String countyId;
}