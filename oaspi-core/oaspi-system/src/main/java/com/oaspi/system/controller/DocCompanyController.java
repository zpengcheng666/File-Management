package com.oaspi.system.controller;

import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.vo.DeptTreeNodeVO;
import com.oaspi.system.service.IDocCompanyService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.system.domain.vo.CompanyTreeNodeVO;
import com.oaspi.system.service.IDocDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.oaspi.common.utils.PageUtils.startPage;

/**
 * 公司Controller
 *
 * @author zpc
 * @date 2025-01-04
 */
@Api(tags = "公司接口")
@RestController
@RequestMapping("/system/docCompany")
public class DocCompanyController extends BaseController {
    @Autowired
    private IDocCompanyService docCompanyService;
    @Autowired
    private IDocDeptService docDeptService;

    /**
     * 查询公司列表
     */
//    @PreAuthorize("@ss.hasPermi('system:company:list')")
    @GetMapping("/list")
    @ApiOperation("查询公司列表")
    public TableDataInfo list(DocCompany docCompany) {
        startPage();
        List<DocCompany> list = docCompanyService.selectDocCompanyList(docCompany);
        return getDataTable(list);
    }

    /**
     * 公司树状列表同时关联部门
     */
    @GetMapping(value = "/treeCompany")
    @ApiOperation("公司树状列表同时关联部门")
    public AjaxResult treeCompany() {
        List<CompanyTreeNodeVO> treeList = docCompanyService.getCompanyDeptTree();
        return AjaxResult.success(treeList);
    }

    /**
     * 公司树状列表
     */
    @GetMapping(value = "/treeCompanyList")
    @ApiOperation("公司树状列表")
    public AjaxResult treeCompanyList() {
        List<CompanyTreeNodeVO> treeList = docCompanyService.getCompanyDeptTreeList();
        return AjaxResult.success(treeList);
    }

    /**
     * 根据公司id查询部门列表
     */
    @GetMapping(value = "/getCompanyByDeptList")
    @ApiOperation("根据公司id查询部门列表")
    public AjaxResult getCompanyByDeptList(@RequestParam Long companyId) {
        List<DeptTreeNodeVO> treeList = docDeptService.getCompanyByDeptList(companyId);
        return AjaxResult.success(treeList);
    }

    /**
     * 导出公司列表
     */
//    @PreAuthorize("@ss.hasPermi('system:company:export')")
    @Log(title = "公司", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出公司列表")
    public void export(HttpServletResponse response, DocCompany docCompany) {
        List<DocCompany> list = docCompanyService.selectDocCompanyList(docCompany);
        ExcelUtil<DocCompany> util = new ExcelUtil<>(DocCompany.class);
        util.exportExcel(response, list, "公司数据");
    }

    /**
     * 获取公司详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:company:query')")
    @GetMapping(value = "/{companyId}")
    @ApiOperation("获取公司详细信息")
    public AjaxResult getInfo(@PathVariable("companyId") Long companyId) {
        return success(docCompanyService.selectDocCompanyByCompanyId(companyId));
    }

    /**
     * 根据公司id查询旗下子公司
     */
    @GetMapping(value = "/getChildrenCompany")
    @ApiOperation("根据公司id查询旗下子公司")
    public AjaxResult getChildrenCompany(@RequestParam Long companyId) {
//        List<CompanyChildrenVO> list = docCompanyService.getChildrenCompany(companyId);
        return success();
    }

    /**
     * 新增公司
     */
//    @PreAuthorize("@ss.hasPermi('system:company:add')")
    @Log(title = "公司", businessType = BusinessType.INSERT)
    @ApiOperation("新增公司")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DocCompany docCompany) {
        return toAjax(docCompanyService.insertDocCompany(docCompany));
    }

    /**
     * 修改公司
     */
//    @PreAuthorize("@ss.hasPermi('system:company:edit')")
    @Log(title = "公司", businessType = BusinessType.UPDATE)
    @ApiOperation("修改公司")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody DocCompany docCompany) {
        return toAjax(docCompanyService.updateDocCompany(docCompany));
    }

    /**
     * 设置公司状态
     */
    @Log(title = "设置公司状态", businessType = BusinessType.UPDATE)
    @ApiOperation("设置公司状态")
    @PostMapping("/editStatus")
    public AjaxResult editStatus(@RequestBody DocCompany docCompany) {
        return toAjax(docCompanyService.editStatus(docCompany));
    }

    /**
     * 删除公司
     */
//    @PreAuthorize("@ss.hasPermi('system:company:remove')")
    @Log(title = "删除公司", businessType = BusinessType.DELETE)
    @DeleteMapping("/{companyIds}")
    @ApiOperation("删除公司")
    public AjaxResult remove(@PathVariable Long companyIds) {
        return toAjax(docCompanyService.deleteDocCompanyByCompanyIds(companyIds));
    }
}
