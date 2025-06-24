package com.oaspi.web.controller.common;

import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.system.domain.SysChunk;
import com.oaspi.system.domain.vo.CheckSysChunkVO;
import com.oaspi.system.service.ISysChunkService;
import com.oaspi.system.service.ISysUpFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 【断点上传】Controller
 *
 * @author yg
 * @date 2024-12-10
 */
@Api(tags = "断点续传")
@RestController
@RequestMapping("/chunk")
public class SysChunkController extends BaseController
{
    @Autowired
    private ISysChunkService sysChunkService;
    @Autowired
    private ISysUpFileService sysUpFileService;

    /**
     * 上传文件
     * @param sysChunk 文件分片实体对象
     * @param response 响应
     * @return 是否上传成功
     */
    @ApiOperation(value = "分块上传",position = 1 , notes = "分块上传")
    @PostMapping("/upload")
    public AjaxResult postFileUpload(@ModelAttribute SysChunk sysChunk, HttpServletResponse response) {
        boolean flag = sysChunkService.postFileUpload(sysChunk, response);
        AjaxResult ajaxResult = toAjax(flag);
        if (ajaxResult.isSuccess()) {
            ajaxResult.put("needMerge", true);
            ajaxResult.put("result", true);
        }
        return ajaxResult;
    }


    /**
     * 检查文件上传状态
     */
    @ApiOperation(value = "检查分块文件上传状态,是否上传完成",position = 2 , notes = "检查分块文件上传状态,是否上传完成")
    @GetMapping("/upload")
    public CheckSysChunkVO  getFileUploadStatus(@ModelAttribute  SysChunk sysChunk, HttpServletResponse response) {
        //查询根据md5查询文件是否存在
        CheckSysChunkVO fileUpload = sysChunkService.getFileUpload(sysChunk, response);
//        AjaxResult ajaxResult = toAjax(true);
//        ajaxResult.put("skipUpload",fileUpload.getSkipUpload());
//        ajaxResult.put("uploads",fileUpload.getUploads());
        return fileUpload;
    }

    /**
     * 合并请求
     * @param sysChunk 已上传文件实体
     * @return 合并是否成功
     */
    @ApiOperation(value = "都上传成功，合并请求",position = 3 , notes = "都上传成功，合并请求")
    @PostMapping("/merge")
    public AjaxResult merge(@RequestBody SysChunk sysChunk) {
        String path = sysChunkService.mergeFile(sysChunk);
        return AjaxResult.success("操作成功", path);
    }

}
