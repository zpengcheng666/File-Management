package com.aspi.docmanage.domain;

import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 档案行为对象 doc_behavior
 * 
 * @author wangy
 * @date 2024-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "档案行为对象", description = "档案行为对象")
public class DocBehavior extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 档案题名 */
    private String docTitle;

    /** 用户名 */
    private String username;

    /** 姓名 */
    private String nickName;

    /** 行为 */
    private String behavior;

    /** 行为时间 */
    private String behaviorTime;

    /** 文档所属部门名称 */
    private String deptName;

    /** 文档所属公司名称 */
    private String companyName;

    /** 查看人所属部门名称 */
    private String personDeptName;

    /** 查看人所属公司名称 */
    private String personCompanyName;

}
