package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocCollectMapper;
import com.aspi.docmanage.domain.DocCollect;
import com.aspi.docmanage.service.IDocCollectService;

/**
 * 我的收藏Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocCollectServiceImpl implements IDocCollectService 
{
    @Autowired
    private DocCollectMapper docCollectMapper;

    /**
     * 查询我的收藏
     * 
     * @param collectId 我的收藏主键
     * @return 我的收藏
     */
    @Override
    public DocCollect selectDocCollectByCollectId(Long collectId)
    {
        return docCollectMapper.selectDocCollectByCollectId(collectId);
    }

    /**
     * 查询我的收藏列表
     * 
     * @param docCollect 我的收藏
     * @return 我的收藏
     */
    @Override
    public List<DocCollect> selectDocCollectList(DocCollect docCollect)
    {
        return docCollectMapper.selectDocCollectList(docCollect);
    }

    /**
     * 新增我的收藏
     * 
     * @param docCollect 我的收藏
     * @return 结果
     */
    @Override
    public int insertDocCollect(DocCollect docCollect)
    {
        docCollect.setCreateTime(DateUtils.getNowDate());
        return docCollectMapper.insertDocCollect(docCollect);
    }

    /**
     * 修改我的收藏
     * 
     * @param docCollect 我的收藏
     * @return 结果
     */
    @Override
    public int updateDocCollect(DocCollect docCollect)
    {
        docCollect.setUpdateTime(DateUtils.getNowDate());
        return docCollectMapper.updateDocCollect(docCollect);
    }

    /**
     * 批量删除我的收藏
     * 
     * @param collectIds 需要删除的我的收藏主键
     * @return 结果
     */
    @Override
    public int deleteDocCollectByCollectIds(Long[] collectIds)
    {
        return docCollectMapper.deleteDocCollectByCollectIds(collectIds);
    }

    /**
     * 删除我的收藏信息
     * 
     * @param collectId 我的收藏主键
     * @return 结果
     */
    @Override
    public int deleteDocCollectByCollectId(Long collectId)
    {
        return docCollectMapper.deleteDocCollectByCollectId(collectId);
    }
}
