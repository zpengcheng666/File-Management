package com.aspi.docmanage.mapper;

import java.util.List;
import com.aspi.docmanage.domain.DocIdentifyLog;

/**
 * 鉴定日志Mapper接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface DocIdentifyLogMapper 
{
    /**
     * 查询鉴定日志
     * 
     * @param id 鉴定日志主键
     * @return 鉴定日志
     */
    public DocIdentifyLog selectDocIdentifyLogById(Long id);

    /**
     * 查询鉴定日志列表
     * 
     * @param docIdentifyLog 鉴定日志
     * @return 鉴定日志集合
     */
    public List<DocIdentifyLog> selectDocIdentifyLogList(DocIdentifyLog docIdentifyLog);

    /**
     * 新增鉴定日志
     * 
     * @param docIdentifyLog 鉴定日志
     * @return 结果
     */
    public int insertDocIdentifyLog(DocIdentifyLog docIdentifyLog);

    /**
     * 修改鉴定日志
     * 
     * @param docIdentifyLog 鉴定日志
     * @return 结果
     */
    public int updateDocIdentifyLog(DocIdentifyLog docIdentifyLog);

    /**
     * 删除鉴定日志
     * 
     * @param id 鉴定日志主键
     * @return 结果
     */
    public int deleteDocIdentifyLogById(Long id);

    /**
     * 批量删除鉴定日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocIdentifyLogByIds(Long[] ids);
}
