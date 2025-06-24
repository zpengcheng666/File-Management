package com.oaspi.framework.aspectj;

import java.util.ArrayList;
import java.util.List;

import com.oaspi.common.core.domain.entity.SysDept;
import com.oaspi.system.service.ISysDeptService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.oaspi.common.annotation.DataScope;
import com.oaspi.common.core.domain.BaseEntity;
import com.oaspi.common.core.domain.entity.SysRole;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.core.domain.model.LoginUser;
import com.oaspi.common.core.text.Convert;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.framework.security.context.PermissionContextHolder;

/**
 * 数据过滤处理
 *
 * @author oaspi
 */
@Aspect
@Component
public class DataScopeAspect
{
    @Autowired
    ISysDeptService sysDeptService;

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable
    {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            //todo 应调查为啥没有，从根本解决
            SysUser currentUser = loginUser.getUser();
            Long tenantId = currentUser.getDept().getTenantId();
            if(tenantId == null){
                SysDept dept = sysDeptService.selectDeptById(currentUser.getDeptId());
                currentUser.setDept(dept);
            }
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin())
            {
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionContextHolder.getContext());
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user 用户
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     * @param permission 权限字符
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias, String permission)
    {
        // 超级管理员可以看到所有租户权限，不走这个过滤方法
        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<String>();



        for (SysRole role : user.getRoles())
        {
            String dataScope = ""; //role.getDataScope();
            if (!DATA_SCOPE_CUSTOM.equals(dataScope) && conditions.contains(dataScope))
            {
                continue;
            }
            /*if (StringUtils.isNotEmpty(permission) && StringUtils.isNotEmpty(role.getPermissions())
                    && !StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission)))
            {
                continue;
            }*/
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                // 配置了所有权限，也只能看到本租户的数据
                sqlString = new StringBuilder();
                sqlString.append(StringUtils.format(" OR {}.tenant_id = {} ", deptAlias, user.getDept().getTenantId()));
                conditions.add(dataScope);
                break;
            }
            else if (DATA_SCOPE_CUSTOM.equals(dataScope))
            {
                // 自定义可以看到勾选的数据
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            }
            else if (DATA_SCOPE_DEPT.equals(dataScope))
            {
                // 本部门可以看到本部门数据
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDept().getDeptId()));
            }
            else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope))
            {
                // 本部门及以下可以看到本部门及以下数据
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM doc_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias, user.getDeptId(), user.getDeptId()));
            }
            else if (DATA_SCOPE_SELF.equals(dataScope))
            {
                // 数据权限为仅本人
                if (StringUtils.isNotBlank(userAlias))
                {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                }
                else
                {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope);
        }

        // 多角色情况下，所有角色都不包含传递过来的权限字符，这个时候sqlString也会为空，所以要限制一下,不查询任何数据
        if (StringUtils.isEmpty(conditions))
        {
            sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
        }

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
            {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }


    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint)
    {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
