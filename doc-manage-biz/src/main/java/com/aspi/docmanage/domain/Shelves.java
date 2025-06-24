package com.aspi.docmanage.domain;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.TreeEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * 库房管理 doc_coffers
 * 
 * @author hongy
 * @date 2024-11-02
 */
public class Shelves extends TreeEntity
{
    private static final long serialVersionUID = 1L;


    /** 档案名称 */
    @Excel(name = "档案架编号")
    @ApiModelProperty(value = "档案架编号")
    private String coffersNo;

    @Excel(name = "档案架名称")
    @ApiModelProperty(value = "档案架名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shelves(String coffersNo,String name) {
        this.coffersNo = coffersNo;
        this.name = name;
    }

    public String getCoffersNo() {
        return coffersNo;
    }

    public void setCoffersNo(String coffersNo) {
        this.coffersNo = coffersNo;
    }
}
