package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.DocBehavior;
import com.aspi.docmanage.service.IDocInfoService;
import com.aspi.docmanage.service.IHomeService;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.entity.SysRole;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.system.service.ISysRoleService;
import com.oaspi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 首页信息Controller
 * @Author wangy
 * @Date 2025/01/02 15:05
 */
@Api(tags = "首页信息")
@RestController
@RequestMapping("/docmanage/home")
public class HomeController extends BaseController {

    @Resource
    private IDocInfoService docInfoService;

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IHomeService homeService;

    @ApiOperation(value = "用户行为日志列表", notes = "用户行为日志列表")
    @GetMapping("/behaviors")
    public TableDataInfo listBehavior()
    {
        DocBehavior docBehavior = new DocBehavior();
        SysRole sysRole = new SysRole();
        List<Long> roleList = sysRoleService.selectRoleListByUserId(getUserId());
        if (!roleList.isEmpty()) {
            sysRole = sysRoleService.selectRoleById(roleList.get(0));
        }
        if (!sysRole.getRoleName().equals("超级管理员")) {
            docBehavior.setUsername(getUsername());
        }
        startPage();
        List<DocBehavior> list = docInfoService.queryDocBehaviorList(docBehavior);
        for (DocBehavior db : list) {
            String username = db.getUsername();
            SysUser sysUser = sysUserService.selectUserByUserName(username);
            db.setPersonDeptName(sysUser.getDept().getDeptName());
            db.setPersonCompanyName(sysUser.getCompanyName());
        }
        return getDataTable(list);
    }

    @ApiOperation(value = "获取总体数量", notes = "获取总体数量")
    @GetMapping("/getNumbers")
    public List<Map<String, String>> getNumbers()
    {
        return homeService.getNumbers();
    }

    @ApiOperation(value = "按种类分类获取数量", notes = "按种类分类获取数量")
    @GetMapping("/groupByCategory")
    public List<Map<String, String>> getDocNumbersGroupByCategory()
    {
        return homeService.getDocNumbersGroupByCategory();
    }

    @ApiOperation(value = "按部门分类获取数量", notes = "按部门分类获取数量")
    @GetMapping("/groupByDept")
    public List<Map<String, String>> getDocNumbersGroupByDepartment()
    {
        return homeService.getDocNumbersGroupByDepartment();
    }

}