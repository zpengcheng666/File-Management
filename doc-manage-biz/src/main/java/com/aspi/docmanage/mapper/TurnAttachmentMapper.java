package com.aspi.docmanage.mapper;


import com.aspi.docmanage.domain.TurnAttachment;
import com.oaspi.system.domain.SysUpFile;

import java.util.List;

/**
 * 移交和附件关系Mapper接口
 *
 * @author zpc
 * @date 2025-01-09
 */
public interface TurnAttachmentMapper
{
    /**
     * 查询移交和附件关系
     *
     * @param turnId 移交和附件关系主键
     * @return 移交和附件关系
     */
    public TurnAttachment selectTurnAttachmentByTurnId(Long turnId);

    /**
     * 查询移交和附件关系列表
     *
     * @param turnAttachment 移交和附件关系
     * @return 移交和附件关系集合
     */
    public List<TurnAttachment> selectTurnAttachmentList(TurnAttachment turnAttachment);

    /**
     * 新增移交和附件关系
     *
     * @param turnAttachment 移交和附件关系
     * @return 结果
     */
    public int insertTurnAttachment(TurnAttachment turnAttachment);

    /**
     * 修改移交和附件关系
     *
     * @param turnAttachment 移交和附件关系
     * @return 结果
     */
    public int updateTurnAttachment(TurnAttachment turnAttachment);

    /**
     * 删除移交和附件关系
     *
     * @param turnId 移交和附件关系主键
     * @return 结果
     */
    public int deleteTurnAttachmentByTurnId(Long turnId);

    /**
     * 批量删除移交和附件关系
     *
     * @param turnIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTurnAttachmentByTurnIds(Long[] turnIds);

}
