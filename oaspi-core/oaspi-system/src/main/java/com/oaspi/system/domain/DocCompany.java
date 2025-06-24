package com.oaspi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author：zpc
 * @Date：2025/1/4
 */

@Data
@ApiModel(value="公司", description="公司")
public class DocCompany extends BaseEntity {
    private Long companyId;
    /** 父id */
    @Excel(name = "父id")
    @ApiModelProperty(value = "父id")
    private Long parentId;
    /** 公司名称 */
    @Excel(name = "公司名称")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    /** 公司编码 */
    @Excel(name = "公司编码")
    @ApiModelProperty(value = "公司编码")
    private String orgCode;

    /** 企业电话 */
    @Excel(name = "企业电话")
    @ApiModelProperty(value = "企业电话")
    private String tel;

    /** 企业联系人 */
    @Excel(name = "企业联系人")
    @ApiModelProperty(value = "企业联系人")
    private String companyLinker;

    /**入驻经办人*/
    @Excel(name = "入驻经办人")
    @ApiModelProperty(value = "入驻经办人")
    private String handleAuthor;

    @Excel(name = "入驻时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "入驻时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date handleTime;

    /**1-顶级公司 2-非顶级公司*/
    @ApiModelProperty(value = "1-顶级公司 2-非顶级公司")
    private Long isTopLevel;

    /**1-可用 0-不可用*/
    @ApiModelProperty(value = "1-可用 0-不可用")
    private int status;

    /**公司描述*/
    @Excel(name = "公司描述")
    @ApiModelProperty(value = "公司描述")
    private String description;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private Integer delFlag;
}
