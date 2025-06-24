package com.aspi.docmanage.domain;

import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 出入库记录对象 doc_stock_record
 * 
 * @author wangy
 * @date 2025-1-11
 */
@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@ApiModel(value = "出入库记录对象", description = "出入库记录对象")
public class StockRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long cofferId;

    @Excel(name = "库房名称")
    private String cofferName;

    @Excel(name = "出入人员")
    private String personName;

    @Excel(name = "出入日期")
    private String inOutDate;

    @Excel(name = "入库时间")
    private String inTime;

    @Excel(name = "出库时间")
    private String outTime;

    @Excel(name = "备注")
    private String stockRemark;

    private int status;

    private int delFlag;

}
