package com.aspi.docmanage.controller;

import com.aspi.docmanage.domain.Coffers;
import com.aspi.docmanage.domain.DocCategory;
import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.DocTemperature;
import com.aspi.docmanage.service.IDocCategoryService;
import com.aspi.docmanage.service.IDocCoffersService;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 库房管理Controller
 * 
 * @author hongy
 * @date 2024-11-02
 */
@RestController
@RequestMapping("/docmanage/docCoffers")
public class DocCoffersController extends BaseController
{
    @Autowired
    private IDocCoffersService coffersService;

    /**
     * 查询档案库列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @GetMapping("/coffersList")
    public AjaxResult coffersList(DocCoffers docCoffers)
    {
        List<DocCoffers> list = coffersService.coffersList(docCoffers);
        return success(list);
    }


    /**
     * 新增库房
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:add')")*/
    @Log(title = "库房管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DocCoffers docCoffers)
    {
        return coffersService.add(docCoffers);
    }


    /**
     * 删除库房
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @RequestMapping("/delCoffers")
    public AjaxResult delCoffers(@RequestBody DocCoffers docCoffers)
    {

        return  coffersService.delCoffers(docCoffers);
    }

    /**
     * 修改库房表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docCoffers:list')")
    @RequestMapping("/updCoffers")
    public AjaxResult updCoffers(@RequestBody DocCoffers docCoffers)
    {

        return  coffersService.updCoffers(docCoffers);
    }

}
