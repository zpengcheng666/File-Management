package com.aspi.docmanage.service.impl;

import com.aspi.docmanage.domain.DocBorrow;
import com.aspi.docmanage.mapper.DocBorrowMapper;
import com.aspi.docmanage.service.IDocBorrowService;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service业务层处理
 *
 * @author wangy
 * @date 2025-01-17
 */
@Service
public class DocBorrowServiceImpl implements IDocBorrowService {

    private static final Logger log = LoggerFactory.getLogger(DocBorrowServiceImpl.class);

    @Resource
    private DocBorrowMapper docBorrowMapper;


    @Override
    public List<DocBorrow> listDocBorrow() {
        return docBorrowMapper.listDocBorrow();
    }

    @Override
    public void addDocBorrow(DocBorrow docBorrow) {
        docBorrowMapper.addDocBorrow(docBorrow);
    }

    @Override
    public void deleteDocBorrow(Long id) {
        docBorrowMapper.deleteDocBorrow(id);
    }

    @Override
    public void updateDocBorrowStatus(DocBorrow docBorrow) {
        docBorrowMapper.updateDocBorrowStatus(docBorrow);
    }

}
