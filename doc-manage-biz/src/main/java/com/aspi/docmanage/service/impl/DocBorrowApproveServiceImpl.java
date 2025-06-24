package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocBorrowApproveMapper;
import com.aspi.docmanage.domain.DocBorrowApprove;
import com.aspi.docmanage.service.IDocBorrowApproveService;

/**
 * 借阅审批Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocBorrowApproveServiceImpl implements IDocBorrowApproveService 
{
    @Autowired
    private DocBorrowApproveMapper docBorrowApproveMapper;

    /**
     * 查询借阅审批
     * 
     * @param id 借阅审批主键
     * @return 借阅审批
     */
    @Override
    public DocBorrowApprove selectDocBorrowApproveById(Long id)
    {
        return docBorrowApproveMapper.selectDocBorrowApproveById(id);
    }

    /**
     * 查询借阅审批列表
     * 
     * @param docBorrowApprove 借阅审批
     * @return 借阅审批
     */
    @Override
    public List<DocBorrowApprove> selectDocBorrowApproveList(DocBorrowApprove docBorrowApprove)
    {
        return docBorrowApproveMapper.selectDocBorrowApproveList(docBorrowApprove);
    }

    /**
     * 新增借阅审批
     * 
     * @param docBorrowApprove 借阅审批
     * @return 结果
     */
    @Override
    public int insertDocBorrowApprove(DocBorrowApprove docBorrowApprove)
    {
        docBorrowApprove.setCreateTime(DateUtils.getNowDate());
        return docBorrowApproveMapper.insertDocBorrowApprove(docBorrowApprove);
    }

    /**
     * 修改借阅审批
     * 
     * @param docBorrowApprove 借阅审批
     * @return 结果
     */
    @Override
    public int updateDocBorrowApprove(DocBorrowApprove docBorrowApprove)
    {
        docBorrowApprove.setUpdateTime(DateUtils.getNowDate());
        return docBorrowApproveMapper.updateDocBorrowApprove(docBorrowApprove);
    }

    /**
     * 批量删除借阅审批
     * 
     * @param ids 需要删除的借阅审批主键
     * @return 结果
     */
    @Override
    public int deleteDocBorrowApproveByIds(Long[] ids)
    {
        return docBorrowApproveMapper.deleteDocBorrowApproveByIds(ids);
    }

    /**
     * 删除借阅审批信息
     * 
     * @param id 借阅审批主键
     * @return 结果
     */
    @Override
    public int deleteDocBorrowApproveById(Long id)
    {
        return docBorrowApproveMapper.deleteDocBorrowApproveById(id);
    }
}
