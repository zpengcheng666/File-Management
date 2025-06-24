package com.oaspi.system.controller;

import com.oaspi.system.domain.DocDept;
import com.oaspi.system.domain.vo.DeptTreeNodeVO;
import com.oaspi.system.domain.vo.DeptVO;
import com.oaspi.system.service.IDocDeptService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 部门Controller
 *
 * @author zpc
 * @date 2025-01-04
 */
@Api(tags = "部门接口")
@RestController
@RequestMapping("/system/docDept")
public class DocDeptController extends BaseController
{
    @Autowired
    private IDocDeptService docDeptService;

    /**
     * 查询部门列表
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation("查询部门列表")
    public TableDataInfo list(DocDept docDept)
    {
        startPage();
        List<DocDept> list = docDeptService.selectDocDeptList(docDept);
        return getDataTable(list);
    }

    /**
     * 部门树状列表
     * @param docDept
     * @return
     */
    @GetMapping(value = "/deptCompany")
    @Log(title = "部门列表")
    @ApiOperation("部门列表")
    public AjaxResult deptList(DocDept docDept)
    {
        List<DeptVO> treeList = docDeptService.deptList(docDept);
        return AjaxResult.success(treeList);
    }

    /**
     * 导出部门列表
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:export')")
    @Log(title = "部门", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出部门列表")
    public void export(HttpServletResponse response, DocDept docDept)
    {
        List<DocDept> list = docDeptService.selectDocDeptList(docDept);
        ExcelUtil<DocDept> util = new ExcelUtil<DocDept>(DocDept.class);
        util.exportExcel(response, list, "部门数据");
    }

    /**
     * 获取部门详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    @ApiOperation("获取部门详细信息")
    public AjaxResult getInfo(@PathVariable("deptId") Long deptId)
    {
        return success(docDeptService.selectDocDeptByDeptId(deptId));
    }

    /**
     * 新增部门
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "新增部门", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation("新增部门")
    public AjaxResult add(@RequestBody DocDept docDept)
    {
        return toAjax(docDeptService.insertDocDept(docDept));
    }

    /**
     * 修改部门
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "修改部门", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ApiOperation("修改部门")
    public AjaxResult edit(@RequestBody DocDept docDept)
    {
        return toAjax(docDeptService.updateDocDept(docDept));
    }

    /**
     * 删除部门
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptIds}")
    @ApiOperation("删除部门")
    public AjaxResult remove(@PathVariable Long[] deptIds)
    {
        return toAjax(docDeptService.deleteDocDeptByDeptIds(deptIds));
    }
}
