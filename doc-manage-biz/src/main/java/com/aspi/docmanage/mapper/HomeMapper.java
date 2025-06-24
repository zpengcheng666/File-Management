package com.aspi.docmanage.mapper;

import com.aspi.docmanage.domain.DocInfo;
import java.util.List;
import java.util.Map;

/**
 * 主页信息Mapper接口
 *
 * @author wangy
 * @date 2025-01-06
 */
public interface HomeMapper {

    public String getCompanyNumber();

    public String getAccountNumber();

    public String getDocNumber();

    public String getAttachmentNumber();

    /**
     * 按种类获取数量
     */
    public List<Map<String, String>> getDocNumbersGroupByCategory();

    /**
     * 按种类部门数量
     */
    public List<Map<String, String>> getDocNumbersGroupByDepartment();

}
