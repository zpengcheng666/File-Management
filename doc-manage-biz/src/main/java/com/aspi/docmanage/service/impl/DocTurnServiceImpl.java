package com.aspi.docmanage.service.impl;

import java.util.List;

import com.aspi.docmanage.domain.DocTurn;
import com.aspi.docmanage.domain.TurnAttachment;
import com.aspi.docmanage.mapper.DocTurnMapper;
import com.aspi.docmanage.service.IDocTurnService;
import com.aspi.docmanage.service.ITurnAttachmentService;
import com.aspi.docmanage.web.api.vo.TurnAttachmentVO;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.bean.BeanUtils;
import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.SysUpFile;
import com.oaspi.system.service.IDocCompanyService;
import com.oaspi.system.service.ISysUpFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 档案移交信息Service业务层处理
 *
 * @author zpc
 * @date 2025-01-09
 */
@Service
public class DocTurnServiceImpl implements IDocTurnService
{
    @Autowired
    private DocTurnMapper docTurnMapper;
    @Autowired
    private IDocCompanyService docCompanyService;
    @Autowired
    private ITurnAttachmentService turnAttachmentService;
    @Autowired
    private ISysUpFileService sysUpFileService;

    /**
     * 查询档案移交信息
     *
     * @param id 档案移交信息主键
     * @return 档案移交信息
     */
    @Override
    public DocTurn selectDocTurnById(Long id)
    {
        return docTurnMapper.selectDocTurnById(id);
    }

    /**
     * 查询档案移交信息列表
     *
     * @param docTurn 档案移交信息
     * @return 档案移交信息
     */
    @Override
    public List<DocTurn> selectDocTurnList(DocTurn docTurn)
    {
        List<DocTurn> docTurns = docTurnMapper.selectDocTurnList(docTurn);
        docTurns.forEach(e -> {
            DocCompany docCompany = docCompanyService.selectDocCompanyByCompanyId(e.getCompanyName());
            //公司名称翻译
            e.setCompanyNameText(docCompany == null ? "" : docCompany.getCompanyName());
        });
        return docTurns;
    }

    /**
     * 新增档案移交信息
     */
    @Override
    public int insertDocTurn(TurnAttachmentVO vo)
    {
        DocTurn docTurn = new DocTurn();
        BeanUtils.copyBeanProp(docTurn, vo);
        docTurn.setId(vo.getId());
        vo.setOperator(SecurityUtils.getUsername());
        docTurnMapper.insertDocTurn(docTurn);

        Long[] fileId = vo.getFileIds();
        for (Long l : fileId) {
            TurnAttachment turnAttachment = new TurnAttachment();
            turnAttachment.setTurnId(docTurn.getId());
            turnAttachment.setAttachment(l);
            turnAttachmentService.insertTurnAttachment(turnAttachment);
        }
        return 1;
    }

    /**
     * 修改档案移交信息
     *
     * @param docTurn 档案移交信息
     * @return 结果
     */
    @Override
    public int updateDocTurn(DocTurn docTurn)
    {
        return docTurnMapper.updateDocTurn(docTurn);
    }

    /**
     * 批量删除档案移交信息
     *
     * @param ids 需要删除的档案移交信息主键
     * @return 结果
     */
    @Override
    public int deleteDocTurnByIds(Long[] ids)
    {
        return docTurnMapper.deleteDocTurnByIds(ids);
    }

    /**
     * 删除档案移交信息信息
     *
     * @param id 档案移交信息主键
     * @return 结果
     */
    @Override
    public int deleteDocTurnById(Long id)
    {
        return docTurnMapper.deleteDocTurnById(id);
    }

    /**
     * 根据移交列表id查询对应附件信息
     */
    @Override
    public List<SysUpFile> turnIdByAttachmentList(Long turnId) {
        return sysUpFileService.turnIdByAttachmentList(turnId);
    }
}

