package com.aspi.docmanage.mapper;

import com.aspi.docmanage.domain.StockRecord;
import java.util.List;
import java.util.Map;

/**
 * 出入库信息Mapper接口
 *
 * @author wangy
 * @date 2025-01-11
 */
public interface StockRecordMapper {

    List<StockRecord> listStockRecord();

    void addStockRecord(StockRecord stockRecord);

    void batchStockRecord(List<StockRecord> stockRecordList);

    void deleteStockRecord(Long id);

}
