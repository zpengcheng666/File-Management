package com.aspi.docmanage.domain;

import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 附件审批对象 doc_attachment_approval
 *
 * @author wangy
 * @date 2025-01-09
 */
@Data
@ApiModel(value="附件审批对象", description="附件审批对象")
public class AttachmentApproval extends BaseEntity {

    private Long id;

    private Long userId;

    private Long[] attachmentIds;

    private Long deptId;

    private String startTime;

    private String handleTime;

    private String remark;

    private Integer canStatus;

    private Long attachmentId;

    private Long ids[];

    private String userName;

    private String attachmentName;

    private String refuseReason;

    private String deptName;

    private String companyName;

    private String docName;

    private String type;

    private Long hours;

}
