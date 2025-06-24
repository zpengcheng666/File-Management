package com.aspi.docmanage.service;

import com.aspi.docmanage.domain.DocBorrow;
import java.util.List;

/**
 * 借阅Service接口
 */
public interface IDocBorrowService {

    public List<DocBorrow> listDocBorrow();

    public void addDocBorrow(DocBorrow docBorrow);

    public void deleteDocBorrow(Long id);

    public void updateDocBorrowStatus(DocBorrow docBorrow);

}
