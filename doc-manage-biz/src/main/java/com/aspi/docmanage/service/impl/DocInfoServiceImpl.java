package com.aspi.docmanage.service.impl;

import com.aspi.docmanage.domain.AttachmentApproval;
import com.aspi.docmanage.domain.DocBehavior;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.utils.file.AddWatermarkAndWriteToZip;
import com.oaspi.common.utils.file.FileUploadUtils;
import com.oaspi.common.utils.file.WatermarkUtils;
import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.DocDept;
import com.oaspi.system.mapper.DocCompanyMapper;
import com.oaspi.system.mapper.DocDeptMapper;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.aspi.docmanage.domain.DocCategory;
import com.aspi.docmanage.service.IDocCategoryService;
import com.aspi.docmanage.web.api.vo.ArchiveStatusVO;
import com.aspi.docmanage.web.api.vo.DelayVO;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.entity.SysDictData;
import com.oaspi.common.exception.ServiceException;
import com.oaspi.common.exception.UtilException;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.PageUtils;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.common.utils.bean.BeanUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.vo.FileDocVo;
import com.oaspi.system.mapper.SysUpFileMapper;
import com.oaspi.system.service.IDocCompanyService;
import com.oaspi.system.service.IDocDeptService;
import com.oaspi.system.service.ISysDictDataService;
import com.oaspi.system.util.MinioUtil;
import io.minio.errors.MinioException;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocInfoMapper;
import com.aspi.docmanage.domain.DocInfo;
import com.aspi.docmanage.service.IDocInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import static com.oaspi.common.utils.SecurityUtils.getUsername;

/**
 * 档案信息Service业务层处理
 *
 * @author hongy
 * @date 2024-11-02
 */
@Service
public class DocInfoServiceImpl implements IDocInfoService {

    private static final Logger log = LoggerFactory.getLogger(DocInfoServiceImpl.class);
    @Autowired
    private DocInfoMapper docInfoMapper;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IDocCategoryService docCategoryService;
    @Autowired
    private SysUpFileMapper sysUpFileMapper;
    @Autowired
    private DocDeptMapper docDeptMapper;
    @Autowired
    private DocCompanyMapper docCompanyMapper;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    IdentityService identityService;
    @Resource
    HistoryService historyService;
    @Resource
    RepositoryService repositoryService;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private IDocCompanyService docCompanyService;
    @Autowired
    private IDocDeptService docDeptService;

    /**
     * 查询档案信息
     *
     * @param id 档案信息主键
     * @return 档案信息
     */
    @Override
    public DocInfo selectDocInfoById(Long id, SysUser sysUser) {
        DocInfo docInfo = docInfoMapper.selectDocInfoById(id);
        DocBehavior docBehavior = new DocBehavior();
        DocDept docDept = docDeptMapper.selectDocDeptByDeptId(docInfo.getDeptId());
        Long deptId = docDept.getDeptId();
        DocCompany docCompany = docCompanyMapper.selectDocCompanyByCompanyId(deptId);
        String deptName = docDept.getDeptName();
        String companyName = docCompany.getCompanyName();
        String docName = docInfo.getTitle();
        docBehavior.setDocTitle(docName);
        docBehavior.setDeptName(deptName);
        docBehavior.setCompanyName(companyName);
        docBehavior.setUsername(sysUser.getUserName());
        docBehavior.setNickName(sysUser.getNickName());
        docBehavior.setBehavior("1"); //query
        Date nowTime = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        docBehavior.setBehaviorTime(sdf.format(nowTime));
        this.addDocBehavior(docBehavior);
        return docInfo;
    }

    @Override
    public DocInfo selectDocInfoByOri(String oriDocId) {
        DocInfo docInfo = docInfoMapper.selectDocInfoByOri(oriDocId);
        //将（档案树）门类id翻译成文本
        DocCategory docCategory = docCategoryService.selectDocCategoryById(docInfo.getCategory());
        docInfo.setCategoryText(docCategory == null ? "" : docCategory.getName());
        //将保密级别期限id翻译成文本
        SysDictData secretLevel = dictDataService.selectDictDataById(docInfo.getSecretLevel());
        docInfo.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
        return docInfo;
    }

    /**
     * 档案行为列表
     */
    @Override
    public List<DocBehavior> queryDocBehaviorList(DocBehavior docBehavior) {
        return docInfoMapper.queryDocBehaviorList(docBehavior);
    }

    /**
     * 添加档案行为
     */
    private void addDocBehavior(DocBehavior docBehavior) {
        docInfoMapper.insertDocBehavior(docBehavior);
    }

    /**
     * 查询档案信息列表
     *
     * @param docInfo 档案信息
     * @return 档案信息
     */
    @Override
    public List<DocInfo> selectDocInfoList(DocInfo docInfo) {
        return docInfoMapper.selectDocInfoList(docInfo);
    }

    /**
     * 新增档案信息
     *
     * @param docInfo 档案信息
     * @return 结果
     */
    @Override
    public int insertDocInfo(DocInfo docInfo) {
        // 判断档案号是否重复
        List<DocInfo> infoList = docInfoMapper.selectDocOriId(docInfo.getOriDocId());
        if (!infoList.isEmpty()){
            throw new UtilException("原档案号重复，请重新输入!");
        }
        //将公司、部门名称添加到档案信息表中
        DocDept docDept = docDeptService.selectDocDeptByDeptId(docInfo.getDeptId());
        DocCompany docCompany = docCompanyService.selectDocCompanyByCompanyId(docInfo.getCompanyId());
        docInfo.setCompanyName(docCompany.getCompanyName());
        docInfo.setDeptName(docDept.getDeptName());

        //写入当前时间
        Date nowDate = DateUtils.getNowDate();
        docInfo.setCreateTime(nowDate);

        //根据密保等级计算剩余天数
        LocalDate localDate = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        SysDictData sysDictData = dictDataService.selectDictDataById(docInfo.getSecretLevel());
        long l = calculateRemainingDays(localDate, Integer.parseInt(sysDictData.getDictValue()));
        docInfo.setRemainingDays(l);

        //根据档案号在minio中创建目录
        String oriDocId = docInfo.getOriDocId();
        String dirPath = FileUploadUtils.convertFileNameToPath(oriDocId);
        String objectName = dirPath.endsWith("/") ? dirPath : dirPath + "/";
        // 创建一个空对象来表示目录
        try {
            minioUtil.putObjectSafe(objectName,new ByteArrayInputStream(new byte[]{}), null, "common");
        } catch (MinioException e) {
            log.error("创建目录失败，请检查档案号书写是否符合规范！");
            throw new UtilException(e);
        }
        return docInfoMapper.insertDocInfo(docInfo);
    }

    /**
     * 每日0点刷新密保有效期剩余天数
     */
    @Scheduled(cron = "0 0 0 * * ?")
//        @Scheduled(cron = "*/10 * * * * ?")
    public void delay() {
        List<DocInfo> docInfos = docInfoMapper.selectDocInfoListAll();
        docInfos.forEach(e -> {
            DocInfo docInfo = new DocInfo();
            docInfo.setId(e.getId());
            docInfo.setRemainingDays(e.getRemainingDays() - 1);
            docInfoMapper.updateRemainingDays(docInfo);
        });
    }

    /**
     * 计算密保问题的有效期剩余天数。
     *
     * @param creationDate 密保问题的创建日期。
     * @return 剩余有效天数。
     */
    public long calculateRemainingDays(LocalDate creationDate, int year) {
        // 计算有效期结束日期
        LocalDate expiryDate = creationDate.plusYears(year);
        // 获取今天的日期（每天0点刷新）
        LocalDate today = LocalDate.now();
        // 计算今天和有效期结束日期之间的天数差
        return ChronoUnit.DAYS.between(today, expiryDate);
    }

    /**
     * 修改档案信息
     *
     * @param docInfo 档案信息
     * @return 结果
     */
    @Override
    public int updateDocInfo(DocInfo docInfo) {
        docInfo.setUpdateTime(DateUtils.getNowDate());
        return docInfoMapper.updateDocInfo(docInfo);
    }

    /**
     * 批量删除档案信息
     *
     * @param ids 需要删除的档案信息主键
     * @return 结果
     */
    @Override
    public int deleteDocInfoByIds(Long[] ids) {
        return docInfoMapper.deleteDocInfoByIds(ids);
    }

    /**
     * 删除档案信息信息
     *
     * @param id 档案信息主键
     * @return 结果
     */
    @Override
    public int deleteDocInfoById(Long id) {
        return docInfoMapper.deleteDocInfoById(id);
    }

    /**
     * 根据门类id查询档案信息列表
     *
     * @param categoryId
     * @return
     */
    /*@Override
    public List<DocInfo> getByCategoryId(Long categoryId, Long docType) {
        if (categoryId != null && docType != null) {
            if (categoryId == 6 || categoryId == 7 || categoryId == 8 || categoryId == 12 || categoryId == 13 || categoryId == 14 || categoryId == 15 || categoryId > 16) {
                return new ArrayList<>();
            }
            //文本、科技档案（包括二级门类科研、基建、设备）查询列表
            if (categoryId == 1 || categoryId == 2 || categoryId == 3 || categoryId == 4 || categoryId == 5) {
                List<DocInfo> technologyTextList = docInfoMapper.queryByText(categoryId, docType);
                return getDocInfos(categoryId, technologyTextList);
            }
            //照片查询列表
            if (categoryId == 9) {
                List<DocInfo> photoList = docInfoMapper.queryByPhoto(categoryId, docType);
                return getDocInfos(categoryId, photoList);
            }
            //录音、录像查询列表
            if (categoryId == 10 || categoryId == 11) {
                List<DocInfo> videoRecordList = docInfoMapper.queryByRecord(categoryId, docType);
                return getDocInfos(categoryId, videoRecordList);
            }
            //实物查询列表
            if (categoryId == 16) {
                List<DocInfo> matterList = docInfoMapper.queryByMatter(categoryId, docType);
                return getDocInfos(categoryId, matterList);
            }
        }

        if (categoryId == null && docType != null) {
            return docInfoMapper.queryByDocTypeId(docType);
        }

        if (docType == null && categoryId != null) {
            if (categoryId == 6 || categoryId == 7 || categoryId == 8 || categoryId == 12 || categoryId == 13 || categoryId == 14 || categoryId == 15 || categoryId > 16) {
                return new ArrayList<>();
            }
            //文本、科技档案（包括二级门类科研、基建、设备）查询列表
            if (categoryId == 1 || categoryId == 2 || categoryId == 3 || categoryId == 4 || categoryId == 5) {
                List<DocInfo> technologyTextList = docInfoMapper.queryByText1(categoryId);
                return getDocInfos(categoryId, technologyTextList);
            }
            //照片查询列表
            if (categoryId == 9) {
                List<DocInfo> photoList = docInfoMapper.queryByPhoto1(categoryId);
                return getDocInfos(categoryId, photoList);
            }
            //录音、录像查询列表
            if (categoryId == 10 || categoryId == 11) {
                List<DocInfo> videoRecordList = docInfoMapper.queryByRecord1(categoryId);
                return getDocInfos(categoryId, videoRecordList);
            }
            //实物查询列表
            if (categoryId == 16) {
                List<DocInfo> matterList = docInfoMapper.queryByMatter1(categoryId);
                return getDocInfos(categoryId, matterList);
            }
        }

        List<DocInfo> docInfoAll = docInfoMapper.selectAllData();
        docInfoAll.forEach(m -> {
            //将（档案树）门类id翻译成文本
            DocCategory docCategory = docCategoryService.selectDocCategoryById(m.getCategory());
            m.setCategoryText(docCategory == null ? "" : docCategory.getName());
            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(m.getDocType());
            m.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
            //将保密级别期限id翻译成文本
            SysDictData secretLevel = dictDataService.selectDictDataById(m.getSecretLevel());
            m.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
        });
        return docInfoAll;
    }*/

    @NotNull
    private List<DocInfo> getDocInfos(Long categoryId, List<DocInfo> matterList) {
        matterList.forEach(e -> {
            //将（档案树）门类id翻译成文本
            DocCategory docCategory = docCategoryService.selectDocCategoryById(categoryId);
            e.setCategoryText(docCategory == null ? "" : docCategory.getName());
            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(e.getDocType());
            e.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
            //将保密级别期限id翻译成文本
            SysDictData secretLevel = dictDataService.selectDictDataById(e.getSecretLevel());
            e.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
        });
        return matterList;
    }

    /**
     * 查询待上架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    @Override
    public List<DocInfo> shelvesUpStopList(DocInfo docInfo) {
        PageUtils.startPage();
        return docInfoMapper.shelvesUpStopList(docInfo);
    }

    /**
     * 查询上架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    @Override
    public List<DocInfo> shelvesUpList(DocInfo docInfo) {
        PageUtils.startPage();
        return docInfoMapper.shelvesUpList(docInfo);
    }

    /**
     * 查询下架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    @Override
    public List<DocInfo> shelvesDownList(DocInfo docInfo) {
        PageUtils.startPage();
        return docInfoMapper.shelvesDownList(docInfo);
    }

    /**
     * 查询待下架列表
     *
     * @param docInfo 库房管理
     * @return 库房管理
     */
    @Override
    public List<DocInfo> shelvesDownStopList(DocInfo docInfo) {
        PageUtils.startPage();
        return docInfoMapper.shelvesDownStopList(docInfo);
    }

    /**
     * 清空档案盒
     *
     * @param docInfo
     * @return 结果
     */
    @Override
    public AjaxResult delShelves(DocInfo docInfo) {
        //修改类型
        docInfoMapper.delShelvesInfo(docInfo);
        return AjaxResult.success();
    }


    /**
     * 上架
     *
     * @param docInfo
     * @return 结果
     */
    @Override
    public AjaxResult shelvesUp(DocInfo docInfo) {
        //插入时间
        docInfo.setCreateTime(DateUtils.getNowDate());
        docInfo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        //上架
        docInfoMapper.shelvesUp(docInfo);
        return AjaxResult.success();
    }

    /**
     * 下架
     *
     * @param docInfo
     * @return 结果
     */
    @Override
    public AjaxResult shelvesDown(DocInfo docInfo) {
        //插入时间
        docInfo.setCreateTime(DateUtils.getNowDate());
        docInfo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        //下架
        docInfoMapper.shelvesDown(docInfo);
        return AjaxResult.success();
    }

    @Override
    public String importDocInfo(List<DocInfo> docInfoList) {
        if (StringUtils.isNull(docInfoList) || docInfoList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }
        try {
            for (DocInfo imp : docInfoList) {
                DocInfo docInfo = new DocInfo();
                BeanUtils.copyProperties(imp, docInfo);

                Date nowDate = DateUtils.getNowDate();
                docInfo.setCreateTime( DateUtils.getNowDate());
                LocalDate localDate = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                //设置公司id以及名称
                DocCompany docCompany = docCompanyService.selectDocCompanyByCompanyName(imp.getCompanyName());
                docInfo.setCompanyId(docCompany.getCompanyId());
                docInfo.setCompanyName(docCompany.getCompanyName());

                //设置部门名称以及部门id
                DocDept docDept = docDeptService.selectDocDeptByDeptName(imp.getDeptName(),docCompany.getCompanyId());
                docInfo.setDeptName(docDept.getDeptName());
                docInfo.setDeptId(docDept.getDeptId());

                //通过秘密等级名称查询字典对象
                SysDictData sysDictData = dictDataService.selectDictDataBySecretName(imp.getSecretName());
                long l = calculateRemainingDays(localDate, Integer.parseInt(sysDictData.getDictValue()));
                //根据查询字典对应秘密期限值，设置剩余天数
                docInfo.setRemainingDays(l);

                //设置秘密等级id
                docInfo.setSecretLevel(sysDictData.getDictCode());
                docInfo.setSecretName(imp.getSecretName());

                //设置门类id以及门类名称
                DocCategory docCategory = docCategoryService.selectDocCategoryByCategory(imp.getCategoryName());
                docInfo.setCategory(docCategory.getId());
                docInfo.setCategoryName(docCategory.getName());

                docInfoMapper.insertDocInfo(docInfo);

                // 根据档案号生成minio目录路径
                String oriDocId = imp.getOriDocId();
                String dirPath = FileUploadUtils.convertFileNameToPath(oriDocId);
                String objectName = dirPath.endsWith("/") ? dirPath : dirPath + "/";
                minioUtil.putObjectSafe(objectName,new ByteArrayInputStream(new byte[]{}), null, "common");
            }
            return "导入成功 " + docInfoList.size() + " !";
        } catch (Exception e) {
            e.printStackTrace();
            return "导入失败,第"+ docInfoList.size() + "行错误 !";
        }
    }

    @Override
    public int updateArchiveStatus(ArchiveStatusVO vo) {
        return docInfoMapper.updateArchiveStatus(vo);
    }

    /**
     * 导入上架模板
     *
     * @param file
     * @return 结果
     */
    @Override
    public AjaxResult importShelvesUp(MultipartFile file) {
        try {
            List<DocInfo> imports = readShelvesUp(file.getInputStream());

            for (DocInfo imp : imports) {
                DocInfo warehouse = new DocInfo();
                //查询档案盒关联id
                String id = docInfoMapper.selId(imp);
                warehouse.setId(imp.getId());
                warehouse.setBoxId(id);
                shelvesUp(warehouse);
            }

            return AjaxResult.success("导入成功 " + imports.size() + "条 !");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 导入下架模板
     *
     * @param file
     * @return 结果
     */
    @Override
    public AjaxResult importShelvesDown(MultipartFile file) {
        try {
            List<DocInfo> imports = readShelvesDown(file.getInputStream());

            for (DocInfo imp : imports) {
                DocInfo warehouse = new DocInfo();
                warehouse.setId(imp.getId());
                shelvesDown(warehouse);
            }

            return AjaxResult.success("导入成功 " + imports.size() + "条 !");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @Override
    public List<FileDocVo> queryGetByDocInfoId(Long[] docIds) {
        List<FileDocVo> fileDocVos = sysUpFileMapper.selectGetByDocInfoId(docIds);
        List<Long> collect = fileDocVos.stream().map(FileDocVo::getCategory).distinct().collect(Collectors.toList());
        fileDocVos.forEach(e -> {
            //将（档案树）门类id翻译成文本
            for (Long l : collect) {
                String name = docCategoryService.selectDocCategoryById(l).getName();
                if (StringUtils.isNotEmpty(name)) {
                    e.setCategoryText(name == null ? "" : name);
                }
            }

            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(e.getDocType());
            e.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
        });
        return fileDocVos;
    }

//    @Override
//    public List<DocInfo> selectDocInfoListArchive(DocInfo docInfo, SysUser sysUser) {
//        return docInfoMapper.selectDocInfoListArchive(docInfo, sysUser);
//    }

    @Override
    public List<DocInfo> listWait(DocInfo docInfo) {
        return docInfoMapper.listWait(docInfo);
    }

    /**
     * 档案管理查询列表
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<DocInfo> docManage(Long categoryId, Long type, Integer archiveStatus, SysUser sysUser) {
        /*if (categoryId != null && docType != null) {
            if (categoryId == 6 || categoryId == 7 || categoryId == 8 || categoryId == 12 || categoryId == 13 || categoryId == 14 || categoryId == 15 || categoryId > 16) {
                return new ArrayList<>();
            }
            //文本、科技档案（包括二级门类科研、基建、设备）查询列表
            if (categoryId == 1 || categoryId == 2 || categoryId == 3 || categoryId == 4 || categoryId == 5) {
                List<DocInfo> technologyTextList = docInfoMapper.queryDocManageByText(categoryId, docType);
                return getDocInfos(categoryId, technologyTextList);
            }
            //照片查询列表
            if (categoryId == 9) {
                List<DocInfo> photoList = docInfoMapper.queryDocManageByPhoto(categoryId, docType);
                return getDocInfos(categoryId, photoList);
            }
            //录音、录像查询列表
            if (categoryId == 10 || categoryId == 11) {
                List<DocInfo> videoRecordList = docInfoMapper.queryDocManageByRecord(categoryId, docType);
                return getDocInfos(categoryId, videoRecordList);
            }
            //实物查询列表
            if (categoryId == 16) {
                List<DocInfo> matterList = docInfoMapper.queryDocManageByMatter(categoryId, docType);
                return getDocInfos(categoryId, matterList);
            }
        }*/

        /*if (categoryId == null && docType != null) {
            return docInfoMapper.queryDocManageByDocTypeId(docType);
        }*/

        /*if (docType == null && categoryId != null) {
            if (categoryId == 6 || categoryId == 7 || categoryId == 8 || categoryId == 12 || categoryId == 13 || categoryId == 14 || categoryId == 15 || categoryId > 16) {
                return new ArrayList<>();
            }
            //文本、科技档案（包括二级门类科研、基建、设备）查询列表
            if (categoryId == 1 || categoryId == 2 || categoryId == 3 || categoryId == 4 || categoryId == 5) {
                List<DocInfo> technologyTextList = docInfoMapper.queryDocManageByText1(categoryId, type);
                return getDocInfos(categoryId, technologyTextList);
            }
            //照片查询列表
            if (categoryId == 9) {
                List<DocInfo> photoList = docInfoMapper.queryDocManageByPhoto1(categoryId, type);
                return getDocInfos(categoryId, photoList);
            }
            //录音、录像查询列表
            if (categoryId == 10 || categoryId == 11) {
                List<DocInfo> videoRecordList = docInfoMapper.queryDocManageByRecord1(categoryId, type);
                return getDocInfos(categoryId, videoRecordList);
            }
            //实物查询列表
            if (categoryId == 16) {
                List<DocInfo> matterList = docInfoMapper.queryDocManageByMatter1(categoryId, type);
                return getDocInfos(categoryId, matterList);
            }
        }*/
        sysUser.setType(type);
        sysUser.setArchiveStatus(archiveStatus);
        sysUser.setCategoryId(categoryId);
        List<DocInfo> docInfoAll = docInfoMapper.selectDocManageAllData(sysUser);
        docInfoAll.forEach(m -> {
            //将（档案树）门类id翻译成文本
            DocCategory docCategory = docCategoryService.selectDocCategoryById(m.getCategory());
            m.setCategoryText(docCategory == null ? "" : docCategory.getName());
            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(m.getDocType());
            m.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
            //将保密级别期限id翻译成文本
            SysDictData secretLevel = dictDataService.selectDictDataById(m.getSecretLevel());
            m.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
        });
        return docInfoAll;
    }

    @Override
    public List<DocInfo> selectDocInfoIds(Long[] longs) {
        return docInfoMapper.selectDocInfoIds(longs);
    }

    /**
     * 档案管理,根据档案信息ids查询电子文件信息上传列表
     *
     * @param longArray
     * @return
     */
    @Override
    public List<FileDocVo> queryGetByDocManageInfoId(Long[] longArray) {
        List<FileDocVo> fileDocVos = sysUpFileMapper.selectGetByDocManageInfoId(longArray);
        List<Long> collect = fileDocVos.stream().map(FileDocVo::getCategory).distinct().collect(Collectors.toList());
        fileDocVos.forEach(e -> {
            //将（档案树）门类id翻译成文本
            for (Long l : collect) {
                String name = docCategoryService.selectDocCategoryById(l).getName();
                if (StringUtils.isNotEmpty(name)) {
                    e.setCategoryText(name);
                }
            }

            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(e.getDocType());
            e.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
        });
        return fileDocVos;
    }

    /**
     * 批量下载文件生成压缩包
     */
    @Override
    public List<SysUpFile> queryVolumeFile(Long[] longArray, HttpServletResponse response) throws IOException, MinioException {
        List<SysUpFile> sysUpFileList = sysUpFileMapper.queryVolumeFile(longArray);
        // 设置响应头
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=filename.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (SysUpFile sysUpFile : sysUpFileList) {
                String downloadName = sysUpFile.getName();
                // 源文件名+uuid生成唯一的文件名
                String uniqueName = generateUniqueFileName(downloadName);

                try {
                    zipOut.putNextEntry(new ZipEntry(uniqueName));
                    // 可选：设置注释为原始文件名
                    zipOut.setComment(downloadName);

                    if (SysUpFile.STORE_TYPE_LOCAL == sysUpFile.getStoreType()) {
                        String downloadPath = sysUpFile.getLocalPath();
                        try (InputStream inputStream = Files.newInputStream(Paths.get(downloadPath))) {
                            AddWatermarkAndWriteToZip.addWatermarkAndWriteToZip(inputStream, zipOut, downloadName);
                        }
                    } else {
                        String url = sysUpFile.getPath();
                        try (InputStream inputStream = minioUtil.getObject(url)) {
                            AddWatermarkAndWriteToZip.addWatermarkAndWriteToZip(inputStream, zipOut, downloadName);
                        }
                    }
                } catch (IOException e) {
                    log.error("处理文件{}时发生错误: {}", downloadName, e.getMessage());
                    // 继续处理下一个文件，而不是直接终止
                } finally {
                    try {
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        log.error("关闭ZIP条目时发生错误: {}", e.getMessage());
                    }
                }
            }
            // 确保所有数据都写入
            zipOut.flush();
        }

        return sysUpFileList;
    }

    /**
     * 源文件名+uuid生成唯一的文件名
     */
    private String generateUniqueFileName(String originalName) {
        // 使用UUID生成唯一标识符
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 将UUID插入到文件名中，例如：originalName_uuid.ext
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex == -1) {
            return originalName + "_" + uuid;
        } else {
            String namePart = originalName.substring(0, dotIndex);
            String extensionPart = originalName.substring(dotIndex);
            return namePart + "_" + uuid + extensionPart;
        }
    }

    /**
     * 档案鉴定销毁
     *
     * @param docInfo
     */
    @Override
    public int updateDocInfoStatus(DocInfo docInfo) {
        docInfo.setStatus(-10L);
        return docInfoMapper.updateDocInfoStatus(docInfo);
    }

    /**
     * 档案鉴定-保管到期列表
     *
     * @param docInfo
     * @return
     */
    @Override
    public List<DocInfo> authenticateExpire(DocInfo docInfo) {
        docInfo.setStatus(0L);
        List<DocInfo> docInfos = docInfoMapper.authenticateExpire(docInfo);
        docInfos.forEach(m -> {
            //将（档案树）门类id翻译成文本
            DocCategory docCategory = docCategoryService.selectDocCategoryById(m.getCategory());
            m.setCategoryText(docCategory == null ? "" : docCategory.getName());
            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(m.getDocType());
            m.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
            //将保密级别期限id翻译成文本
            SysDictData secretLevel = dictDataService.selectDictDataById(m.getSecretLevel());
            m.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
            //档案状态翻译
            String status = dictDataService.selectDictLabel("doc_status", String.valueOf(m.getStatus()));
            m.setStatusText(status == null ? "" : status);

        });
        return docInfos;
    }

    /**
     * 发起鉴定延期流程,状态为20审批中
     *
     * @param vo
     * @return
     */
    @Override
    public void updateStatus(DelayVO vo) {
        //发起流程
        identityService.setAuthenticatedUserId(String.valueOf(vo.getTaskid()));
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("applyuserid", String.valueOf(vo.getTaskid()));
        variables.put("approver", "admin");
        runtimeService.startProcessInstanceByKey("authenticate", String.valueOf(vo.getTaskid()), variables);
        // 自动完成第一个任务
        Task autoTask = taskService.createTaskQuery()
                .processDefinitionKey("authenticate")
                .processInstanceBusinessKey(String.valueOf(vo.getTaskid()))
                .singleResult();
        taskService.complete(autoTask.getId());

        DocInfo docInfo = new DocInfo();
        docInfo.setId(vo.getTaskid());
        docInfo.setStatus(20L);
        docInfo.setReasons(vo.getReason());
        docInfo.setSecretLevel(vo.getSecretLevel());
        docInfoMapper.updateStatus(docInfo);
    }

    /**
     * 驳回鉴定延期，状态30
     *
     * @param delayVO
     * @return
     */
    @Override
    public void updateStatusByBack(DelayVO delayVO) {
        DocInfo docInfo = new DocInfo();
        docInfo.setId(delayVO.getTaskid());
        docInfo.setStatus(30L);
        docInfo.setReasons(delayVO.getReason());
        docInfoMapper.updateStatusByBack(docInfo);
    }

    /**
     * 审批档案延期流程,审批通过状态为100
     *
     * @param
     * @return
     */
    @Override
    public void updateStatusPass(DelayVO vo, Map<String, Object> variables) {
        String username = getUsername();
        String s = String.valueOf(vo.getTaskid());
        taskService.setAssignee(s, username);
        // 查出流程实例id
        String processInstanceId = taskService.createTaskQuery().taskId(s).singleResult().getProcessInstanceId();
        if (variables == null) {
            taskService.complete(s);
        } else {
            // 添加审批意见
            if (variables.get("comment") != null) {
                taskService.addComment(s, processInstanceId, (String) variables.get("comment"));
                variables.remove("comment");
            }
            taskService.complete(s, variables);
        }

        DocInfo docInfo = new DocInfo();
        docInfo.setId(vo.getDocId());
        docInfo.setStatus(100L);
        docInfo.setReasons(vo.getReason());
        docInfoMapper.updateStatusPass(docInfo);
    }

    @Override
    public int updateDocInfoStatusFull(DocInfo docInfo) {
        docInfo.setStatus(-20L);
        return docInfoMapper.updateDocInfoStatusFull(docInfo);
    }

    @Override
    public List<DocInfo> authenticateApprove(DocInfo docInfo) {
        List<DocInfo> docInfos = docInfoMapper.authenticateExpire(docInfo);
        docInfos.forEach(m -> {
            //将（档案树）门类id翻译成文本
            DocCategory docCategory = docCategoryService.selectDocCategoryById(m.getCategory());
            m.setCategoryText(docCategory == null ? "" : docCategory.getName());
            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(m.getDocType());
            m.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
            //将保密级别期限id翻译成文本
            SysDictData secretLevel = dictDataService.selectDictDataById(m.getSecretLevel());
            m.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
            //档案状态翻译
            String status = dictDataService.selectDictLabel("doc_status", String.valueOf(m.getStatus()));
            m.setStatusText(status == null ? "" : status);
        });
        return docInfos;
    }

    /**
     * 审批状态统计
     *
     * @return
     */
    @Override
    public List<DocInfo> selectDocInfoListTotal() {
        return docInfoMapper.selectDocInfoListTotal();
    }

    @Override
    public String approvalResult(Long userId, Long[] attachmentIds) {
        for (int i = 0; i < attachmentIds.length; i++) {
            if (docInfoMapper.beforeApprovalResult(userId, attachmentIds[i]) == 0) {
                return "false";
            }
            int alreadyCount = docInfoMapper.checkAlready(userId, attachmentIds[i]);
            if (alreadyCount != 0) {
                return "already";
            }
            int count = docInfoMapper.approvalResult(userId, attachmentIds[i]);
            if (count == 0) {
                return "false";
            }
        }
        return "true";
    }

    /**
     * 添加附件审批
     *
     * @return
     */
    @Override
    public void addApprovalResult(AttachmentApproval attachmentApproval) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(new Date());
        Long userId = attachmentApproval.getUserId();
        Long[] attachmentIds = attachmentApproval.getAttachmentIds();
        for (int i = 0; i < attachmentIds.length; i++) {
            int count = docInfoMapper.approvalResult(userId, attachmentIds[i]);
            if (count == 0) {
                attachmentApproval.setStartTime(startTime);
                attachmentApproval.setCanStatus(0);
                attachmentApproval.setAttachmentId(attachmentIds[i]);
                docInfoMapper.addApprovalResult(attachmentApproval);
            }
        }
    }

    /**
     * 审批附件审批
     *
     * @return
     */
    @Override
    public void updateApprovalResult(AttachmentApproval attachmentApproval) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String handleTime = sdf.format(new Date());
        attachmentApproval.setHandleTime(handleTime);
        docInfoMapper.updateApprovalResult(attachmentApproval);
    }

    /**
     * 过期附件审批
     *
     * @return
     */
    @Override
    public void expireApprovalResult() throws Exception {
        DateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;
        // long nh = 1000 * 60 * 60;
        long nowTime = new Date().getTime();
        List<Long> ids = new ArrayList<>();
        List<AttachmentApproval> list = docInfoMapper.queryWaitList();
        for (AttachmentApproval aa: list) {
            Long markTime = sdf.parse(aa.getHandleTime()).getTime();
            if (aa.getCanStatus() == 1 && markTime + nd * 3 < nowTime) {
                ids.add(aa.getId());
            }
        }
        if (!ids.isEmpty()) {
            docInfoMapper.expireApprovalResult(ids);
        }

    }

    /**
     * 等待我审批的附件审批列表
     *
     * @return
     */
    @Override
    public List<AttachmentApproval> myTask(AttachmentApproval attachmentApproval) throws Exception {
        List<AttachmentApproval> list = docInfoMapper.myTask(attachmentApproval);
        return list;
    }

    /**
     * 查看我申请过的附件列表
     * @return
     */
    @Override
    public List<AttachmentApproval> myRequest(AttachmentApproval attachmentApproval) throws Exception {
        DateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nowDate = new Date().getTime();
        List<AttachmentApproval> list = docInfoMapper.myRequest(attachmentApproval);
        for (AttachmentApproval aa: list) {
            if (aa.getCanStatus() == 1) {
                long diff = sdf.parse(aa.getHandleTime()).getTime() + nd * 3 - nowDate;
                aa.setHours(diff % nd / nh);
            }
        }
        return list;
    }

    /**
     * 回收站删除档案信息
     */
    @Override
    public void recycleRemoveByIds(Long[] ids) {
        docInfoMapper.recycleRemoveByIds(ids);
    }

    /**
     * 回收站根据档案id查询附件列表
     */
    @Override
    public List<FileDocVo> queryGetByRecycleInfoId(Long[] array) {
        List<FileDocVo> fileDocVos = sysUpFileMapper.queryGetByRecycleInfoId(array);
        List<Long> collect = fileDocVos.stream().map(FileDocVo::getCategory).distinct().collect(Collectors.toList());
        fileDocVos.forEach(e -> {
            //将（档案树）门类id翻译成文本
            for (Long l : collect) {
                String name = docCategoryService.selectDocCategoryById(l).getName();
                if (StringUtils.isNotEmpty(name)) {
                    e.setCategoryText(name);
                }
            }

            //将分类id翻译成文本
            SysDictData sysDictData = dictDataService.selectDictDataById(e.getDocType());
            e.setDocTypeText(sysDictData == null ? "" : sysDictData.getDictLabel());
        });
        return fileDocVos;
    }

    /**
     * 回收站档案信息还原
     */
    @Override
    @Transactional
    public void updateDocInfoRestore(Long[] ids) {
        docInfoMapper.updateDocInfoRestore(ids);
    }

    /**
     * 解析上架excel
     *
     * @param inputStream
     * @return 结果
     */
    public static List<DocInfo> readShelvesUp(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<DocInfo> warehouses = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) { // Skip header
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            DocInfo warehouse = new DocInfo();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIdx) {
                    case 0:
                        warehouse.setId((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    case 1:
                        warehouse.setBoxName(ExcelUtil.getCellValueAsString(currentCell));
                        break;
                    case 2:
                        warehouse.setShelvesNo((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    default:
                        break;
                }
                cellIdx++;
            }

            warehouses.add(warehouse);
            rowNumber++;
        }

        workbook.close();
        return warehouses;
    }

    /**
     * 解析下架excel
     *
     * @param inputStream
     * @return 结果
     */
    public static List<DocInfo> readShelvesDown(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<DocInfo> warehouses = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) { // Skip header
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            DocInfo warehouse = new DocInfo();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIdx) {
                    case 0:
                        warehouse.setId((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    default:
                        break;
                }
                cellIdx++;
            }

            warehouses.add(warehouse);
            rowNumber++;
        }

        workbook.close();
        return warehouses;
    }
}
