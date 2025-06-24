package com.oaspi.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import kotlin.jvm.Transient;
import lombok.Data;

@Data
public class FileDocVo {

    @ApiModelProperty(value = "门类")
    private Long category;

    @Transient
    @ApiModelProperty(value = "门类文本")
    private String categoryText;

    @ApiModelProperty(value = "分类")
    private Long docType;

    @Transient
    @ApiModelProperty(value = "分类文本")
    private String docTypeText;

    private Long id;

    @ApiModelProperty(value = "档案信息id")
    private Long docinfoId;

    @ApiModelProperty(value = "存储类型")
    private Integer storeType;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "原文件名")
    private String oriName;

    @ApiModelProperty(value = "新文件名")
    private String newName;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件 MD5")
    private String url;

    @ApiModelProperty(value = "文件 URL")
    private String md5;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "档案信息id，业务编号")
    private String bizId;

    @ApiModelProperty(value = "下载票据")
    private String downTicket;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    /** 是否删除 */
    private String delFlag;

    private String bizVale;

    private String flowNo;

    private Integer sort;

    @ApiModelProperty(value = "页数")
    private Integer pages;

    @ApiModelProperty(value = "分辨率")
    private Integer dpi;
}
