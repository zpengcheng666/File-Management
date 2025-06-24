package com.oaspi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 多租户对象 sys_tenant
 * 
 * @author hongy
 * @date 2024-11-09
 */
public class SysTenant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long tenantId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String name;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactUser;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }


    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }


    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }


    public void setContactUser(String contactUser) 
    {
        this.contactUser = contactUser;
    }

    public String getContactUser() 
    {
        return contactUser;
    }


    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }


    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }


    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }













    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tenantId", getTenantId())
            .append("status", getStatus())
            .append("name", getName())
            .append("contactUser", getContactUser())
            .append("phone", getPhone())
            .append("address", getAddress())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
