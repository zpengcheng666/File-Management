package com.oaspi.system.service;

import java.util.List;
import com.oaspi.system.domain.SysTenant;

/**
 * 多租户Service接口
 * 
 * @author hongy
 * @date 2024-11-09
 */
public interface ISysTenantService 
{
    /**
     * 查询多租户
     * 
     * @param tenantId 多租户主键
     * @return 多租户
     */
    public SysTenant selectSysTenantByTenantId(Long tenantId);

    /**
     * 查询多租户列表
     * 
     * @param sysTenant 多租户
     * @return 多租户集合
     */
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 新增多租户
     * 
     * @param sysTenant 多租户
     * @return 结果
     */
    public int insertSysTenant(SysTenant sysTenant);

    /**
     * 修改多租户
     * 
     * @param sysTenant 多租户
     * @return 结果
     */
    public int updateSysTenant(SysTenant sysTenant);

    /**
     * 批量删除多租户
     * 
     * @param tenantIds 需要删除的多租户主键集合
     * @return 结果
     */
    public int deleteSysTenantByTenantIds(Long[] tenantIds);

    /**
     * 删除多租户信息
     * 
     * @param tenantId 多租户主键
     * @return 结果
     */
    public int deleteSysTenantByTenantId(Long tenantId);
}
