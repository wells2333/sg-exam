package com.github.tangyi.user.api.dto;

import java.io.Serializable;

/**
 * DashboardDto
 *
 * @author tangyi
 * @date 2019-03-01 13:56
 */
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

    public String getOnlineUserNumber() {
        return onlineUserNumber;
    }

    public void setOnlineUserNumber(String onlineUserNumber) {
        this.onlineUserNumber = onlineUserNumber;
    }

    public String getExaminationNumber() {
        return examinationNumber;
    }

    public void setExaminationNumber(String examinationNumber) {
        this.examinationNumber = examinationNumber;
    }
}
