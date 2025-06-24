package com.oaspi.web.controller.system;

import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.entity.SysDept;
import com.oaspi.common.core.domain.entity.SysRole;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.common.utils.uuid.IdUtils;
import com.oaspi.system.service.ISysDeptService;
import com.oaspi.system.service.ISysPostService;
import com.oaspi.system.service.ISysRoleService;
import com.oaspi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息
 * 
 * @author oaspi
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    /**
     * 获取用户列表
     */
    // @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    @ApiOperation(value = "用户信息列表", notes = "用户信息列表")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据部门id返回用户下拉框
     */
    // @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/byDeptId/{deptId}")
    @ApiOperation(value = "根据部门id返回用户下拉框", notes = "根据部门id返回用户下拉框")
    public AjaxResult listByDeptId(@PathVariable("deptId") Long deptId)
    {
        List<SysUser> list = userService.selectUserByDeptId(deptId);
        return AjaxResult.success(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    // @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    // @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    // @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (!userService.checkUserNameUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setStatus("3"); //3代表从未修改过密码
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    // @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    // @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId()))
        {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    // @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户修改密码", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码")
    @PutMapping("/user/updatePwd")
    public AjaxResult userUpdatePwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.userUpdatePwd(user));
    }

    @Log(title = "管理员修改密码", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "管理员修改密码", notes = "管理员修改密码")
    @PutMapping("/manager/updatePwd")
    public AjaxResult adminUpdatePwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.adminUpdatePwd(user));
    }

    /**
     * 状态修改
     */
    // @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    // @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    // @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    // @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept)
    {
        return success(deptService.selectDeptTreeList(dept));
    }

    /**
     * 获取随机密码
     */
    @ApiOperation(value = "获取随机密码", notes = "获取随机密码")
    @GetMapping("/getPassword")
    public AjaxResult getPassword()
    {
        AjaxResult ajax = AjaxResult.success();
        return ajax.put("password", IdUtils.getRandomString(10));
    }

    /**
     * 冻结用户
     */
    @Log(title = "冻结用户", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "冻结用户", notes = "冻结用户")
    @PutMapping("/freeze")
    public AjaxResult freezeUser(@RequestBody SysUser sysUser)
    {
        userService.freezeUser(sysUser.getUserIds());
        return success();
    }

    /**
     * 解冻用户
     */
    @Log(title = "解冻用户", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "解冻用户", notes = "解冻用户")
    @PutMapping("/unFreeze")
    public AjaxResult unFreezeUser(@RequestBody SysUser sysUser)
    {
        userService.unFreezeUser(sysUser.getUserIds());
        return success();
    }

}
