package com.github.tangyi.common.core.persistence;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.utils.DateUtils;
import com.github.tangyi.common.core.utils.IdGen;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类
 *
 * @author tangyi
 * @date 2018-08-24 18:58
 */
public class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;

    protected String creator;    // 创建者

    protected String createDate;    // 创建日期

    protected String modifier;    // 更新者

    protected String modifyDate;    // 更新日期

    protected Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;  // 删除标记 0:正常，1-删除

    protected String applicationCode;   // 系统编号

    protected boolean isNewRecord;  // 是否为新记录

    protected String[] ids; // ID数组

    protected String idString;  // ID字符串，多个用逗号隔开

    public BaseEntity() {

    }

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
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }
}

