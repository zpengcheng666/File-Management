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
import com.aspi.docmanage.domain.DocBox;
import com.aspi.docmanage.service.IDocBoxService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 档案盒Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docmanage/box")
public class DocBoxController extends BaseController
{
    @Autowired
    private IDocBoxService docBoxService;

    /**
     * 查询档案盒列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:box:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocBox docBox)
    {
        startPage();
        List<DocBox> list = docBoxService.selectDocBoxList(docBox);
        return getDataTable(list);
    }

    /**
     * 导出档案盒列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:box:export')")
    @Log(title = "档案盒", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocBox docBox)
    {
        List<DocBox> list = docBoxService.selectDocBoxList(docBox);
        ExcelUtil<DocBox> util = new ExcelUtil<DocBox>(DocBox.class);
        util.exportExcel(response, list, "档案盒数据");
    }

    /**
     * 获取档案盒详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:box:query')")
    @GetMapping(value = "/{boxId}")
    public AjaxResult getInfo(@PathVariable("boxId") String boxId)
    {
        return success(docBoxService.selectDocBoxByBoxId(boxId));
    }

    /**
     * 新增档案盒
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:box:add')")
    @Log(title = "档案盒", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocBox docBox)
    {
        return toAjax(docBoxService.insertDocBox(docBox));
    }

    /**
     * 修改档案盒
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:box:edit')")
    @Log(title = "档案盒", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocBox docBox)
    {
        return toAjax(docBoxService.updateDocBox(docBox));
    }

    /**
     * 删除档案盒
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:box:remove')")
    @Log(title = "档案盒", businessType = BusinessType.DELETE)
	@DeleteMapping("/{boxIds}")
    public AjaxResult remove(@PathVariable String[] boxIds)
    {
        return toAjax(docBoxService.deleteDocBoxByBoxIds(boxIds));
    }
}
