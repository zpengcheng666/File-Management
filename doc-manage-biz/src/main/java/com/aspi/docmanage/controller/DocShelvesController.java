package com.aspi.docmanage.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.aspi.docmanage.domain.Coffers;
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
import com.aspi.docmanage.domain.DocShelves;
import com.aspi.docmanage.service.IDocShelvesService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 档案架Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docmanage/shelves")
public class DocShelvesController extends BaseController
{
    @Autowired
    private IDocShelvesService docShelvesService;

    /**
     * 查询档案架列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:shelves:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocShelves docShelves)
    {
        startPage();
        List<DocShelves> list = docShelvesService.selectDocShelvesList(docShelves);
        return getDataTable(list);
    }

    /**
     * 导出档案架列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:shelves:export')")
    @Log(title = "档案架", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocShelves docShelves)
    {
        List<DocShelves> list = docShelvesService.selectDocShelvesList(docShelves);
        ExcelUtil<DocShelves> util = new ExcelUtil<DocShelves>(DocShelves.class);
        util.exportExcel(response, list, "档案架数据");
    }

    /**
     * 获取档案架详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:shelves:query')")
    @GetMapping(value = "/{shelvesNo}")
    public AjaxResult getInfo(@PathVariable("shelvesNo") Long shelvesNo)
    {
        return success(docShelvesService.selectDocShelvesByShelvesNo(shelvesNo));
    }

    /**
     * 新增档案架
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:shelves:add')")
    @Log(title = "档案架", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocShelves docShelves)
    {
        return toAjax(docShelvesService.insertDocShelves(docShelves));
    }

    /**
     * 修改档案架
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:shelves:edit')")
    @Log(title = "档案架", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocShelves docShelves)
    {
        return toAjax(docShelvesService.updateDocShelves(docShelves));
    }

    /**
     * 删除档案架
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:shelves:remove')")
    @Log(title = "档案架", businessType = BusinessType.DELETE)
	@DeleteMapping("/{shelvesNos}")
    public AjaxResult remove(@PathVariable Long[] shelvesNos)
    {
        return toAjax(docShelvesService.deleteDocShelvesByShelvesNos(shelvesNos));
    }


    /**
     * 库存管理二级联动
     */
    /*    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:add')")*/
    @Log(title = "库房管理", businessType = BusinessType.INSERT)
    @RequestMapping("/getCoffersAndShelves")
    public AjaxResult getCoffersAndShelves() {
        Map<String, Coffers> coffersMap = docShelvesService.getCoffersWithShelves();
        return AjaxResult.success(new ArrayList<>(coffersMap.values()));
    }

}
