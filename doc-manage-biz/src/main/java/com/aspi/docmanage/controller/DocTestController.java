package com.aspi.docmanage.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import com.oaspi.common.config.OaspiConfig;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.utils.file.FileUploadUtils;
import com.oaspi.common.utils.sign.Md5Utils;
import com.oaspi.framework.web.domain.server.Sys;
import com.oaspi.system.util.MinioUtil;
import com.oaspi.system.util.minio.MultiPartCompleteResp;
import com.oaspi.system.util.minio.MultiPartUploadResp;
import com.oaspi.system.util.minio.UploadMinioService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/doc/test")
@Slf4j
public class DocTestController {
    @Autowired
    MinioUtil minioUtil;

    @Autowired
    UploadMinioService uploadService;


    @RequestMapping("/uztm")
    public String uztm(){
        minioUtil.processAndUploadZipFileWithStructure(null, "test");
        return "success";
    }


    // 直接上传到minio分片，注意，这种方法不能解压缩到minio，所以弃用了
    /**
     * 分片初始化
     *
     * @param requestParam 用户参数
     * @return /
     */
    @PostMapping("/minio/multipart/init")
    public AjaxResult  initMultiPartUpload(@RequestBody JSONObject requestParam) {
        // 路径（存入minio的路径）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String pathText = sdf.format(new Date());//当前日期
        String path = requestParam.getStr("path", pathText);
        // 文件名
        String filename = requestParam.getStr("filename", "test.obj");
        // content-type
        String contentType = requestParam.getStr("contentType", "application/octet-stream");
        // md5-可进行秒传判断
        String md5 = requestParam.getStr("md5", "");
        // 分片数量
        Integer partCount = requestParam.getInt("partCount", 100);

        //TODO::业务判断+秒传判断

        MultiPartUploadResp result = uploadService.initMultiPartUpload(path, filename, partCount, contentType,md5);
        log.info(JSONUtil.toJsonPrettyStr(result));
        return AjaxResult.success(result);
    }

    /**
     * 合并分片文件，完成上传
     *
     * @param requestParam 用户参数
     * @return /
     */
    @PutMapping("/minio/multipart/complete")
    public ResponseEntity<Object> completeMultiPartUpload(
            @RequestBody JSONObject requestParam
    ) {
        // 文件名完整路径
        String objectName = requestParam.getStr("objectName");
        //初始化返回的uploadId
        String uploadId = requestParam.getStr("uploadId");
        Assert.notNull(objectName, "objectName must not be null");
        Assert.notNull(uploadId, "uploadId must not be null");
        boolean result = uploadService.mergeMultipartUpload(objectName, uploadId);

        return new ResponseEntity<>(ImmutableMap.of("success", result), HttpStatus.OK);
    }


    // 本地上传分片文件，方便后续调用解压缩。前端调用过程为：
    // 1. 调用init接口，初始化分片信息
    // 2. 调用upload接口，上传分片文件
    // 3. 调用complete接口，合并分片文件

    /**
     * 分片初始化
     *
     * @param requestParam 用户参数
     * @return /
     */
    @PostMapping("/local/multipart/init")
    public AjaxResult  initLocalMultiPartUpload(@RequestBody JSONObject requestParam) {
        // 路径（存入minio的路径）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String pathText = sdf.format(new Date());//当前日期
        String path = requestParam.getStr("path", pathText);
        // 文件名
        String filename = requestParam.getStr("filename", "test.obj");
        // content-type
        String contentType = requestParam.getStr("contentType", "application/octet-stream");
        // md5-可进行秒传判断
        String md5 = requestParam.getStr("md5", "");
        // 分片数量
        Integer partCount = requestParam.getInt("partCount", 100);
        //TODO::业务判断+秒传判断
        MultiPartUploadResp result = null;
        try {
            result = uploadService.initLocalMultiPartUpload(path, filename, partCount, contentType,md5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info(JSONUtil.toJsonPrettyStr(result));
        return AjaxResult.success(result);
    }

    /**
     * 合并分片文件，完成上传
     *
     * @param requestParam 用户参数
     * @return /
     */
    @PostMapping("/local/multipart/complete")
    public AjaxResult completeLocalMultiPartUpload(
            @RequestBody JSONObject requestParam
    ) {

        //初始化返回的uploadId
        String uploadId = requestParam.getStr("uploadId");


        boolean result = false;
        try {
            result = uploadService.mergeLocalMultipartUpload( uploadId);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        MultiPartCompleteResp completeResp = new MultiPartCompleteResp();
        completeResp.setSucceed(result);
        completeResp.setUploadId(uploadId);
        log.info(JSONUtil.toJsonPrettyStr(completeResp));
        return AjaxResult.success(completeResp);
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/local/multipart/upload")
    @ApiOperation(value = "上传分片", notes = "上传请求（单个）")
    public AjaxResult uploadLocalFile(MultipartFile file,
                                      @RequestParam("uploadId") String upId,
                                      @RequestParam("partNum") String partNum) throws Exception {

        String absPath = OaspiConfig.getUploadPath() + "/tmp/" + upId;
        String schema = absPath + "/" + "schema.json";
        File schemaFile = new File(schema);
        if (!schemaFile.exists() ) {
           return AjaxResult.error("错误的upId");
        }

        if(uploadService.uploadLccalPartFile(file, upId,partNum)){
            return AjaxResult.success();
        }

        return AjaxResult.error();
    }
}
