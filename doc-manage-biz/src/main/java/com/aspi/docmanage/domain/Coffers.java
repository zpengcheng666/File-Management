package com.aspi.docmanage.domain;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.TreeEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 库房管理 doc_coffers
 * 
 * @author hongy
 * @date 2024-11-02
 */
public class Coffers extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "库房编号")
    @ApiModelProperty(value = "库房编号")
    private String coffersNo;

    @Excel(name = "库房名称")
    @ApiModelProperty(value = "库房名称")
    private String name;

    private List<Shelves> shelvesList;

    public Coffers(String coffersNo, List<Shelves> shelvesList) {
        this.coffersNo = coffersNo;
        this.shelvesList = shelvesList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoffersNo() {
        return coffersNo;
    }

    public void setCoffersNo(String coffersNo) {
        this.coffersNo = coffersNo;
    }

    public List<Shelves> getShelvesList() {
        return shelvesList;
    }

    public void setShelvesList(List<Shelves> shelvesList) {
        this.shelvesList = shelvesList;
    }
}
