package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocTemperatureMapper;
import com.aspi.docmanage.domain.DocTemperature;
import com.aspi.docmanage.service.IDocTemperatureService;

/**
 * 温湿度Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocTemperatureServiceImpl implements IDocTemperatureService 
{
    @Autowired
    private DocTemperatureMapper docTemperatureMapper;

    /**
     * 查询温湿度
     * 
     * @param id 温湿度主键
     * @return 温湿度
     */
    @Override
    public DocTemperature selectDocTemperatureById(Long id)
    {
        return docTemperatureMapper.selectDocTemperatureById(id);
    }

    /**
     * 查询温湿度列表
     * 
     * @param docTemperature 温湿度
     * @return 温湿度
     */
    @Override
    public List<DocTemperature> selectDocTemperatureList(DocTemperature docTemperature)
    {
        PageUtils.startPage();
        return docTemperatureMapper.selectDocTemperatureList(docTemperature);
    }

    /**
     * 新增温湿度
     * 
     * @param docTemperature 温湿度
     * @return 结果
     */
    @Override
    public int insertDocTemperature(DocTemperature docTemperature)
    {
        docTemperature.setCreateTime(DateUtils.getNowDate());
        return docTemperatureMapper.insertDocTemperature(docTemperature);
    }

    /**
     * 修改温湿度
     * 
     * @param docTemperature 温湿度
     * @return 结果
     */
    @Override
    public int updateDocTemperature(DocTemperature docTemperature)
    {
        docTemperature.setUpdateTime(DateUtils.getNowDate());
        return docTemperatureMapper.updateDocTemperature(docTemperature);
    }

    /**
     * 批量删除温湿度
     * 
     * @param ids 需要删除的温湿度主键
     * @return 结果
     */
    @Override
    public int deleteDocTemperatureByIds(Long[] ids)
    {
        return docTemperatureMapper.deleteDocTemperatureByIds(ids);
    }

    /**
     * 删除温湿度信息
     * 
     * @param id 温湿度主键
     * @return 结果
     */
    @Override
    public int deleteDocTemperatureById(Long id)
    {
        return docTemperatureMapper.deleteDocTemperatureById(id);
    }
}
