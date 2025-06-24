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
import com.aspi.docmanage.domain.DocTemperature;
import com.aspi.docmanage.service.IDocTemperatureService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 温湿度Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docmanage/temperature")
public class DocTemperatureController extends BaseController
{
    @Autowired
    private IDocTemperatureService docTemperatureService;

    /**
     * 查询温湿度列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:temperature:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocTemperature docTemperature)
    {
        startPage();
        List<DocTemperature> list = docTemperatureService.selectDocTemperatureList(docTemperature);
        return getDataTable(list);
    }

    /**
     * 导出温湿度列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:temperature:export')")
    @Log(title = "温湿度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocTemperature docTemperature)
    {
        List<DocTemperature> list = docTemperatureService.selectDocTemperatureList(docTemperature);
        ExcelUtil<DocTemperature> util = new ExcelUtil<DocTemperature>(DocTemperature.class);
        util.exportExcel(response, list, "温湿度数据");
    }

    /**
     * 获取温湿度详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:temperature:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docTemperatureService.selectDocTemperatureById(id));
    }

    /**
     * 新增温湿度
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:temperature:add')")
    @Log(title = "温湿度", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocTemperature docTemperature)
    {
        return toAjax(docTemperatureService.insertDocTemperature(docTemperature));
    }

    /**
     * 修改温湿度
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:temperature:edit')")
    @Log(title = "温湿度", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocTemperature docTemperature)
    {
        return toAjax(docTemperatureService.updateDocTemperature(docTemperature));
    }

    /**
     * 删除温湿度
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:temperature:remove')")
    @Log(title = "温湿度", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docTemperatureService.deleteDocTemperatureByIds(ids));
    }
}
