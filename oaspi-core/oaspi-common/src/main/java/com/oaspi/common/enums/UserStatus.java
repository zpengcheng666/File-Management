package com.oaspi.common.enums;

/**
 * 用户状态
 * 
 * @author oaspi
 */
public enum UserStatus
{
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除"), NOT_UPDATE_PWD("3", "未改过密码");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
