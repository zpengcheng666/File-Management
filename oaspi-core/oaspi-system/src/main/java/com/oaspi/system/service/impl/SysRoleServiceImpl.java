package com.oaspi.system.service.impl;

import com.oaspi.system.mapper.SysUserMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.oaspi.common.core.domain.entity.SysDept;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oaspi.common.annotation.DataScope;
import com.oaspi.common.constant.UserConstants;
import com.oaspi.common.core.domain.entity.SysRole;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.exception.ServiceException;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.common.utils.spring.SpringUtils;
import com.oaspi.system.domain.SysRoleDept;
import com.oaspi.system.domain.SysRoleMenu;
import com.oaspi.system.domain.SysUserRole;
import com.oaspi.system.mapper.SysRoleDeptMapper;
import com.oaspi.system.mapper.SysRoleMapper;
import com.oaspi.system.mapper.SysRoleMenuMapper;
import com.oaspi.system.mapper.SysUserRoleMapper;
import com.oaspi.system.service.ISysRoleService;

/**
 * 角色 业务层处理
 * 
 * @author oaspi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role)
    {
        List<SysRole> list = roleMapper.selectRoleList(role);
        for (SysRole r : list) {
            Long roleId = r.getRoleId();
            int count = userRoleMapper.countUserRoleByRoleId(roleId);
            String menuChar = roleMenuMapper.getMenuCharByRoleId(roleId).toString();
            r.setAccountNumber(count);
            r.setPermissionRange(menuChar);
        }
        return list;
    }

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId)
    {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        /*List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;*/
        return userRoles;
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        /*for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }*/
        return permsSet;
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll()
    {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role)
    {
//        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
//        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
//        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
//        {
//            return UserConstants.NOT_UNIQUE;
//        }
//        return UserConstants.UNIQUE;
        return false;
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     * 
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(Long roleId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
            if (StringUtils.isEmpty(roles))
            {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    @Override
    public List<SysUser> selectUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.selectUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        updateRoleTenant(role);
        // 新增角色信息
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        updateRoleTenant(role);
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        updateRoleTenant(role);
        return  roleMapper.updateRole(role);
    }

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role)
    {
        updateRoleTenant(role);
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    private void updateRoleTenant(SysRole role){
        if (!SysUser.isAdmin(SecurityUtils.getUserId())){
            /*if(role.getTenantId() == null || role.getTenantId() == 0){
                role.setTenantId(SecurityUtils.getLoginUser().getTenantId());
                if(role.getTenantId() == null || role.getTenantId() == 0){
                    throw new ServiceException("租户ID为空，无法修改角色信息");
                }
            }*/
        }
    }


    /**
     * 新增角色菜单信息
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        if (null != role.getMenuIds()) {
            for (Long menuId : role.getMenuIds())
            {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(role.getRoleId());
                rm.setMenuId(menuId);
                list.add(rm);
            }
            if (list.size() > 0)
            {
                rows = roleMenuMapper.batchRoleMenu(list);
            }
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role)
    {
//        int rows = 1;
//        // 新增角色与部门（数据权限）管理
//        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
//        for (Long deptId : role.getDeptIds())
//        {
//            SysRoleDept rd = new SysRoleDept();
//            rd.setRoleId(role.getRoleId());
//            rd.setDeptId(deptId);
//            list.add(rd);
//        }
//        if (list.size() > 0)
//        {
//            rows = roleDeptMapper.batchRoleDept(list);
//        }
//        return rows;
        return 0;
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId)
    {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds)
    {
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDept(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds)
    {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }

    @Override
    public void updatePermission(SysRole sysRole) {
        Long roleId = sysRole.getRoleId();
        Long[] menuIds = sysRole.getMenuIds();
        SysRoleMenu srm = null;
        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        for (int i = 0; i < menuIds.length; i++) {
            srm = new SysRoleMenu();
            srm.setRoleId(roleId);
            srm.setMenuId(menuIds[i]);
            roleMenuList.add(srm);
        }
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        roleMenuMapper.batchRoleMenu(roleMenuList);
    }

    @Override
    public void updateRelation(SysRole sysRole) {
        Long roleId = sysRole.getRoleId();
        Long[] userIds = sysRole.getUserIds();
        SysUserRole sur = null;
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (int i = 0; i < userIds.length; i++) {
            sur = new SysUserRole();
            sur.setRoleId(roleId);
            sur.setUserId(userIds[i]);
            userRoleList.add(sur);
        }
        userRoleMapper.deleteUserRoleByRoleId(roleId);
        userRoleMapper.batchUserRole(userRoleList);
    }

}
