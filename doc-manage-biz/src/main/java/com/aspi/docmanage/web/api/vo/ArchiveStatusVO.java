package com.aspi.docmanage.web.api.vo;

import lombok.Data;

@Data
public class ArchiveStatusVO {
    private Long id;
    private String archiveStatus;
    private String reason;
    private String refuseReason;
}
