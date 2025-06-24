package com.aspi.docmanage.service.impl;

import com.aspi.docmanage.domain.StockRecord;
import com.aspi.docmanage.mapper.HomeMapper;
import com.aspi.docmanage.mapper.StockRecordMapper;
import com.aspi.docmanage.service.IStockRecordService;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 首页信息Service业务层处理
 *
 * @author wangy
 * @date 2025-01-06
 */
@Service
public class StockRecordServiceImpl implements IStockRecordService {

    private static final Logger log = LoggerFactory.getLogger(StockRecordServiceImpl.class);

    @Resource
    private StockRecordMapper stockRecordMapper;


    @Override
    public List<StockRecord> listStockRecord() {
        return stockRecordMapper.listStockRecord();
    }

    @Override
    public void addStockRecord(StockRecord stockRecord) {
        stockRecord.setStatus(0);
        stockRecord.setDelFlag(0);
        stockRecordMapper.addStockRecord(stockRecord);
    }

    @Override
    public void batchStockRecord(List<StockRecord> stockRecordList) {
        stockRecordMapper.batchStockRecord(stockRecordList);
    }

    @Override
    public void deleteStockRecord(Long id) {
        stockRecordMapper.deleteStockRecord(id);
    }

}
