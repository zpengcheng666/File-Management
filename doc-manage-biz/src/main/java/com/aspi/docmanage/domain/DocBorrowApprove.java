package com.aspi.docmanage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 借阅审批对象 doc_borrow_approve
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocBorrowApprove extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 借阅单号 */
    @Excel(name = "借阅单号")
    private Long borrowId;

    @Excel(name = "档案编号")
    private Long docId;

    /** 审批人 */
    @Excel(name = "审批人")
    private Long approveUserId;

    /** 审批结果（0 不同意 1同意） */
    @Excel(name = "审批结果", readConverterExp = "0=,不=同意,1=同意")
    private Long approvalResult;

    /** 审批建议 */
    @Excel(name = "审批建议")
    private String approvalSuggest;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalTime;

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

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setBorrowId(Long borrowId)
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
    }



    private DocBorrowApprove borrowIdMap;

    public void setBorrowIdMap(DocBorrowApprove borrowIdMap)
    {
        this.borrowIdMap = borrowIdMap;
    }

    public DocBorrowApprove getBorrowIdMap()
    {
        return borrowIdMap;
    }

    public void setApproveUserId(Long approveUserId) 
    {
        this.approveUserId = approveUserId;
    }

    public Long getApproveUserId() 
    {
        return approveUserId;
    }



    private SysUser approveUserIdMap;

    public void setApproveUserIdMap(SysUser approveUserIdMap)
    {
        this.approveUserIdMap = approveUserIdMap;
    }

    public SysUser getApproveUserIdMap()
    {
        return approveUserIdMap;
    }

    public void setApprovalResult(Long approvalResult) 
    {
        this.approvalResult = approvalResult;
    }

    public Long getApprovalResult() 
    {
        return approvalResult;
    }


    public void setApprovalSuggest(String approvalSuggest) 
    {
        this.approvalSuggest = approvalSuggest;
    }

    public String getApprovalSuggest() 
    {
        return approvalSuggest;
    }


    public void setApprovalTime(Date approvalTime) 
    {
        this.approvalTime = approvalTime;
    }

    public Date getApprovalTime() 
    {
        return approvalTime;
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
            .append("borrowId", getBorrowId())
            .append("approveUserId", getApproveUserId())
            .append("approvalResult", getApprovalResult())
            .append("approvalSuggest", getApprovalSuggest())
            .append("approvalTime", getApprovalTime())
            .toString();
    }
}
