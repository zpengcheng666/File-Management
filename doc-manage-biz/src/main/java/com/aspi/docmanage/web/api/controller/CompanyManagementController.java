package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.DocBorrowRecords;
import com.aspi.docmanage.service.IDocBorrowRecordsService;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.entity.SysDept;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公司管理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Api(tags = "我的")
@RestController
@RequestMapping("/docmanage/companyManagement")
public class CompanyManagementController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;


    /**
     * 公司管理查询
     */
    @PostMapping("/list")
    @ApiOperation(value = "公司管理查询", notes = "公司管理查询")
    public TableDataInfo list(@RequestBody SysDept sysDept)
    {
        startPage();
        List<SysDept> list = deptService.list(sysDept);
        return getDataTable(list);
    }

    /**
     * 关联上级公司查询
     */
    @PostMapping("/selDeptList")
    @ApiOperation(value = "关联上级公司查询", notes = "关联上级公司查询")
    public AjaxResult selDeptList(@RequestBody SysDept sysDept)
    {
        List<SysDept> list = deptService.selDeptList(sysDept);
        return AjaxResult.success(list);
    }

    /**
     * 部门
     */
    @PostMapping("/selDept")
    @ApiOperation(value = "部门", notes = "部门")
    public AjaxResult selDept(@RequestBody SysDept sysDept)
    {
        List<SysDept> list = deptService.selDept(sysDept);
        return AjaxResult.success(list);
    }



}
