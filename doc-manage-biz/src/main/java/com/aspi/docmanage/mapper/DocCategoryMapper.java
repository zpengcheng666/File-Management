package com.aspi.docmanage.mapper;

import java.util.List;
import com.aspi.docmanage.domain.DocCategory;

/**
 * 档案分类Mapper接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface DocCategoryMapper 
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
     * 删除档案分类
     * 
     * @param id 档案分类主键
     * @return 结果
     */
    public int deleteDocCategoryById(Long id);

    /**
     * 批量删除档案分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocCategoryByIds(Long[] ids);

    DocCategory selectDocCategoryByCategory(String categoryName);
}
