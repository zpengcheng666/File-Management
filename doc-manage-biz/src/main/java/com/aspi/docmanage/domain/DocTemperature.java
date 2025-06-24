package com.aspi.docmanage.domain;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.TreeEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 库房管理 doc_coffers
 * 
 * @author hongy
 * @date 2024-11-02
 */
public class DocTemperature extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    @Excel(name = "记录人")
    @ApiModelProperty(value = "记录人")
    private String userName;

    @Excel(name = "室内温度")
    @ApiModelProperty(value = "室内温度")
    private String indoorTemperature;

    @Excel(name = "室外温度")
    @ApiModelProperty(value = "室外温度")
    private String outsideTemperature;

    @Excel(name = "室内湿度")
    @ApiModelProperty(value = "室内湿度")
    private String indoorHumidity;

    @Excel(name = "室外湿度")
    @ApiModelProperty(value = "室外湿度")
    private String outsideHumidity;

    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "id集合")
    private List<Integer> list;

    @ApiModelProperty(value = "库房编号")
    private String coffersNo;

    private String delFlag;

    public String getCoffersNo() {
        return coffersNo;
    }

    public void setCoffersNo(String coffersNo) {
        this.coffersNo = coffersNo;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIndoorTemperature() {
        return indoorTemperature;
    }

    public void setIndoorTemperature(String indoorTemperature) {
        this.indoorTemperature = indoorTemperature;
    }

    public String getOutsideTemperature() {
        return outsideTemperature;
    }

    public void setOutsideTemperature(String outsideTemperature) {
        this.outsideTemperature = outsideTemperature;
    }

    public String getIndoorHumidity() {
        return indoorHumidity;
    }

    public void setIndoorHumidity(String indoorHumidity) {
        this.indoorHumidity = indoorHumidity;
    }

    public String getOutsideHumidity() {
        return outsideHumidity;
    }

    public void setOutsideHumidity(String outsideHumidity) {
        this.outsideHumidity = outsideHumidity;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
