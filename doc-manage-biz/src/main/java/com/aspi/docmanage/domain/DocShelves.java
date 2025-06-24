package com.aspi.docmanage.domain;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 档案架对象 doc_shelves
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocShelves extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键id")
    @ApiModelProperty(value = "主键id")
    private String id;

    /** 档案架号 */
    @Excel(name = "档案架号")
    @ApiModelProperty(value = "档案架号")
    private Long shelvesNo;

    /** 档案架名称 */
    @Excel(name = "档案架名称")
    @ApiModelProperty(value = "档案架名称")
    private String shelvesName;

    /** 类型 */
    @Excel(name = "类型")
    @ApiModelProperty(value = "类型")
    private String shelvesType;

    /** 盒数 */
    @Excel(name = "盒数")
    @ApiModelProperty(value = "盒数")
    private Long box;

    /** 关联库房号 */
    @Excel(name = "关联库房号")
    @ApiModelProperty(value = "关联库房号")
    private String coffersNo;

    @Excel(name = "题名")
    @ApiModelProperty(value = "题名")
    private String title;

    @Excel(name = "原档号")
    @ApiModelProperty(value = "原档号")
    private String oriDocId;

    @Excel(name = "库房名称")
    @ApiModelProperty(value = "库房名称")
    private String name;

    @Excel(name = "档案架行数")
    @ApiModelProperty(value = "档案架行数")
    private String shelvesRow;

    @Excel(name = "档案架列数")
    @ApiModelProperty(value = "档案架列数")
    private String shelvesColumn;


    /** 删除标识 */
    private String delFlag;

    public String getShelvesRow() {
        return shelvesRow;
    }

    public void setShelvesRow(String shelvesRow) {
        this.shelvesRow = shelvesRow;
    }

    public String getShelvesColumn() {
        return shelvesColumn;
    }

    public void setShelvesColumn(String shelvesColumn) {
        this.shelvesColumn = shelvesColumn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriDocId() {
        return oriDocId;
    }

    public void setOriDocId(String oriDocId) {
        this.oriDocId = oriDocId;
    }

    public void setShelvesNo(Long shelvesNo)
    {
        this.shelvesNo = shelvesNo;
    }

    public Long getShelvesNo() 
    {
        return shelvesNo;
    }


    public void setShelvesName(String shelvesName) 
    {
        this.shelvesName = shelvesName;
    }

    public String getShelvesName() 
    {
        return shelvesName;
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

    public void setBox(Long box) 
    {
        this.box = box;
    }

    public Long getBox() 
    {
        return box;
    }



    private DocCoffers boxMap;

    public void setBoxMap(DocCoffers boxMap)
    {
        this.boxMap = boxMap;
    }

    public DocCoffers getBoxMap()
    {
        return boxMap;
    }

    public void setCoffersNo(String coffersNo) 
    {
        this.coffersNo = coffersNo;
    }

    public String getCoffersNo() 
    {
        return coffersNo;
    }


    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("shelvesNo", getShelvesNo())
            .append("shelvesName", getShelvesName())
            .append("shelvesType", getShelvesType())
            .append("box", getBox())
            .append("coffersNo", getCoffersNo())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
