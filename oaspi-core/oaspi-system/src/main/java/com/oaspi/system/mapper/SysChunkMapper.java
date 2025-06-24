package com.oaspi.system.mapper;

import com.oaspi.system.domain.SysChunk;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-12-10
 */
public interface SysChunkMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param chunkId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SysChunk selectSysChunkById(Long chunkId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sysChunk 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysChunk> selectSysChunkList(SysChunk sysChunk);

    /**
     * 新增【请填写功能名称】
     *
     * @param sysChunk 【请填写功能名称】
     * @return 结果
     */
    public int insertSysChunk(SysChunk sysChunk);

    /**
     * 修改【请填写功能名称】
     *
     * @param sysChunk 【请填写功能名称】
     * @return 结果
     */
    public int updateSysChunk(SysChunk sysChunk);

    /**
     * 删除【请填写功能名称】
     *
     * @param chunkId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSysChunkById(Long chunkId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param chunkIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysChunkByIds(String[] chunkIds);
}
