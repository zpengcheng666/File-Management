package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.*;
import com.aspi.docmanage.service.IDocBorrowRecordsService;
import com.aspi.docmanage.service.IDocInfoService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 档案利用controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Api(tags = "档案利用")
@RestController
@RequestMapping("/docborrow/archivesUse")
public class ArchivesUseController extends BaseController
{
    @Autowired
    private IDocBorrowRecordsService docBorrowRecordsService;
    private IDocInfoService docInfoService;

    /**
     * 查询借阅基本信息
     */
    @GetMapping(value = "getInfo")
    @ApiOperation(value = "查询借阅基本信息", notes = "查询借阅基本信息")
    public AjaxResult getInfo()
    {
        return success(docBorrowRecordsService.getInfo());
    }


    /**
     * 新增借阅单
     */
    @Log(title = "新增借阅单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation(value = "新增借阅单", notes = "新增借阅单")
    public AjaxResult add(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        return docBorrowRecordsService.insertDocBorrowRecords(docBorrowRecords);
    }

    /**
     * 导入借阅单
     */
    @PostMapping("/importArchives")
    @ApiOperation(value = "导入借阅单", notes = "导入借阅单")
    public AjaxResult importArchives(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        return docBorrowRecordsService.importArchives(file);
    }

    @PostMapping("/exportArchives")
    @ApiOperation(value = "下载借阅单模板", notes = "下载借阅单模板")
    public void exportTemplate(HttpServletResponse response) {
        try {
            //获取要下载的模板名称
            String fileName = "docBorrowRecords.xlsx";
            //设置头文件
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //设置文件传输类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //模板文件存放路径
            String filePath = getClass().getResource("/templates/"+fileName).getPath();
            //IO流处理模板
            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b,0,len);
            }
            //返回请求访问的结果
            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
        } catch (Exception e) {
            logger.error("getApplicationTemplate :", e);
        }
    }

    /**
     * 加入收藏
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/updCollectionType")
    @ApiOperation(value = "加入收藏", notes = "加入收藏")
    public AjaxResult updCollectionType(@RequestBody DocBorrowRecords docBorrowRecords)
    {

        return  docBorrowRecordsService.updCollectionType(docBorrowRecords);
    }

    /**
     * 借阅待审批查询
     */
    @GetMapping("/archivesApploval")

    @ApiOperation(value = "借阅待审批查询", notes = "借阅待审批查询")
    public TableDataInfo archivesApploval()
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.archivesApplovalList();
        return getDataTable(list);
    }

    /**
     * 借阅审批查询
     */
    @GetMapping("/archivesApplovalList")

    @ApiOperation(value = "借阅审批查询", notes = "借阅审批查询")
    public TableDataInfo archivesApplovalList()
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.archivesApploval();
        return getDataTable(list);
    }

    /**
     * 查询是否借阅
     */
    @GetMapping("/selArchives")

    @ApiOperation(value = "查询是否借阅", notes = "查询是否借阅")
    public AjaxResult selArchives(DocBorrowRecords docBorrowRecords)
    {
        return docBorrowRecordsService.selArchives(docBorrowRecords);
    }

    /**
     * 同意
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/approve")
    @ApiOperation(value = "同意", notes = "同意")
    public AjaxResult approve(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        for (Integer docId : docBorrowRecords.getDocIdList()){
            docBorrowRecords.setApprovalResult(1L);
            docBorrowRecords.setDocId(Long.valueOf(docId));
            docBorrowRecordsService.approve(docBorrowRecords);
        }
        return AjaxResult.success();
    }

    /**
     * 拒绝
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/refuse")
    @ApiOperation(value = "拒绝", notes = "拒绝")
    public AjaxResult refuse(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        for (Integer borrowId : docBorrowRecords.getDocIdList()){
            docBorrowRecords.setApprovalResult(0L);
            docBorrowRecords.setDocId(Long.valueOf(borrowId));
            docBorrowRecordsService.approve(docBorrowRecords);
        }
        return AjaxResult.success();
    }

    /**
     * 归还
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/returnDoc")
    @ApiOperation(value = "归还", notes = "归还")
    public AjaxResult returnDoc(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        for (Integer docId : docBorrowRecords.getDocIdList()){
            docBorrowRecords.setDocId(Long.valueOf(docId));
            docBorrowRecordsService.returnDoc(docBorrowRecords);
        }
        return AjaxResult.success();
    }

    /**
     * 导入归还
     */
    @PostMapping("/importReturn")
    @ApiOperation(value = "导入归还", notes = "导入归还")
    public AjaxResult importReturn(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        return docBorrowRecordsService.importReturn(file);
    }

    @PostMapping("/exportReturn")
    @ApiOperation(value = "下载归还模板", notes = "下载归还模板")
    public void exportReturn(HttpServletResponse response) {
        try {
            //获取要下载的模板名称
            String fileName = "docReturn.xlsx";
            //设置头文件
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //设置文件传输类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //模板文件存放路径
            String filePath = getClass().getResource("/templates/"+fileName).getPath();
            //IO流处理模板
            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b,0,len);
            }
            //返回请求访问的结果
            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
        } catch (Exception e) {
            logger.error("getApplicationTemplate :", e);
        }
    }

    /**
     * 续借
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/renew")
    @ApiOperation(value = "续借", notes = "续借")
    public AjaxResult renew(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        for (Integer docId : docBorrowRecords.getDocIdList()){
            docBorrowRecords.setDocId(Long.valueOf(docId));
            docBorrowRecordsService.renew(docBorrowRecords);
        }
        return AjaxResult.success();
    }

    /**
     * 导入续借
     */
    @PostMapping("/importRenew")
    @ApiOperation(value = "导入续借", notes = "导入续借")
    public AjaxResult importRenew(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        return docBorrowRecordsService.importRenew(file);
    }

    @PostMapping("/exportRenew")
    @ApiOperation(value = "下载续借模板", notes = "下载续借模板")
    public void exportRenew(HttpServletResponse response) {
        try {
            //获取要下载的模板名称
            String fileName = "docRenew.xlsx";
            //设置头文件
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //设置文件传输类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //模板文件存放路径
            String filePath = getClass().getResource("/templates/"+fileName).getPath();
            //IO流处理模板
            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b,0,len);
            }
            //返回请求访问的结果
            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
        } catch (Exception e) {
            logger.error("getApplicationTemplate :", e);
        }
    }

    /**
     * 查询待归还记录
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/beReturnList")
    @ApiOperation(value = "查询待归还记录", notes = "查询待归还记录")
    public TableDataInfo beReturnList(DocBorrowRecords docBorrowRecords)
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.beReturnList(docBorrowRecords);
        return getDataTable(list);
    }

    /**
     * 查询归还记录
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/returnList")
    @ApiOperation(value = "查询归还记录", notes = "查询归还记录")
    public TableDataInfo returnList(DocBorrowRecords docBorrowRecords)
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.returnList(docBorrowRecords);
        return getDataTable(list);
    }

    /**
     * 超期
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/extended")
    @ApiOperation(value = "超期", notes = "超期")
    public TableDataInfo extended(DocBorrowRecords docBorrowRecords)
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.extended(docBorrowRecords);
        return getDataTable(list);
    }


}
