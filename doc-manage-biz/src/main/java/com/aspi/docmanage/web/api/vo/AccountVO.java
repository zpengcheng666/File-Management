package com.aspi.docmanage.web.api.vo;

import com.oaspi.common.core.domain.entity.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/8 15:06
 */
@Data
public class AccountVO {

    private String id;
    private String username;
    private String trueName;
    private String email;
    private String phone;
    private String idno;
    private String createdBy;
    private Date createdTime;
    private String status;


    public static AccountVO convert(SysUser sysUser){
        AccountVO accountVO = new AccountVO();
        accountVO.setId(sysUser.getUserId().toString());
        accountVO.setUsername(sysUser.getUserName());
        accountVO.setTrueName(sysUser.getNickName());
        accountVO.setEmail(sysUser.getEmail());
        accountVO.setPhone(sysUser.getPhonenumber());
        accountVO.setIdno(sysUser.getIdno());
        accountVO.setCreatedBy(sysUser.getCreateBy());
        accountVO.setCreatedTime(sysUser.getCreateTime());
        accountVO.setStatus(sysUser.getStatus());
        return accountVO;
    }



}