package com.aspi.docmanage.service.impl;

import com.aspi.docmanage.mapper.DocInfoMapper;
import com.aspi.docmanage.mapper.HomeMapper;
import com.aspi.docmanage.service.IHomeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首页信息Service业务层处理
 *
 * @author wangy
 * @date 2025-01-06
 */
@Service
public class HomeServiceImpl implements IHomeService {

    private static final Logger log = LoggerFactory.getLogger(HomeServiceImpl.class);

    @Resource
    private HomeMapper homeMapper;

    @Override
    public List<Map<String, String>> getNumbers() {
        String companyNumber = homeMapper.getCompanyNumber();
        String accountNumber = homeMapper.getAccountNumber();
        String docNumber = homeMapper.getDocNumber();
        String attachmentNumber = homeMapper.getAttachmentNumber();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        map = new HashMap<>();
        map.put("companyNumber", companyNumber);
        list.add(map);
        map = new HashMap<>();
        map.put("accountNumber", accountNumber);
        list.add(map);
        map = new HashMap<>();
        map.put("docNumber", docNumber);
        list.add(map);
        map = new HashMap<>();
        map.put("attachmentNumber", attachmentNumber);
        list.add(map);
        return list;
    }

    @Override
    public List<Map<String, String>> getDocNumbersGroupByCategory() {
        return homeMapper.getDocNumbersGroupByCategory();
    }

    @Override
    public List<Map<String, String>> getDocNumbersGroupByDepartment() {
        return homeMapper.getDocNumbersGroupByDepartment();
    }

}
