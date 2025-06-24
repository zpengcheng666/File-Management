package com.oaspi.framework.web.service;

import com.oaspi.common.core.domain.entity.SysDept;
import com.oaspi.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.core.domain.model.LoginUser;
import com.oaspi.common.enums.UserStatus;
import com.oaspi.common.exception.ServiceException;
import com.oaspi.common.utils.MessageUtils;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author oaspi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }
        /*else if (UserStatus.NOT_UPDATE_PWD.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 未修改密码.", username);
            throw new ServiceException(MessageUtils.message("user.not.update.pwd"));
        }*/
        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
        SysDept dept = deptService.selectDeptById(user.getDeptId());
        loginUser.setTenantId(dept.getTenantId());
        return loginUser;
    }
}
