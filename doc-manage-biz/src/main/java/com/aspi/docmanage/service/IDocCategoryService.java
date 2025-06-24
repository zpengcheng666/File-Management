package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocCategory;
import com.aspi.docmanage.web.api.vo.ClassificationVO;
import com.aspi.docmanage.web.api.vo.SelectTreeNode;
import com.oaspi.common.core.domain.entity.SysDictData;

/**
 * 档案分类Service接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface IDocCategoryService 
{
    /**
     * 查询档案分类
     * 
     * @param id 档案分类主键
     * @return 档案分类
     */
    public DocCategory selectDocCategoryById(Long id);

    /**
     * 查询档案分类列表
     * 
     * @param docCategory 档案分类
     * @return 档案分类集合
     */
    public List<DocCategory> selectDocCategoryList(DocCategory docCategory);

    /**
     * 新增档案分类
     * 
     * @param docCategory 档案分类
     * @return 结果
     */
    public int insertDocCategory(DocCategory docCategory);

    /**
     * 修改档案分类
     * 
     * @param docCategory 档案分类
     * @return 结果
     */
    public int updateDocCategory(DocCategory docCategory);

    /**
     * 批量删除档案分类
     * 
     * @param ids 需要删除的档案分类主键集合
     * @return 结果
     */
    public int deleteDocCategoryByIds(Long[] ids);

    /**
     * 删除档案分类信息
     * 
     * @param id 档案分类主键
     * @return 结果
     */
    public int deleteDocCategoryById(Long id);

    /**
     * 档案门类树状列表
     * @param docCategory
     * @return
     */
    List<SelectTreeNode> treeList(DocCategory docCategory);

    /**
     * 全部分类下拉框
     * @return
     */
    List<ClassificationVO> fullClassification(SysDictData sysDictData);

    /**
     * 密级下拉框
     * @return
     */
    List<ClassificationVO> secretDuration(SysDictData sysDictData);

    DocCategory selectDocCategoryByIds(Long[] docIds);

    DocCategory selectDocCategoryByCategory(String categoryName);
}
