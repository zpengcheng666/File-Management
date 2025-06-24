package com.oaspi.system.mapper;

import java.util.List;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.vo.FileDocVo;
import com.oaspi.system.domain.vo.SysUpFileVO;

/**
 * 上传文件Mapper接口
 *
 * @author hongy
 * @date 2024-10-03
 */
public interface SysUpFileMapper
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
     * 删除上传文件
     *
     * @param id 上传文件主键
     * @return 结果
     */
    public int deleteSysUpFileById(Long id);

    /**
     * 批量删除上传文件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUpFileByIds(Long[] ids);

    /**
     * 根据档案信息docIds查询电子文件信息上传列表
     * @param docIds
     */
    List<FileDocVo> selectGetByDocInfoId(Long[] docIds);

    /**
     * 修改电子文件信息vo
     * @param vo
     * @return
     */
    int updateSysUpFileVO(SysUpFileVO vo);

    /**
     * 档案管理,根据档案信息ids查询电子文件信息上传列表
     */
    List<FileDocVo> selectGetByDocManageInfoId(Long[] longArray);

    /**
     * 散文件查询列表
     * @return
     */
    List<SysUpFile> selectScatterFileList();

    List<SysUpFile> queryVolumeFile(Long[] longArray);

    List<FileDocVo> queryGetByRecycleInfoId(Long[] array);

    /**
     * 根据移交列表id查询对应附件信息
     */
    List<SysUpFile> turnIdByAttachmentList(Long turnId);

    Long getNewNameByDocInfoId(String newName);

    Long[] selectFileIdbyNewName(String newName);
}
