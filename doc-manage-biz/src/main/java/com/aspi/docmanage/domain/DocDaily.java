package com.aspi.docmanage.domain;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 日常情况对象 doc_daily
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocDaily extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Excel(name = "主键id")
    @ApiModelProperty(value = "主键id")
    private Long id;

    /** 原档号 */
    @Excel(name = "原档号")
    @ApiModelProperty(value = "原档号")
    private String oriDocId;

    /** 题名 */
    @Excel(name = "题名")
    @ApiModelProperty(value = "题名")
    private String title;

    /** 库房位置 */
    @Excel(name = "库房位置")
    @ApiModelProperty(value = "库房位置")
    private String address;

    /** 档案类型 */
    @Excel(name = "档案类型")
    @ApiModelProperty(value = "档案类型")
    private String shelvesType;

    /** 存储状态
 */
    @Excel(name = "存储状态")
    @ApiModelProperty(value = "存储状态")
    private String status;

    /** 异常情况 */
    @Excel(name = "异常情况")
    @ApiModelProperty(value = "异常情况")
    private String error;

    /** 删除标识 */
    private String delFlag;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "库房编号")
    private String coffersNo;

    @ApiModelProperty(value = "id集合")
    private List<Integer> list;

    public List<Integer> getList() {
        return list;
    }

    public String getCoffersNo() {
        return coffersNo;
    }

    public void setCoffersNo(String coffersNo) {
        this.coffersNo = coffersNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }


    public void setOriDocId(String oriDocId) 
    {
        this.oriDocId = oriDocId;
    }

    public String getOriDocId() 
    {
        return oriDocId;
    }


    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }


    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }


    public void setShelvesType(String shelvesType) 
    {
        this.shelvesType = shelvesType;
    }

    public String getShelvesType() 
    {
        return shelvesType;
    }



    private DocCategory shelvesTypeMap;

    public void setShelvesTypeMap(DocCategory shelvesTypeMap)
    {
        this.shelvesTypeMap = shelvesTypeMap;
    }

    public DocCategory getShelvesTypeMap()
    {
        return shelvesTypeMap;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }


    public void setError(String error) 
    {
        this.error = error;
    }

    public String getError() 
    {
        return error;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("oriDocId", getOriDocId())
            .append("title", getTitle())
            .append("address", getAddress())
            .append("shelvesType", getShelvesType())
            .append("status", getStatus())
            .append("error", getError())
            .append("remark", getRemark())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
