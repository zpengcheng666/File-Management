package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.service.IDocManageTenantService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.system.controller.SysTenantController;
import com.oaspi.system.domain.SysTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/9 12:13
 */
@RestController
@RequestMapping("/docmanage/tenant")
public class TenantController extends SysTenantController {

    @Autowired
    private IDocManageTenantService tenantService;


    // @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "多租户", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional
    public AjaxResult add(@RequestBody SysTenant sysTenant)
    {
        sysTenant.setCreateBy(getUsername());
        int rows = tenantService.createTenant(sysTenant);
        return toAjax(rows);
    }

}