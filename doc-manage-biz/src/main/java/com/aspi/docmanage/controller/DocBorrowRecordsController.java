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
import com.aspi.docmanage.domain.DocBorrowRecords;
import com.aspi.docmanage.service.IDocBorrowRecordsService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 借阅记录Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docborrow/records")
public class DocBorrowRecordsController extends BaseController
{
    @Autowired
    private IDocBorrowRecordsService docBorrowRecordsService;

    /**
     * 查询借阅记录列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:records:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocBorrowRecords docBorrowRecords)
    {
        startPage();
        List<DocBorrowRecords> list = docBorrowRecordsService.selectDocBorrowRecordsList(docBorrowRecords);
        return getDataTable(list);
    }

    /**
     * 导出借阅记录列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:records:export')")
    @Log(title = "借阅记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocBorrowRecords docBorrowRecords)
    {
        List<DocBorrowRecords> list = docBorrowRecordsService.selectDocBorrowRecordsList(docBorrowRecords);
        ExcelUtil<DocBorrowRecords> util = new ExcelUtil<DocBorrowRecords>(DocBorrowRecords.class);
        util.exportExcel(response, list, "借阅记录数据");
    }

    /**
     * 获取借阅记录详细信息
     */
/*    // @PreAuthorize("@ss.hasPermi('docborrow:records:query')")*/
    @PostMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        return success(docBorrowRecordsService.selectDocBorrowRecordsByBorrowId(docBorrowRecords.getBorrowId()));
    }

    /**
     * 新增借阅记录
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:records:add')")
    @Log(title = "借阅记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        return docBorrowRecordsService.insertDocBorrowRecords(docBorrowRecords);
    }

    /**
     * 修改借阅记录
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:records:edit')")
    @Log(title = "借阅记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocBorrowRecords docBorrowRecords)
    {
        return toAjax(docBorrowRecordsService.updateDocBorrowRecords(docBorrowRecords));
    }

    /**
     * 删除借阅记录
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:records:remove')")
    @Log(title = "借阅记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(docBorrowRecordsService.deleteDocBorrowRecordsByBorrowIds(borrowIds));
    }
}
