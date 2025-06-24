package com.aspi.docmanage.web.api.vo;

import lombok.Data;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/8 16:37
 */
@Data
public class RoleVO {

    private String corpName;
    private String subCorpName;
    private String deptName;
    private Long deptId;

    private String roleName;
    private Long roleId;
    private String roleDescription;




}