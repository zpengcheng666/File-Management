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
import com.aspi.docmanage.domain.DocCollect;
import com.aspi.docmanage.service.IDocCollectService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 我的收藏Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/collect/collect")
public class DocCollectController extends BaseController
{
    @Autowired
    private IDocCollectService docCollectService;

    /**
     * 查询我的收藏列表
     */
    // @PreAuthorize("@ss.hasPermi('collect:collect:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocCollect docCollect)
    {
        startPage();
        List<DocCollect> list = docCollectService.selectDocCollectList(docCollect);
        return getDataTable(list);
    }

    /**
     * 导出我的收藏列表
     */
    // @PreAuthorize("@ss.hasPermi('collect:collect:export')")
    @Log(title = "我的收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocCollect docCollect)
    {
        List<DocCollect> list = docCollectService.selectDocCollectList(docCollect);
        ExcelUtil<DocCollect> util = new ExcelUtil<DocCollect>(DocCollect.class);
        util.exportExcel(response, list, "我的收藏数据");
    }

    /**
     * 获取我的收藏详细信息
     */
    // @PreAuthorize("@ss.hasPermi('collect:collect:query')")
    @GetMapping(value = "/{collectId}")
    public AjaxResult getInfo(@PathVariable("collectId") Long collectId)
    {
        return success(docCollectService.selectDocCollectByCollectId(collectId));
    }

    /**
     * 新增我的收藏
     */
    // @PreAuthorize("@ss.hasPermi('collect:collect:add')")
    @Log(title = "我的收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocCollect docCollect)
    {
        return toAjax(docCollectService.insertDocCollect(docCollect));
    }

    /**
     * 修改我的收藏
     */
    // @PreAuthorize("@ss.hasPermi('collect:collect:edit')")
    @Log(title = "我的收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocCollect docCollect)
    {
        return toAjax(docCollectService.updateDocCollect(docCollect));
    }

    /**
     * 删除我的收藏
     */
    // @PreAuthorize("@ss.hasPermi('collect:collect:remove')")
    @Log(title = "我的收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{collectIds}")
    public AjaxResult remove(@PathVariable Long[] collectIds)
    {
        return toAjax(docCollectService.deleteDocCollectByCollectIds(collectIds));
    }
}
