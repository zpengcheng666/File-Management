package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocDailyMapper;
import com.aspi.docmanage.domain.DocDaily;
import com.aspi.docmanage.service.IDocDailyService;

/**
 * 日常情况Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocDailyServiceImpl implements IDocDailyService 
{
    @Autowired
    private DocDailyMapper docDailyMapper;

    /**
     * 查询日常情况
     * 
     * @param id 日常情况主键
     * @return 日常情况
     */
    @Override
    public DocDaily selectDocDailyById(Long id)
    {
        return docDailyMapper.selectDocDailyById(id);
    }

    /**
     * 查询日常情况列表
     * 
     * @param docDaily 日常情况
     * @return 日常情况
     */
    @Override
    public List<DocDaily> selectDocDailyList(DocDaily docDaily)
    {
        PageUtils.startPage();
        return docDailyMapper.selectDocDailyList(docDaily);
    }

    /**
     * 新增日常情况
     * 
     * @param docDaily 日常情况
     * @return 结果
     */
    @Override
    public int insertDocDaily(DocDaily docDaily)
    {
        docDaily.setCreateTime(DateUtils.getNowDate());
        return docDailyMapper.insertDocDaily(docDaily);
    }

    /**
     * 修改日常情况
     * 
     * @param docDaily 日常情况
     * @return 结果
     */
    @Override
    public int updateDocDaily(DocDaily docDaily)
    {
        docDaily.setUpdateTime(DateUtils.getNowDate());
        return docDailyMapper.updateDocDaily(docDaily);
    }

    /**
     * 批量删除日常情况
     * 
     * @param ids 需要删除的日常情况主键
     * @return 结果
     */
    @Override
    public int deleteDocDailyByIds(Long[] ids)
    {
        return docDailyMapper.deleteDocDailyByIds(ids);
    }

    /**
     * 删除日常情况信息
     * 
     * @param id 日常情况主键
     * @return 结果
     */
    @Override
    public int deleteDocDailyById(Long id)
    {
        return docDailyMapper.deleteDocDailyById(id);
    }
}
