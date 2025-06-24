package com.aspi.docmanage.domain;


import com.oaspi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 移交和附件关系对象 turn_attachment
 *
 * @author zpc
 * @date 2025-01-09
 */
@Data
public class TurnAttachment extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long turnId;
    private Long attachment;
}

