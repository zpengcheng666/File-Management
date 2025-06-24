package com.oaspi.system.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oaspi.system.mapper.SysTenantMapper;
import com.oaspi.system.domain.SysTenant;
import com.oaspi.system.service.ISysTenantService;

/**
 * 多租户Service业务层处理
 * 
 * @author hongy
 * @date 2024-11-09
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService 
{
    @Autowired
    private SysTenantMapper sysTenantMapper;

    /**
     * 查询多租户
     * 
     * @param tenantId 多租户主键
     * @return 多租户
     */
    @Override
    public SysTenant selectSysTenantByTenantId(Long tenantId)
    {
        return sysTenantMapper.selectSysTenantByTenantId(tenantId);
    }

    /**
     * 查询多租户列表
     * 
     * @param sysTenant 多租户
     * @return 多租户
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant)
    {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增多租户
     * 
     * @param sysTenant 多租户
     * @return 结果
     */
    @Override
    public int insertSysTenant(SysTenant sysTenant)
    {
        sysTenant.setCreateTime(DateUtils.getNowDate());
        return sysTenantMapper.insertSysTenant(sysTenant);
    }

    /**
     * 修改多租户
     * 
     * @param sysTenant 多租户
     * @return 结果
     */
    @Override
    public int updateSysTenant(SysTenant sysTenant)
    {
        sysTenant.setUpdateTime(DateUtils.getNowDate());
        return sysTenantMapper.updateSysTenant(sysTenant);
    }

    /**
     * 批量删除多租户
     * 
     * @param tenantIds 需要删除的多租户主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantByTenantIds(Long[] tenantIds)
    {
        return sysTenantMapper.deleteSysTenantByTenantIds(tenantIds);
    }

    /**
     * 删除多租户信息
     * 
     * @param tenantId 多租户主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantByTenantId(Long tenantId)
    {
        return sysTenantMapper.deleteSysTenantByTenantId(tenantId);
    }
}
