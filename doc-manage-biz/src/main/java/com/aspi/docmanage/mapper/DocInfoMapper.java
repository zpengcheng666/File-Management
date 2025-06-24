package com.aspi.docmanage.mapper;

import com.aspi.docmanage.domain.AttachmentApproval;
import com.aspi.docmanage.domain.DocBehavior;
import com.oaspi.common.core.domain.entity.SysUser;
import java.util.List;

import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.DocInfo;
import com.aspi.docmanage.web.api.vo.ArchiveStatusVO;

/**
 * 档案信息Mapper接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface DocInfoMapper 
{
    /**
     * 查询档案信息
     * 
     * @param id 档案信息主键
     * @return 档案信息
     */
    public DocInfo selectDocInfoById(Long id);

    public DocInfo selectDocInfoByOri(String oriDocId);

    /**
     * 查询档案信息列表
     * 
     * @param docInfo 档案信息
     * @return 档案信息集合
     */
    public List<DocInfo> selectDocInfoList(DocInfo docInfo);

    /**
     * 新增档案信息
     * 
     * @param docInfo 档案信息
     * @return 结果
     */
    public int insertDocInfo(DocInfo docInfo);

    /**
     * 修改档案信息
     * 
     * @param docInfo 档案信息
     * @return 结果
     */
    public int updateDocInfo(DocInfo docInfo);

    /**
     * 删除档案信息
     * 
     * @param id 档案信息主键
     * @return 结果
     */
    public int deleteDocInfoById(Long id);

    /**
     * 批量删除档案信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocInfoByIds(Long[] ids);

    /**
     * 根据分类id查询档案信息
     * @param categoryId
     * @return
     */
    List<DocInfo> queryByCategoryId(Long categoryId);

//    List<DocInfo> selectAllData();

    /**
     * 查询文本,科技门类相关列表
     * @param categoryId
     * @return
     */
    List<DocInfo> queryByText(Long categoryId,Long docType);

    /**
     * 查询照片相关列表
     * @param categoryId
     * @return
     */
    List<DocInfo> queryByPhoto(Long categoryId,Long docType);

    /**
     * 查询录音、录像门类相关列表
     * @param categoryId
     * @return
     */
    List<DocInfo> queryByRecord(Long categoryId,Long docType);

    /**
     * 查询实物门类相关列表
     * @param categoryId
     * @return
     */
    List<DocInfo> queryByMatter(Long categoryId,Long docType);

    /**
     * 查询待上架列表
     * @param docInfo
     * @return
     */
    public List<DocInfo> shelvesUpStopList(DocInfo docInfo);

    /**
     * 查询上架列表
     * @param docInfo
     * @return
     */
    public List<DocInfo> shelvesUpList(DocInfo docInfo);

    /**
     * 查询待下架列表
     * @param docInfo
     * @return
     */
    public List<DocInfo> shelvesDownList(DocInfo docInfo);

    /**
     * 查询下架列表
     * @param docInfo
     * @return
     */
    public List<DocInfo> shelvesDownStopList(DocInfo docInfo);

    /**
     * 清空档案盒
     *
     * @param docInfo
     * @return 结果
     */
    public int delShelvesInfo(DocInfo docInfo);

    /**
     * 上架
     *
     * @param docInfo
     * @return 结果
     */
    public int shelvesUp(DocInfo docInfo);

    /**
     * 下架
     *
     * @param docInfo
     * @return 结果
     */
    public int shelvesDown(DocInfo docInfo);

    /**
     * 档案归档操作
     * @param vo
     * @return
     */
    int updateArchiveStatus(ArchiveStatusVO vo);

    public String selId(DocInfo docInfo);

//    List<DocInfo> queryByDocTypeId(Long docType);
//
//    List<DocInfo> queryByText1(Long categoryId);
//
//    List<DocInfo> queryByPhoto1(Long categoryId);
//
//    List<DocInfo> queryByRecord1(Long categoryId);
//
//    List<DocInfo> queryByMatter1(Long categoryId);

//    List<DocInfo> selectDocInfoListArchive(DocInfo docInfo, SysUser sysUser);
//
//    List<DocInfo> queryDocManageByText(Long categoryId, Long docType);
//
//    List<DocInfo> queryDocManageByPhoto(Long categoryId, Long docType);
//
//    List<DocInfo> queryDocManageByRecord(Long categoryId, Long docType);
//
//    List<DocInfo> queryDocManageByMatter(Long categoryId, Long docType);
//
//    List<DocInfo> queryDocManageByDocTypeId(Long docType);
//
//    List<DocInfo> queryDocManageByText1(Long categoryId,Long type);
//
//    List<DocInfo> queryDocManageByPhoto1(Long categoryId,Long type);
//
//    List<DocInfo> queryDocManageByRecord1(Long categoryId,Long type);
//
//    List<DocInfo> queryDocManageByMatter1(Long categoryId,Long type);

    List<DocInfo> listWait(DocInfo docInfo);

    List<DocInfo> selectDocManageAllData(SysUser sysUser);

    List<DocInfo> selectDocInfoIds(Long[] longs);

    int updateDocInfoStatus(DocInfo docInfo);

    List<DocInfo> authenticateExpire(DocInfo docInfo);

    List<DocInfo> selectDocInfoListAll();

    void updateRemainingDays(DocInfo docInfo);

    /**
     * 新增档案行为
     *
     * @param docBehavior 档案信息
     * @rturn 结果
     */
    int insertDocBehavior(DocBehavior docBehavior);

    /**
     * 档案行为列表
     */
    List<DocBehavior> queryDocBehaviorList(DocBehavior docBehavior);

    /**
     * 发起鉴定延期流程,状态为20审批中
     * @param docInfo
     * @return
     */
    void updateStatus(DocInfo docInfo);

    /**
     * 驳回鉴定延期，状态30
     *
     * @param docInfo
     * @return
     */
    void updateStatusByBack(DocInfo docInfo);

    /**
     * 审批档案延期流程,审批通过状态为100
     * @param docInfo
     * @return
     */
    void updateStatusPass(DocInfo docInfo);

    int updateDocInfoStatusFull(DocInfo docInfo);

    List<DocInfo> selectDocInfoListTotal();

    /**
     * 附件审批结果
     * @return
     */
    Integer beforeApprovalResult(Long userId, Long attachmentId);
    Integer approvalResult(Long userId, Long attachmentId);
    Integer checkAlready(Long userId, Long attachmentId);

    /**
     * 添加附件审批
     * @return
     */
    void addApprovalResult(AttachmentApproval attachmentApproval);

    /**
     * 审批附件审批
     * @return
     */
    void updateApprovalResult(AttachmentApproval attachmentApproval);

    /**
     * 过期附件审批
     * @return
     */
    void expireApprovalResult(List<Long> ids);

    /**
     * 查看我申请过的附件列表
     * @return
     */
    List<AttachmentApproval> myRequest(AttachmentApproval attachmentApproval) throws Exception;

    /**
     * 等待我审批的附件审批列表
     * @return
     */
    List<AttachmentApproval> myTask(AttachmentApproval attachmentApproval);

    /**
     * 查询等待审批的附件列表
     * @return
     */
    List<AttachmentApproval> queryWaitList();


    /**
     * 回收站删除档案信息
     */
    void recycleRemoveByIds(Long[] ids);

    /**
     * 回收站档案信息还原
     */
    void updateDocInfoRestore(Long[] ids);

    List<DocInfo> selectDocOriId(String oriDocId);
}
