package com.oaspi.system.service;

import java.io.IOException;
import java.util.List;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.vo.FileDocVo;
import com.oaspi.system.domain.vo.SysUpFileVO;
import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件Service接口
 * 
 * @author hongy
 * @date 2024-10-03
 */
public interface ISysUpFileService 
{
    /**
     * 查询上传文件
     * 
     * @param id 上传文件主键
     * @return 上传文件
     */
    public SysUpFile selectSysUpFileById(Long id);

    /**
     * 查询上传文件列表
     * 
     * @param sysUpFile 上传文件
     * @return 上传文件集合
     */
    public List<SysUpFile> selectSysUpFileList(SysUpFile sysUpFile);

    /**
     * 新增上传文件
     * 
     * @param sysUpFile 上传文件
     * @return 结果
     */
    public int insertSysUpFile(SysUpFile sysUpFile);

    /**
     * 修改上传文件
     * 
     * @param sysUpFile 上传文件
     * @return 结果
     */
    public int updateSysUpFile(SysUpFile sysUpFile);

    /**
     * 批量删除上传文件
     * 
     * @param ids 需要删除的上传文件主键集合
     * @return 结果
     */
    public int deleteSysUpFileByIds(Long[] ids);

    /**
     * 删除上传文件信息
     * 
     * @param id 上传文件主键
     * @return 结果
     */
    public int deleteSysUpFileById(Long id);

    List<SysUpFile> uploadFile(MultipartFile file, String username) throws IOException, MinioException;

    List<SysUpFile> uploadFile(MultipartFile file, String bizType, String bizValue, String username) throws IOException, MinioException;

    /**
     * 根据档案信息docIds查询电子文件信息上传列表
     * @param docIds
     */
    List<FileDocVo> selectgetByDocInfoId(Long[] docIds);

    /**
     * 修改电子文件信息vo
     * @param vo
     * @return
     */
    int updateSysUpFileVO(SysUpFileVO vo);

    /**
     * 散文件查询列表
     * @return
     */
    List<SysUpFile> selectScatterFileList();

    /**
     * 根据移交列表id查询对应附件信息
     */
    List<SysUpFile> turnIdByAttachmentList(Long turnId);
}
