package com.github.tangyi.api.user.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * 消息 sys_message
 * 
 * @author tangyi
 * @date 2022-08-16
 */
@Data
@Table(name = "sys_message")
@EqualsAndHashCode(callSuper = true)
public class SysMessage extends BaseEntity<SysMessage> {

    /**
     * 消息标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 消息内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 开始时间
     */
    @JSONField(format = DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE)
    @Column(name = "startTime")
    private Date starttime;

    /**
     * 结束时间
     */
    @JSONField(format = DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE)
    @Column(name = "endTime")
    private Date endtime;

    /**
     * 消息类型
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 消息状态，1: 自动，2：显示，3：未显示
     */
    @Column(name = "status")
    private Integer status;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
