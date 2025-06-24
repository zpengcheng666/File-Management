package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocBorrowArchiveMapper;
import com.aspi.docmanage.domain.DocBorrowArchive;
import com.aspi.docmanage.service.IDocBorrowArchiveService;

/**
 * 借阅档案关联Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocBorrowArchiveServiceImpl implements IDocBorrowArchiveService 
{
    @Autowired
    private DocBorrowArchiveMapper docBorrowArchiveMapper;

    /**
     * 查询借阅档案关联
     * 
     * @param id 借阅档案关联主键
     * @return 借阅档案关联
     */
    @Override
    public DocBorrowArchive selectDocBorrowArchiveById(Long id)
    {
        return docBorrowArchiveMapper.selectDocBorrowArchiveById(id);
    }

    /**
     * 查询借阅档案关联列表
     * 
     * @param docBorrowArchive 借阅档案关联
     * @return 借阅档案关联
     */
    @Override
    public List<DocBorrowArchive> selectDocBorrowArchiveList(DocBorrowArchive docBorrowArchive)
    {
        return docBorrowArchiveMapper.selectDocBorrowArchiveList(docBorrowArchive);
    }

    /**
     * 新增借阅档案关联
     * 
     * @param docBorrowArchive 借阅档案关联
     * @return 结果
     */
    @Override
    public int insertDocBorrowArchive(DocBorrowArchive docBorrowArchive)
    {
        docBorrowArchive.setCreateTime(DateUtils.getNowDate());
        return docBorrowArchiveMapper.insertDocBorrowArchive(docBorrowArchive);
    }

    /**
     * 修改借阅档案关联
     * 
     * @param docBorrowArchive 借阅档案关联
     * @return 结果
     */
    @Override
    public int updateDocBorrowArchive(DocBorrowArchive docBorrowArchive)
    {
        docBorrowArchive.setUpdateTime(DateUtils.getNowDate());
        return docBorrowArchiveMapper.updateDocBorrowArchive(docBorrowArchive);
    }

    /**
     * 批量删除借阅档案关联
     * 
     * @param ids 需要删除的借阅档案关联主键
     * @return 结果
     */
    @Override
    public int deleteDocBorrowArchiveByIds(Long[] ids)
    {
        return docBorrowArchiveMapper.deleteDocBorrowArchiveByIds(ids);
    }

    /**
     * 删除借阅档案关联信息
     * 
     * @param id 借阅档案关联主键
     * @return 结果
     */
    @Override
    public int deleteDocBorrowArchiveById(Long id)
    {
        return docBorrowArchiveMapper.deleteDocBorrowArchiveById(id);
    }
}
