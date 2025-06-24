package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.web.api.vo.CompanyVO;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.R;
import com.oaspi.common.core.domain.entity.SysDept;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/7 13:14
 */
@RestController
@Api(tags = "公司管理")
@RequestMapping("/docmanage/company")
public class CompanyController extends BaseController {
    @Autowired
    private ISysDeptService deptService;
    /**
     * 新增部门
     */
    // @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "公司管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "添加、修改公司", notes = "添加、修改公司信息", httpMethod = "POST")
    public R<Integer> add(@RequestBody @Validated CompanyVO companyVO)
    {
        SysDept dept = companyVO.toSysDept();
        if(dept.getDeptId() == null || dept.getDeptId() == 0){
            // 添加公司信息
            if (!deptService.checkDeptNameUnique(dept))
            {
                return R.fail("新增公司'" + dept.getDeptName() + "'失败，该公司已存在");
            }
            dept.setCreateBy(getUsername());
            return R.ok(deptService.insertCorp(dept));
        }

        // 修改公司信息
        dept.setUpdateBy(getUsername());
        dept.setUpdateTime(new Date());
        return R.ok(deptService.updateDept(dept));
    }

    // @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询公司列表", notes = "根据特定条件查询公司列表", httpMethod = "GET", response = CompanyVO.class)
    public R<List<CompanyVO>> list(CompanyVO companyVO)
    {
        SysDept dept = companyVO.toSysDept();
        List<SysDept> depts = deptService.selectCorpList(dept);
        List<CompanyVO> companyVOS = new ArrayList<>();
        for (SysDept sysDept : depts) {
            companyVOS.add(CompanyVO.fromSysDept(sysDept));
        }
        return R.ok(companyVOS);
    }

    /**
     * 绑定
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @PostMapping("/bindDept")
    @ApiOperation(value = "绑定", notes = "绑定")
    public AjaxResult bindDept(@RequestBody SysDept dept)
    {

        return  deptService.bindDept(dept);
    }
}