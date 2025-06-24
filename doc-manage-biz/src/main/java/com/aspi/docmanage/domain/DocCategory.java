package com.aspi.docmanage.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.oaspi.common.core.domain.TreeEntity;

/**
 * 档案分类对象 doc_category
 * 
 * @author hongy
 * @date 2024-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "档案分类对象", description = "档案分类对象")
public class DocCategory extends TreeEntity {
    private static final long serialVersionUID = 1L;
    /** 编号 */
    private Long id;

    /** 档案名称 */
    private String name;

    /** 拼音 */
    private String pinyin;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
}
