package com.oaspi.system.service;

import com.oaspi.system.domain.DocAttachments;

/**
 * 档案信息与附件关联表接口
 */
public interface IDocAttachmentsService {
    void insertDocAttachments(DocAttachments docAttachments);

    void deleteDocAttachmentsByIds(Long[] ids);

    void removeDocInfoByIds(Long[] ids);

    Long[] queryDocinfoIdsBySysUpFileIds(Long[] ids);
}
