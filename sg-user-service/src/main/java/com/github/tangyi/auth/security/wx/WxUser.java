package com.github.tangyi.auth.security.wx;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class WxUser implements Serializable {

    @Serial
	private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 详细描述
     */
    private String userDesc;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 语言
     */
    private String languang;

}
