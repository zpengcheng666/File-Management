package com.oaspi.web.controller.activiti;

import com.oaspi.common.core.controller.BaseController;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.core.domain.entity.SysUser;
import com.oaspi.common.core.page.TableDataInfo;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import com.oaspi.system.domain.Meeting;
import com.oaspi.system.domain.Purchase;
import com.oaspi.system.service.IMeetingService;
import com.oaspi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
@Api(value = "会议接口")
@Controller
@RequestMapping("/meeting")
public class MeetingController extends BaseController {

    private String prefix = "activiti/meeting";

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IMeetingService meetingService;



    /**
     * 查询会议列表
     */
    @ApiOperation("查询会议列表")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Meeting meeting)
    {
        startPage();
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        return getDataTable(list);
    }

    /**
     * 导出会议列表
     */
    @ApiOperation("导出会议列表")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Meeting meeting)
    {
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        ExcelUtil<Meeting> util = new ExcelUtil<Meeting>(Meeting.class);
        return util.exportExcel(list, "会议数据");
    }


    /**
     * 新增保存会议
     */
    @ApiOperation("新增保存会议")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Meeting meeting)
    {
        return toAjax(meetingService.insertMeeting(meeting));
    }

    @ApiOperation("修改会议申请")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult edit(Meeting meeting)
    {
        return toAjax(meetingService.updateMeeting(meeting));
    }

    /**
     * 删除会议
     */
    @ApiOperation("删除会议")
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(meetingService.deleteMeetingByIds(ids));
    }

    /**
     * 会议签到
     */
    @ApiOperation("会议签到")
    @GetMapping("/signate")
    @ResponseBody
    public AjaxResult signate(String taskid)
    {
        Task t = taskService.createTaskQuery().taskId(taskid).singleResult();
        String processId = t.getProcessInstanceId();
        ProcessInstance p = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if (p != null) {
            Meeting apply = meetingService.selectMeetingById(Long.parseLong(p.getBusinessKey()));
            return AjaxResult.success(apply);
        }
        return AjaxResult.error("流程不存在");
    }

    /**
     * 填写会议纪要
     */
    @ApiOperation("填写会议纪要")
    @GetMapping("/input")
    @ResponseBody
    public AjaxResult input(String taskid)
    {
        Task t = taskService.createTaskQuery().taskId(taskid).singleResult();
        String processId = t.getProcessInstanceId();
        ProcessInstance p = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if (p != null) {
            Meeting apply = meetingService.selectMeetingById(Long.parseLong(p.getBusinessKey()));
            return AjaxResult.success(apply);
        }
        return AjaxResult.error("流程不存在");
    }
}
