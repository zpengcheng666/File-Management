package com.aspi.docmanage.mapper;

import java.util.List;
import com.aspi.docmanage.domain.DocBox;

/**
 * 档案盒Mapper接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface DocBoxMapper 
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
     * 删除档案盒
     * 
     * @param boxId 档案盒主键
     * @return 结果
     */
    public int deleteDocBoxByBoxId(String boxId);

    /**
     * 批量删除档案盒
     * 
     * @param boxIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocBoxByBoxIds(String[] boxIds);
}
