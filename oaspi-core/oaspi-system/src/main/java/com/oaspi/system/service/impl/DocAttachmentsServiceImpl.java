package com.oaspi.system.service.impl;

import com.oaspi.system.domain.DocAttachments;
import com.oaspi.system.mapper.DocAttachmentsMapper;
import com.oaspi.system.service.IDocAttachmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DocAttachmentsServiceImpl implements IDocAttachmentsService {
    @Autowired
    private DocAttachmentsMapper docAttachmentsMapper;

    @Override
    public void insertDocAttachments(DocAttachments docAttachments) {
        docAttachmentsMapper.insertDocAttachments(docAttachments);
    }

    @Override
    public void deleteDocAttachmentsByIds(Long[] ids) {
        docAttachmentsMapper.deleteDocAttachmentsByIds(ids);
    }

    @Override
    public void removeDocInfoByIds(Long[] ids) {
        docAttachmentsMapper.removeDocInfoByIds(ids);
    }

    @Override
    public Long[] queryDocinfoIdsBySysUpFileIds(Long[] ids) {
        return docAttachmentsMapper.queryDocinfoIdsBySysUpFileIds(ids);
    }
}
