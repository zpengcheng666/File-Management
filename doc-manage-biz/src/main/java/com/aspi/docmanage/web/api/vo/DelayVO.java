package com.aspi.docmanage.web.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author：zpc
 * @Package：com.aspi.docmanage.web.api.vo
 * @Project：dangan
 * @name：DelayVO
 * @Date：2024/12/23 下午2:04
 * @Filename：DelayVO
 */

@Data
public class DelayVO {
    private Long secretLevel;
    private Date date;
    private String reason;
    private Long taskid;
    private Long docId;
}
