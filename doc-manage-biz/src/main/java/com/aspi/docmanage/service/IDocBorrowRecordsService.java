package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocBorrowRecords;
import com.aspi.docmanage.domain.DocShelves;
import com.oaspi.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 借阅记录Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocBorrowRecordsService 
{
    /**
     * 查询借阅记录
     * 
     * @param borrowId 借阅记录主键
     * @return 借阅记录
     */
    public DocBorrowRecords selectDocBorrowRecordsByBorrowId(Long borrowId);

    public DocBorrowRecords getInfo();


    /**
     * 查询借阅记录列表
     * 
     * @param docBorrowRecords 借阅记录
     * @return 借阅记录集合
     */
    public List<DocBorrowRecords> selectDocBorrowRecordsList(DocBorrowRecords docBorrowRecords);
    public List<DocBorrowRecords> collectionTypeList(DocBorrowRecords docBorrowRecords);

    /**
     * 新增借阅记录
     * 
     * @param docBorrowRecords 借阅记录
     * @return 结果
     */
    public AjaxResult insertDocBorrowRecords(DocBorrowRecords docBorrowRecords);

    /**
     * 修改借阅记录
     * 
     * @param docBorrowRecords 借阅记录
     * @return 结果
     */
    public int updateDocBorrowRecords(DocBorrowRecords docBorrowRecords);

    /**
     * 批量删除借阅记录
     * 
     * @param borrowIds 需要删除的借阅记录主键集合
     * @return 结果
     */
    public int deleteDocBorrowRecordsByBorrowIds(Long[] borrowIds);

    /**
     * 删除借阅记录信息
     * 
     * @param borrowId 借阅记录主键
     * @return 结果
     */
    public int deleteDocBorrowRecordsByBorrowId(Long borrowId);


    /**
     * 导入借阅单
     * @param file
     * @return 结果
     */
    AjaxResult importArchives(MultipartFile file);

    /**
     * 加入我的收藏
     * @param docBorrowRecords
     * @return 结果
     */
    public AjaxResult updCollectionType(DocBorrowRecords docBorrowRecords);
    public AjaxResult delCollectionType(DocBorrowRecords docBorrowRecords);

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

    /**
     * 同意
     *
     * @param docBorrowRecords
     * @return 结果
     */
    public AjaxResult approve(DocBorrowRecords docBorrowRecords);


    /**
     * 归还
     *
     * @param docBorrowRecords
     * @return 结果
     */
    public AjaxResult returnDoc(DocBorrowRecords docBorrowRecords);

    /**
     * 导入归还
     */
    AjaxResult importReturn(MultipartFile file);

    AjaxResult importRenew(MultipartFile file);

    /**
     * 归还
     *
     * @param docBorrowRecords
     * @return 结果
     */
    public AjaxResult renew(DocBorrowRecords docBorrowRecords);

    /**
     * 查询待归还记录
     *
     * @param docBorrowRecords
     * @return 结果
     */
    public List<DocBorrowRecords> beReturnList(DocBorrowRecords docBorrowRecords);
    public List<DocBorrowRecords> returnList(DocBorrowRecords docBorrowRecords);
    public List<DocBorrowRecords> extended(DocBorrowRecords docBorrowRecords);
    /**
     * 查询是否借阅
     */
    public AjaxResult selArchives(DocBorrowRecords docBorrowRecords);
}
