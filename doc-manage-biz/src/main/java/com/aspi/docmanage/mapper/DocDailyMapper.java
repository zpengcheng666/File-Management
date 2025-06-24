package com.aspi.docmanage.mapper;

import java.util.List;
import com.aspi.docmanage.domain.DocDaily;

/**
 * 日常情况Mapper接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface DocDailyMapper 
{
    /**
     * 查询日常情况
     * 
     * @param id 日常情况主键
     * @return 日常情况
     */
    public DocDaily selectDocDailyById(Long id);

    /**
     * 查询日常情况列表
     * 
     * @param docDaily 日常情况
     * @return 日常情况集合
     */
    public List<DocDaily> selectDocDailyList(DocDaily docDaily);

    /**
     * 新增日常情况
     * 
     * @param docDaily 日常情况
     * @return 结果
     */
    public int insertDocDaily(DocDaily docDaily);

    /**
     * 修改日常情况
     * 
     * @param docDaily 日常情况
     * @return 结果
     */
    public int updateDocDaily(DocDaily docDaily);

    /**
     * 删除日常情况
     * 
     * @param id 日常情况主键
     * @return 结果
     */
    public int deleteDocDailyById(Long id);

    /**
     * 批量删除日常情况
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocDailyByIds(Long[] ids);
}
