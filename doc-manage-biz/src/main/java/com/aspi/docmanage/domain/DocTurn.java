package com.aspi.docmanage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 档案移交信息对象 doc_turn
 *
 * @author zpc
 * @date 2025-01-09
 */
@Api(tags = "档案移交信息对象")
@Data
public class DocTurn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 企业名称 */
    @Excel(name = "企业名称")
    @ApiModelProperty("企业名称")
    private Long companyName;

    @ApiModelProperty("企业名称文本")
    private String companyNameText;

    /** 移交类型 */
    @Excel(name = "移交类型")
    @ApiModelProperty("移交类型")
    private Long turnType;

    /** 档案号 */
    @Excel(name = "档案号")
    @ApiModelProperty("档案号")
    private String num;

    /** 档案题名 */
    @Excel(name = "档案题名")
    @ApiModelProperty("档案题名")
    private String title;

    /** 经办人
     */
    @Excel(name = "经办人 ")
    @ApiModelProperty("经办人")
    private String operator;

    /** 移交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "移交时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("移交时间")
    private Date turnTime;

    /** 交接人 */
    @Excel(name = "交接人")
    @ApiModelProperty("交接人")
    private String heir;

    /** 交接人手机号 */
    @Excel(name = "交接人手机号")
    @ApiModelProperty("交接人手机号")
    private String tel;

    /** 文件数量 */
    @Excel(name = "文件数量")
    @ApiModelProperty("文件数量")
    private Long fileCount;

    /** 档案数量 */
    @Excel(name = "档案数量")
    @ApiModelProperty("档案数量")
    private Long docCount;
}

