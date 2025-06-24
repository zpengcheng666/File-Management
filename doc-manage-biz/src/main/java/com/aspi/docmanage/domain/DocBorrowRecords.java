package com.aspi.docmanage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oaspi.common.core.domain.entity.SysDept;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 借阅记录对象 doc_borrow_records
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocBorrowRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借阅单号 */
    private Long borrowId;

    /** 借阅人 */
    @Excel(name = "借阅人")
    @ApiModelProperty(value = "借阅人ID")
    private Long borrowerUserId;

    /** 借阅人 */
    @Excel(name = "借阅人")
    @ApiModelProperty(value = "借阅人")
    private String borrower;

    /** 借阅部门 */
    @Excel(name = "借阅部门")
    @ApiModelProperty(value = "借阅部门ID")
    private Long deptId;

    /** 借阅部门 */
    @Excel(name = "借阅部门")
    @ApiModelProperty(value = "借阅部门")
    private String department;

    /** 借阅类型（0 实体档案） */
    @Excel(name = "借阅类型", readConverterExp = "0=,实=体档案")
    @ApiModelProperty(value = "借阅类型")
    private Long borrowType;

    /** 借阅时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借阅时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "借阅时间")
    private Date borrowDate;

    /** 归还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "归还时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "归还时间")
    private Date returnDate;

    /** 超期 */
    @Excel(name = "超期")
    @ApiModelProperty(value = "超期")
    private Integer isOverdue;

    /** 归还确认人 */
    @Excel(name = "归还确认人")
    @ApiModelProperty(value = "归还确认人")
    private Long returnConfirmerUserId;

    /** 归还确认人 */
    @Excel(name = "归还确认人")
    @ApiModelProperty(value = "归还确认人")
    private String returnConfirmer;

    /** 借阅事由 */
    @Excel(name = "借阅事由")
    @ApiModelProperty(value = "借阅事由")
    private String borrowReason;

    /** 审批结果（0 不同意 1同意） */
    @Excel(name = "审批结果", readConverterExp = "0=,不=同意,1=同意")
    @ApiModelProperty(value = "审批结果")
    private Long approvalResult;

    /** 审批建议 */
    @Excel(name = "审批建议")
    @ApiModelProperty(value = "审批建议")
    private String approvalSuggest;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    @ApiModelProperty(value = "档案编号集合")
    private List<Integer> docIdList;

    @ApiModelProperty(value = "档案编号")
    private Long docId;

    /** 原档号 */
    @Excel(name = "原档号")
    @ApiModelProperty(value = "原档号")
    private String oriDocId;

    /** 题名 */
    @Excel(name = "题名")
    @ApiModelProperty(value = "题名")
    private String title;

    @Excel(name = "密级及保管期限")
    @ApiModelProperty(value = "密级及保管期限")
    private String secretLevel;

    @Excel(name = "文号")
    @ApiModelProperty(value = "文号")
    private String docContentId;

    @ApiModelProperty(value = "超期天数")
    private String extendedDay;

    @ApiModelProperty(value = "续借天数")
    private Long renewDay;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "门类")
    private String categoryId;
    private String id;

    @ApiModelProperty(value = "状态")
    private String returnType;

    @ApiModelProperty(value = "门类")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocContentId() {
        return docContentId;
    }

    public void setDocContentId(String docContentId) {
        this.docContentId = docContentId;
    }

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtendedDay() {
        return extendedDay;
    }

    public void setExtendedDay(String extendedDay) {
        this.extendedDay = extendedDay;
    }

    public Long getRenewDay() {
        return renewDay;
    }

    public void setRenewDay(Long renewDay) {
        this.renewDay = renewDay;
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

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public List<Integer> getDocIdList() {
        return docIdList;
    }

    public void setDocIdList(List<Integer> docIdList) {
        this.docIdList = docIdList;
    }

    public void setBorrowId(Long borrowId)
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
    }


    public void setBorrowerUserId(Long borrowerUserId) 
    {
        this.borrowerUserId = borrowerUserId;
    }

    public Long getBorrowerUserId() 
    {
        return borrowerUserId;
    }



    private SysUser borrowerUserIdMap;

    public void setBorrowerUserIdMap(SysUser borrowerUserIdMap)
    {
        this.borrowerUserIdMap = borrowerUserIdMap;
    }

    public SysUser getBorrowerUserIdMap()
    {
        return borrowerUserIdMap;
    }

    public void setBorrower(String borrower) 
    {
        this.borrower = borrower;
    }

    public String getBorrower() 
    {
        return borrower;
    }


    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }



    private SysDept deptIdMap;

    public void setDeptIdMap(SysDept deptIdMap)
    {
        this.deptIdMap = deptIdMap;
    }

    public SysDept getDeptIdMap()
    {
        return deptIdMap;
    }

    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }


    public void setBorrowType(Long borrowType) 
    {
        this.borrowType = borrowType;
    }

    public Long getBorrowType() 
    {
        return borrowType;
    }


    public void setBorrowDate(Date borrowDate) 
    {
        this.borrowDate = borrowDate;
    }

    public Date getBorrowDate() 
    {
        return borrowDate;
    }


    public void setReturnDate(Date returnDate) 
    {
        this.returnDate = returnDate;
    }

    public Date getReturnDate() 
    {
        return returnDate;
    }


    public void setIsOverdue(Integer isOverdue) 
    {
        this.isOverdue = isOverdue;
    }

    public Integer getIsOverdue() 
    {
        return isOverdue;
    }


    public void setReturnConfirmerUserId(Long returnConfirmerUserId) 
    {
        this.returnConfirmerUserId = returnConfirmerUserId;
    }

    public Long getReturnConfirmerUserId() 
    {
        return returnConfirmerUserId;
    }



    private SysUser returnConfirmerUserIdMap;

    public void setReturnConfirmerUserIdMap(SysUser returnConfirmerUserIdMap)
    {
        this.returnConfirmerUserIdMap = returnConfirmerUserIdMap;
    }

    public SysUser getReturnConfirmerUserIdMap()
    {
        return returnConfirmerUserIdMap;
    }

    public void setReturnConfirmer(String returnConfirmer) 
    {
        this.returnConfirmer = returnConfirmer;
    }

    public String getReturnConfirmer() 
    {
        return returnConfirmer;
    }


    public void setBorrowReason(String borrowReason) 
    {
        this.borrowReason = borrowReason;
    }

    public String getBorrowReason() 
    {
        return borrowReason;
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




    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }











    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("borrowId", getBorrowId())
            .append("borrowerUserId", getBorrowerUserId())
            .append("borrower", getBorrower())
            .append("deptId", getDeptId())
            .append("department", getDepartment())
            .append("borrowType", getBorrowType())
            .append("borrowDate", getBorrowDate())
            .append("returnDate", getReturnDate())
            .append("isOverdue", getIsOverdue())
            .append("returnConfirmerUserId", getReturnConfirmerUserId())
            .append("returnConfirmer", getReturnConfirmer())
            .append("borrowReason", getBorrowReason())
            .append("approvalResult", getApprovalResult())
            .append("approvalSuggest", getApprovalSuggest())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
