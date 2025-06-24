package com.aspi.docmanage.mapper;

import com.aspi.docmanage.domain.DocCoffers;

import java.util.List;

/**
 * 档案分类Mapper接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface DocCoffersMapper
{

    /**
     * 查询
     *
     * @param docCoffers 库房管理
     * @return 结果
     */
    public List<DocCoffers> coffersList(DocCoffers docCoffers);

    /**
     * 新增库房管理
     * 
     * @param docCoffers 库房管理
     * @return 结果
     */
    public int instCoffers(DocCoffers docCoffers);
    /**
     * 修改
     *
     * @param docCoffers 库房管理
     * @return 结果
     */
    public int updCoffers(DocCoffers docCoffers);

    /**
     * 下架档案
     *
     * @param docCoffers 库房管理
     * @return 结果
     */
    public int down(DocCoffers docCoffers);
    /**
     * 把档案架数据逻辑删除
     *
     * @param docCoffers 库房管理
     * @return 结果
     */
    public int delShelves(DocCoffers docCoffers);
    /**
     * 把库房数据逻辑删除
     *
     * @param docCoffers 库房管理
     * @return 结果
     */
    public int delCoffers(DocCoffers docCoffers);
    /**
     * 把档案盒数据逻辑删除
     *
     * @param docCoffers 库房管理
     * @return 结果
     */
    public int delBox(DocCoffers docCoffers);

    /**
     * 根据名称查询库房
     */
    public DocCoffers getCoffersByName(String name);

}
