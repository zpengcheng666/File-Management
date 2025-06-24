package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocBox;

/**
 * 档案盒Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocBoxService 
{
    /**
     * 查询档案盒
     * 
     * @param boxId 档案盒主键
     * @return 档案盒
     */
    public DocBox selectDocBoxByBoxId(String boxId);

    /**
     * 查询档案盒列表
     * 
     * @param docBox 档案盒
     * @return 档案盒集合
     */
    public List<DocBox> selectDocBoxList(DocBox docBox);

    /**
     * 新增档案盒
     * 
     * @param docBox 档案盒
     * @return 结果
     */
    public int insertDocBox(DocBox docBox);

    /**
     * 修改档案盒
     * 
     * @param docBox 档案盒
     * @return 结果
     */
    public int updateDocBox(DocBox docBox);

    /**
     * 批量删除档案盒
     * 
     * @param boxIds 需要删除的档案盒主键集合
     * @return 结果
     */
    public int deleteDocBoxByBoxIds(String[] boxIds);

    /**
     * 删除档案盒信息
     * 
     * @param boxId 档案盒主键
     * @return 结果
     */
    public int deleteDocBoxByBoxId(String boxId);
}
