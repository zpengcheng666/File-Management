package com.oaspi.system.controller;

import com.oaspi.common.core.controller.BaseController;
import com.oaspi.system.service.IDocAttachmentsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档案信息与附件关联表
 */
@Api(tags = "档案信息")
@RestController
@RequestMapping("/docAttachments/info")
public class DocAttachmentsController extends BaseController {
    @Autowired
    private IDocAttachmentsService docAttachmentsService;
}
