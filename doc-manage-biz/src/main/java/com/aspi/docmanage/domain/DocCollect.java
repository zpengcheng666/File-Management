package com.aspi.docmanage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysUser;

/**
 * 我的收藏对象 doc_collect
 * 
 * @author hongy
 * @date 2024-12-08
 */
public class DocCollect extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏主键 */
    private Long collectId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 档案编号 */
    @Excel(name = "档案编号")
    private Long docInfoId;

    public void setCollectId(Long collectId) 
    {
        this.collectId = collectId;
    }

    public Long getCollectId() 
    {
        return collectId;
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



    private SysUser docInfoIdMap;

    public void setDocInfoIdMap(SysUser docInfoIdMap)
    {
        this.docInfoIdMap = docInfoIdMap;
    }

    public SysUser getDocInfoIdMap()
    {
        return docInfoIdMap;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("collectId", getCollectId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("docInfoId", getDocInfoId())
            .toString();
    }
}
