package com.oaspi.web.controller.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oaspi.common.annotation.Log;
import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.enums.BusinessType;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.oaspi.system.domain.Process;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * 流程管理
 */
@Api(value = "部署管理接口")
@Controller
@RequestMapping("/flow/manage")
public class FlowController extends BaseController {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    ProcessEngineConfiguration configuration;

    @Resource
    IdentityService identityService;


    @ApiOperation("上传一个工作流文件")
    @RequestMapping(value = "/uploadworkflow", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult fileupload(@RequestParam MultipartFile uploadfile) {
        try {
            String filename = uploadfile.getOriginalFilename();
            InputStream is = uploadfile.getInputStream();
            if (filename.endsWith("zip")) {
                repositoryService.createDeployment().name(filename).addZipInputStream(new ZipInputStream(is)).deploy();
            } else if (filename.endsWith("bpmn") || filename.endsWith("xml")) {
                repositoryService.createDeployment().name(filename).addInputStream(filename, is).deploy();
            } else {
                return AjaxResult.error("文件格式错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("部署失败");
        }
        return AjaxResult.success("部署成功");
    }

    @ApiOperation("查询已部署工作流列表")
    @RequestMapping(value = "/getprocesslists", method = RequestMethod.POST)
    @ResponseBody
    public TableDataInfo getlist(@RequestParam(required = false) String key, @RequestParam(required = false) String name,
                                 @RequestParam(required = false) Boolean latest, Integer pageSize, Integer pageNum) {
        ProcessDefinitionQuery queryCondition = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotEmpty(key)) {
            queryCondition.processDefinitionKey(key);
        }
        if (StringUtils.isNotEmpty(name)) {
            queryCondition.processDefinitionName(name);
        }
        if (latest) {
            queryCondition.latestVersion();
        }
        int total = queryCondition.list().size();
        int start = (pageNum - 1) * pageSize;
        List<ProcessDefinition> pageList = queryCondition.orderByDeploymentId().desc().listPage(start, pageSize);
        List<Process> mylist = new ArrayList<Process>();
        for (int i = 0; i < pageList.size(); i++) {
            Process p = new Process();
            p.setDeploymentId(pageList.get(i).getDeploymentId());
            p.setId(pageList.get(i).getId());
            p.setKey(pageList.get(i).getKey());
            p.setName(pageList.get(i).getName());
            p.setResourceName(pageList.get(i).getResourceName());
            p.setDiagramresourceName(pageList.get(i).getDiagramResourceName());
            p.setVersion(pageList.get(i).getVersion());
            p.setSuspended(pageList.get(i).isSuspended());
            mylist.add(p);
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(mylist);
        rspData.setTotal(total);
        return rspData;
    }

    @ApiOperation("删除一次部署的工作流")
    @RequestMapping(value = "/remove/{deploymentId}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult remove(@PathVariable String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return AjaxResult.success();
    }


    @ApiOperation("查看工作流图片")
    @RequestMapping(value = "/showresource", method = RequestMethod.GET)
    public void showresource(@RequestParam("pdid") String pdid,
                       HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg;charset=UTF-8");
        response.setHeader("Content-Disposition","inline;filename=process.jpg");
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pdid);
        ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
        InputStream is = diagramGenerator.generateDiagram(bpmnModel, "jpg",  "宋体", "宋体", "宋体", configuration.getClassLoader(), 1.0);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(is, output);
    }

    @ApiOperation("查看工作流定义")
    @RequestMapping(value = "/showProcessDefinition/{pdid}/{resource}", method = RequestMethod.GET)
    public void showProcessDefinition(@PathVariable("pdid") String pdid, @PathVariable(value="resource") String resource,
                       HttpServletResponse response) throws Exception {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition","inline;filename=process.bpmn20.xml");
        InputStream is = repositoryService.getResourceAsStream(pdid, resource);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(is, output);
    }

    @ApiOperation("将流程定义转为模型")
    @RequestMapping(value = "/exchangeProcessToModel/{pdid}", method = RequestMethod.GET)
    @ResponseBody
    public String exchangeProcessToModel(@PathVariable("pdid") String pdid, HttpServletResponse response) throws Exception {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definition.getId());
        ObjectNode objectNode = new BpmnJsonConverter().convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(definition.getKey());
        modelData.setName(definition.getName());
        modelData.setCategory(definition.getCategory());
        ObjectNode modelJson = new ObjectMapper().createObjectNode();
        modelJson.put(ModelDataJsonConstants.MODEL_NAME, definition.getName());
        modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, definition.getDescription());
        List<Model> models = repositoryService.createModelQuery().modelKey(definition.getKey()).list();
        if (models.size() > 0) {
            Integer version = models.get(0).getVersion();
            version++;
            modelJson.put(ModelDataJsonConstants.MODEL_REVISION, version);
            // 删除旧模型
            repositoryService.deleteModel(models.get(0).getId());
            modelData.setVersion(version);
        } else {
            modelJson.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        }
        modelData.setMetaInfo(modelJson.toString());
        modelData.setDeploymentId(definition.getDeploymentId());
        // 保存新模型
        repositoryService.saveModel(modelData);
        // 保存模型json
        repositoryService.addModelEditorSource(modelData.getId(), objectNode.toString().getBytes(StandardCharsets.UTF_8));
        return objectNode.toString();
    }

    @ApiOperation("挂起一个流程定义")
    @RequestMapping(value = "/suspendProcessDefinition", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult suspendProcessDefinition(@RequestParam("pdid") String pdid, @RequestParam("flag") Boolean flag,
                                               @RequestParam(value="date", required = false) String date) throws Exception {
        if (StringUtils.isNotEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            repositoryService.suspendProcessDefinitionById(pdid, flag,  sdf.parse(date));
        } else {
            repositoryService.suspendProcessDefinitionById(pdid, flag, null);
        }
        return AjaxResult.success();
    }

    @ApiOperation("激活一个流程定义")
    @RequestMapping(value = "/activateProcessDefinition", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult activateProcessDefinition(@RequestParam("pdid") String pdid, @RequestParam("flag") Boolean flag, @RequestParam(value="date", required = false) String date) throws Exception {
        if (StringUtils.isNotEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            repositoryService.activateProcessDefinitionById(pdid, flag,  sdf.parse(date));
        } else {
            repositoryService.activateProcessDefinitionById(pdid, flag, null);
        }
        return AjaxResult.success();
    }


    @ApiOperation("发起一个流程")
    @Log(title = "发起一个流程", businessType = BusinessType.INSERT)
    @PostMapping("/startProcess")
    @ResponseBody
    public AjaxResult startProcess(@RequestParam String pdid)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        identityService.setAuthenticatedUserId(user.getUserName());
        HashMap<String, Object> v = new HashMap<>();
        v.put("name", "wsz");
        runtimeService.startProcessInstanceById(pdid, v);
        return AjaxResult.success();
    }
}
