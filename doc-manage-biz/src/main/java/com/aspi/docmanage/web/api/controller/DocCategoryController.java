package com.aspi.docmanage.web.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.aspi.docmanage.web.api.vo.ClassificationVO;
import com.aspi.docmanage.web.api.vo.SelectTreeNode;
import com.oaspi.common.core.domain.entity.SysDictData;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.enums.BusinessType;
import com.aspi.docmanage.domain.DocCategory;
import com.aspi.docmanage.service.IDocCategoryService;
import com.oaspi.common.utils.poi.ExcelUtil;

/**
 * 档案分类Controller
 * 
 * @author hongy
 * @date 2024-11-02
 */
@Api(tags = "档案分类")
@RestController
@RequestMapping("/docmanage/doccategory")
public class DocCategoryController extends BaseController
{
    @Autowired
    private IDocCategoryService docCategoryService;

    /**
     * 查询档案分类列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:doccategory:list')")
    @GetMapping("/list")
    @Log(title = "查询档案分类列表")
    public AjaxResult list(DocCategory docCategory)
    {
        List<DocCategory> list = docCategoryService.selectDocCategoryList(docCategory);
        return success(list);
    }

    /**
     * 导出档案分类列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:doccategory:export')")
    @Log(title = "档案分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocCategory docCategory)
    {
        List<DocCategory> list = docCategoryService.selectDocCategoryList(docCategory);
        ExcelUtil<DocCategory> util = new ExcelUtil<DocCategory>(DocCategory.class);
        util.exportExcel(response, list, "档案分类数据");
    }

    /**
     * 获取档案分类详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:doccategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docCategoryService.selectDocCategoryById(id));
    }

    /**
     * 新增档案分类
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:doccategory:add')")
    @Log(title = "档案分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocCategory docCategory)
    {
        return toAjax(docCategoryService.insertDocCategory(docCategory));
    }

    /**
     * 修改档案分类
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:doccategory:edit')")
    @Log(title = "档案分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocCategory docCategory)
    {
        return toAjax(docCategoryService.updateDocCategory(docCategory));
    }

    /**
     * 删除档案分类
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:doccategory:remove')")
    @Log(title = "档案分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docCategoryService.deleteDocCategoryByIds(ids));
    }

    /**
     * 档案门类树状列表
     * @param docCategory
     * @return
     */
    @GetMapping(value = "/treeSelectDoc")
    @Log(title = "档案门类树状图")
    public AjaxResult treeSelectDoc(DocCategory docCategory)
    {
        List<SelectTreeNode> treeList = docCategoryService.treeList(docCategory);
        return AjaxResult.success(treeList);
    }

    /**
     * 全部分类下拉列框
     */
    @GetMapping(value = "/selectQBFL")
    @Log(title = "全部分类下拉列框")
    public AjaxResult selectQBFL(SysDictData sysDictData)
    {
        List<ClassificationVO> classificationVOS = docCategoryService.fullClassification(sysDictData);
        return AjaxResult.success(classificationVOS);
    }

    /**
     * 密级及保管期限下拉框
     */
    @GetMapping(value = "/secretDuration")
    @Log(title = "密级及保管期限下拉框")
    public AjaxResult secretDuration(SysDictData sysDictData)
    {
        List<ClassificationVO> classificationVOS = docCategoryService.secretDuration(sysDictData);
        return AjaxResult.success(classificationVOS);
    }
}
