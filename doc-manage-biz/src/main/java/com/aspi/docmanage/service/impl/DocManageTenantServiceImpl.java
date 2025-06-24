package com.aspi.docmanage.service.impl;

import com.aspi.docmanage.service.IDocManageTenantService;
import com.oaspi.common.annotation.DataScope;
import com.oaspi.common.core.domain.entity.SysDept;
import com.oaspi.common.core.domain.entity.SysRole;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.system.domain.SysRoleMenu;
import com.oaspi.system.domain.SysTenant;
import com.oaspi.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.oaspi.framework.aspectj.DataScopeAspect.*;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/9 18:06
 */
@Slf4j
@Service
public class DocManageTenantServiceImpl implements IDocManageTenantService {

    @Autowired
    private ISysTenantService sysTenantService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;


    @Override
    @Transactional
    public int createTenant(SysTenant sysTenant) {
//        SysTenant example = new SysTenant();
//        example.setName(sysTenant.getName());
//        List<SysTenant> list = sysTenantService.selectSysTenantList(example);
//        if(!list.isEmpty()){
//            log.warn("租户名称重复" + sysTenant.getName() );
//            return 0;
//        }
//
//        int rows = sysTenantService.insertSysTenant(sysTenant);
//        if (rows > 0){
//            SysRole sysRole = new SysRole();
//            // 创建这个租户的默认公司
//            String[]  roleKeys = {"biz_admin","doc_admin","manager","common"};
//            for(String rolekey : roleKeys){
//                SysRole exampleRole = new SysRole();
//                exampleRole.setRoleKey(rolekey);
//                List<SysRole> roles = sysRoleService.selectRoleList(exampleRole);
//                if( roles.isEmpty()){
//                    throw  new RuntimeException("默认角色不存在，请联系管理员");
//                }
//
//                SysRole role = roles.get(0);
//                List<Long> menuIds = sysMenuService.selectMenuListByRoleId(role.getRoleId());
//                role.setRoleId(null);
//                role.setTenantId(sysTenant.getTenantId());
//                role.setCreateBy(sysTenant.getCreateBy());
//                role.setCreateTime(new Date());
//                role.setUpdateBy(null);
//                role.setUpdateTime(null);
//                role.setMenuIds(menuIds.toArray(new Long[0]));
//                sysRoleService.insertRole(role);
//
//                log.info("创建{} 角色 {} 成功：" ,sysTenant.getName() , role.getRoleName());
//                if(role.getRoleKey().equals("biz_admin")){
//                    sysRole = role;
//                }
//            }
//
//            SysDept sysDept = new SysDept();
//            sysDept.setDeptName(sysTenant.getName());
//            sysDept.setParentId(0L);
//            sysDept.setTenantId(sysTenant.getTenantId());
//            sysDept.setCreateBy(sysTenant.getCreateBy());
//            sysDeptService.insertCorp(sysDept);
//            log.info("创建{} 公司 {} 成功：" ,sysTenant.getName() , sysDept.getDeptName());
//
//
//
//            // 自动创建这个角色的超级管理员
//            SysUser sysUser = new SysUser();
//            sysUser.setUserName( sysTenant.getName() + "超级管理员");
//            sysUser.setNickName( sysTenant.getName() + "超级管理员");
//            String defaultPassword = "Aa123.456";
//            sysUser.setPassword(SecurityUtils.encryptPassword(defaultPassword));
//            sysUser.setDeptId(sysDept.getDeptId());
//            sysUserService.insertUser(sysUser);
//            sysRoleService.insertAuthUsers(sysRole.getRoleId(), new Long[]{sysUser.getUserId()});
//            log.info("创建{} 超级管理员 {} 成功：" ,sysTenant.getName() , sysUser.getUserName());
//        }
//        return rows;
        return 0;
    }


}