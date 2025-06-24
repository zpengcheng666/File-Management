package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.DocBorrow;
import com.aspi.docmanage.service.IDocBorrowService;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 线下借阅管理
 * @Author wangy
 * @Date 2025/01/17 15:16
 */
@Api(tags = "线下借阅管理")
@RestController
@RequestMapping("/docmanage/borrow")
public class DocBorrowController extends BaseController {

    @Resource
    private IDocBorrowService docBorrowService;

    @ApiOperation(value = "借阅列表", notes = "借阅列表")
    @GetMapping("/list")
    public TableDataInfo listDocBorrow() {
        startPage();
        List<DocBorrow> list = docBorrowService.listDocBorrow();
        return getDataTable(list);
    }

    @ApiOperation(value = "新增借阅记录", notes = "新增借阅记录")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DocBorrow docBorrow) {
        docBorrowService.addDocBorrow(docBorrow);
        return success("添加成功！");
    }

    @ApiOperation(value = "删除借阅记录", notes = "删除借阅记录")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        docBorrowService.deleteDocBorrow(id);
        return success("删除成功！");
    }

    @ApiOperation(value = "修改借阅状态", notes = "修改借阅状态")
    @PostMapping("/update/status")
    public AjaxResult updateStatus(@RequestBody DocBorrow docBorrow) {
        docBorrowService.updateDocBorrowStatus(docBorrow);
        return success("修改成功！");
    }

}
