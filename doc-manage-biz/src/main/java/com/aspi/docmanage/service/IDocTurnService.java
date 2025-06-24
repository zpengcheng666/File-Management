package com.aspi.docmanage.service;


import com.aspi.docmanage.domain.DocTurn;
import com.aspi.docmanage.domain.TurnAttachment;
import com.aspi.docmanage.web.api.vo.TurnAttachmentVO;
import com.oaspi.system.domain.SysUpFile;

import java.util.List;

/**
 * 档案移交信息Service接口
 *
 * @author zpc
 * @date 2025-01-09
 */
public interface IDocTurnService
{
    /**
     * 查询档案移交信息
     *
     * @param id 档案移交信息主键
     * @return 档案移交信息
     */
    public DocTurn selectDocTurnById(Long id);

    /**
     * 查询档案移交信息列表
     *
     * @param docTurn 档案移交信息
     * @return 档案移交信息集合
     */
    public List<DocTurn> selectDocTurnList(DocTurn docTurn);

    /**
     * 新增档案移交信息
     *
     * @return 结果
     */
    public int insertDocTurn(TurnAttachmentVO vo);

    /**
     * 修改档案移交信息
     *
     * @param docTurn 档案移交信息
     * @return 结果
     */
    public int updateDocTurn(DocTurn docTurn);

    /**
     * 批量删除档案移交信息
     *
     * @param ids 需要删除的档案移交信息主键集合
     * @return 结果
     */
    public int deleteDocTurnByIds(Long[] ids);

    /**
     * 删除档案移交信息信息
     *
     * @param id 档案移交信息主键
     * @return 结果
     */
    public int deleteDocTurnById(Long id);

    /**
     * 根据移交列表id查询对应附件信息
     */
    List<SysUpFile> turnIdByAttachmentList(Long turnId);
}

