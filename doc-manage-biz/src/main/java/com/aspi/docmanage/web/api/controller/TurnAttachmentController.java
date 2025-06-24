package com.aspi.docmanage.web.api.controller;


import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.aspi.docmanage.domain.TurnAttachment;
import com.aspi.docmanage.service.ITurnAttachmentService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.poi.ExcelUtil;
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


/**
 * 移交和附件关系Controller
 *
 * @author zpc
 * @date 2025-01-09
 */
@RestController
@RequestMapping("/system/attachment")
public class TurnAttachmentController extends BaseController
{
    @Autowired
    private ITurnAttachmentService turnAttachmentService;

    /**
     * 查询移交和附件关系列表
     */
//    @PreAuthorize("@ss.hasPermi('system:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(TurnAttachment turnAttachment)
    {
        startPage();
        List<TurnAttachment> list = turnAttachmentService.selectTurnAttachmentList(turnAttachment);
        return getDataTable(list);
    }

    /**
     * 导出移交和附件关系列表
     */
//    @PreAuthorize("@ss.hasPermi('system:attachment:export')")
    @Log(title = "移交和附件关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TurnAttachment turnAttachment)
    {
        List<TurnAttachment> list = turnAttachmentService.selectTurnAttachmentList(turnAttachment);
        ExcelUtil<TurnAttachment> util = new ExcelUtil<TurnAttachment>(TurnAttachment.class);
        util.exportExcel(response, list, "移交和附件关系数据");
    }

    /**
     * 获取移交和附件关系详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:attachment:query')")
    @GetMapping(value = "/{turnId}")
    public AjaxResult getInfo(@PathVariable("turnId") Long turnId)
    {
        return success(turnAttachmentService.selectTurnAttachmentByTurnId(turnId));
    }

    /**
     * 新增移交和附件关系
     */
//    @PreAuthorize("@ss.hasPermi('system:attachment:add')")
    @Log(title = "移交和附件关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TurnAttachment turnAttachment)
    {
        return toAjax(turnAttachmentService.insertTurnAttachment(turnAttachment));
    }

    /**
     * 修改移交和附件关系
     */
//    @PreAuthorize("@ss.hasPermi('system:attachment:edit')")
    @Log(title = "移交和附件关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TurnAttachment turnAttachment)
    {
        return toAjax(turnAttachmentService.updateTurnAttachment(turnAttachment));
    }

    /**
     * 删除移交和附件关系
     */
//    @PreAuthorize("@ss.hasPermi('system:attachment:remove')")
    @Log(title = "移交和附件关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{turnIds}")
    public AjaxResult remove(@PathVariable Long[] turnIds)
    {
        return toAjax(turnAttachmentService.deleteTurnAttachmentByTurnIds(turnIds));
    }
}
