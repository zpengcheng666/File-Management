package com.aspi.docmanage.web.api.vo;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 库房管理 doc_coffers
 *
 * @author hongy
 * @date 2024-11-02
 */
public class DocCoffersVo extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 档案名称 */
    @Excel(name = "库房编号")
    private int coffersNo;
    @Excel(name = "库房名称")
    private String name;
    @Excel(name = "档案架数量")
    private int shelvesNum;
    @Excel(name = "档案架行数")
    private int shelvesRow;
    @Excel(name = "档案架列数")
    private int shelvesColumn;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "盒数")
    private int box;
    @Excel(name = "管理员")
    private String user;

    @Excel(name = "档案架号")
    private int  shelvesNo;
    @Excel(name = "档案架名称")
    private String  shelvesName;
    @Excel(name = "类型")
    private String shelvesType;

    @Excel(name = "行")
    private int boxRow;
    @Excel(name = "列")
    private int boxColumn;
    @Excel(name = "盒子名称")
    private String boxName;
    @Excel(name = "盒子号")
    private String boxId;
    @Excel(name = "原档号")
    private String oriDocId;
    @Excel(name = "提名")
    private String title;

    private String status;

    private String boxcount;
    private String userId;
    private String address;

    private String secretLevel;

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Excel(name = "密级及保管期限")
    private String secret_level;

    public String getSecret_level() {
        return secret_level;
    }

    public void setSecret_level(String secret_level) {
        this.secret_level = secret_level;
    }

    public String getBoxcount() {
        return boxcount;
    }

    public void setBoxcount(String boxcount) {
        this.boxcount = boxcount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriDocId() {
        return oriDocId;
    }

    public void setOriDocId(String oriDocId) {
        this.oriDocId = oriDocId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getCoffersNo() {
        return coffersNo;
    }

    public void setCoffersNo(int coffersNo) {
        this.coffersNo = coffersNo;
    }

    public int getShelvesNum() {
        return shelvesNum;
    }

    public void setShelvesNum(int shelvesNum) {
        this.shelvesNum = shelvesNum;
    }

    public int getShelvesRow() {
        return shelvesRow;
    }

    public void setShelvesRow(int shelvesRow) {
        this.shelvesRow = shelvesRow;
    }

    public int getShelvesColumn() {
        return shelvesColumn;
    }

    public void setShelvesColumn(int shelvesColumn) {
        this.shelvesColumn = shelvesColumn;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getShelvesNo() {
        return shelvesNo;
    }

    public void setShelvesNo(int shelvesNo) {
        this.shelvesNo = shelvesNo;
    }

    public String getShelvesName() {
        return shelvesName;
    }

    public void setShelvesName(String shelvesName) {
        this.shelvesName = shelvesName;
    }

    public String getShelvesType() {
        return shelvesType;
    }

    public void setShelvesType(String shelvesType) {
        this.shelvesType = shelvesType;
    }

    public int getBoxRow() {
        return boxRow;
    }

    public void setBoxRow(int boxRow) {
        this.boxRow = boxRow;
    }

    public int getBoxColumn() {
        return boxColumn;
    }

    public void setBoxColumn(int boxColumn) {
        this.boxColumn = boxColumn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("coffersNo", getCoffersNo())
                .append("shelvesNum", getShelvesNum())
                .append("shelvesRow", getShelvesRow())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("shelvesColumn", getShelvesColumn())
                .append("box", getBox())
                .append("remark", getRemark())
                .append("user", getUser())
                .append("shelvesNo", getShelvesNo())
                .append("shelvesName", getShelvesName())
                .append("shelvesType", getShelvesType())
                .append("boxRow", getBoxRow())
                .append("boxColumn", getBoxColumn())
                .toString();
    }
}
