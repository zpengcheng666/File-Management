package com.aspi.docmanage.service;

import com.aspi.docmanage.domain.AttachmentApproval;
import com.aspi.docmanage.domain.DocBehavior;
import com.oaspi.common.core.domain.entity.SysUser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.DocInfo;
import com.aspi.docmanage.web.api.vo.ArchiveStatusVO;
import com.aspi.docmanage.web.api.vo.DelayVO;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.vo.FileDocVo;
import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 档案信息Service接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface IDocInfoService
{
    /**
     * 查询档案信息
     *
     * @param id 档案信息主键
     * @return 档案信息
     */
    public DocInfo selectDocInfoById(Long id, SysUser sysUser);

    public DocInfo selectDocInfoByOri(String oriDocId);

    /**
     * 档案行为列表
     */
    List<DocBehavior> queryDocBehaviorList(DocBehavior docBehavior);

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
    public int insertDocInfo(DocInfo docInfo) ;

    /**
     * 修改档案信息
     * 
     * @param docInfo 档案信息
     * @return 结果
     */
    public int updateDocInfo(DocInfo docInfo);

    /**
     * 批量删除档案信息
     * 
     * @param ids 需要删除的档案信息主键集合
     * @return 结果
     */
    public int deleteDocInfoByIds(Long[] ids);

    /**
     * 删除档案信息信息
     * 
     * @param id 档案信息主键
     * @return 结果
     */
    public int deleteDocInfoById(Long id);

    /**
     * 根据门类id查询档案信息列表
     * @param categoryId
     * @return
     */
//    List<DocInfo> getByCategoryId(Long categoryId,Long docType);

    /**
     * 查询待上架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    public List<DocInfo> shelvesUpStopList(DocInfo docInfo);

    /**
     * 查询上架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    public List<DocInfo> shelvesUpList(DocInfo docInfo);

    /**
     * 查询下架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    public List<DocInfo> shelvesDownList(DocInfo docInfo);

    /**
     * 查询待下架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    public List<DocInfo> shelvesDownStopList(DocInfo docInfo);


    /**
     * 清空档案盒
     * @param docInfo
     * @return 结果
     */
    public AjaxResult delShelves(DocInfo docInfo);

    /**
     * 上架
     * @param docInfo
     * @return 结果
     */
    public AjaxResult shelvesUp(DocInfo docInfo);
    /**
     * 下架
     * @param docInfo
     * @return 结果
     */
    public AjaxResult shelvesDown(DocInfo docInfo);

    /**
     * 导入档案信息
     * @param userList
     * @return
     */
    String importDocInfo(List<DocInfo> userList);

    /**
     * 档案归档操作
     * @param vo
     * @return
     */
    int updateArchiveStatus(ArchiveStatusVO vo);

    /**
     * 导入上架模板
     * @param file
     * @return
     */
    AjaxResult importShelvesUp(MultipartFile file);

    /**
     * 导入下架模板
     * @param file
     * @return
     */
    AjaxResult importShelvesDown(MultipartFile file);

    List<FileDocVo> queryGetByDocInfoId(Long[] docIds);

    // List<DocInfo> selectDocInfoListArchive(DocInfo docInfo, SysUser sysUser);

    List<DocInfo> listWait(DocInfo docInfo);

    List<DocInfo> docManage(Long categoryId, Long type, Integer archiveStatus, SysUser sysUser);

    List<DocInfo> selectDocInfoIds(Long[] longs);

    /**
     * 档案管理,根据档案信息ids查询电子文件信息上传列表
     * @param longArray
     * @return
     */
    List<FileDocVo> queryGetByDocManageInfoId(Long[] longArray);

    /**
     * 批量下载文件生成压缩包
     *
     * @param longArray
     */
    List<SysUpFile> queryVolumeFile(Long[] longArray,HttpServletResponse response) throws IOException, MinioException;

    /**
     * 档案鉴定销毁
     * @param docInfo
     * @return
     */
    int updateDocInfoStatus(DocInfo docInfo);

    /**
     * 档案鉴定-保管到期列表
     * @param docInfo
     * @return
     */
    List<DocInfo> authenticateExpire(DocInfo docInfo);

    /**
     * 发起鉴定延期流程,状态为20审批中
     *
     * @param delayVO
     * @return
     */
    void updateStatus(DelayVO delayVO);

    /**
     * 驳回鉴定延期，状态30
     *
     * @param vo
     * @return
     */
    void updateStatusByBack(DelayVO vo);

    /**
     * 审批档案延期流程,审批通过状态为100
     * @param vo
     * @return
     */
    void updateStatusPass(DelayVO vo,Map<String, Object> variables);

    /**
     * 档案销毁,代办销毁,彻底销毁
     */
    int updateDocInfoStatusFull(DocInfo docInfo);

    /**
     * 档案鉴定-保管鉴定页签
     */
    List<DocInfo> authenticateApprove(DocInfo docInfo);

    /**
     * 审批状态统计
     * @return
     */
    List<DocInfo> selectDocInfoListTotal();

    /**
     * 附件审批结果
     * @return
     */
    String approvalResult(Long userId, Long[] attachmentIds);

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
    void expireApprovalResult() throws Exception;

    /**
     * 查看我申请过的附件列表
     * @return
     */
    List<AttachmentApproval> myRequest(AttachmentApproval attachmentApproval) throws Exception;

    /**
     * 等待我审批的附件审批列表
     * @return
     */
    List<AttachmentApproval> myTask(AttachmentApproval attachmentApproval) throws Exception;


    /**
     * 回收站删除档案信息
     */
    void recycleRemoveByIds(Long[] ids);

    /**
     * 回收站根据档案id查询附件列表
     */
    List<FileDocVo> queryGetByRecycleInfoId(Long[] array);

    /**
     * 回收站档案信息还原
     */
    void updateDocInfoRestore(Long[] ids);
}
