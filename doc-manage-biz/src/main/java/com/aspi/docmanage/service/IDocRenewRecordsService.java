package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocRenewRecords;

/**
 * 续借记录Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocRenewRecordsService 
{
    /**
     * 查询续借记录
     * 
     * @param renewId 续借记录主键
     * @return 续借记录
     */
    public DocRenewRecords selectDocRenewRecordsByRenewId(Long renewId);

    /**
     * 查询续借记录列表
     * 
     * @param docRenewRecords 续借记录
     * @return 续借记录集合
     */
    public List<DocRenewRecords> selectDocRenewRecordsList(DocRenewRecords docRenewRecords);

    /**
     * 新增续借记录
     * 
     * @param docRenewRecords 续借记录
     * @return 结果
     */
    public int insertDocRenewRecords(DocRenewRecords docRenewRecords);

    /**
     * 修改续借记录
     * 
     * @param docRenewRecords 续借记录
     * @return 结果
     */
    public int updateDocRenewRecords(DocRenewRecords docRenewRecords);

    /**
     * 批量删除续借记录
     * 
     * @param renewIds 需要删除的续借记录主键集合
     * @return 结果
     */
    public int deleteDocRenewRecordsByRenewIds(Long[] renewIds);

    /**
     * 删除续借记录信息
     * 
     * @param renewId 续借记录主键
     * @return 结果
     */
    public int deleteDocRenewRecordsByRenewId(Long renewId);
}
