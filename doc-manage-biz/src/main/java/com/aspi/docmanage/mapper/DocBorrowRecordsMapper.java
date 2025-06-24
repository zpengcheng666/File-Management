package com.aspi.docmanage.mapper;

import java.util.List;
import com.aspi.docmanage.domain.DocBorrowRecords;

/**
 * 借阅记录Mapper接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface DocBorrowRecordsMapper 
{
    /**
     * 查询借阅记录
     * 
     * @param borrowId 借阅记录主键
     * @return 借阅记录
     */
    public DocBorrowRecords selectDocBorrowRecordsByBorrowId(Long borrowId);

    /**
     * 查询借阅记录列表
     * 
     * @param docBorrowRecords 借阅记录
     * @return 借阅记录集合
     */
    public List<DocBorrowRecords> selectDocBorrowRecordsList(DocBorrowRecords docBorrowRecords);

    /**
     * 我的收藏查询
     *
     * @param docBorrowRecords 借阅记录
     * @return 借阅记录集合
     */
    public List<DocBorrowRecords> collectionTypeList(DocBorrowRecords docBorrowRecords);


    /**
     * 新增借阅记录
     * 
     * @param docBorrowRecords 借阅记录
     * @return 结果
     */
    public int insertDocBorrowRecords(DocBorrowRecords docBorrowRecords);

    /**
     * 修改借阅记录
     * 
     * @param docBorrowRecords 借阅记录
     * @return 结果
     */
    public int updateDocBorrowRecords(DocBorrowRecords docBorrowRecords);
    public int updApprove(DocBorrowRecords docBorrowRecords);
    public int updReturn(DocBorrowRecords docBorrowRecords);
    public int updRenew(DocBorrowRecords docBorrowRecords);

    public String selUserName(String userId);

    public int selPostId(String userId);
    public String selDeptId(String userId);
    public String selDept(String deptId);
    public Long selBorrowId(Long docId);

    /**
     * 删除借阅记录
     * 
     * @param borrowId 借阅记录主键
     * @return 结果
     */
    public int deleteDocBorrowRecordsByBorrowId(Long borrowId);

    /**
     * 批量删除借阅记录
     * 
     * @param borrowIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocBorrowRecordsByBorrowIds(Long[] borrowIds);

    /**
     * 加入收藏
     *
     * @param docBorrowRecords 档案架
     * @return 结果
     */
    public int updCollectionType(DocBorrowRecords docBorrowRecords);

    /**
     * 移除收藏
     *
     * @param docBorrowRecords 档案架
     * @return 结果
     */
    public int delCollectionType(DocBorrowRecords docBorrowRecords);


    /**
     * 借阅待审批查询
     * @return 结果
     */
    public List<DocBorrowRecords> archivesApplovalList();
    public List<DocBorrowRecords> selarchivesApplovalList(DocBorrowRecords docBorrowRecords);
    public List<DocBorrowRecords> selDocInfoList(DocBorrowRecords docBorrowRecords);

    /**
     * 借阅审批查询
     * @return 结果
     */
    public List<DocBorrowRecords> archivesApploval();

    public List<DocBorrowRecords> beReturnList(DocBorrowRecords docBorrowRecords);
    public List<DocBorrowRecords> returnList(DocBorrowRecords docBorrowRecords);
    public List<DocBorrowRecords> extended(DocBorrowRecords docBorrowRecords);

    /**
     * 查询是否借阅
     * @return 结果
     */
    public int selArchives(DocBorrowRecords docBorrowRecords);
}
