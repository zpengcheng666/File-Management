package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocBorrowApprove;

/**
 * 借阅审批Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocBorrowApproveService 
{
    /**
     * 查询借阅审批
     * 
     * @param id 借阅审批主键
     * @return 借阅审批
     */
    public DocBorrowApprove selectDocBorrowApproveById(Long id);

    /**
     * 查询借阅审批列表
     * 
     * @param docBorrowApprove 借阅审批
     * @return 借阅审批集合
     */
    public List<DocBorrowApprove> selectDocBorrowApproveList(DocBorrowApprove docBorrowApprove);

    /**
     * 新增借阅审批
     * 
     * @param docBorrowApprove 借阅审批
     * @return 结果
     */
    public int insertDocBorrowApprove(DocBorrowApprove docBorrowApprove);

    /**
     * 修改借阅审批
     * 
     * @param docBorrowApprove 借阅审批
     * @return 结果
     */
    public int updateDocBorrowApprove(DocBorrowApprove docBorrowApprove);

    /**
     * 批量删除借阅审批
     * 
     * @param ids 需要删除的借阅审批主键集合
     * @return 结果
     */
    public int deleteDocBorrowApproveByIds(Long[] ids);

    /**
     * 删除借阅审批信息
     * 
     * @param id 借阅审批主键
     * @return 结果
     */
    public int deleteDocBorrowApproveById(Long id);
}
