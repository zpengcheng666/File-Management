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
import com.aspi.docmanage.domain.DocRenewRecords;
import com.aspi.docmanage.service.IDocRenewRecordsService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 续借记录Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docborrow/renewrecords")
public class DocRenewRecordsController extends BaseController
{
    @Autowired
    private IDocRenewRecordsService docRenewRecordsService;

    /**
     * 查询续借记录列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:renewrecords:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocRenewRecords docRenewRecords)
    {
        startPage();
        List<DocRenewRecords> list = docRenewRecordsService.selectDocRenewRecordsList(docRenewRecords);
        return getDataTable(list);
    }

    /**
     * 导出续借记录列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:renewrecords:export')")
    @Log(title = "续借记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocRenewRecords docRenewRecords)
    {
        List<DocRenewRecords> list = docRenewRecordsService.selectDocRenewRecordsList(docRenewRecords);
        ExcelUtil<DocRenewRecords> util = new ExcelUtil<DocRenewRecords>(DocRenewRecords.class);
        util.exportExcel(response, list, "续借记录数据");
    }

    /**
     * 获取续借记录详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:renewrecords:query')")
    @GetMapping(value = "/{renewId}")
    public AjaxResult getInfo(@PathVariable("renewId") Long renewId)
    {
        return success(docRenewRecordsService.selectDocRenewRecordsByRenewId(renewId));
    }

    /**
     * 新增续借记录
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:renewrecords:add')")
    @Log(title = "续借记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocRenewRecords docRenewRecords)
    {
        return toAjax(docRenewRecordsService.insertDocRenewRecords(docRenewRecords));
    }

    /**
     * 修改续借记录
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:renewrecords:edit')")
    @Log(title = "续借记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocRenewRecords docRenewRecords)
    {
        return toAjax(docRenewRecordsService.updateDocRenewRecords(docRenewRecords));
    }

    /**
     * 删除续借记录
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:renewrecords:remove')")
    @Log(title = "续借记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{renewIds}")
    public AjaxResult remove(@PathVariable Long[] renewIds)
    {
        return toAjax(docRenewRecordsService.deleteDocRenewRecordsByRenewIds(renewIds));
    }
}
