package com.aspi.docmanage.web.api.controller;

import com.aspi.docmanage.domain.AttachmentApproval;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.system.domain.DocAttachments;
import com.aspi.docmanage.domain.DocBehavior;
import com.aspi.docmanage.domain.DocInfo;
import com.oaspi.system.mapper.SysUserMapper;
import com.oaspi.system.service.IDocAttachmentsService;
import com.aspi.docmanage.service.IDocInfoService;
import com.aspi.docmanage.web.api.vo.*;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.entity.SysDictData;
import com.oaspi.common.core.domain.entity.SysRole;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.StringUtils;
import com.oaspi.common.utils.bean.BeanUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.domain.TaskInfo;
import com.oaspi.system.domain.vo.FileDocVo;
import com.oaspi.system.service.ISysDictDataService;
import com.oaspi.system.service.ISysRoleService;
import com.oaspi.system.service.ISysUpFileService;
import com.oaspi.system.service.ISysUserService;
import com.oaspi.system.util.MinioUtil;
import io.minio.errors.MinioException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.aspi.docmanage.util.ConvertStringArrayToLongArray.convertStringArrayToLongArray;
import static com.aspi.docmanage.util.ToPrimitiveLongArray.toPrimitiveLongArray;

/**
 * 档案信息Controller
 *
 * @author hongy
 * @date 2024-11-02
 */
@Api(tags = "档案信息")
@RestController
@RequestMapping("/docmanage/docinfo")
public class DocInfoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DocInfoController.class);
    @Autowired
    private IDocInfoService docInfoService;
    @Autowired
    private ISysUpFileService sysUpFileService;
    @Autowired
    private IDocAttachmentsService docAttachmentsService;

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    IdentityService identityService;
    @Resource
    HistoryService historyService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    FormService formService;

    public DocInfoController(ISysUpFileService sysUpFileService, MinioUtil minioUtil) {
        this.sysUpFileService = sysUpFileService;
        this.minioUtil = minioUtil;
    }

    /**
     * 查询档案信息列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询档案信息列表", notes = "查询档案信息列表")
    public TableDataInfo list(DocInfo docInfo) {
        startPage();
        List<DocInfo> list = docInfoService.selectDocInfoList(docInfo);
        return getDataTable(list);
    }

    /**
     * 查询已经归档的档案信息列表
     *
     * @param docInfo
     * @return
     */
//    @GetMapping("/listArchive")
//    @ApiOperation(value = "查询已经归档的档案信息列表", notes = "查询已经归档的档案信息列表")
//    public TableDataInfo listArchive(DocInfo docInfo) {
//        SysUser sysUser = sysUserService.selectUserById(getUserId());
//        startPage();
//        List<DocInfo> list = docInfoService.selectDocInfoListArchive(docInfo, sysUser);
//        return getDataTable(list);
//    }

    /**
     * 待归档案卷
     * @return
     */
    @GetMapping(value = "/list/wait")
    // @Log(title = "待归档案卷列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "待归档案卷列表", notes = "待归档案卷列表")
    public TableDataInfo listWait(DocInfo docInfo) {
        startPage();
        List<DocInfo> list = docInfoService.listWait(docInfo);
        return getDataTable(list);
    }

    /**
     * 根据门类id查询档案信息列表
     *
     * @param categoryId
     * @return
     */
    @GetMapping(value = "/getByCategoryId")
    @ApiOperation(value = "根据门类id查询档案信息列表", notes = "根据门类id查询档案信息列表")
    public TableDataInfo getByCategoryId(Long categoryId, Long docType) {
//        startPage();
//        List<DocInfo> docInfoList = docInfoService.getByCategoryId(categoryId, docType);
//        return getDataTable(docInfoList);
        return null;
    }

    /**
     * 档案管理模块
     * 根据门类id查询档案管理信息
     *
     * @param categoryId
     * @return
     */
    @GetMapping(value = "/docManage")
    @Log(title = "查询档案列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "根据门类id查询档案管理信息", notes = "根据门类id查询档案管理信息")
    public TableDataInfo docManage(Long categoryId, Long type, Integer archiveStatus) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        startPage();
        List<DocInfo> docInfoList = docInfoService.docManage(categoryId, type, archiveStatus, sysUser);
        return getDataTable(docInfoList);
    }

    /**
     * 档案归档操作
     *
     * @param vo
     * @return
     */
    @Log(title = "档案信息", businessType = BusinessType.UPDATE)
    @PostMapping("/editArchiveStatus")
    @ApiOperation(value = "档案归档操作", notes = "档案归档操作")
    public AjaxResult editArchiveStatus(@RequestBody ArchiveStatusVO vo) {
        return toAjax(docInfoService.updateArchiveStatus(vo));
    }

    /**
     * 导出档案信息列表
     *
     * @param response
     * @param docInfo
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:export')")
    @Log(title = "档案信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "查询档案信息导出", notes = "查询档案信息导出")
    public void export(HttpServletResponse response, DocInfo docInfo) {
        List<DocInfo> list = docInfoService.selectDocInfoList(docInfo);
        ExcelUtil<DocInfo> util = new ExcelUtil<>(DocInfo.class);
        util.exportExcel(response, list, "档案信息数据");
    }

    /**
     * 导入档案信息下载模板
     *
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<DocInfo> util = new ExcelUtil<>(DocInfo.class);
        util.importTemplateExcel(response, "档案列表信息");
    }

    /**
     * 导入档案鉴定
     * 下载模板
     * @param response
     */
    @PostMapping("/importTemplateAuthenticate")
    public void importTemplateAuthenticate(HttpServletResponse response) {
        ExcelUtil<AuthenticateVO> util = new ExcelUtil<>(AuthenticateVO.class);
        util.importTemplateExcel(response, "档案鉴定");
    }

    /**
     * 导入档案信息数据
     *
     * @param file
     * @return
     * @throws Exception
     */
    // @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<DocInfo> util = new ExcelUtil<>(DocInfo.class);
        List<DocInfo> list = util.importExcel(file.getInputStream());
        String message = docInfoService.importDocInfo(list);
        return success(message);
    }

    /**
     * 导入档案鉴定 数据
     *
     * @param file
     * @return
     * @throws Exception
     */
    // @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importDataAuthenticate")
    public AjaxResult importDataAuthenticate(MultipartFile file) throws Exception {
        ExcelUtil<DocInfo> util = new ExcelUtil<>(DocInfo.class);
        List<DocInfo> list = util.importExcel(file.getInputStream());
        String message = docInfoService.importDocInfo(list);
        return success(message);
    }

    /**
     * 获取档案信息详细信息
     *
     * @param id
     * @return
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:query')")
    @GetMapping(value = "/{id}")
    @Log(title = "在线查阅档案详细信息", businessType = BusinessType.OTHER)
    @ApiOperation(value = "获取档案信息详细信息", notes = "获取档案信息详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(docInfoService.selectDocInfoById(id, getLoginUser().getUser()));
    }

    /**
     * 新增档案信息
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "新增档案信息", notes = "新增档案信息")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DocInfoFileVo vo) {
        DocInfo docInfo = new DocInfo();
        BeanUtils.copyBeanProp(docInfo, vo);
        docInfoService.insertDocInfo(docInfo);
        insetDocAttachments(vo, docInfo.getId());

        return success("添加成功！");
    }

    /**
     * 修改档案信息
     *
     * @param vo
     * @return
     */
    @Log(title = "档案信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ApiOperation(value = "修改档案信息", notes = "修改档案信息")
    public AjaxResult edit(@RequestBody DocInfoFileVo vo) {
        DocInfo docInfo = new DocInfo();
        BeanUtils.copyBeanProp(docInfo, vo);
        docInfo.setId(vo.getDocId());
        docInfoService.updateDocInfo(docInfo);
        insetDocAttachments(vo, vo.getDocId());

        return success("编辑成功！");
    }

    private void insetDocAttachments(@RequestBody DocInfoFileVo vo, Long id) {
        if (StringUtils.isNotEmpty(vo.getFileId())) {
            String[] split = vo.getFileId().split(",");
            for (String s : split) {
                DocAttachments docAttachments = new DocAttachments();
                docAttachments.setDocId(id);//档案信息id
                docAttachments.setAttachments(Long.valueOf(s));//附件id
                docAttachmentsService.insertDocAttachments(docAttachments);
            }
        }
    }

    /**
     * 删除档案信息以及附件关联关系
     *
     * @param ids
     * @return
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:remove')")
    @Log(title = "档案信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除档案信息以及附件关联关系", notes = "删除档案信息以及附件关联关系")
    public AjaxResult remove(@PathVariable Long[] ids) {
        //删除档案信息
        docInfoService.deleteDocInfoByIds(ids);
        //根据档案信息id删除对应附件
        Long[] docAttachments = docAttachmentsService.queryDocinfoIdsBySysUpFileIds(ids);
        if (docAttachments.length > 0){
            sysUpFileService.deleteSysUpFileByIds(docAttachments);
            //删除档案相关联附件信息
            docAttachmentsService.removeDocInfoByIds(ids);
        }
        return success("删除成功!");
    }


    /**
     * 根据档案信息ids查询电子文件信息上传列表
     *
     * @param docIds
     * @return
     */
    @GetMapping("/getByDocInfoId")
    @ApiOperation(value = "根据档案信息ids查询电子文件信息上传列表")
    public TableDataInfo getByDocInfoId(String docIds) {
        if (StringUtils.isNotEmpty(docIds)) {
            String[] split = docIds.split(",");
            Long[] longArray = convertStringArrayToLongArray(split);
            startPage();
            List<FileDocVo> list = docInfoService.queryGetByDocInfoId(longArray);
            return getDataTable(list);
        }
        return getDataTable(new ArrayList<>());
    }

    /**
     * 档案管理
     * 档案管理,根据档案信息ids查询电子文件信息上传列表
     *
     * @param docIds
     * @return
     */
    @GetMapping("/getByDocManageInfoId")
    @ApiOperation(value = "档案管理,根据档案信息ids查询电子文件信息上传列表")
    public TableDataInfo getByDocManageInfoId(String docIds) {
        if (StringUtils.isNotEmpty(docIds)) {
            String[] split = docIds.split(",");
            Long[] longArray = convertStringArrayToLongArray(split);
            startPage();
            List<FileDocVo> list = docInfoService.queryGetByDocManageInfoId(longArray);
            return getDataTable(list);
        }
        return getDataTable(new ArrayList<>());
    }

    /**
     * 合并档案
     *
     * @param vo
     * @return
     */
    @Log(title = "合并档案", businessType = BusinessType.UPDATE)
    @PostMapping("/merge")
    @ApiOperation(value = "合并档案", notes = "合并档案")
    public AjaxResult merge(@RequestBody MergeVO vo) {
        String[] split = vo.getDocIds().split(",");
        Long[] longs = convertStringArrayToLongArray(split);
        //根据传入ids查询档案信息
        List<DocInfo> docInfoList = docInfoService.selectDocInfoIds(longs);
        //获取档案ids
        List<Long> collect = docInfoList.stream().map(DocInfo::getId).collect(Collectors.toList());
        Long[] docIds = toPrimitiveLongArray(collect);
        //通过档案ids，在关联表中获取附件ids
        Long[] attachmentsIds = docAttachmentsService.queryDocinfoIdsBySysUpFileIds(docIds);

        //判断门类、分类、组织机构代码、密级及保管期限是否相同，必须相同的案卷才能进行合并
        List<Long> collect1 = docInfoList.stream().map(DocInfo::getDocType).distinct().collect(Collectors.toList());
        List<Long> collect2 = docInfoList.stream().map(DocInfo::getCategory).distinct().collect(Collectors.toList());
        List<String> collect3 = docInfoList.stream().map(DocInfo::getOrgCode).distinct().collect(Collectors.toList());
        List<Long> collect4 = docInfoList.stream().map(DocInfo::getSecretLevel).distinct().collect(Collectors.toList());
        if (collect1.size() > 1 || collect2.size() > 1 || collect3.size() > 1 || collect4.size() > 1) {
            return AjaxResult.error("门类、分类、组织机构代码、密级及保管期限，必须相同的案卷才能进行合并!");
        }

        List<Long> list = Arrays.asList(longs);
        List<Long> longsSub = list.subList(1, list.size());
        for (Long l : longsSub) {
            //合并需求只保留下第一条档案信息，其他的都删除
            docInfoService.deleteDocInfoById(l);
        }

        //获取第一条档案信息对象
        DocInfo docInfo = docInfoService.selectDocInfoById(longs[0], getLoginUser().getUser());

        //按照需求更改档案信息
        docInfo.setOriDocId(null);//原档案号需要清空
        docInfo.setDocType(docInfo.getDocType());//分类
        docInfo.setCreateBy(docInfo.getCreateBy());//没类
        docInfo.setOrgCode(docInfo.getOrgCode());//组织机构代码
        docInfoService.updateDocInfo(docInfo);

        //先根据docids删除关联表中对应数据
        docAttachmentsService.removeDocInfoByIds(docIds);

        //再重新按照第一条档案信息关联附件并插入关联表中
        for (Long l : attachmentsIds) {
            DocAttachments docAttachments = new DocAttachments();
            docAttachments.setDocId(docInfo.getId());//档案信息id
            docAttachments.setAttachments(l);//附件id
            docAttachmentsService.insertDocAttachments(docAttachments);
        }
        return success("合并成功！");
    }

    /**
     * 删除附件以及关联关系
     *
     * @param ids
     * @return
     */
    @Log(title = "删除附件以及关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除附件以及关联关系", notes = "删除附件以及关联关系")
    public AjaxResult delete(@PathVariable Long[] ids) {
        //删除附件信息
        sysUpFileService.deleteSysUpFileByIds(ids);
        //删除附件关联关系
        docAttachmentsService.deleteDocAttachmentsByIds(ids);
        return success();
    }

    /**
     * 删除的附件还原
     */
    @Log(title = "删除的附件还原", businessType = BusinessType.DELETE)
    @PostMapping("attachmentsRestore/{ids}")
    @ApiOperation(value = "删除附件以及关联关系", notes = "删除的附件还原")
    public AjaxResult attachmentsRestore(@PathVariable Long[] ids) {
        //删除附件信息
        sysUpFileService.deleteSysUpFileByIds(ids);
        //删除附件关联关系
        docAttachmentsService.deleteDocAttachmentsByIds(ids);
        return success();
    }

    /**
     * 打散档案文件
     *
     * @param vo
     * @return
     */
    @PostMapping("/scatter")
    @ApiOperation(value = "打散档案文件", notes = "打散档案文件")
    public AjaxResult scatter(@RequestBody MergeVO vo) {
        String[] split = vo.getDocIds().split(",");
        Long[] longs = convertStringArrayToLongArray(split);
        docAttachmentsService.removeDocInfoByIds(longs);
        return success();
    }

    /**
     * 散文件查询列表
     *
     * @return
     */
    @GetMapping("/scatterFiles")
    @ApiOperation(value = "散文件查询列表", notes = "散文件查询列表")
    public TableDataInfo scatterFiles() {
        startPage();
        List<SysUpFile> list = sysUpFileService.selectScatterFileList();
        return getDataTable(list);
    }

    /**
     * 批量下载文件生成压缩包
     */
    @ApiOperation(value = "批量下载文件生成压缩包", notes = "批量下载文件生成压缩包")
    @GetMapping("/download/fileZip")
    public void sysUpfileDownloadZip(Long[] attachmentId, HttpServletResponse response) throws IOException, MinioException {
        if (attachmentId != null && attachmentId.length > 0) {
            List<SysUpFile> list = docInfoService.queryVolumeFile(attachmentId,response);
            if (list == null || list.isEmpty()) {
                throw new IOException("文件不存在");
            }
        }
    }


    /**
     * 图片预览
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "图片预览", notes = "图片预览")
    @GetMapping("/images/{id}")
    public AjaxResult viewImage(@PathVariable Long id) {
        SysUpFile sysUpFile = sysUpFileService.selectSysUpFileById(id);
        String path = sysUpFile.getPath();
        return success(minioUtil.getUrl() + ":" + minioUtil.getPort() + "/" + minioUtil.getBucketName() + "/" + path);
    }

    /**
     * 档案鉴定销毁
     *
     * @param docInfo
     * @return
     */
    @ApiOperation(value = "档案鉴定销毁", notes = "档案鉴定销毁")
    @PostMapping("/destroy")
    public AjaxResult destroy(@RequestBody DocInfo docInfo) {
        return toAjax(docInfoService.updateDocInfoStatus(docInfo));
    }

    /**
     * 档案销毁,代办销毁,彻底销毁
     *
     * @param docInfo
     * @return
     */
    @ApiOperation(value = "档案销毁,代办销毁,彻底销毁", notes = "档案销毁,代办销毁,彻底销毁")
    @PostMapping("/destroyFull")
    public AjaxResult destroyFull(@RequestBody DocInfo docInfo) {
        return toAjax(docInfoService.updateDocInfoStatusFull(docInfo));
    }

    /**
     * 档案销毁,代办销毁查询列表
     *
     * @return
     */
    @ApiOperation(value = "档案销毁,代办销毁查询列表", notes = "档案销毁,代办销毁查询列表")
    @GetMapping("/destroydbList")
    public TableDataInfo destroydbList(DocInfo docInfo) {
        startPage();
        docInfo.setStatus(-10L);
        return getTableDataText(docInfo);
    }

    /**
     * 档案销毁,历史销毁查询列表
     *
     * @return
     */
    @ApiOperation(value = "历史销毁查询列表", notes = "历史销毁查询列表")
    @GetMapping("/destroydbFullList")
    public TableDataInfo destroydbFullList(DocInfo docInfo) {
        startPage();
        docInfo.setStatus(-20L);
        return getTableDataText(docInfo);
    }

    private TableDataInfo getTableDataText(DocInfo docInfo) {
        List<DocInfo> list = docInfoService.selectDocInfoList(docInfo);
        list.forEach(m -> {
            //将保密级别期限id翻译成文本
            SysDictData secretLevel = dictDataService.selectDictDataById(m.getSecretLevel());
            m.setSecretLevelText(secretLevel == null ? "" : secretLevel.getDictLabel());
            //档案状态翻译
            String status = dictDataService.selectDictLabel("doc_status", String.valueOf(m.getStatus()));
            m.setStatusText(status == null ? "" : status);

        });
        return getDataTable(list);
    }

    /**
     * 档案鉴定-保管到期列表
     *
     * @param docInfo
     * @return
     */
    @ApiOperation(value = "档案鉴定-保管到期列表", notes = "档案鉴定-保管到期列表")
    @GetMapping("/authenticateExpire")
    public TableDataInfo authenticateExpire(DocInfo docInfo) {
        startPage();
        List<DocInfo> list = docInfoService.authenticateExpire(docInfo);
        return getDataTable(list);
    }

    /**
     * 档案鉴定-保管鉴定页签
     *
     * @param docInfo
     * @return
     */
    @ApiOperation(value = "档案鉴定-保管鉴定页签", notes = "档案鉴定-保管鉴定页签")
    @GetMapping("/authenticateApprove")
    public TableDataInfo authenticateApprove(DocInfo docInfo) {
        startPage();
        if(docInfo.getStatus() == 1L){//总数
            docInfo.setStatus(null);
            docInfo.setDelFlag("0");
            List<DocInfo> list1 = docInfoService.authenticateApprove(docInfo);
            return getDataTable(list1);
        }
        if(docInfo.getStatus() == 2L){//待鉴定
            docInfo.setDelFlag("0");
            docInfo.setStatus(10L);
            List<DocInfo> list2 = docInfoService.authenticateApprove(docInfo);
            return getDataTable(list2);
        }
        if(docInfo.getStatus() == 3L){//审批中
            docInfo.setDelFlag("0");
            docInfo.setStatus(20L);
            List<DocInfo> list3 = docInfoService.authenticateApprove(docInfo);
            return getDataTable(list3);
        }
        if(docInfo.getStatus() == 4L){//通过
            docInfo.setDelFlag("0");
            docInfo.setStatus(100L);
            List<DocInfo> list4 = docInfoService.authenticateApprove(docInfo);
            return getDataTable(list4);
        }
        if(docInfo.getStatus() == 5L){//驳回
            docInfo.setDelFlag("0");
            docInfo.setStatus(30L);
            List<DocInfo> list5 = docInfoService.authenticateApprove(docInfo);
            return getDataTable(list5);
        }
        return null;
    }

    /**
     * 审批状态统计
     * @return
     */
    @ApiOperation(value = "审批状态统计", notes = "审批状态统计")
    @GetMapping("/approveTotal")
    public HashMap<String, Object> approveTotal(DocInfo docInfo) {
        docInfo.setStatus(null);
        docInfo.setDelFlag("0");
        List<DocInfo> docInfos = docInfoService.authenticateApprove(docInfo);
        //待审批
        List<DocInfo> collect = docInfos.stream().filter(m -> m.getStatus() == 0L).collect(Collectors.toList());
        //审批中
        List<DocInfo> collect1 = docInfos.stream().filter(m -> m.getStatus() == 20L).collect(Collectors.toList());
        //审批通过
        List<DocInfo> collect2 = docInfos.stream().filter(m -> m.getStatus() == 100L).collect(Collectors.toList());
        //审批驳回
        List<DocInfo> collect3 = docInfos.stream().filter(m -> m.getStatus() == 30L).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("总", docInfos.size());
        map.put("待鉴定", collect.size());
        map.put("审批中", collect1.size());
        map.put("已通过", collect2.size());
        map.put("被驳回", collect3.size());
        return map;
    }

    /**
     * 发起鉴定延期流程,状态为20审批中
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "发起鉴定延期流程", notes = "发起鉴定延期流程")
    @PostMapping("/delay")
    public AjaxResult delay(@RequestBody DelayVO vo) {
        docInfoService.updateStatus(vo);
        return success("发起鉴定延期流程!");
    }

    /**
     * 审批档案延期流程,审批通过状态为100
     * @return
     */
    @ApiOperation(value = "审批档案延期流程", notes = "审批档案延期流程")
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody(required=false) Map<String, Object> variables,
                              @RequestBody DelayVO vo) {
        docInfoService.updateStatusPass(vo,variables);
        return AjaxResult.success();
    }

    /**
     * 查询我的待办任务列表
     */
    @ApiOperation("查询我的待办任务列表")
    @PostMapping("/mylist")
    @ResponseBody
    public List<TaskInfo> mylist(@RequestBody TaskInfo param) {
        String username = getUsername();
        TaskQuery condition = taskService.createTaskQuery().taskAssignee(username);
        if (StringUtils.isNotEmpty(param.getTaskName())) {
            condition.taskName(param.getTaskName());
        }
        if (StringUtils.isNotEmpty(param.getProcessName())) {
            condition.processDefinitionName(param.getProcessName());
        }
        // 过滤掉流程挂起的待办任务
        int total = condition.active().orderByTaskCreateTime().desc().list().size();
        int start = (param.getPageNum()-1) * param.getPageSize();
        List<Task> taskList = condition.active().orderByTaskCreateTime().desc().listPage(start, param.getPageSize());
        List<TaskInfo> tasks = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        taskList.forEach(a->{
            ProcessInstance process = runtimeService.createProcessInstanceQuery().processInstanceId(a.getProcessInstanceId()).singleResult();
            TaskInfo info = new TaskInfo();
            info.setAssignee(a.getAssignee());
            info.setBusinessKey(process.getBusinessKey());
            info.setCreateTime(sdf.format(a.getCreateTime()));
            info.setTaskName(a.getName());
            info.setExecutionId(a.getExecutionId());
            info.setProcessInstanceId(a.getProcessInstanceId());
            info.setProcessName(process.getProcessDefinitionName());
            info.setStarter(process.getStartUserId());
            info.setStartTime(sdf.format(process.getStartTime()));
            info.setTaskId(a.getId());
            String formKey = formService.getTaskFormData(a.getId()).getFormKey();
            info.setFormKey(formKey);
            tasks.add(info);
        });
        List<TaskInfo> collect = tasks.stream().filter(e -> StringUtils.equals(e.getStarter(), param.getId())).collect(Collectors.toList());
        return collect;
    }

    /**
     * 驳回鉴定延期，状态30
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "驳回鉴定延期", notes = "驳回鉴定延期")
    @GetMapping("/back/{taskId}/{sid}")
    @ResponseBody
    public AjaxResult back(@PathVariable String taskId, @PathVariable String sid,@RequestBody DelayVO vo) {
        Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult().getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 寻找流程实例当前任务的activeId
        Execution execution = runtimeService.createExecutionQuery().executionId(t.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode currentNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(activityId);
        FlowNode targetNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(sid);
        // 创建连接线
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newFlow");
        newSequenceFlow.setSourceFlowElement(currentNode);
        newSequenceFlow.setTargetFlowElement(targetNode);
        newSequenceFlowList.add(newSequenceFlow);
        // 备份原有方向
        List<SequenceFlow> dataflows = currentNode.getOutgoingFlows();
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(dataflows);
        // 清空原有方向
        currentNode.getOutgoingFlows().clear();
        // 设置新方向
        currentNode.setOutgoingFlows(newSequenceFlowList);
        // 完成当前任务
        taskService.addComment(taskId, t.getProcessInstanceId(), "comment", "跳转节点");
        taskService.complete(taskId);
        // 恢复原有方向
        currentNode.setOutgoingFlows(oriSequenceFlows);

        docInfoService.updateStatusByBack(vo);

        return AjaxResult.success();
    }


    /**
     * 回收站档案信息查询列表
     */
    @GetMapping("/recycle")
    @ApiOperation(value = "回收站档案信息查询列表", notes = "回收站档案信息查询列表")
    public TableDataInfo recycle(DocInfo docInfo) {
        startPage();
        docInfo.setDelFlag("1");
        List<DocInfo> list = docInfoService.selectDocInfoList(docInfo);
        return getDataTable(list);
    }

    /**
     * 回收站根据档案id查询附件列表
     */
    @GetMapping("/recycleByAttachments")
    @ApiOperation(value = "回收站根据档案id查询附件列表", notes = "回收站根据档案id查询附件列表")
    public TableDataInfo recycleByAttachments(@RequestParam Long[] docIds) {
        startPage();
        List<FileDocVo> fileDocVos = docInfoService.queryGetByRecycleInfoId(docIds);
        return getDataTable(fileDocVos);
    }

    /**
     * 回收站档案信息还原
     */
    @Log(title = "回收站档案信息还原", businessType = BusinessType.DELETE)
    @PostMapping("/restore")
    @ApiOperation(value = "回收站档案信息还原", notes = "回收站档案信息还原")
    public AjaxResult restore(@RequestBody RestoreVO vo) {
        docInfoService.updateDocInfoRestore(vo.getDocIds());
        return success("档案还原成功！");
    }

    /**
     * 回收站删除档案信息
     */
    @Log(title = "回收站删除档案信息", businessType = BusinessType.DELETE)
    @DeleteMapping("recycleRemove/{ids}")
    @ApiOperation(value = "回收站删除档案信息", notes = "回收站删除档案信息")
    public AjaxResult recycleRemove(@PathVariable Long[] ids) {
        //删除档案信息
        docInfoService.recycleRemoveByIds(ids);
        //根据档案信息id删除对应附件
        Long[] docAttachments = docAttachmentsService.queryDocinfoIdsBySysUpFileIds(ids);
        if (docAttachments.length > 0){
            sysUpFileService.deleteSysUpFileByIds(docAttachments);
            //删除档案相关联附件信息
            docAttachmentsService.removeDocInfoByIds(ids);
        }
        return success("删除成功!");
    }

    /**
     * 档案行为列表
     * @return
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:query')")
    @GetMapping("behaviors")
    @ApiOperation(value = "档案行为列表(日志页)", notes = "档案行为列表")
    public TableDataInfo queryDocBehaviorList() {
        startPage();
        DocBehavior docBehavior = new DocBehavior();
        SysRole sysRole = sysRoleService.selectRoleById(getUserId());
        if (!sysRole.getRoleName().equals("超级管理员")) {
            docBehavior.setUsername(getUsername());
        }
        return getDataTable(docInfoService.queryDocBehaviorList(docBehavior));
    }

    /**
     * 查询附件审批
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @GetMapping("/approval/result")
    @ApiOperation(value = "查询附件审批结果", notes = "查询附件审批结果")
    public AjaxResult approvalResult(String idStr) {
        String[] strIds = idStr.split(",");
        Long[] attachmentIds = new Long[strIds.length];
        for (int i = 0; i < strIds.length; i++) {
            attachmentIds[i] = Long.parseLong(strIds[i]);
        }
        String result = docInfoService.approvalResult(getUserId(), attachmentIds);
        if (result.equals("true")) {//能看
            return new AjaxResult(200, "200");
        } else if (result.equals("false")) {//不能看
            return new AjaxResult(200, "201");
        } else {//重复提交
            return new AjaxResult(200, "202");
        }
    }

    /**
     * 添加附件审批
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @PostMapping("/approval/add")
    @ApiOperation(value = "添加附件审批", notes = "添加附件审批")
    public AjaxResult addApprovalResult(@RequestBody AttachmentApproval attachmentApproval) {
        // attachmentApproval.setUserId(getUserId());
        docInfoService.addApprovalResult(attachmentApproval);
        return AjaxResult.success();
    }

    /**
     * 审批附件审批
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @PostMapping("/approval/update")
    @ApiOperation(value = "审批附件审批", notes = "审批附件审批")
    public AjaxResult updateApprovalResult(@RequestBody AttachmentApproval attachmentApproval) {
        // attachmentApproval.setUserId(getUserId());
        docInfoService.updateApprovalResult(attachmentApproval);
        return AjaxResult.success();
    }

    /**
     * 等待我审批的附件审批列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @GetMapping("/approval/myTask")
    @ApiOperation(value = "等待我审批的附件审批列表", notes = "等待我审批的附件审批列表")
    public TableDataInfo myTask(AttachmentApproval attachmentApproval) throws Exception {
        startPage();
        List<AttachmentApproval> list = docInfoService.myTask(attachmentApproval);
        return getDataTable(list);
    }

    /**
     * 查看我申请过的附件列表
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @GetMapping("/approval/myRequest")
    @ApiOperation(value = "查看我申请过的附件列表", notes = "查看我申请过的附件列表")
    public TableDataInfo myRequest(AttachmentApproval attachmentApproval) throws Exception {
        attachmentApproval.setUserId(getUserId());
        startPage();
        List<AttachmentApproval> list = docInfoService.myRequest(attachmentApproval);
        return getDataTable(list);
    }

    /**
     * 过期处理附件审批
     */
    // @PreAuthorize("@ss.hasPermi('docmanage:docinfo:list')")
    @PostMapping("/approval/expire")
    @ApiOperation(value = "过期处理附件审批", notes = "过期处理附件审批")
    public AjaxResult expireApprovalResult() throws Exception {
        docInfoService.expireApprovalResult();
        return AjaxResult.success();
    }

}
