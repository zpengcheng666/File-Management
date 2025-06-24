package com.oaspi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oaspi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author：zpc
 * @Date：2025/1/4
 */

@Data
public class CompanyTreeNodeVO implements Serializable {
    private Long id;
    private Long pid;
    private String name;
    //父级公司名称
    private String parentName;

    /**入驻经办人*/
    private String handleAuthor;

    //入驻时间
    private Date handleTime;

    /**公司状态 1-可用 0-不可用*/
    private int status;

    //部门数量
    private int deptCount;
    //账号数量
    private int userCount;
    //档案数量
    private int docCount;

    /**公司描述*/
    private String description;

    //组织机构代码
    private String orgCode;

    /** 企业联系人 */
    private String companyLinker;

    //联系电话
    private String tel;

    private List<CompanyTreeNodeVO> children = new ArrayList<>();

    private List<DeptTreeNodeVO> depts = new ArrayList<>();
    public void addChild(CompanyTreeNodeVO child){
        if (this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
