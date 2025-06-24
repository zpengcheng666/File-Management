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
 * 鉴定日志对象 doc_identify_log
 * 
 * @author hongy
 * @date 2024-11-02
 */
public class DocIdentifyLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 文档编号 */
    @Excel(name = "文档编号")
    private Long docId;

    /** 原状态 */
    @Excel(name = "原状态")
    private Long fromStatus;

    /** 目标状态 */
    @Excel(name = "目标状态")
    private Long toStatus;

    /** 鉴定结果 */
    @Excel(name = "鉴定结果")
    private Integer identifyResult;

    /** 鉴定人 */
    @Excel(name = "鉴定人")
    private Long identifyUser;

    /** 鉴定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "鉴定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date identifyTime;

    /** 鉴定意见 */
    @Excel(name = "鉴定意见")
    private String comment;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }


    public void setDocId(Long docId) 
    {
        this.docId = docId;
    }

    public Long getDocId() 
    {
        return docId;
    }


    public void setFromStatus(Long fromStatus) 
    {
        this.fromStatus = fromStatus;
    }

    public Long getFromStatus() 
    {
        return fromStatus;
    }


    public void setToStatus(Long toStatus) 
    {
        this.toStatus = toStatus;
    }

    public Long getToStatus() 
    {
        return toStatus;
    }


    public void setIdentifyResult(Integer identifyResult) 
    {
        this.identifyResult = identifyResult;
    }

    public Integer getIdentifyResult() 
    {
        return identifyResult;
    }


    public void setIdentifyUser(Long identifyUser) 
    {
        this.identifyUser = identifyUser;
    }

    public Long getIdentifyUser() 
    {
        return identifyUser;
    }



    private SysUser identifyUserMap;

    public void setIdentifyUserMap(SysUser identifyUserMap)
    {
        this.identifyUserMap = identifyUserMap;
    }

    public SysUser getIdentifyUserMap()
    {
        return identifyUserMap;
    }

    public void setIdentifyTime(Date identifyTime) 
    {
        this.identifyTime = identifyTime;
    }

    public Date getIdentifyTime() 
    {
        return identifyTime;
    }


    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getComment() 
    {
        return comment;
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
            .append("id", getId())
            .append("docId", getDocId())
            .append("fromStatus", getFromStatus())
            .append("toStatus", getToStatus())
            .append("identifyResult", getIdentifyResult())
            .append("identifyUser", getIdentifyUser())
            .append("identifyTime", getIdentifyTime())
            .append("comment", getComment())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
