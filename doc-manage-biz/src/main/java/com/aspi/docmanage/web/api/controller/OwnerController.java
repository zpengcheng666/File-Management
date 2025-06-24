package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.DocBorrowRecords;
import com.aspi.docmanage.service.IDocBorrowRecordsService;
import com.aspi.docmanage.service.IDocInfoService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 我的controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Api(tags = "我的")
@RestController
@RequestMapping("/docmanage/owner")
public class OwnerController extends BaseController
{
    @Autowired
    private IDocBorrowRecordsService docBorrowRecordsService;


    /**
     * 我的收藏查询
     */
    @PostMapping("/collectionTypeList")
    @ApiOperation(value = "我的收藏查询", notes = "我的收藏查询")
    public TableDataInfo collectionTypeList(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.collectionTypeList(docBorrowRecords);
        return getDataTable(list);
    }

    /**
     * 移除收藏
     */
    @PostMapping("/delCollectionType")
    @ApiOperation(value = "移除收藏", notes = "移除收藏")
    public AjaxResult delCollectionType(@RequestBody DocBorrowRecords docBorrowRecords)
    {

        return  docBorrowRecordsService.delCollectionType(docBorrowRecords);
    }


    /**
     * 借阅待审批查询
     */
    @GetMapping("/archivesApploval")

    @ApiOperation(value = "借阅待审批查询", notes = "借阅待审批查询")
    public TableDataInfo archivesApploval(DocBorrowRecords docBorrowRecords)
    {
        startPage();
        docBorrowRecords.setApprovalResult(1L);
        List<DocBorrowRecords> list = docBorrowRecordsService.selarchivesApplovalList(docBorrowRecords);
        return getDataTable(list);
    }

    /**
     * 借阅审批历史
     */
    @GetMapping("/archivesApplovalList")

    @ApiOperation(value = "借阅审批历史", notes = "借阅审批历史")
    public TableDataInfo archivesApplovalList(DocBorrowRecords docBorrowRecords)
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.selarchivesApplovalList(docBorrowRecords);
        return getDataTable(list);
    }

    /**
     * 查询档案信息
     */
    @PostMapping("/selDocInfoList")

    @ApiOperation(value = "查询档案信息", notes = "查询档案信息")
    public AjaxResult selDocInfoList(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        List<DocBorrowRecords> list = docBorrowRecordsService.selDocInfoList(docBorrowRecords);
        return AjaxResult.success(list);
    }


}
