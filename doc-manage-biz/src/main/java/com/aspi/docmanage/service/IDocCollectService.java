package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocCollect;

/**
 * 我的收藏Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocCollectService 
{
    /**
     * 查询我的收藏
     * 
     * @param collectId 我的收藏主键
     * @return 我的收藏
     */
    public DocCollect selectDocCollectByCollectId(Long collectId);

    /**
     * 查询我的收藏列表
     * 
     * @param docCollect 我的收藏
     * @return 我的收藏集合
     */
    public List<DocCollect> selectDocCollectList(DocCollect docCollect);

    /**
     * 新增我的收藏
     * 
     * @param docCollect 我的收藏
     * @return 结果
     */
    public int insertDocCollect(DocCollect docCollect);

    /**
     * 修改我的收藏
     * 
     * @param docCollect 我的收藏
     * @return 结果
     */
    public int updateDocCollect(DocCollect docCollect);

    /**
     * 批量删除我的收藏
     * 
     * @param collectIds 需要删除的我的收藏主键集合
     * @return 结果
     */
    public int deleteDocCollectByCollectIds(Long[] collectIds);

    /**
     * 删除我的收藏信息
     * 
     * @param collectId 我的收藏主键
     * @return 结果
     */
    public int deleteDocCollectByCollectId(Long collectId);
}
