package com.aspi.docmanage.service.impl;


import java.util.List;

import com.aspi.docmanage.domain.TurnAttachment;
import com.aspi.docmanage.mapper.TurnAttachmentMapper;
import com.aspi.docmanage.service.ITurnAttachmentService;
import com.oaspi.system.domain.SysUpFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 移交和附件关系Service业务层处理
 *
 * @author zpc
 * @date 2025-01-09
 */
@Service
public class TurnAttachmentServiceImpl implements ITurnAttachmentService
{
    @Autowired
    private TurnAttachmentMapper turnAttachmentMapper;

    /**
     * 查询移交和附件关系
     *
     * @param turnId 移交和附件关系主键
     * @return 移交和附件关系
     */
    @Override
    public TurnAttachment selectTurnAttachmentByTurnId(Long turnId)
    {
        return turnAttachmentMapper.selectTurnAttachmentByTurnId(turnId);
    }

    /**
     * 查询移交和附件关系列表
     *
     * @param turnAttachment 移交和附件关系
     * @return 移交和附件关系
     */
    @Override
    public List<TurnAttachment> selectTurnAttachmentList(TurnAttachment turnAttachment)
    {
        return turnAttachmentMapper.selectTurnAttachmentList(turnAttachment);
    }

    /**
     * 新增移交和附件关系
     *
     * @param turnAttachment 移交和附件关系
     * @return 结果
     */
    @Override
    public int insertTurnAttachment(TurnAttachment turnAttachment)
    {
        return turnAttachmentMapper.insertTurnAttachment(turnAttachment);
    }

    /**
     * 修改移交和附件关系
     *
     * @param turnAttachment 移交和附件关系
     * @return 结果
     */
    @Override
    public int updateTurnAttachment(TurnAttachment turnAttachment)
    {
        return turnAttachmentMapper.updateTurnAttachment(turnAttachment);
    }

    /**
     * 批量删除移交和附件关系
     *
     * @param turnIds 需要删除的移交和附件关系主键
     * @return 结果
     */
    @Override
    public int deleteTurnAttachmentByTurnIds(Long[] turnIds)
    {
        return turnAttachmentMapper.deleteTurnAttachmentByTurnIds(turnIds);
    }

    /**
     * 删除移交和附件关系信息
     *
     * @param turnId 移交和附件关系主键
     * @return 结果
     */
    @Override
    public int deleteTurnAttachmentByTurnId(Long turnId)
    {
        return turnAttachmentMapper.deleteTurnAttachmentByTurnId(turnId);
    }
}

