package com.oaspi.system.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.text.Convert;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.system.domain.SysChunk;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.vo.CheckSysChunkVO;
import com.oaspi.system.mapper.SysChunkMapper;
import com.oaspi.system.service.ISysChunkService;
import com.oaspi.system.service.ISysUpFileService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-10
 */
@Service
@Transactional
public class SysChunkServiceImpl implements ISysChunkService
{
    private static final Logger log = LoggerFactory.getLogger(SysChunkServiceImpl.class);

    @Autowired
    private SysChunkMapper sysChunkMapper;

    @Autowired
    private ISysUpFileService sysUpFileService;

    @Value("${oaspi.profile}")
    private String filePath;

    private final static String folderPath = "/chunk";

    /**
     * 每一个上传块都会包含如下分块信息：
     * chunkNumber: 当前块的次序，第一个块是 1，注意不是从 0 开始的。
     * totalChunks: 文件被分成块的总数。
     * chunkSize: 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大。
     * currentChunkSize: 当前块的大小，实际大小。
     * totalSize: 文件总大小。
     * identifier: 这个就是每个文件的唯一标示,md5码
     * fileName: 文件名。
     * relativePath: 文件夹上传的时候文件的相对路径属性。
     * 一个分块可以被上传多次，当然这肯定不是标准行为，，这种重传也但是在实际上传过程中是可能发生这种事情的是本库的特性之一。
     */
    @Override
    public boolean postFileUpload(SysChunk sysChunk, HttpServletResponse response) {
        boolean flag = false;
        MultipartFile file = sysChunk.getFile();
        log.debug(() -> String.format("file originName: %s, chunkNumber: %d", file.getName(), sysChunk.getChunkNumber()));
        Path path = Paths.get(generatePath(filePath + folderPath, sysChunk));
        try {
            Files.write(path, sysChunk.getFile().getBytes());
            log.debug(() -> String.format("文件 %s 写入成功, md5:%s", sysChunk.getFilename(), sysChunk.getIdentifier()));
            sysChunkMapper.insertSysChunk(sysChunk);
            flag = true;
            //写入数据库
        } catch (IOException e) {
            throw new RuntimeException("上传失败" + e.getMessage());
        }
        return flag;
    }

    @Override
    public CheckSysChunkVO getFileUpload(SysChunk sysChunk, HttpServletResponse response) {

        // 检查该文件是否存在于fileList中吗,直接返回skipUpload为true,执行闪传
        CheckSysChunkVO checkSysChunkVO = new CheckSysChunkVO();
        String identifier = sysChunk.getIdentifier();

        // 先查询文件分片管理表和已上传文件记录表
        List<SysChunk> sysChunkList = sysChunkMapper.selectSysChunkList(sysChunk);
//        SysUpFile sysUpFile = new SysUpFile();
//        sysUpFile.setMd5(sysChunk.getIdentifier());
//        List<SysUpFile> sysFilelistList = sysUpFileService.selectSysUpFileList(sysUpFile);
//
//        // 检查文件中是否存在于sysFilelistList中
//        if (sysFilelistList != null && !sysFilelistList.isEmpty()) {
//            checkSysChunkVO.setSkipUpload(true);
//        }

        if (sysChunkList != null && !sysChunkList.isEmpty()&&sysChunkList.size()==sysChunk.getTotalChunks()){
            checkSysChunkVO.setSkipUpload(true);
        }

            // 获取已存在的块的chunkNumber列表并返回给前端
        if (sysChunkList != null && !sysChunkList.isEmpty()) {
            List<Long> uploadedChunks = sysChunkList.stream()
                    .map(SysChunk::getChunkNumber)
                    .collect(Collectors.toList());
            checkSysChunkVO.setUploads(uploadedChunks);
        }

        return checkSysChunkVO;

    }

    /**
     * 合并请求
     * @param sysChunk 已上传文件实体
     * @return 合并是否成功
     */
    @Override
    public String mergeFile(SysChunk sysChunk) {
        // 获取文件的名称
        String fileName = sysChunk.getFilename();
        String file = filePath + folderPath + "/" + sysChunk.getIdentifier() + "/" + fileName;
        String folder = filePath + folderPath + "/" + sysChunk.getIdentifier();
        String url = folderPath + "/" + sysChunk.getIdentifier() + "/" + fileName;
        merge(file, folder, fileName);

        // 插入文件记录成功后,删除chunk表中的对应记录,释放空间

        List<SysChunk> lstSysChunk = sysChunkMapper.selectSysChunkList(sysChunk);
        for (SysChunk sysChunk1 : lstSysChunk){
            sysChunkMapper.deleteSysChunkById(sysChunk1.getChunkId());
        }
        return url;


    }

    /**
     * 合并请求
     * @param sysFilelist 已上传文件实体
     * @return 合并是否成功
     */
    //@Override
    public String mergeFile1(SysUpFile sysFilelist) {
        // 获取文件的名称
        String fileName = sysFilelist.getName();
        String file = filePath + folderPath + "/" + sysFilelist.getMd5() + "/" + fileName;
        String folder = filePath + folderPath + "/" + sysFilelist.getMd5();
        String url = folderPath + "/" + sysFilelist.getMd5() + "/" + fileName;
        merge(file, folder, fileName);

        //当前文件已存在数据库中时,返回已存在标识
        List<SysUpFile> lst = sysUpFileService.selectSysUpFileList(sysFilelist);


        if (lst.size() > 0) {
            return url;
        }

        sysFilelist.setPath(file);
        sysFilelist.setUrl(url);
        sysFilelist.setCreateTime(new Date());
        sysFilelist.setCreateBy(SecurityUtils.getUsername());

        int flag = sysUpFileService.insertSysUpFile(sysFilelist);
        if (flag>0) {

            // 插入文件记录成功后,删除chunk表中的对应记录,释放空间
            SysChunk sysChunk = new SysChunk();
            sysChunk.setFilename(sysFilelist.getName());
            sysChunk.setIdentifier(sysFilelist.getMd5());
            List<SysChunk> lstSysChunk = sysChunkMapper.selectSysChunkList(sysChunk);
            for (SysChunk sysChunk1 : lstSysChunk){
                sysChunkMapper.deleteSysChunkById(sysChunk1.getChunkId());
            }


        }
        return url;


    }

    /**
     * 生成块文件所在地址
     */
    private String generatePath(String uploadFolder, SysChunk sysChunk) {
        StringBuilder stringBuilder = new StringBuilder();
        // 文件夹地址md5
        stringBuilder.append(uploadFolder).append("/").append(sysChunk.getIdentifier());
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(stringBuilder.toString()))) {
            try {
                Files.createDirectories(Paths.get(stringBuilder.toString()));
            } catch (IOException e) {
                log.error(() -> "生成时出现问题: " + e.getMessage(), e);
                throw new RuntimeException("生成时出现问题" + e.getMessage());
            }
        }

        //文件夹地址/md5/文件名-1
        return stringBuilder.append("/")
                .append(sysChunk.getFilename())
                .append("-")
                .append(sysChunk.getChunkNumber()).toString();
    }

    /**
     * 文件合并
     *
     * @param targetFile 要形成的文件名
     * @param folder     要形成的文件夹地址
     * @param fileName   文件的名称
     */
    public static void merge(String targetFile, String folder, String fileName) {
//        try {
//            Files.createFile(Paths.get(targetFile));
//            Files.list(Paths.get(folder))
//                    .filter(path -> !path.getFileName().toString().equals(fileName))
//                    .sorted((o1, o2) -> {
//                        String p1 = o1.getFileName().toString();
//                        String p2 = o2.getFileName().toString();
//                        int i1 = p1.lastIndexOf("-");
//                        int i2 = p2.lastIndexOf("-");
//                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
//                    })
//                    .forEach(path -> {
//                        try {
//                            // 以追加的形式写入文件
//                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
//                            // 合并后删除该块
//                            Files.delete(path);
//                        } catch (IOException e) {
//                            log.error(e.getMessage(), e);
//                        }
//                    });
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException("合并出现错误，" + e.getMessage());
//        }

        try {
            // 创建目标文件
            Files.createFile(Paths.get(targetFile));

            // 收集需要合并的文件路径并排序
            List<Path> filesToMerge = Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(fileName))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .collect(Collectors.toList());

            // 注释掉的逻辑中 是以追加的形式写入文件  这样做在传送大文件时会出现合并失败的情况
            // 我的推断：可能是因为每次循环迭代中都会执行文件的读取和写入操作，这种方式对于大文件来说效率不高，并且可能会导致内存不足或者文件操作异常
            // 所以这里使用缓冲流逐个合并文件, 不一次性读取整个文件内容，而是使用缓冲区逐段读取和写入，以降低内存使用量
            try (OutputStream out = Files.newOutputStream(Paths.get(targetFile), StandardOpenOption.APPEND)) {
                for (Path path : filesToMerge) {
                    try (InputStream in = Files.newInputStream(path)) {
                        byte[] buffer = new byte[8192]; // 8KB缓冲区
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                    // 合并后删除该块
                    Files.delete(path);
                }
            }

        } catch (IOException e) {
            log.error(() -> "合并出现错误：" + e.getMessage(), e);
            throw new RuntimeException("合并出现错误，" + e.getMessage());
        }
    }

    /**
     * 查询【请填写功能名称】
     *
     * @param chunkId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SysChunk selectSysChunkById(Long chunkId)
    {
        return sysChunkMapper.selectSysChunkById(chunkId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sysChunk 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysChunk> selectSysChunkList(SysChunk sysChunk)
    {
        return sysChunkMapper.selectSysChunkList(sysChunk);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param sysChunk 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysChunk(SysChunk sysChunk)
    {
        sysChunk.setCreateTime(DateUtils.getNowDate());
        return sysChunkMapper.insertSysChunk(sysChunk);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param sysChunk 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysChunk(SysChunk sysChunk)
    {
        sysChunk.setUpdateTime(DateUtils.getNowDate());
        return sysChunkMapper.updateSysChunk(sysChunk);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysChunkByIds(String ids)
    {
        return sysChunkMapper.deleteSysChunkByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param chunkId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSysChunkById(Long chunkId)
    {
        return sysChunkMapper.deleteSysChunkById(chunkId);
    }
}
