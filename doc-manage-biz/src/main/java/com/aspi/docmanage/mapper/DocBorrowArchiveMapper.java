package com.aspi.docmanage.mapper;

import java.util.List;
import com.aspi.docmanage.domain.DocBorrowArchive;

/**
 * 借阅档案关联Mapper接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface DocBorrowArchiveMapper 
{
    /**
     * 查询借阅档案关联
     * 
     * @param id 借阅档案关联主键
     * @return 借阅档案关联
     */
    public DocBorrowArchive selectDocBorrowArchiveById(Long id);

    /**
     * 查询借阅档案关联列表
     * 
     * @param docBorrowArchive 借阅档案关联
     * @return 借阅档案关联集合
     */
    public List<DocBorrowArchive> selectDocBorrowArchiveList(DocBorrowArchive docBorrowArchive);

    /**
     * 新增借阅档案关联
     * 
     * @param docBorrowArchive 借阅档案关联
     * @return 结果
     */
    public int insertDocBorrowArchive(DocBorrowArchive docBorrowArchive);

    /**
     * 修改借阅档案关联
     * 
     * @param docBorrowArchive 借阅档案关联
     * @return 结果
     */
    public int updateDocBorrowArchive(DocBorrowArchive docBorrowArchive);

    /**
     * 删除借阅档案关联
     * 
     * @param id 借阅档案关联主键
     * @return 结果
     */
    public int deleteDocBorrowArchiveById(Long id);

    /**
     * 批量删除借阅档案关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocBorrowArchiveByIds(Long[] ids);
}
