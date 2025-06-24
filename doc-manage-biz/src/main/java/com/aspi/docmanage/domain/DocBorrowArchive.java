package com.aspi.docmanage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 借阅档案关联对象 doc_borrow_archive
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocBorrowArchive extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 档案编号 */
    @Excel(name = "档案编号")
    private Long docInfoId;

    /** 借阅单号 */
    @Excel(name = "借阅单号")
    private Long borrowId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }


    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }










    public void setDocInfoId(Long docInfoId) 
    {
        this.docInfoId = docInfoId;
    }

    public Long getDocInfoId() 
    {
        return docInfoId;
    }



    private DocInfo docInfoIdMap;

    public void setDocInfoIdMap(DocInfo docInfoIdMap)
    {
        this.docInfoIdMap = docInfoIdMap;
    }

    public DocInfo getDocInfoIdMap()
    {
        return docInfoIdMap;
    }

    public void setBorrowId(Long borrowId) 
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
    }



    private DocBorrowRecords borrowIdMap;

    public void setBorrowIdMap(DocBorrowRecords borrowIdMap)
    {
        this.borrowIdMap = borrowIdMap;
    }

    public DocBorrowRecords getBorrowIdMap()
    {
        return borrowIdMap;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("docInfoId", getDocInfoId())
            .append("borrowId", getBorrowId())
            .toString();
    }
}
