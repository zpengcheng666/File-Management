package com.aspi.docmanage.domain;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;

import java.util.Date;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 设备对象 doc_equipment
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocEquipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 设备名称 */
    @Excel(name = "设备名称")
    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    /** 数量 */
    @Excel(name = "数量")
    @ApiModelProperty(value = "数量")
    private Long num;

    /** 删除标识 */
    private String delFlag;

    @ApiModelProperty(value = "id集合")
    private List<Integer> list;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "库房编号")
    private String coffersNo;

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

    public List<Integer> getList() {
        return list;
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






    public void setEquipmentName(String equipmentName) 
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() 
    {
        return equipmentName;
    }


    public void setNum(Long num) 
    {
        this.num = num;
    }

    public Long getNum() 
    {
        return num;
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
            .append("equipmentName", getEquipmentName())
            .append("num", getNum())
            .append("remark", getRemark())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
