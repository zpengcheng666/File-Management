package com.aspi.docmanage.web.api.vo;

import com.oaspi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * @Author：zpc
 * @Package：com.aspi.docmanage.web.api.vo
 * @Project：dangan
 * @name：AuthenticateVO
 * @Date：2024/12/26 下午2:19
 * @Filename：AuthenticateVO
 */

@Data
public class AuthenticateVO {
    /** 档案编号 */
    private Long id;

    /** 原档号 */
    @Excel(name = "原档号")
    @ApiModelProperty(value = "原档号")
    private String oriDocId;

    /** 题名 */
    @Excel(name = "题名")
    @ApiModelProperty(value = "题名")
    private String title;

    /** 密级及保管期限 */
    @Excel(name = "密级及保管期限")
    @ApiModelProperty(value = "密级及保管期限")
    private Long secretLevel;

    @Transient
    @ApiModelProperty(value = "密级及保管期限文本")
    private String secretLevelText;

    @Excel(name = "剩余天数")
    @ApiModelProperty(value = "剩余天数")
    private Long remainingDays;
}
