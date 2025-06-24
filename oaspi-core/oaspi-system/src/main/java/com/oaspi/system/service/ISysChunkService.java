package com.oaspi.system.service;

import com.oaspi.system.domain.SysChunk;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.vo.CheckSysChunkVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-12-10
 */
public interface ISysChunkService
{
    public boolean postFileUpload(SysChunk sysChunk, HttpServletResponse response) ;

    public CheckSysChunkVO getFileUpload(SysChunk sysChunk, HttpServletResponse response);

    public String mergeFile(SysChunk sysChunk);
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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysChunkByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param chunkId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSysChunkById(Long chunkId);
}
