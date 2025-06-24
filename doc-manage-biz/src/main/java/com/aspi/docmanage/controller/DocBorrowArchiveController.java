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
import com.aspi.docmanage.domain.DocBorrowArchive;
import com.aspi.docmanage.service.IDocBorrowArchiveService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 借阅档案关联Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docborrow/archive")
public class DocBorrowArchiveController extends BaseController
{
    @Autowired
    private IDocBorrowArchiveService docBorrowArchiveService;

    /**
     * 查询借阅档案关联列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:archive:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocBorrowArchive docBorrowArchive)
    {
        startPage();
        List<DocBorrowArchive> list = docBorrowArchiveService.selectDocBorrowArchiveList(docBorrowArchive);
        return getDataTable(list);
    }

    /**
     * 导出借阅档案关联列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:archive:export')")
    @Log(title = "借阅档案关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocBorrowArchive docBorrowArchive)
    {
        List<DocBorrowArchive> list = docBorrowArchiveService.selectDocBorrowArchiveList(docBorrowArchive);
        ExcelUtil<DocBorrowArchive> util = new ExcelUtil<DocBorrowArchive>(DocBorrowArchive.class);
        util.exportExcel(response, list, "借阅档案关联数据");
    }

    /**
     * 获取借阅档案关联详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:archive:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docBorrowArchiveService.selectDocBorrowArchiveById(id));
    }

    /**
     * 新增借阅档案关联
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:archive:add')")
    @Log(title = "借阅档案关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocBorrowArchive docBorrowArchive)
    {
        return toAjax(docBorrowArchiveService.insertDocBorrowArchive(docBorrowArchive));
    }

    /**
     * 修改借阅档案关联
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:archive:edit')")
    @Log(title = "借阅档案关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocBorrowArchive docBorrowArchive)
    {
        return toAjax(docBorrowArchiveService.updateDocBorrowArchive(docBorrowArchive));
    }

    /**
     * 删除借阅档案关联
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:archive:remove')")
    @Log(title = "借阅档案关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docBorrowArchiveService.deleteDocBorrowArchiveByIds(ids));
    }
}
