package com.aspi.docmanage.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.enums.BusinessType;
import com.aspi.docmanage.domain.DocIdentifyLog;
import com.aspi.docmanage.service.IDocIdentifyLogService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 鉴定日志Controller
 * 
 * @author hongy
 * @date 2024-11-02
 */
@RestController
@RequestMapping("/docmanage/docidentifylog")
public class DocIdentifyLogController extends BaseController
{
    @Autowired
    private IDocIdentifyLogService docIdentifyLogService;

    /**
     * 查询鉴定日志列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docidentifylog:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocIdentifyLog docIdentifyLog)
    {
        startPage();
        List<DocIdentifyLog> list = docIdentifyLogService.selectDocIdentifyLogList(docIdentifyLog);
        return getDataTable(list);
    }

    /**
     * 导出鉴定日志列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docidentifylog:export')")
    @Log(title = "鉴定日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocIdentifyLog docIdentifyLog)
    {
        List<DocIdentifyLog> list = docIdentifyLogService.selectDocIdentifyLogList(docIdentifyLog);
        ExcelUtil<DocIdentifyLog> util = new ExcelUtil<DocIdentifyLog>(DocIdentifyLog.class);
        util.exportExcel(response, list, "鉴定日志数据");
    }

    /**
     * 获取鉴定日志详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docidentifylog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docIdentifyLogService.selectDocIdentifyLogById(id));
    }

    /**
     * 新增鉴定日志
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docidentifylog:add')")
    @Log(title = "鉴定日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocIdentifyLog docIdentifyLog)
    {
        return toAjax(docIdentifyLogService.insertDocIdentifyLog(docIdentifyLog));
    }

    /**
     * 修改鉴定日志
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docidentifylog:edit')")
    @Log(title = "鉴定日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocIdentifyLog docIdentifyLog)
    {
        return toAjax(docIdentifyLogService.updateDocIdentifyLog(docIdentifyLog));
    }

    /**
     * 删除鉴定日志
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docidentifylog:remove')")
    @Log(title = "鉴定日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docIdentifyLogService.deleteDocIdentifyLogByIds(ids));
    }
}
