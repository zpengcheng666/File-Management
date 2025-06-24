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
import com.aspi.docmanage.domain.DocBorrowApprove;
import com.aspi.docmanage.service.IDocBorrowApproveService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 借阅审批Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docborrow/approve")
public class DocBorrowApproveController extends BaseController
{
    @Autowired
    private IDocBorrowApproveService docBorrowApproveService;

    /**
     * 查询借阅审批列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:approve:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocBorrowApprove docBorrowApprove)
    {
        startPage();
        List<DocBorrowApprove> list = docBorrowApproveService.selectDocBorrowApproveList(docBorrowApprove);
        return getDataTable(list);
    }

    /**
     * 导出借阅审批列表
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:approve:export')")
    @Log(title = "借阅审批", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocBorrowApprove docBorrowApprove)
    {
        List<DocBorrowApprove> list = docBorrowApproveService.selectDocBorrowApproveList(docBorrowApprove);
        ExcelUtil<DocBorrowApprove> util = new ExcelUtil<DocBorrowApprove>(DocBorrowApprove.class);
        util.exportExcel(response, list, "借阅审批数据");
    }

    /**
     * 获取借阅审批详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:approve:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docBorrowApproveService.selectDocBorrowApproveById(id));
    }

    /**
     * 新增借阅审批
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:approve:add')")
    @Log(title = "借阅审批", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocBorrowApprove docBorrowApprove)
    {
        return toAjax(docBorrowApproveService.insertDocBorrowApprove(docBorrowApprove));
    }

    /**
     * 修改借阅审批
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:approve:edit')")
    @Log(title = "借阅审批", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocBorrowApprove docBorrowApprove)
    {
        return toAjax(docBorrowApproveService.updateDocBorrowApprove(docBorrowApprove));
    }

    /**
     * 删除借阅审批
     */
    // @PreAuthorize("@ss.hasPermi('docborrow:approve:remove')")
    @Log(title = "借阅审批", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docBorrowApproveService.deleteDocBorrowApproveByIds(ids));
    }
}
