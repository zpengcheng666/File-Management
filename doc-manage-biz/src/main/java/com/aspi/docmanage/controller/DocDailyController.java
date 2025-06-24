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
import com.aspi.docmanage.domain.DocDaily;
import com.aspi.docmanage.service.IDocDailyService;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.core.page.TableDataInfo;

/**
 * 日常情况Controller
 * 
 * @author hongy
 * @date 2024-12-08
 */
@RestController
@RequestMapping("/docmanage/daily")
public class DocDailyController extends BaseController
{
    @Autowired
    private IDocDailyService docDailyService;

    /**
     * 查询日常情况列表
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:daily:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(DocDaily docDaily)
    {
        startPage();
        List<DocDaily> list = docDailyService.selectDocDailyList(docDaily);
        return getDataTable(list);
    }

    /**
     * 导出日常情况列表
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:daily:export')")*/
    @Log(title = "日常情况", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocDaily docDaily)
    {
        List<DocDaily> list = docDailyService.selectDocDailyList(docDaily);
        ExcelUtil<DocDaily> util = new ExcelUtil<DocDaily>(DocDaily.class);
        util.exportExcel(response, list, "日常情况数据");
    }

    /**
     * 获取日常情况详细信息
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:daily:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(docDailyService.selectDocDailyById(id));
    }

    /**
     * 新增日常情况
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:daily:add')")*/
    @Log(title = "日常情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocDaily docDaily)
    {
        return toAjax(docDailyService.insertDocDaily(docDaily));
    }

    /**
     * 修改日常情况
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:daily:edit')")*/
    @Log(title = "日常情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocDaily docDaily)
    {
        return toAjax(docDailyService.updateDocDaily(docDaily));
    }

    /**
     * 删除日常情况
     */
/*    // @PreAuthorize("@ss.hasPermi('docmanage:daily:remove')")*/
    @Log(title = "日常情况", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(docDailyService.deleteDocDailyByIds(ids));
    }
}
