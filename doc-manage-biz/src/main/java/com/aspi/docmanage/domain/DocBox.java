package com.aspi.docmanage.domain;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 档案盒对象 doc_box
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocBox extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String boxId;

    /** 盒号 */
    @Excel(name = "盒号")
    @ApiModelProperty(value = "盒号")
    private String boxName;

    /** 关联档案架号 */
    @Excel(name = "关联档案架号")
    @ApiModelProperty(value = "关联档案架号")
    private String shelvesNo;

    /** 行号 */
    @Excel(name = "行号")
    @ApiModelProperty(value = "行号")
    private Long boxRow;

    /** 列号 */
    @Excel(name = "列号")
    @ApiModelProperty(value = "列号")
    private Long boxColumn;

    /** 关联库号 */
    @Excel(name = "关联库号")
    @ApiModelProperty(value = "关联库号")
    private Long coffersNo;

    @ApiModelProperty(value = "盒子数量")
    private String boxcount;

    /** 删除标识 */
    private String delFlag;

    public String getBoxcount() {
        return boxcount;
    }

    public void setBoxcount(String boxcount) {
        this.boxcount = boxcount;
    }

    public void setBoxId(String boxId)
    {
        this.boxId = boxId;
    }

    public String getBoxId() 
    {
        return boxId;
    }


    public void setBoxName(String boxName) 
    {
        this.boxName = boxName;
    }

    public String getBoxName() 
    {
        return boxName;
    }


    public void setShelvesNo(String shelvesNo) 
    {
        this.shelvesNo = shelvesNo;
    }

    public String getShelvesNo() 
    {
        return shelvesNo;
    }



    private DocShelves shelvesNoMap;

    public void setShelvesNoMap(DocShelves shelvesNoMap)
    {
        this.shelvesNoMap = shelvesNoMap;
    }

    public DocShelves getShelvesNoMap()
    {
        return shelvesNoMap;
    }

    public void setBoxRow(Long boxRow) 
    {
        this.boxRow = boxRow;
    }

    public Long getBoxRow() 
    {
        return boxRow;
    }


    public void setBoxColumn(Long boxColumn) 
    {
        this.boxColumn = boxColumn;
    }

    public Long getBoxColumn() 
    {
        return boxColumn;
    }


    public void setCoffersNo(Long coffersNo) 
    {
        this.coffersNo = coffersNo;
    }

    public Long getCoffersNo() 
    {
        return coffersNo;
    }



    private DocCoffers coffersNoMap;

    public void setCoffersNoMap(DocCoffers coffersNoMap)
    {
        this.coffersNoMap = coffersNoMap;
    }

    public DocCoffers getCoffersNoMap()
    {
        return coffersNoMap;
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
            .append("boxId", getBoxId())
            .append("boxName", getBoxName())
            .append("shelvesNo", getShelvesNo())
            .append("boxRow", getBoxRow())
            .append("boxColumn", getBoxColumn())
            .append("coffersNo", getCoffersNo())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
