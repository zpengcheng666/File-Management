package com.oaspi.common.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oaspi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;

/**
 * Entity基类
 * 
 * @author oaspi
 */
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @JsonIgnore
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 描述 */
    private String description;

    public SysUser getBizCreateBy() {
        return bizCreateBy;
    }

    public void setBizCreateBy(SysUser bizCreateBy) {
        this.bizCreateBy = bizCreateBy;
    }

    public SysUser getBizUpdateBy() {
        return bizUpdateBy;
    }

    public void setBizUpdateBy(SysUser bizUpdateBy) {
        this.bizUpdateBy = bizUpdateBy;
    }
    @ApiModelProperty(hidden = true)
    private SysUser bizCreateBy;


    @ApiModelProperty(hidden = true)
    private SysUser bizUpdateBy;



    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}
