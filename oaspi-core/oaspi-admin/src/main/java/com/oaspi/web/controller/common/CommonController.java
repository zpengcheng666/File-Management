package com.oaspi.web.controller.common;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oaspi.common.core.domain.entity.SysDictData;
import com.oaspi.common.utils.DictUtils;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.file.FileUploadUtils;
import com.oaspi.common.utils.file.WatermarkUtils;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.service.ISysUpFileService;
import com.oaspi.system.util.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.oaspi.common.config.OaspiConfig;
import com.oaspi.common.constant.Constants;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.common.utils.file.FileUtils;

/**
 * 通用请求处理
 *
 * @author oaspi
 */
@Api(tags = "通用请求处理", value = "通用请求处理")
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    private static final String FILE_DELIMETER = ",";

    @Autowired
    private ISysUpFileService sysUpFileService;
    @Autowired
    private MinioUtil minioUtil;

    public CommonController(ISysUpFileService sysUpFileService, MinioUtil minioUtil) {
        this.sysUpFileService = sysUpFileService;
        this.minioUtil = minioUtil;
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    @Deprecated
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = OaspiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传请求（单个）", notes = "上传请求（单个）")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            List<SysUpFile> sysUpFile = sysUpFileService.uploadFile(file, SecurityUtils.getUsername());
//            String url = sysUpFile.getUrl();
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put("url", url);
//            ajax.put("fileName", sysUpFile.getName());
//            ajax.put("newFileName", sysUpFile.getNewName());
//            ajax.put("originalFilename", file.getOriginalFilename());
//            ajax.put("fileId", sysUpFile.getId());
//            ajax.put("downTicket", sysUpFile.getDownTicket());
            AjaxResult ajax = AjaxResult.success();
            for (SysUpFile upFile : sysUpFile) {
                ajax.put("urls", StringUtils.join(upFile.getPath(), FILE_DELIMETER));
                ajax.put("fileNames", StringUtils.join(upFile.getName(), FILE_DELIMETER));
                ajax.put("newFileNames", StringUtils.join(upFile.getName(), FILE_DELIMETER));
                ajax.put("originalFilenames", StringUtils.join(upFile.getOriName(), FILE_DELIMETER));
                ajax.put("fileIds", StringUtils.join(upFile.getId(), FILE_DELIMETER));
                ajax.put("downTickets", StringUtils.join(upFile.getDownTicket(), FILE_DELIMETER));
            }

            return AjaxResult.success(ajax);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 依据文件名上传至minio中
     */
    @PostMapping("/uploadByFileName")
    @ApiOperation(value = "依据文件名上传", notes = "依据文件名上传")
    public String uploadByFileName(List<MultipartFile> files) {
        String objectName = null;
        try {
            for (MultipartFile multipartFile : files) {
                // 获取文件名
                String originalFilename = multipartFile.getOriginalFilename();

                // 去掉文件名后缀
                String filenameWithoutExtension = Objects.requireNonNull(originalFilename).substring(0, originalFilename.lastIndexOf('.'));

                // 转换为目录路径
                String dirPath = FileUploadUtils.convertFileNameToPath("ZT40-WS·2023-Y-B-0001");

                // 构建完整的对象路径
                objectName = dirPath + "/" + originalFilename;

                // 上传文件到MinIO
                return minioUtil.putObjectSafe(objectName, multipartFile.getInputStream(), multipartFile.getSize(), "Common");
            }
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
        return objectName;
    }

    /**
     * 下载minio中对应的桶下目录中的文件保存为ZIP
     * @param directoryPath 桶下目录路径
     */
    @GetMapping("/downloadDirectoryAsZip")
    public void downloadDirectoryAsZip(String directoryPath, HttpServletResponse response) {
        try {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "downLoad.zip" + "\"");

            // 获取目录下所有文件, 如果目录为空则返回204
            List<String> files = minioUtil.listFiles(directoryPath);
            if (files.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            for (String filePath : files) {
                addFileToZip(filePath, zipOut);
            }
        } catch (Exception e) {
            log.error("Failed to create ZIP file for directory: {}", directoryPath, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 将文件添加到ZIP中，保持目录层级结构
     * @param filePath 桶下的文件路径
     * @param zipOut ZIP输出流
     */
    private void addFileToZip(String filePath, ZipOutputStream zipOut) {
        InputStream fileStream = null;
        try {
            // 获取桶内目录下的文件内容
            fileStream = minioUtil.getObject(filePath);
            if (fileStream == null) {
                log.warn("未找到文件: {}", filePath);
                return;
            }

            // 规范化文件路径，确保使用正斜杠
            String normalizedPath = filePath.replace("\\", "/");
            String fileExtension = FilenameUtils.getExtension(normalizedPath).toLowerCase();

            // 处理需要添加水印的文件类型
            InputStream processedStream = fileStream;
            if (isWatermarkRequired(fileExtension)) {
                processedStream = addWatermark(fileStream, fileExtension);
            }

            // 直接创建文件条目，ZIP会自动处理目录结构
            ZipEntry zipEntry = new ZipEntry(normalizedPath);
            zipOut.putNextEntry(zipEntry);

            // 写入文件内容
            byte[] buffer = new byte[8192];
            int len;
            while ((len = processedStream.read(buffer)) > 0) {
                zipOut.write(buffer, 0, len);
            }

            zipOut.closeEntry();
        } catch (Exception e) {
            log.error("添加ZIP失败: {}", filePath, e);
            throw new RuntimeException("添加ZIP失败", e);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    log.error("关闭流资源失败！", e);
                }
            }
        }
    }

    // 判断是否需要添加水印
    private boolean isWatermarkRequired(String fileExtension) {
        return Arrays.asList("jpg", "jpeg", "png", "pdf").contains(fileExtension);
    }

    // 添加水印处理
    private InputStream addWatermark(InputStream inputStream, String fileExtension) throws IOException {
        if (fileExtension.equals("pdf")) {
            return addPdfWatermark(inputStream);
        } else {
            return addImageWatermark(inputStream);
        }
    }

    // 为图片添加水印
    private InputStream addImageWatermark(InputStream inputStream) throws IOException {
        // 读取原始图片
        BufferedImage sourceImage = ImageIO.read(inputStream);
        BufferedImage watermarkedImage = WatermarkUtils.addWatermarkToImage(sourceImage);
        // 将处理后的图片转换为InputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(watermarkedImage, "png", baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    // 为PDF添加水印
    private InputStream addPdfWatermark(InputStream inputStream) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        WatermarkUtils.addWatermarkToPdf(document);

        // 将处理后的PDF转换为InputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 通用上传请求（多个）
     */
//    @PostMapping("/uploads")
//    @ApiOperation(value = "上传请求（多个）", notes = "上传请求（多个）")
//    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
//        try {
//            // 上传文件路径
//            String filePath = OaspiConfig.getUploadPath();
//            List<String> urls = new ArrayList<String>();
//            List<String> fileNames = new ArrayList<String>();
//            List<String> newFileNames = new ArrayList<String>();
//            List<String> originalFilenames = new ArrayList<String>();
//            List<String> fileIds = new ArrayList<String>();
//            List<String> downTickets = new ArrayList<String>();
//
//            for (MultipartFile file : files) {
//                SysUpFile sysUpFile = sysUpFileService.uploadFile(file, SecurityUtils.getUsername());
//                urls.add(sysUpFile.getUrl());
//                fileNames.add(sysUpFile.getName());
//                newFileNames.add(sysUpFile.getNewName());
//                originalFilenames.add(sysUpFile.getOriName());
//                fileIds.add(sysUpFile.getId().toString());
//                downTickets.add(sysUpFile.getDownTicket());
//
//            }
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
//            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
//            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
//            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
//            ajax.put("fileIds", StringUtils.join(fileIds, FILE_DELIMETER));
//            ajax.put("downTickets", StringUtils.join(downTickets, FILE_DELIMETER));
//            return ajax;
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    @Deprecated
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = OaspiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 单个文件，从minio中下载文件，如果是压缩包则解压后处理图片或pdf添加水印，
     * 如果不是压缩包则直接下载处理图片或pdf添加水印
     */
    @GetMapping("/download/sysupfile/{ticket}")
    @ApiOperation(value = "单个文件从minio中下载文件", notes = "单个文件从minio中下载文件")
    public void sysUpfileDownload(@PathVariable("ticket") String ticket, HttpServletResponse response) {
        try {
            SysUpFile example = new SysUpFile();
            example.setDownTicket(ticket);
            List<SysUpFile> sysUpFileList = sysUpFileService.selectSysUpFileList(example);
            if (sysUpFileList == null || sysUpFileList.isEmpty()) {
                throw new Exception("文件不存在");
            }
            SysUpFile sysUpFile = sysUpFileList.get(0);
            // 下载名称
            String downloadName = sysUpFile.getOriName();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);

            if (sysUpFile.getStoreType().equals(SysUpFile.STORE_TYPE_LOCAL)) {
                // 数据库资源地址
                String downloadPath = sysUpFile.getLocalPath();
                addWatermarkAndWriteToResponse(downloadPath, response.getOutputStream(), downloadName);
            } else {
                String url = sysUpFile.getPath();
                try (InputStream inputStream = minioUtil.getObject(url)) {
                    addWatermarkAndWriteToResponse(inputStream, response.getOutputStream(), downloadName);
                }
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 多个文件，从minio中下载文件，如果是压缩包则解压后处理图片或pdf添加水印，
     * 如果不是压缩包则直接下载处理图片或pdf添加水印
     */
    @GetMapping("/download/sysupfiles/{tickets}")
    @ApiOperation(value = "多个文件从minio中下载文件", notes = "多个文件从minio中下载文件")
    public void sysUpfileDownloads(@PathVariable("tickets") String tickets, HttpServletResponse response) throws Exception {
        String[] split = tickets.split(",");
        for (String s : split) {
            SysUpFile example = new SysUpFile();
            example.setDownTicket(s);
            List<SysUpFile> sysUpFileList = sysUpFileService.selectSysUpFileList(example);
            if (sysUpFileList == null || sysUpFileList.isEmpty()) {
                throw new Exception("文件不存在");
            }
            SysUpFile sysUpFile = sysUpFileList.get(0);
            // 下载名称
            String downloadName = sysUpFile.getName();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);

            if (sysUpFile.getStoreType().equals(SysUpFile.STORE_TYPE_LOCAL)) {
                // 数据库资源地址
                String downloadPath = sysUpFile.getLocalPath();
                addWatermarkAndWriteToResponse(downloadPath, response.getOutputStream(), downloadName);
            } else {
                String url = sysUpFile.getPath();
                try (InputStream inputStream = minioUtil.getObject(url)) {
                    addWatermarkAndWriteToResponse(inputStream, response.getOutputStream(), downloadName);
                }
            }
        }
    }

    private void addWatermarkAndWriteToResponse(String filePath, OutputStream outputStream, String fileName) throws IOException, FileNotFoundException {
        File file = new File(filePath);
        addWatermarkAndWriteToResponse(Files.newInputStream(file.toPath()), outputStream, fileName);
    }

    private void addWatermarkAndWriteToResponse(InputStream inputStream, OutputStream outputStream, String fileName) throws IOException {
        if (fileName.toLowerCase().endsWith(".zip")) {
            // 处理压缩包
            processZip(inputStream, outputStream, fileName);
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".png")) {
            // 添加图片水印
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage watermarkedImage = WatermarkUtils.addWatermarkToImage(image);
            ImageIO.write(watermarkedImage, "png", outputStream);
        } else if (fileName.toLowerCase().endsWith(".pdf")) {
            // 添加PDF水印
            PDDocument document = PDDocument.load(inputStream);
            WatermarkUtils.addWatermarkToPdf(document);
            document.save(outputStream);
            document.close();
        } else {
            // 其他类型的文件直接写入输出流
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

    private void processZip(InputStream inputStream, OutputStream outputStream, String fileName) throws IOException {
        File tempDir = Files.createTempDirectory("unzipped").toFile();
        try (ZipInputStream zipIn = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = tempDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }

        // 处理解压后的文件
        File[] files = tempDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String relativePath = file.getAbsolutePath().substring(tempDir.getAbsolutePath().length() + 1);
                if (relativePath.toLowerCase().endsWith(".jpg") || relativePath.toLowerCase().endsWith(".jpeg") || relativePath.toLowerCase().endsWith(".png")) {
                    BufferedImage image = ImageIO.read(file);
                    BufferedImage watermarkedImage = WatermarkUtils.addWatermarkToImage(image);
                    ImageIO.write(watermarkedImage, "png", Files.newOutputStream(file.toPath()));
                } else if (relativePath.toLowerCase().endsWith(".pdf")) {
                    PDDocument document = PDDocument.load(file);
                    WatermarkUtils.addWatermarkToPdf(document);
                    document.save(file);
                    document.close();
                }
            }
        }

        // 重新压缩文件
        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            addToZip(tempDir, "", zipOut);
        }

        // 删除临时目录
        deleteDirectory(tempDir);
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[8192];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    private void addToZip(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    addToZip(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[8192];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
