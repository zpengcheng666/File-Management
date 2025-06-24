package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocIdentifyLogMapper;
import com.aspi.docmanage.domain.DocIdentifyLog;
import com.aspi.docmanage.service.IDocIdentifyLogService;

/**
 * 鉴定日志Service业务层处理
 * 
 * @author hongy
 * @date 2024-11-02
 */
@Service
public class DocIdentifyLogServiceImpl implements IDocIdentifyLogService 
{
    @Autowired
    private DocIdentifyLogMapper docIdentifyLogMapper;

    /**
     * 查询鉴定日志
     * 
     * @param id 鉴定日志主键
     * @return 鉴定日志
     */
    @Override
    public DocIdentifyLog selectDocIdentifyLogById(Long id)
    {
        return docIdentifyLogMapper.selectDocIdentifyLogById(id);
    }

    /**
     * 查询鉴定日志列表
     * 
     * @param docIdentifyLog 鉴定日志
     * @return 鉴定日志
     */
    @Override
    public List<DocIdentifyLog> selectDocIdentifyLogList(DocIdentifyLog docIdentifyLog)
    {
        return docIdentifyLogMapper.selectDocIdentifyLogList(docIdentifyLog);
    }

    /**
     * 新增鉴定日志
     * 
     * @param docIdentifyLog 鉴定日志
     * @return 结果
     */
    @Override
    public int insertDocIdentifyLog(DocIdentifyLog docIdentifyLog)
    {
        docIdentifyLog.setCreateTime(DateUtils.getNowDate());
        return docIdentifyLogMapper.insertDocIdentifyLog(docIdentifyLog);
    }

    /**
     * 修改鉴定日志
     * 
     * @param docIdentifyLog 鉴定日志
     * @return 结果
     */
    @Override
    public int updateDocIdentifyLog(DocIdentifyLog docIdentifyLog)
    {
        docIdentifyLog.setUpdateTime(DateUtils.getNowDate());
        return docIdentifyLogMapper.updateDocIdentifyLog(docIdentifyLog);
    }

    /**
     * 批量删除鉴定日志
     * 
     * @param ids 需要删除的鉴定日志主键
     * @return 结果
     */
    @Override
    public int deleteDocIdentifyLogByIds(Long[] ids)
    {
        return docIdentifyLogMapper.deleteDocIdentifyLogByIds(ids);
    }

    /**
     * 删除鉴定日志信息
     * 
     * @param id 鉴定日志主键
     * @return 结果
     */
    @Override
    public int deleteDocIdentifyLogById(Long id)
    {
        return docIdentifyLogMapper.deleteDocIdentifyLogById(id);
    }
}
