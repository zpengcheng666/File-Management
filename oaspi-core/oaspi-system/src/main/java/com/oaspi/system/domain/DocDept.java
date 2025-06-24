package com.oaspi.system.domain;

import com.oaspi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 部门
 * @Author：zpc
 * @Date：2025/1/4 上午11:02
 */
@Data
public class DocDept {
    private Long deptId;

    @Excel(name = "部门名称")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @Excel(name = "部门编码")
    @ApiModelProperty(value = "部门编码")
    private String deptCode;

    @Excel(name = "部门负责人")
    @ApiModelProperty(value = "部门负责人")
    private String deptLinker;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "公司id")
    private Long companyId;

    @ApiModelProperty(value = "父id")
    private Long parentId;
}
