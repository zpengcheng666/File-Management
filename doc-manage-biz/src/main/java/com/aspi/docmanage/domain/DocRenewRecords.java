package com.aspi.docmanage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 续借记录对象 doc_renew_records
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocRenewRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 续借主键 */
    private Long renewId;

    /** 续借天数 */
    @Excel(name = "续借天数")
    private Long renewDay;

    /** 借阅单号 */
    @Excel(name = "借阅单号")
    private Long borrowId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setRenewId(Long renewId) 
    {
        this.renewId = renewId;
    }

    public Long getRenewId() 
    {
        return renewId;
    }


    public void setRenewDay(Long renewDay) 
    {
        this.renewDay = renewDay;
    }

    public Long getRenewDay() 
    {
        return renewDay;
    }


    public void setBorrowId(Long borrowId) 
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
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
            .append("renewId", getRenewId())
            .append("renewDay", getRenewDay())
            .append("borrowId", getBorrowId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
