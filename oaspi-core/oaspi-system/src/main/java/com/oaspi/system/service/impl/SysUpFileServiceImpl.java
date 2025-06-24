package com.oaspi.system.service.impl;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.oaspi.common.config.OaspiConfig;
import com.oaspi.common.exception.UtilException;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.file.FileUploadUtils;
import com.oaspi.common.utils.file.FileUtils;
import com.oaspi.common.utils.uuid.UUID;
import com.oaspi.system.domain.DocAttachments;
import com.oaspi.system.domain.vo.FileDocVo;
import com.oaspi.system.domain.vo.SysUpFileVO;
import com.oaspi.system.mapper.DocAttachmentsMapper;
import com.oaspi.system.util.MinioUtil;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oaspi.system.mapper.SysUpFileMapper;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.service.ISysUpFileService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件Service业务层处理
 *
 * @author hongy
 * @date 2024-10-03
 */
@Slf4j
@Service
public class SysUpFileServiceImpl implements ISysUpFileService
{
    @Autowired
    private SysUpFileMapper sysUpFileMapper;
    @Autowired
    private DocAttachmentsMapper docAttachmentsMapper;

    @Autowired
    private MinioUtil minioUtil;

    private final AtomicInteger sequenceNumber = new AtomicInteger(0);

    /**
     * 查询上传文件
     *
     * @param id 上传文件主键
     * @return 上传文件
     */
    @Override
    public SysUpFile selectSysUpFileById(Long id)
    {
        return sysUpFileMapper.selectSysUpFileById(id);
    }

    /**
     * 查询上传文件列表
     *
     * @param sysUpFile 上传文件
     * @return 上传文件
     */
    @Override
    public List<SysUpFile> selectSysUpFileList(SysUpFile sysUpFile)
    {
        return sysUpFileMapper.selectSysUpFileList(sysUpFile);
    }

    /**
     * 新增上传文件
     *
     * @param sysUpFile 上传文件
     * @return 结果
     */
    @Override
    public int insertSysUpFile(SysUpFile sysUpFile)
    {
        sysUpFile.setCreateTime(DateUtils.getNowDate());
        sysUpFile.setDownTicket(UUID.randomUUID().toString().replace("-", ""));
//        sysUpFile.setUrl("/common/download/sysupfile/" + sysUpFile.getDownTicket());
        return sysUpFileMapper.insertSysUpFile(sysUpFile);
    }

    /**
     * 修改上传文件
     *
     * @param sysUpFile 上传文件
     * @return 结果
     */
    @Override
    public int updateSysUpFile(SysUpFile sysUpFile)
    {
        sysUpFile.setUpdateTime(DateUtils.getNowDate());
        return sysUpFileMapper.updateSysUpFile(sysUpFile);
    }

    /**
     * 批量删除上传文件
     *
     * @param ids 需要删除的上传文件主键
     * @return 结果
     */
    @Override
    public int deleteSysUpFileByIds(Long[] ids)
    {
        return sysUpFileMapper.deleteSysUpFileByIds(ids);
    }

    /**
     * 删除上传文件信息
     *
     * @param id 上传文件主键
     * @return 结果
     */
    @Override
    public int deleteSysUpFileById(Long id)
    {
        return sysUpFileMapper.deleteSysUpFileById(id);
    }

    @Override
    public List<SysUpFile> uploadFile(MultipartFile file, String username) throws IOException, MinioException {
        return uploadFile(file, "Common", null, username);
    }

    @Override
    public List<SysUpFile> uploadFile(MultipartFile file, String bizType, String bizValue, String username) throws IOException, MinioException {
        String fileName = file.getOriginalFilename();
        // 检查是否为ZIP文件
        if (FilenameUtils.getExtension(fileName) != null && FilenameUtils.getExtension(fileName).equalsIgnoreCase("zip")) {
            return handleZipFile(file, bizType, bizValue, username);
        }
        // 非压缩包，单文件上传
        return handleSingleFile(file, bizType, bizValue, username);
    }

    /**
     * 处理ZIP文件
     */
    private List<SysUpFile> handleZipFile(MultipartFile file, String bizType, String bizValue, String username) throws IOException, MinioException {
        List<SysUpFile> sysUpFiles = new ArrayList<>();
        String newName;
        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream(), Charset.forName("GBK"))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // minio中创建"文件夹"对象
                    minioUtil.createDirectoryInMinIO(minioUtil.getBucketName(), entryName);
                }
                else {
                    // 读取文件内容到ByteArrayInputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zipInputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }

                    // 上传文件到MinIO
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    minioUtil.uploadToMinIO(bais, minioUtil.getBucketName(), entryName);

                    SysUpFile sysUpFile = new SysUpFile();
                    String fileName = FilenameUtils.getName(entryName);
                    String relativePath;
                    String md5;

                    if (minioUtil.isEnable()) {
                        ByteArrayInputStream mdIs = new ByteArrayInputStream(baos.toByteArray());
                        md5 = DigestUtils.md5Hex(mdIs);
                        sysUpFile.setUrl(minioUtil.getBucketName() + MinioUtil.URL_SEPRATOR + entryName);
                        sysUpFile.setStoreType(SysUpFile.STORE_TYPE_MINIO);
                    } else {
                        String absPath = FileUploadUtils.getUploadAbsPath(OaspiConfig.getUploadPath(), file);
                        File newFile = new File(absPath, fileName);
                        relativePath = OaspiConfig.getUploadPath() + File.separator + bizType;
                        relativePath = FileUtils.resolveRelativePath(newFile.getAbsolutePath(), relativePath);
                        try (FileOutputStream fos = new FileOutputStream(newFile)) {
                            fos.write(baos.toByteArray());
                        }
                        sysUpFile.setUrl(absPath);
                        sysUpFile.setStoreType(SysUpFile.STORE_TYPE_LOCAL);
                        md5 = DigestUtils.md5Hex(Files.newInputStream(Paths.get(absPath)));
                    }
                    /*将文件名称截取最后一个"-"之前的字符，作为档案信息中的档案号，也是其档案号对应生成的目录*/
                    newName = fileName.substring(0, fileName.lastIndexOf("-"));

                    sysUpFile.setPath(entryName);
                    sysUpFile.setName(fileName);
                    sysUpFile.setOriName(file.getOriginalFilename());
                    sysUpFile.setNewName(newName);
                    sysUpFile.setSize((long) baos.size());
                    sysUpFile.setBizType(bizType);
                    sysUpFile.setBizId(bizValue);
                    sysUpFile.setCreateBy(username);
                    sysUpFile.setType(FilenameUtils.getExtension(fileName));
                    sysUpFile.setMd5(md5);
                    sysUpFile.setPages(1);
                    sysUpFile.setCreateTime(DateUtils.getNowDate());
                    sysUpFile.setDownTicket(UUID.randomUUID().toString().replace("-", ""));
                    sysUpFileMapper.insertSysUpFile(sysUpFile);
                    sysUpFiles.add(sysUpFile);

                    // 插入关联关系，便于查询
                    insertDocAttachments(newName);
                }
                zipInputStream.closeEntry();//关闭资源
            }
        } catch (Exception e) {
            throw new RuntimeException("处理ZIP文件失败", e);
        }
        return sysUpFiles;
    }

    // 插入关联关系，便于查询
    private void insertDocAttachments(String newName) {
        // 修改批量插入关系表的逻辑
        Long docId = sysUpFileMapper.getNewNameByDocInfoId(newName);
        if (docId != null) {
            List<Long> existingAttachments = docAttachmentsMapper.selectAttachmentsByDocId(docId);
            Long[] fileIds = sysUpFileMapper.selectFileIdbyNewName(newName);
            // 只插入不存在的关联关系
            for (Long fileId : fileIds) {
                if (!existingAttachments.contains(fileId)) {
                    DocAttachments docAttachments = new DocAttachments();
                    docAttachments.setDocId(docId);
                    docAttachments.setAttachments(fileId);
                    docAttachmentsMapper.insertDocAttachments(docAttachments);
                }
            }
        }
    }

    /**
     * 处理单个文件
     */
    private List<SysUpFile> handleSingleFile(MultipartFile file, String bizType, String bizValue, String username) {
        List<SysUpFile> sysUpFiles = new ArrayList<>();
        SysUpFile sysUpFile = new SysUpFile();
        File tempFile = null;
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("上传文件是空！");
            }
            String fileName = file.getOriginalFilename();
            String relativePath = bizType;
            String md5;

            if (minioUtil.isEnable()) {
                try (InputStream is = file.getInputStream()) {
                    relativePath = minioUtil.putObjectSafe(fileName, is, file.getSize(), relativePath);
                    md5 = DigestUtils.md5Hex(is);
                    sysUpFile.setUrl(minioUtil.getBucketName() + MinioUtil.URL_SEPRATOR + relativePath);
                    sysUpFile.setStoreType(SysUpFile.STORE_TYPE_MINIO);
                }
            }
            else {
                String absPath = FileUploadUtils.getUploadAbsPath(OaspiConfig.getUploadPath(), file);
                tempFile = new File(absPath);

                // 确保目标目录存在
                File parentDir = tempFile.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs()) {
                    throw new IOException("创建目录失败: " + parentDir);
                }

                // 使用临时文件转移
                File tempUploadFile = File.createTempFile("upload_", "_temp");
                file.transferTo(tempUploadFile);
                md5 = DigestUtils.md5Hex(Files.newInputStream(tempUploadFile.toPath()));

                // 安全地移动到最终位置
                if (!tempUploadFile.renameTo(tempFile)) {
                    Files.move(tempUploadFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                // 清理临时上传文件
                Files.deleteIfExists(tempUploadFile.toPath());

                fileName = tempFile.getName();
                relativePath = OaspiConfig.getUploadPath() + File.separator + bizType;
                relativePath = FileUtils.resolveRelativePath(tempFile.getAbsolutePath(), relativePath);
                sysUpFile.setUrl(absPath);
                sysUpFile.setStoreType(SysUpFile.STORE_TYPE_LOCAL);
            }
            sysUpFile.setPath(relativePath);
            sysUpFile.setName(fileName);
            sysUpFile.setOriName(file.getOriginalFilename());
            sysUpFile.setNewName(FileUtils.getName(fileName));
            sysUpFile.setSize(file.getSize());
            sysUpFile.setBizType(bizType);
            sysUpFile.setBizId(bizValue);
            sysUpFile.setCreateBy(username);
            sysUpFile.setType(FilenameUtils.getExtension(fileName));
            sysUpFile.setMd5(md5);
            sysUpFile.setPages(1);

            insertSysUpFile(sysUpFile);
            sysUpFiles.add(sysUpFile);

            return sysUpFiles;
        } catch (Exception e) {
            // 发生异常时清理临时文件
            cleanupTempFile(tempFile);
            throw new UtilException("无法处理文件上传: " + e.getMessage(), e);
        } finally {
            // 清理MultipartFile资源
            if (file != null) {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.warn("无法关闭 multipart 文件输入流", e);
                }
            }
        }
    }

    /**
     * 清理临时文件
     */
    private void cleanupTempFile(File file) {
        if (file != null && file.exists()) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                log.warn("无法删除临时文件: " + file.getAbsolutePath(), e);
            }
        }
    }

    /**
     * 根据档案信息ids查询电子文件信息上传列表
     * @param docIds
     * @return
     */
    @Override
    public List<FileDocVo> selectgetByDocInfoId(Long[] docIds) {
        return sysUpFileMapper.selectGetByDocInfoId(docIds);
    }

    /**
     * 修改电子文件信息vo
     * @param vo
     * @return
     */
    @Override
    public int updateSysUpFileVO(SysUpFileVO vo) {
        return sysUpFileMapper.updateSysUpFileVO(vo);
    }

    /**
     * 散文件查询列表
     * @return
     */
    @Override
    public List<SysUpFile> selectScatterFileList() {
        return sysUpFileMapper.selectScatterFileList();
    }

    /**
     * 根据移交列表id查询对应附件信息
     */
    @Override
    public List<SysUpFile> turnIdByAttachmentList(Long turnId) {
        return sysUpFileMapper.turnIdByAttachmentList(turnId);
    }

}
