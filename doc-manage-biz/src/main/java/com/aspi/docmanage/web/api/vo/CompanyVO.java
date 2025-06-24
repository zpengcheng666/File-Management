package com.aspi.docmanage.web.api.vo;

import com.oaspi.common.core.domain.entity.SysDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/7 13:16
 */
@Data
@ApiModel(value = "公司", description = "公司信息")
public class CompanyVO {
    @ApiModelProperty(value = "编号")
    private Long id;
    @ApiModelProperty(value = "公司名", required = true)
    private String name;
    @ApiModelProperty(value = "组织机构代码证", required = true)
    private String code;
    @ApiModelProperty(value = "公司描述")
    private String description;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    public SysDept toSysDept(){
        SysDept dept = new SysDept();
        dept.setDeptId(this.getId());
        dept.setStatus(this.getStatus());
        dept.setDeptName(this.getName());
        dept.setRemark(this.getDescription());
        dept.setParentId(0L);
        dept.setStatus(this.getStatus());
        dept.setCreateBy(this.getCreateBy());
        dept.setCreateTime(this.getCreateTime());
        dept.setOrgCode(this.getCode());
        return dept;
    }

    public static CompanyVO fromSysDept(SysDept dept){
        CompanyVO companyVO = new CompanyVO();
        companyVO.setId(dept.getDeptId());
        companyVO.setName(dept.getDeptName());
        companyVO.setDescription(dept.getRemark());
        companyVO.setStatus(dept.getStatus());
        companyVO.setCreateBy(dept.getCreateBy());
        companyVO.setCreateTime(dept.getCreateTime());
        companyVO.setCode(dept.getOrgCode());
        return companyVO;
    }
}