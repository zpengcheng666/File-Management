package com.aspi.docmanage.domain;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 借阅对象 doc_borrow
 * 
 * @author wangy
 * @date 2025-1-17
 */
@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@ApiModel(value = "借阅对象", description = "借阅对象")
public class DocBorrow extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String category;

    private String secureLevel;

    private Long companyId;

    private Long deptId;

    private Long userId;

    private String companyName;

    private String deptName;

    private String nickName;

    private String startTime;

    private String endTime;

    private Integer borrowStatus;

    private String description;

    private String docInfoNum;

    private Long docInfoId;

    private Integer delFlag;

}
