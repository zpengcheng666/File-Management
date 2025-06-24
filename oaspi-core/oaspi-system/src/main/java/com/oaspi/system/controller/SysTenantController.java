package com.oaspi.system.controller;

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
import com.oaspi.system.domain.SysTenant;
import com.oaspi.system.service.ISysTenantService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 多租户Controller
 * 
 * @author hongy
 * @date 2024-11-09
 */
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController
{
    @Autowired
    protected ISysTenantService sysTenantService;

    /**
     * 查询多租户列表
     */
    // @PreAuthorize("@ss.hasPermi('system:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTenant sysTenant)
    {
        startPage();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return getDataTable(list);
    }

    /**
     * 导出多租户列表
     */
    // @PreAuthorize("@ss.hasPermi('system:tenant:export')")
    @Log(title = "多租户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenant sysTenant)
    {
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        ExcelUtil<SysTenant> util = new ExcelUtil<SysTenant>(SysTenant.class);
        util.exportExcel(response, list, "多租户数据");
    }

    /**
     * 获取多租户详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:tenant:query')")
    @GetMapping(value = "/{tenantId}")
    public AjaxResult getInfo(@PathVariable("tenantId") Long tenantId)
    {
        return success(sysTenantService.selectSysTenantByTenantId(tenantId));
    }

    /**
     * 新增多租户
     */
    // @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "多租户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.insertSysTenant(sysTenant));
    }

    /**
     * 修改多租户
     */
    // @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "多租户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.updateSysTenant(sysTenant));
    }

    /**
     * 删除多租户
     */
    // @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "多租户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{tenantIds}")
    public AjaxResult remove(@PathVariable Long[] tenantIds)
    {
        return toAjax(sysTenantService.deleteSysTenantByTenantIds(tenantIds));
    }
}
