package com.aspi.docmanage.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 档案库对象 doc_coffers
 *
 * @author hongy
 * @date 2024-12-08
 */
public class DocCoffers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库房编号 */
    @Excel(name = "库房编号")
    @ApiModelProperty(value = "库房编号")
    private Long coffersNo;

    /** 库房名称 */
    @Excel(name = "库房名称")
    @ApiModelProperty(value = "库房名称")
    private String name;

    @Excel(name = "档案架名称")
    @ApiModelProperty(value = "档案架名称")
    private String ShelvesName;

    /** 档案架数量 */
    @Excel(name = "档案架数量")
    @ApiModelProperty(value = "档案架数量")
    private Long shelvesNum;

    /** 档案架行数 */
    @Excel(name = "档案架行数")
    @ApiModelProperty(value = "档案架行数")
    private Long shelvesRow;

    /** 档案架列数 */
    @Excel(name = "档案架列数")
    @ApiModelProperty(value = "档案架列数")
    private Long shelvesColumn;

    /** 盒数 */
    @Excel(name = "盒数")
    @ApiModelProperty(value = "盒数")
    private Long box;

    /** 管理员 */
    @Excel(name = "管理员")
    @ApiModelProperty(value = "管理员")
    private String user;

    /** 删除标识 */
    private String delFlag;

    public String getShelvesName() {
        return ShelvesName;
    }

    public void setShelvesName(String shelvesName) {
        ShelvesName = shelvesName;
    }

    public void setCoffersNo(Long coffersNo)
    {
        this.coffersNo = coffersNo;
    }

    public Long getCoffersNo()
    {
        return coffersNo;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }


    public void setShelvesNum(Long shelvesNum)
    {
        this.shelvesNum = shelvesNum;
    }

    public Long getShelvesNum()
    {
        return shelvesNum;
    }


    public void setShelvesRow(Long shelvesRow)
    {
        this.shelvesRow = shelvesRow;
    }

    public Long getShelvesRow()
    {
        return shelvesRow;
    }


    public void setShelvesColumn(Long shelvesColumn)
    {
        this.shelvesColumn = shelvesColumn;
    }

    public Long getShelvesColumn()
    {
        return shelvesColumn;
    }




    public void setBox(Long box)
    {
        this.box = box;
    }

    public Long getBox()
    {
        return box;
    }


    public void setUser(String user)
    {
        this.user = user;
    }

    public String getUser()
    {
        return user;
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
                .append("coffersNo", getCoffersNo())
                .append("name", getName())
                .append("shelvesNum", getShelvesNum())
                .append("shelvesRow", getShelvesRow())
                .append("shelvesColumn", getShelvesColumn())
                .append("remark", getRemark())
                .append("box", getBox())
                .append("user", getUser())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
