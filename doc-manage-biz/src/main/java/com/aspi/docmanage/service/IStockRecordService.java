package com.aspi.docmanage.service;

import com.aspi.docmanage.domain.StockRecord;
import java.util.List;
import java.util.Map;

/**
 * 出入库Service接口
 */
public interface IStockRecordService {

    public List<StockRecord> listStockRecord();

    public void addStockRecord(StockRecord stockRecord);

    public void batchStockRecord(List<StockRecord> stockRecordList);

    public void deleteStockRecord(Long id);


}
