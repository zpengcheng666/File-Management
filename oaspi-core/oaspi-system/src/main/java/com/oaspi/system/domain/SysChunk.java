package com.oaspi.system.domain;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 上传文件对象 sys_chunk
 * 分块记录
 * @author yg
 * @date 2024-12-10
 */
@Data
@ApiModel(value="文件分块信息", description="文件分块信息")
public class SysChunk extends BaseEntity {

    /** 主键 */
    private Long chunkId;

    /** 文件块编号 */
    @ApiModelProperty(value = "文件分块编号")
    private Long chunkNumber;

    /** 分块大小 */
    @ApiModelProperty(value = "分块大小")
    private Long chunkSize;

    /** 当前分块大小 */
    @ApiModelProperty(value = "当前分块大小")
    private Long currentChunkSize;

    /** 文件名 */
    @ApiModelProperty(value = "文件名")
    private String filename;

    /** 文件标识,MD5 */
    @ApiModelProperty(value = "整个文件唯一标识,MD5值")
    private String identifier;

    /** 相对路径 */
    @ApiModelProperty(value = "存储相对路径，后端返回")
    private String relativePath;

    /** 总块数 */
    @ApiModelProperty(value = "总块数")
    private Long totalChunks;

    /** 总大小 */
    @ApiModelProperty(value = "文件总大小")
    private Long totalSize;

    /** 是否删除 */
    private String delFlag;

    /**
     * 二进制文件
     */
    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    public Long getChunkId() {
        return chunkId;
    }

    public void setChunkId(Long chunkId) {
        this.chunkId = chunkId;
    }

    public Long getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(Long chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Long getCurrentChunkSize() {
        return currentChunkSize;
    }

    public void setCurrentChunkSize(Long currentChunkSize) {
        this.currentChunkSize = currentChunkSize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Long getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Long totalChunks) {
        this.totalChunks = totalChunks;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
