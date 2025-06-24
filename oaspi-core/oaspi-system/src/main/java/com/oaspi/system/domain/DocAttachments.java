package com.oaspi.system.domain;

import lombok.Data;

/**
 * 档案信息与附件关系表 doc_attachments
 */
@Data
public class DocAttachments {
    private Long docId;
    private Long attachments;
}
