package com.aspi.docmanage.mapper;

import java.util.List;

import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.DocShelves;
import com.aspi.docmanage.web.api.vo.DocCoffersVo;

/**
 * 档案架Mapper接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface DocShelvesMapper 
{
    /**
     * 查询档案架
     * 
     * @param shelvesNo 档案架主键
     * @return 档案架
     */
    public DocShelves selectDocShelvesByShelvesNo(Long shelvesNo);

    /**
     * 查询档案架列表
     * 
     * @param docShelves 档案架
     * @return 档案架集合
     */
    public List<DocShelves> selectDocShelvesList(DocShelves docShelves);

    /**
     * 新增档案架
     * 
     * @param docShelves 档案架
     * @return 结果
     */
    public int insertDocShelves(DocShelves docShelves);

    /**
     * 修改档案架
     * 
     * @param docShelves 档案架
     * @return 结果
     */
    public int updateDocShelves(DocShelves docShelves);

    /**
     * 删除档案架
     * 
     * @param shelvesNo 档案架主键
     * @return 结果
     */
    public int deleteDocShelvesByShelvesNo(Long shelvesNo);

    /**
     * 批量删除档案架
     * 
     * @param shelvesNos 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocShelvesByShelvesNos(Long[] shelvesNos);

    public List<DocShelves> selectAll();
}
