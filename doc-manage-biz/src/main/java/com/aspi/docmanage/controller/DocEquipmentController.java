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
import com.aspi.docmanage.domain.DocEquipment;
import com.aspi.docmanage.service.IDocEquipmentService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 设备Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docmanage/equipment")
public class DocEquipmentController extends BaseController
{
    @Autowired
    private IDocEquipmentService docEquipmentService;

    /**
     * 查询设备列表
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:equipment:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(DocEquipment docEquipment)
    {
        startPage();
        List<DocEquipment> list = docEquipmentService.selectDocEquipmentList(docEquipment);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:equipment:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocEquipment docEquipment)
    {
        List<DocEquipment> list = docEquipmentService.selectDocEquipmentList(docEquipment);
        ExcelUtil<DocEquipment> util = new ExcelUtil<DocEquipment>(DocEquipment.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:equipment:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docEquipmentService.selectDocEquipmentById(id));
    }

    /**
     * 新增设备
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:equipment:add')")*/
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocEquipment docEquipment)
    {
        return toAjax(docEquipmentService.insertDocEquipment(docEquipment));
    }

    /**
     * 修改设备
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:equipment:edit')")*/
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocEquipment docEquipment)
    {
        return toAjax(docEquipmentService.updateDocEquipment(docEquipment));
    }

    /**
     * 删除设备
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:equipment:remove')")*/
    @Log(title = "设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docEquipmentService.deleteDocEquipmentByIds(ids));
    }
}
