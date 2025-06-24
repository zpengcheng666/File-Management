package com.aspi.docmanage.mapper;

import com.aspi.docmanage.domain.DocBorrow;
import com.aspi.docmanage.domain.StockRecord;
import java.util.List;

/**
 * 借阅信息Mapper接口
 *
 * @author wangy
 * @date 2025-01-17
 */
public interface DocBorrowMapper {

    public List<DocBorrow> listDocBorrow();

    public void addDocBorrow(DocBorrow docBorrow);

    public void deleteDocBorrow(Long id);

    public void updateDocBorrowStatus(DocBorrow docBorrow);

}
