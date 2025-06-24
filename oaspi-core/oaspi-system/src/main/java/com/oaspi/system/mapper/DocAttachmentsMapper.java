package com.oaspi.system.mapper;

import com.oaspi.system.domain.DocAttachments;

import java.util.List;

/**
 * 档案附件Mapper接口
 */
public interface DocAttachmentsMapper {
    void insertDocAttachments(DocAttachments docAttachments);

    void deleteDocAttachmentsByIds(Long[] ids);

    void removeDocInfoByIds(Long[] ids);

    Long[] queryDocinfoIdsBySysUpFileIds(Long[] ids);

    List<Long> selectAttachmentsByDocId(Long docId);
}
