package com.oaspi.system.domain;

import com.oaspi.common.config.OaspiConfig;
import com.oaspi.common.constant.Constants;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.common.utils.file.FileUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.oaspi.common.annotation.Excel;
import java.util.List;
import com.oaspi.common.core.domain.BaseEntity;

/**
 * 上传文件对象 sys_up_file
 *
 * @author hongy
 * @date 2024-10-03
 */
@ApiModel(value="上传文件对象", description="上传文件对象")
@Data
public class SysUpFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    public static final int STORE_TYPE_MINIO = 1;
    public static final int STORE_TYPE_LOCAL = 5;

    /** 文件编号 */
    private Long id;

    /** 档案信息id */
    @ApiModelProperty(value = "档案信息id")
    private Long docinfoId;

    /** 存储类型 */
    @Excel(name = "存储类型")
    @ApiModelProperty(value = "存储类型")
    private Integer storeType;

    /** 文件名 */
    @Excel(name = "文件名")
    @ApiModelProperty(value = "文件名")
    private String name;

    /** 原文件名 */
    @Excel(name = "原文件名")
    @ApiModelProperty(value = "原文件名")
    private String oriName;

    /** 新文件名 */
    @Excel(name = "新文件名")
    @ApiModelProperty(value = "新文件名")
    private String newName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    @ApiModelProperty(value = "文件路径")
    private String path;

    /** 文件 MD5 */
    @Excel(name = "文件 MD5")
    @ApiModelProperty(value = "文件 MD5")
    private String url;

    /** 文件 URL */
    @Excel(name = "文件 URL")
    @ApiModelProperty(value = "文件 URL")
    private String md5;

    /** 文件类型 */
    @Excel(name = "文件类型")
    @ApiModelProperty(value = "文件类型")
    private String type;

    /** 业务类型 */
    @Excel(name = "业务类型")
    @ApiModelProperty(value = "业务类型")
    private String bizType;

    /** 档案信息id，业务编号 */
    @Excel(name = "档案信息id，业务编号")
    @ApiModelProperty(value = "档案信息id，业务编号")
    private String bizId;

    /** 下载票据 */
    @Excel(name = "下载票据")
    @ApiModelProperty(value = "下载票据")
    private String downTicket;

    /** 文件大小 */
    @Excel(name = "文件大小")
    @ApiModelProperty(value = "文件大小")
    private Long size;

    /** 是否删除 */
    private String delFlag;


    // 以下为其他业务预留的字段
    private String bizValb;

    private String bizValc;

    private String bizVald;

    private String bizVale;

    private String flowNo;

    private Integer sort;

    @ApiModelProperty(value = "页数")
    private Integer pages;

    private Integer dpi;

    public String getLocalPath(){
        String localPath = OaspiConfig.getUploadPath();
        // 数据库资源地址
        String downloadPath = String.valueOf(FileUtils.combinePaths(localPath, getPath()));
        Integer storeType = getStoreType();
        if(storeType != null && getStoreType() == 0){
            // 本地文件,downloadPath默认取URL，检测是否存在
            if(!FileUtils.checkFileExists(downloadPath)){
                //TODO 需要后期修改
                downloadPath = localPath + StringUtils.substringAfter(downloadPath, Constants.RESOURCE_PREFIX);
            }
        }
        return downloadPath;
    }
}
