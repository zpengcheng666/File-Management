package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.*;
import com.aspi.docmanage.service.*;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 库房管理Controller
 * 
 * @author hongy
 * @date 2024-11-02
 */
@Api(tags = "库存管理")
@RestController
@RequestMapping("/docmanage/CoffersManagement")
public class CoffersManagementController extends BaseController
{
    @Autowired
    private IDocCoffersService coffersService;

    @Autowired
    private IDocEquipmentService equipmentService;

    @Autowired
    private IDocDailyService dailyService;

    @Autowired
    private IDocTemperatureService temperatureService;

    @Autowired
    private IDocShelvesService docShelvesService;

    @Autowired
    private IDocBoxService boxService;

    @Autowired
    private IDocInfoService docInfoService;

    /**
     * 查询档案库列表
     *
     *
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/coffersList")
    @ApiOperation(value = "查询档案库列表", notes = "查询档案库列表")
    public AjaxResult coffersList(DocCoffers docCoffers)
    {
        List<DocCoffers> list = coffersService.coffersList(docCoffers);
        return success(list);
    }

    /**
     * 查询档案架列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/shelvesList")
    @ApiOperation(value = "查询档案架列表", notes = "查询档案架列表")
    public AjaxResult shelvesList(DocShelves docShelves)
    {
        List<DocShelves> list = docShelvesService.selectDocShelvesList(docShelves);
        return success(list);
    }

    /**
     * 查询盒子列表
     */
// @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/boxList")
    @ApiOperation(value = "查询盒子列表", notes = "查询盒子列表")
    public AjaxResult boxList(DocBox docBox)
    {
        List<DocBox> list = boxService.selectDocBoxList(docBox);
        return success(list);
    }

    /**
     * 查询档案列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询档案列表", notes = "查询档案列表")
    public AjaxResult list(DocInfo docInfo)
    {
        List<DocInfo> list = docInfoService.selectDocInfoList(docInfo);
        return success(list);
    }


    /**
     * 查询待上架记录
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/shelvesUpStopList")
    @ApiOperation(value = "查询待上架记录", notes = "查询待上架记录")
    public TableDataInfo shelvesUpStopList(DocInfo docInfo)
    {
        startPage();
        List<DocInfo> list = docInfoService.shelvesUpStopList(docInfo);
        return getDataTable(list);
    }

    /**
     * 查询待下架记录
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/shelvesDownStopList")
    @ApiOperation(value = "查询待下架记录", notes = "查询待下架记录")
    public TableDataInfo shelvesDownStopList(DocInfo docInfo)
    {
        startPage();
        List<DocInfo> list = docInfoService.shelvesDownStopList(docInfo);
        return getDataTable(list);
    }

    /**
     * 查询上架记录
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/shelvesUpList")
    @ApiOperation(value = "查询上架记录", notes = "查询上架记录")
    public TableDataInfo shelvesUpList(DocInfo docInfo)
    {
        startPage();
        List<DocInfo> list = docInfoService.shelvesUpList(docInfo);
        return getDataTable(list);
    }

    /**
     * 查询下架记录
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/shelvesDownList")
    @ApiOperation(value = "查询下架记录", notes = "查询下架记录")
    public TableDataInfo shelvesDownList(DocInfo docInfo)
    {
        startPage();
        List<DocInfo> list = docInfoService.shelvesDownList(docInfo);
        return getDataTable(list);
    }

    /**
     * 更新档案架类型
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/updShelves")
    @ApiOperation(value = "更新档案架类型", notes = "更新档案架类型")
    public AjaxResult updShelves(@RequestBody DocShelves docShelves)
    {

        return  toAjax(docShelvesService.updateDocShelves(docShelves));
    }


    /**
     * 清空档案盒
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/delShelves")
    @ApiOperation(value = "清空档案盒", notes = "清空档案盒")
    public AjaxResult delShelves(@RequestBody DocInfo docInfo)
    {

        return  docInfoService.delShelves(docInfo);
    }

    /**
     * 上架
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/shelvesUp")
    @ApiOperation(value = "上架", notes = "上架")
    public AjaxResult shelvesUp(@RequestBody DocInfo docInfo)
    {
        //循环档案编号
        for (Integer id : docInfo.getDocIdList()){
            DocInfo info = new DocInfo();
            info.setId(Long.valueOf(id));
            info.setBoxId(docInfo.getBoxId());
            docInfoService.shelvesUp(info);
        }
        return  AjaxResult.success();
    }

    /**
     * 下架
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/shelvesDown")
    @ApiOperation(value = "下架", notes = "下架")
    public AjaxResult shelvesDown(@RequestBody DocInfo docInfo)
    {
        for (Integer id : docInfo.getDocIdList()) {
            DocInfo info = new DocInfo();
            info.setId(Long.valueOf(id));
            docInfoService.shelvesDown(info);
        }
        return  AjaxResult.success();
    }

    /**
     * 新增库房
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:add')")*/
    @Log(title = "新增库房", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation(value = "新增库房", notes = "新增库房")
    public AjaxResult add(@RequestBody DocCoffers docCoffers)
    {
        return coffersService.add(docCoffers);
    }

    /**
     * 库存管理二级联动
     */
    /*    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:add')")*/
    @Log(title = "库存管理二级联动", businessType = BusinessType.INSERT)
    @GetMapping("/getCoffersAndShelves")
    @ApiOperation(value = "库存管理二级联动",notes = "库存管理二级联动")
    public AjaxResult getCoffersAndShelves() {
        Map<String, Coffers> coffersMap = docShelvesService.getCoffersWithShelves();
        return AjaxResult.success(new ArrayList<>(coffersMap.values()));
    }


    /**
     * 删除库房
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/delCoffers")
    @ApiOperation(value = "删除库房", notes = "删除库房")
    public AjaxResult delCoffers(@RequestBody DocCoffers docCoffers)
    {

        return  coffersService.delCoffers(docCoffers);
    }

    /**
     * 修改库房表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/updCoffers")
    @ApiOperation(value = "修改库房表", notes = "修改库房表")
    public AjaxResult updCoffers(@RequestBody DocCoffers docCoffers)
    {

        return  coffersService.updCoffers(docCoffers);
    }

    /**
     * 查询温湿度表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/temperatureList")
    @ApiOperation(value = "查询温湿度表", notes = "查询温湿度表")
    public TableDataInfo temperatureList(DocTemperature docTemperature)
    {
        startPage();
        List<DocTemperature> list = temperatureService.selectDocTemperatureList(docTemperature);
        return getDataTable(list);
    }

    /**
     * 添加温湿度表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/temperatureAdd")
    @ApiOperation(value = "添加温湿度表", notes = "添加温湿度表")
    public AjaxResult temperatureAdd(@RequestBody DocTemperature docTemperature)
    {

        return  toAjax(temperatureService.insertDocTemperature(docTemperature));
    }
    /**
     * 修改温湿度表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/temperatureUpd")
    @ApiOperation(value = "修改温湿度表", notes = "修改温湿度表")
    public AjaxResult temperatureUpd(@RequestBody DocTemperature docTemperature)
    {

        return  toAjax(temperatureService.updateDocTemperature(docTemperature));
    }

    /**
     * 删除温湿度表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/temperatureDel")
    @ApiOperation(value = "删除温湿度表", notes = "删除温湿度表")
    public AjaxResult temperatureDel(@RequestBody  DocTemperature docTemperature)
    {
        for (Integer id : docTemperature.getList()){
            temperatureService.deleteDocTemperatureById(Long.valueOf(id));
        }
        return  AjaxResult.success();
    }

    /**
     * 查询日常情况表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/dailyList")

    @ApiOperation(value = "查询日常情况表", notes = "查询日常情况表")
    public TableDataInfo dailyList(DocDaily docDaily)
    {
        startPage();
        List<DocDaily> list = dailyService.selectDocDailyList(docDaily);
        return getDataTable(list);
    }

    /**
     * 添加日常情况表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/dailyAdd")
    @ApiOperation(value = "添加日常情况表", notes = "添加日常情况表")
    public AjaxResult dailyAdd(@RequestBody DocDaily docDaily)
    {

        return  toAjax(dailyService.insertDocDaily(docDaily));
    }
    /**
     * 修改日常情况表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/dailyUpd")
    @ApiOperation(value = "修改日常情况表", notes = "修改日常情况表")
    public AjaxResult dailyUpd(@RequestBody DocDaily docDaily)
    {

        return  toAjax(dailyService.updateDocDaily(docDaily));
    }

    /**
     * 删除日常情况表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/dailyDel")
    @ApiOperation(value = "删除日常情况表", notes = "删除日常情况表")
    public AjaxResult dailyDel(@RequestBody DocDaily docDaily)
    {
        for (Integer id : docDaily.getList()){
            dailyService.deleteDocDailyById(Long.valueOf(id));
        }
        return  AjaxResult.success();
    }

    /**
     * 查询库房设备
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/equipmentList")

    @ApiOperation(value = "查询库房设备", notes = "查询库房设备")
    public TableDataInfo equipmentList(DocEquipment docEquipment)
    {
        startPage();
        List<DocEquipment> list = equipmentService.selectDocEquipmentList(docEquipment);
        return getDataTable(list);
    }

    /**
     * 添加库房设备
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/equipmentAdd")
    @ApiOperation(value = "添加库房设备", notes = "添加库房设备")
    public AjaxResult equipmentAdd(@RequestBody DocEquipment docEquipment)
    {

        return  toAjax(equipmentService.insertDocEquipment(docEquipment));
    }
    /**
     * 修改库房设备
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/equipmentUpd")
    @ApiOperation(value = "修改库房设备", notes = "修改库房设备")
    public AjaxResult equipmentUpd(@RequestBody DocEquipment docEquipment)
    {

        return  toAjax(equipmentService.updateDocEquipment(docEquipment));
    }

    /**
     * 删除库房设备
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/equipmentDel")
    @ApiOperation(value = "删除库房设备", notes = "删除库房设备")
    public AjaxResult equipmentDel(@RequestBody DocEquipment docEquipment)
    {
        for (Integer id : docEquipment.getList()){
            equipmentService.deleteDocEquipmentById(Long.valueOf(id));
        }
        return  AjaxResult.success();
    }

    /**
     * 导入库房
     */
    @PostMapping("/importDocCoffers")
    @ApiOperation(value = "导入库房", notes = "导入库房")
    public AjaxResult importDocCoffers(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        return coffersService.importDocCoffers(file);
    }

    @PostMapping("/exportTemplate")
    @ApiOperation(value = "下载库房模板", notes = "下载库房模板")
    public void exportTemplate(HttpServletResponse response) {
        try {
            //获取要下载的模板名称
            String fileName = "docCoffers.xlsx";
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
     * 导入上架
     */
    @PostMapping("/importShelvesUp")
    @ApiOperation(value = "导入上架", notes = "导入上架")
    public AjaxResult importShelvesUp(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
       return docInfoService.importShelvesUp(file);
    }

    @PostMapping("/exportTemplateUp")
    @ApiOperation(value = "下载上架模板", notes = "下载上架模板")
    public void exportTemplateUp(HttpServletResponse response) {
        try {
            //获取要下载的模板名称
            String fileName = "docShelvesUp.xlsx";
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
     * 导入下架
     */
    @PostMapping("/importShelvesDown")
    @ApiOperation(value = "导入下架", notes = "导入下架")
    public AjaxResult importShelvesDown(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        return docInfoService.importShelvesDown(file);
    }

    @PostMapping("/exportTemplateDown")
    @ApiOperation(value = "下载下架模板", notes = "下载下架模板")
    public void exportTemplateDown(HttpServletResponse response) {
        try {
            //获取要下载的模板名称
            String fileName = "docShelvesDown.xlsx";
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
}
