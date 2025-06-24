package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocRenewRecordsMapper;
import com.aspi.docmanage.domain.DocRenewRecords;
import com.aspi.docmanage.service.IDocRenewRecordsService;

/**
 * 续借记录Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocRenewRecordsServiceImpl implements IDocRenewRecordsService 
{
    @Autowired
    private DocRenewRecordsMapper docRenewRecordsMapper;

    /**
     * 查询续借记录
     * 
     * @param renewId 续借记录主键
     * @return 续借记录
     */
    @Override
    public DocRenewRecords selectDocRenewRecordsByRenewId(Long renewId)
    {
        return docRenewRecordsMapper.selectDocRenewRecordsByRenewId(renewId);
    }

    /**
     * 查询续借记录列表
     * 
     * @param docRenewRecords 续借记录
     * @return 续借记录
     */
    @Override
    public List<DocRenewRecords> selectDocRenewRecordsList(DocRenewRecords docRenewRecords)
    {
        return docRenewRecordsMapper.selectDocRenewRecordsList(docRenewRecords);
    }

    /**
     * 新增续借记录
     * 
     * @param docRenewRecords 续借记录
     * @return 结果
     */
    @Override
    public int insertDocRenewRecords(DocRenewRecords docRenewRecords)
    {
        docRenewRecords.setCreateTime(DateUtils.getNowDate());
        return docRenewRecordsMapper.insertDocRenewRecords(docRenewRecords);
    }

    /**
     * 修改续借记录
     * 
     * @param docRenewRecords 续借记录
     * @return 结果
     */
    @Override
    public int updateDocRenewRecords(DocRenewRecords docRenewRecords)
    {
        docRenewRecords.setUpdateTime(DateUtils.getNowDate());
        return docRenewRecordsMapper.updateDocRenewRecords(docRenewRecords);
    }

    /**
     * 批量删除续借记录
     * 
     * @param renewIds 需要删除的续借记录主键
     * @return 结果
     */
    @Override
    public int deleteDocRenewRecordsByRenewIds(Long[] renewIds)
    {
        return docRenewRecordsMapper.deleteDocRenewRecordsByRenewIds(renewIds);
    }

    /**
     * 删除续借记录信息
     * 
     * @param renewId 续借记录主键
     * @return 结果
     */
    @Override
    public int deleteDocRenewRecordsByRenewId(Long renewId)
    {
        return docRenewRecordsMapper.deleteDocRenewRecordsByRenewId(renewId);
    }
}
