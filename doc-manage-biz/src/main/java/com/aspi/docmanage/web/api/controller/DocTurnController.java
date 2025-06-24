package com.aspi.docmanage.web.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.aspi.docmanage.domain.DocTurn;
import com.aspi.docmanage.domain.TurnAttachment;
import com.aspi.docmanage.service.IDocTurnService;
import com.aspi.docmanage.web.api.vo.TurnAttachmentVO;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.system.domain.SysUpFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 档案移交信息Controller
 *
 * @author zpc
 * @date 2025-01-09
 */
@Api(tags = "档案移交信息Controller",value = "档案移交信息Controller")
@RestController
@RequestMapping("/docmanage/turn")
public class DocTurnController extends BaseController
{
    @Autowired
    private IDocTurnService docTurnService;

    /**
     * 查询档案移交信息列表
     */
    @GetMapping("/list")
    @ApiOperation("查询档案移交信息列表")
    public TableDataInfo list(DocTurn docTurn)
    {
        startPage();
        List<DocTurn> list = docTurnService.selectDocTurnList(docTurn);
        return getDataTable(list);
    }

    /**
     * 根据移交列表id查询对应附件信息
     */
    @GetMapping("/turnIdByAttachmentList")
    @ApiOperation("根据移交列表id查询对应附件信息")
    public TableDataInfo turnIdByAttachmentList(@RequestParam Long turnId)
    {
        startPage();
        List<SysUpFile> list = docTurnService.turnIdByAttachmentList(turnId);
        return getDataTable(list);
    }

    /**
     * 导出档案移交信息列表
     */
    @Log(title = "档案移交信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出档案移交信息列表")
    public void export(HttpServletResponse response, DocTurn docTurn)
    {
        List<DocTurn> list = docTurnService.selectDocTurnList(docTurn);
        ExcelUtil<DocTurn> util = new ExcelUtil<DocTurn>(DocTurn.class);
        util.exportExcel(response, list, "档案移交信息数据");
    }

    /**
     * 获取档案移交信息详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取档案移交信息详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docTurnService.selectDocTurnById(id));
    }

    /**
     * 新增档案移交信息
     */
    @Log(title = "档案移交信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation("新增档案移交信息")
    public AjaxResult add(@RequestBody TurnAttachmentVO vo)
    {
        return toAjax(docTurnService.insertDocTurn(vo));
    }

    /**
     * 修改档案移交信息
     */
    @Log(title = "档案移交信息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    @ApiOperation("修改档案移交信息")
    public AjaxResult edit(@RequestBody DocTurn docTurn)
    {
        return toAjax(docTurnService.updateDocTurn(docTurn));
    }

    /**
     * 删除档案移交信息
     */
    @Log(title = "档案移交信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除档案移交信息")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docTurnService.deleteDocTurnByIds(ids));
    }
}

