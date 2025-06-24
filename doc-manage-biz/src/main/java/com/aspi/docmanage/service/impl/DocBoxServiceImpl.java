package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocBoxMapper;
import com.aspi.docmanage.domain.DocBox;
import com.aspi.docmanage.service.IDocBoxService;

/**
 * 档案盒Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocBoxServiceImpl implements IDocBoxService 
{
    @Autowired
    private DocBoxMapper docBoxMapper;

    /**
     * 查询档案盒
     * 
     * @param boxId 档案盒主键
     * @return 档案盒
     */
    @Override
    public DocBox selectDocBoxByBoxId(String boxId)
    {
        return docBoxMapper.selectDocBoxByBoxId(boxId);
    }

    /**
     * 查询档案盒列表
     * 
     * @param docBox 档案盒
     * @return 档案盒
     */
    @Override
    public List<DocBox> selectDocBoxList(DocBox docBox)
    {
        return docBoxMapper.selectDocBoxList(docBox);
    }

    /**
     * 新增档案盒
     * 
     * @param docBox 档案盒
     * @return 结果
     */
    @Override
    public int insertDocBox(DocBox docBox)
    {
        docBox.setCreateTime(DateUtils.getNowDate());
        return docBoxMapper.insertDocBox(docBox);
    }

    /**
     * 修改档案盒
     * 
     * @param docBox 档案盒
     * @return 结果
     */
    @Override
    public int updateDocBox(DocBox docBox)
    {
        return docBoxMapper.updateDocBox(docBox);
    }

    /**
     * 批量删除档案盒
     * 
     * @param boxIds 需要删除的档案盒主键
     * @return 结果
     */
    @Override
    public int deleteDocBoxByBoxIds(String[] boxIds)
    {
        return docBoxMapper.deleteDocBoxByBoxIds(boxIds);
    }

    /**
     * 删除档案盒信息
     * 
     * @param boxId 档案盒主键
     * @return 结果
     */
    @Override
    public int deleteDocBoxByBoxId(String boxId)
    {
        return docBoxMapper.deleteDocBoxByBoxId(boxId);
    }
}
