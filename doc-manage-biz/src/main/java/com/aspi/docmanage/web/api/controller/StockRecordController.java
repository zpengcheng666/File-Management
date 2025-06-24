package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.StockRecord;
import com.aspi.docmanage.service.IDocCoffersService;
import com.aspi.docmanage.service.IStockRecordService;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 出入库管理Controller
 * @Author wangy
 * @Date 2025/01/11 15:16
 */
@Api(tags = "出入库管理")
@RestController
@RequestMapping("/docmanage/stock")
public class StockRecordController extends BaseController {

    @Resource
    private IStockRecordService stockRecordService;

    @Resource
    private IDocCoffersService coffersService;

    @ApiOperation(value = "出入库列表", notes = "出入库列表")
    @GetMapping("/records")
    public TableDataInfo listStockRecord() {
        startPage();
        List<StockRecord> list = stockRecordService.listStockRecord();
        return getDataTable(list);
    }

    @ApiOperation(value = "新增出入库记录", notes = "新增出入库记录")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody StockRecord stockRecord) {
        stockRecordService.addStockRecord(stockRecord);
        return success("添加成功！");
    }

    @ApiOperation(value = "删除出入库记录", notes = "删除出入库记录")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        stockRecordService.deleteStockRecord(id);
        return success("删除成功！");
    }

    /**
     * 出入库下载模板
     * @param response
     */
    @ApiOperation(value = "出入库下载模板", notes = "出入库下载模板")
    @PostMapping("/template")
    public void template(HttpServletResponse response) {
        ExcelUtil<StockRecord> util = new ExcelUtil<>(StockRecord.class);
        util.importTemplateExcel(response, "出入库记录");
    }

    /**
     * 导入出入库记录
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "导入出入库记录", notes = "导入出入库记录")
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<StockRecord> util = new ExcelUtil<>(StockRecord.class);
        List<StockRecord> list = util.importExcel(file.getInputStream());
        DateFormat sdf0 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        for (StockRecord s: list) {
            DocCoffers coffer = coffersService.getCoffersByName(s.getCofferName());
            if (coffer.getCoffersNo() != null && coffer.getCoffersNo() != 0) {
                s.setCofferId(coffer.getCoffersNo());
            }
            s.setInOutDate(sdf1.format(sdf0.parse(s.getInOutDate())));
            s.setInTime(sdf2.format(sdf0.parse(s.getInTime())));
            s.setOutTime(sdf2.format(sdf0.parse(s.getOutTime())));
        }
        stockRecordService.batchStockRecord(list);
        return success();
    }

}
