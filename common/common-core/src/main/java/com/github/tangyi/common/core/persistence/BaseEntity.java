package com.github.tangyi.common.core.persistence;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.utils.DateUtils;
import com.github.tangyi.common.core.utils.IdGen;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类
 *
 * @author tangyi
 * @date 2018-08-24 18:58
 */
@Data
@NoArgsConstructor
public class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;

    /**
     * 创建者
     */
    protected String creator;

    /**
     * 创建日期
     */
    protected String createDate;

    /**
     * 更新者
     */
    protected String modifier;

    /**
     * 更新日期
     */
    protected String modifyDate;

    /**
     * 删除标记 0:正常，1-删除
     */
    protected Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;

    /**
     * 系统编号
     */
    protected String applicationCode;

    /**
     * 租户编号
     */
    protected String tenantCode;

    /**
     * 是否为新记录
     */
    protected boolean isNewRecord;

    /**
     * ID数组
     */
    protected String[] ids;

    /**
     * ID字符串，多个用逗号隔开
     */
    protected String idString;

    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    /**
     * 是否为新记录
     *
     * @return boolean
     */
    public boolean isNewRecord() {
        return this.isNewRecord || StringUtils.isBlank(this.getId());
    }

    /**
     * 设置基本属性
     *
     * @param userCode        用户编码
     * @param applicationCode 系统编号
     */
    public void setCommonValue(String userCode, String applicationCode) {
        setCommonValue(userCode, applicationCode, "");
    }

    /**
     * 设置基本属性
     *
     * @param userCode        用户编码
     * @param applicationCode 系统编号
     * @param tenantCode      租户编号
     */
    public void setCommonValue(String userCode, String applicationCode, String tenantCode) {
        String currentDate = DateUtils.localDateToString(LocalDateTime.now());
        if (this.isNewRecord()) {
            this.setId(IdGen.snowflakeId());
            this.setNewRecord(true);
            this.creator = userCode;
            this.createDate = currentDate;
        }
        this.modifier = userCode;
        this.modifyDate = currentDate;
        this.delFlag = 0;
        this.applicationCode = applicationCode;
        this.tenantCode = tenantCode;
    }
}

