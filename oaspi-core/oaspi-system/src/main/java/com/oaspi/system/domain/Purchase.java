package com.oaspi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;

/**
 * 采购对象 purchase
 * 
 * @author shenzhanwang
 * @date 2022-05-28
 */
public class Purchase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /**  */
    @Excel(name = "物品列表")
    private String itemlist;

    /**  */
    @Excel(name = "总价")
    private String total;

    /**  */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applytime;

    /**  */
    @Excel(name = "申请人")
    private String applyer;

    // 采购经理
    private String purchasemanager;

    // 财务
    private String financeName;

    // 出纳
    private String pay;

    // 总经理
    private String managerName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setItemlist(String itemlist) 
    {
        this.itemlist = itemlist;
    }

    public String getItemlist() 
    {
        return itemlist;
    }
    public void setTotal(String total) 
    {
        this.total = total;
    }

    public String getTotal() 
    {
        return total;
    }
    public void setApplytime(Date applytime) 
    {
        this.applytime = applytime;
    }

    public Date getApplytime() 
    {
        return applytime;
    }
    public void setApplyer(String applyer) 
    {
        this.applyer = applyer;
    }

    public String getApplyer() 
    {
        return applyer;
    }

    public String getPurchasemanager() {
        return purchasemanager;
    }

    public void setPurchasemanager(String purchasemanager) {
        this.purchasemanager = purchasemanager;
    }



    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getFinanceName() {
        return financeName;
    }

    public void setFinanceName(String financeName) {
        this.financeName = financeName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("itemlist", getItemlist())
            .append("total", getTotal())
            .append("applytime", getApplytime())
            .append("applyer", getApplyer())
            .toString();
    }
}
