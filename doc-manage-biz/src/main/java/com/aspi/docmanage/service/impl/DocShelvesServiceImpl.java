package com.aspi.docmanage.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.aspi.docmanage.domain.Coffers;
import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.Shelves;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocShelvesMapper;
import com.aspi.docmanage.domain.DocShelves;
import com.aspi.docmanage.service.IDocShelvesService;

/**
 * 档案架Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocShelvesServiceImpl implements IDocShelvesService 
{
    @Autowired
    private DocShelvesMapper docShelvesMapper;

    /**
     * 查询档案架
     * 
     * @param shelvesNo 档案架主键
     * @return 档案架
     */
    @Override
    public DocShelves selectDocShelvesByShelvesNo(Long shelvesNo)
    {
        return docShelvesMapper.selectDocShelvesByShelvesNo(shelvesNo);
    }

    /**
     * 查询档案架列表
     * 
     * @param docShelves 档案架
     * @return 档案架
     */
    @Override
    public List<DocShelves> selectDocShelvesList(DocShelves docShelves)
    {
        return docShelvesMapper.selectDocShelvesList(docShelves);
    }

    /**
     * 新增档案架
     * 
     * @param docShelves 档案架
     * @return 结果
     */
    @Override
    public int insertDocShelves(DocShelves docShelves)
    {
        docShelves.setCreateTime(DateUtils.getNowDate());
        return docShelvesMapper.insertDocShelves(docShelves);
    }

    /**
     * 修改档案架
     * 
     * @param docShelves 档案架
     * @return 结果
     */
    @Override
    public int updateDocShelves(DocShelves docShelves)
    {
        return docShelvesMapper.updateDocShelves(docShelves);
    }

    /**
     * 批量删除档案架
     * 
     * @param shelvesNos 需要删除的档案架主键
     * @return 结果
     */
    @Override
    public int deleteDocShelvesByShelvesNos(Long[] shelvesNos)
    {
        return docShelvesMapper.deleteDocShelvesByShelvesNos(shelvesNos);
    }

    /**
     * 删除档案架信息
     * 
     * @param shelvesNo 档案架主键
     * @return 结果
     */
    @Override
    public int deleteDocShelvesByShelvesNo(Long shelvesNo)
    {
        return docShelvesMapper.deleteDocShelvesByShelvesNo(shelvesNo);
    }

    @Override
    public Map<String, Coffers> getCoffersWithShelves() {
        List<DocShelves> records = docShelvesMapper.selectAll();
        Map<String, Coffers> coffersMap = new LinkedHashMap<>();

        for (DocShelves record : records) {
            String cofferNo = String.valueOf(record.getCoffersNo());
            String id = String.valueOf(record.getId());
            String name = String.valueOf(record.getName());
            String shelvesName = String.valueOf(record.getShelvesName());
            Coffers coffer = coffersMap.computeIfAbsent(cofferNo, k -> {
                Coffers newCoffer = new Coffers(k, new ArrayList<>());
                newCoffer.setName(name); // 设置名称
                return newCoffer;
            });
            coffer.getShelvesList().add(new Shelves(id,shelvesName));

        }

        return coffersMap;
    }
}
