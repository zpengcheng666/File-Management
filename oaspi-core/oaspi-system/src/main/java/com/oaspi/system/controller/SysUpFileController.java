package com.oaspi.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.oaspi.common.utils.StringUtils;
import com.oaspi.system.domain.vo.FileDocVo;
import com.oaspi.system.domain.vo.SysUpFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.service.ISysUpFileService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 电子文件信息上传列表Controller
 * 
 * @author hongy
 * @date 2024-10-03
 */
@Api(tags = "电子文件信息上传列表")
@RestController
@RequestMapping("/system/file")
public class SysUpFileController extends BaseController
{
    @Autowired
    private ISysUpFileService sysUpFileService;

    /**
     * 查询上传文件列表
     */
    // @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询电子文件信息列表")
    public TableDataInfo list(SysUpFile sysUpFile)
    {
        startPage();
        List<SysUpFile> list = sysUpFileService.selectSysUpFileList(sysUpFile);
        return getDataTable(list);
    }

    /**
     * 导出上传文件列表
     */
    // @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "上传文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出电子文件信息列表")
    public void export(HttpServletResponse response, SysUpFile sysUpFile)
    {
        List<SysUpFile> list = sysUpFileService.selectSysUpFileList(sysUpFile);
        ExcelUtil<SysUpFile> util = new ExcelUtil<SysUpFile>(SysUpFile.class);
        util.exportExcel(response, list, "上传文件数据");
    }

    /**
     * 获取上传文件详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取电子文件信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysUpFileService.selectSysUpFileById(id));
    }

    /**
     * 新增上传文件
     */
    // @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "上传文件", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增电子文件信息")
    public AjaxResult add(@RequestBody SysUpFile sysUpFile)
    {
        return toAjax(sysUpFileService.insertSysUpFile(sysUpFile));
    }

    /**
     * 修改上传文件
     */
    // @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "上传文件", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改电子文件信息")
    public AjaxResult edit(@RequestBody SysUpFile sysUpFile)
    {
        return toAjax(sysUpFileService.updateSysUpFile(sysUpFile));
    }

    /**
     * 修改电子文件信息vo
     * @param vo
     * @return
     */
    @PostMapping("/editVo")
    @ApiOperation(value = "修改电子文件信息vo")
    public AjaxResult editVo(@RequestBody SysUpFileVO vo)
    {
        return toAjax(sysUpFileService.updateSysUpFileVO(vo));
    }

    /**
     * 删除上传文件
     */
    // @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "上传文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation(value = "删除电子文件信息")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysUpFileService.deleteSysUpFileByIds(ids));
    }
}
