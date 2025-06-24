package com.aspi.docmanage.service;

import java.util.List;
import java.util.Map;

/**
 * 首页Service接口
 */
public interface IHomeService {

    /**
     * 获取总体数量
     */
    public List<Map<String, String>> getNumbers();

    /**
     * 按种类获取数量
     */
    public List<Map<String, String>> getDocNumbersGroupByCategory();

    /**
     * 按种类部门数量
     */
    public List<Map<String, String>> getDocNumbersGroupByDepartment();

}
