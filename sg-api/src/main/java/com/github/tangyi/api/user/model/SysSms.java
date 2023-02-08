package com.github.tangyi.api.exam.model;

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
 * 消息发送 sys_sms
 */
@Data
@Table(name = "sys_sms")
@EqualsAndHashCode(callSuper = true)
public class SysSms extends BaseEntity<SysSms> {

    /**
     * 接收方
     */
    @Column(name = "receiver")
    private String receiver;

    /**
     * 发送内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 消息响应
     */
    @Column(name = "response")
    private String response;

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
