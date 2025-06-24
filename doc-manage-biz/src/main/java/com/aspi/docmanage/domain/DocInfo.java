package com.aspi.docmanage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oaspi.common.annotation.Excel;
import com.oaspi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

/**
 * 档案信息对象 doc_info
 * 
 * @author hongy
 * @date 2024-11-02
 */

@Data
@ApiModel(value="案信息对象", description="案信息对象")
public class DocInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 档案编号 */
    private Long id;

    /** 门类 */
    @ApiModelProperty(value = "门类id")
    private Long category;

    @Excel(name = "门类")
    private String categoryName;

    @Transient
    @ApiModelProperty(value = "门类文本")
    private String categoryText;

    @Excel(name = "部门名称")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "创建部门编号")
    private Long deptId;

    @Excel(name = "公司名称")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "公司id")
    private Long companyId;

    /** 分类 */
    @ApiModelProperty(value = "分类")
    private Long docType;

    @Transient
    @ApiModelProperty(value = "分类文本")
    private String docTypeText;

    /** 原档号 */
    @Excel(name = "原档号")
    @ApiModelProperty(value = "原档号")
    private String oriDocId;

    /** 发文单位 */
    @Excel(name = "发文单位")
    @ApiModelProperty(value = "发文单位")
    private String recieveDept;

    /** 题名 */
    @Excel(name = "题名")
    @ApiModelProperty(value = "题名")
    private String title;

    /** 密级及保管期限 */
    @ApiModelProperty(value = "密级及保管期限")
//    @Excel(name = "密级及保管期限", readConverterExp = "秘密永久=9999,秘密30年=30,秘密10年=10")
    private Long secretLevel;

    @Transient
    @ApiModelProperty(value = "密级及保管期限文本")
    private String secretLevelText;

    @Excel(name = "密级及保管期限")
    private String secretName;

    /** 组织机构代码 */
    @Excel(name = "组织机构代码")
    @ApiModelProperty(value = "组织机构代码")
    private String orgCode;

    /** 开放审核 */
    @ApiModelProperty(value = "开放审核")
    private Integer openAudit;

    /** 成文日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成文日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "成文日期")
    private Date writeDate;

    /** 所涉人员 */
    @Excel(name = "所涉人员")
    @ApiModelProperty(value = "所涉人员")
    private String assosiatePersons;

    /** 文号 */
    @Excel(name = "文号")
    @ApiModelProperty(value = "文号")
    private String docContentId;

    /** 专题名称 */
    @Excel(name = "专题名称")
    @ApiModelProperty(value = "专题名称")
    private String specsubiName;

    /** 关键字 */
    @Excel(name = "关键字")
    @ApiModelProperty(value = "关键字")
    private String keywords;

    /** 摄影者 */
    @Excel(name = "摄影者")
    @ApiModelProperty(value = "摄影者")
    private String recoder;

    /** 拍摄地点 */
    @Excel(name = "拍摄地点")
    @ApiModelProperty(value = "拍摄地点")
    private String recordPlace;

    /** 拍摄时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拍摄时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "拍摄时间")
    private Date recordDate;

    /** 时长（秒） */
    @Excel(name = "时长", readConverterExp = "秒=")
    @ApiModelProperty(value = "时长（秒）")
    private Long duration;

    /** 格式名称 */
    @Excel(name = "格式名称")
    @ApiModelProperty(value = "格式名称")
    private String format;

    /** 物品来源 */
    @Excel(name = "物品来源")
    @ApiModelProperty(value = "物品来源")
    private String objFrom;

    /** 责任人 */
    @Excel(name = "责任人")
    @ApiModelProperty(value = "责任人")
    private String resposible;

    /** 内容描述 */
    @Excel(name = "内容描述")
    @ApiModelProperty(value = "内容描述")
    private String content;

    /** 有无上传 */
    @ApiModelProperty(value = "有无上传")
    private Integer hasFile;

    /** 页数 */
    @ApiModelProperty(value = "页数")
    private Long pageCnt;

    /** 档案状态 */
    @ApiModelProperty(value = "档案状态")
    private Long status;

    @ApiModelProperty(value = "状态翻译")
    @Transient
    private String statusText;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "盒子编号")
    private String boxId;

    @ApiModelProperty(value = "上架状态")
    private String shelvesStatus;

    @ApiModelProperty(value = "档案盒名称")
    private String boxName;

    @ApiModelProperty(value = "库房位置")
    private String address;

    @ApiModelProperty(value = "档案架类型")
    private String shelvesType;

    @ApiModelProperty(value = "档案归档状态")
    private String archiveStatus;

    @ApiModelProperty(value = "档案编号")
    private Long shelvesNo;

    @ApiModelProperty(value = "档案编号集合")
    private List<Integer> docIdList;

    @ApiModelProperty(value = "请求归档原因")
    private String reason;

    @ApiModelProperty(value = "拒绝请求归档原因")
    private String refuseReason;

    @Getter
    @Setter
    @ApiModelProperty(value = "上传文件id")
    private String attachments;

    @ApiModelProperty(value = "是否收藏")
    private String collectionType;

    @ApiModelProperty(value = "销毁原因")
    private String destroyReason;

    @ApiModelProperty(value = "剩余天数")
    private Long remainingDays;

    @ApiModelProperty(value = "审批是否同意理由")
    private String reasons;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;

    @ApiModelProperty(value = "移交时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date turnTime;

    @ApiModelProperty(value = "接收人")
    private String recipient;

    @ApiModelProperty(value = "经办人")
    private String operator;

    @ApiModelProperty(value = "提审时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bringTime;

    @ApiModelProperty(value = "审核人")
    private String checker;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;

    @ApiModelProperty(value = "销毁时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date destroyTime;

    @ApiModelProperty(value = "移交状态")
    private int turnStatus;

    @ApiModelProperty(value = "销毁状态")
    private int destroyStatus;

    @ApiModelProperty("移交说明")
    private String explains;

    @ApiModelProperty(value = "鉴定密级")
    private Long authenticSecret;

    private String authenticSecretText;

    @ApiModelProperty(value = "鉴定日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date authenticDate;

    @ApiModelProperty("鉴定延期理由")
    private String authenticReason;
}
