package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocTemperature;

/**
 * 温湿度Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocTemperatureService 
{
    /**
     * 查询温湿度
     * 
     * @param id 温湿度主键
     * @return 温湿度
     */
    public DocTemperature selectDocTemperatureById(Long id);

    /**
     * 查询温湿度列表
     * 
     * @param docTemperature 温湿度
     * @return 温湿度集合
     */
    public List<DocTemperature> selectDocTemperatureList(DocTemperature docTemperature);

    /**
     * 新增温湿度
     * 
     * @param docTemperature 温湿度
     * @return 结果
     */
    public int insertDocTemperature(DocTemperature docTemperature);

    /**
     * 修改温湿度
     * 
     * @param docTemperature 温湿度
     * @return 结果
     */
    public int updateDocTemperature(DocTemperature docTemperature);

    /**
     * 批量删除温湿度
     * 
     * @param ids 需要删除的温湿度主键集合
     * @return 结果
     */
    public int deleteDocTemperatureByIds(Long[] ids);

    /**
     * 删除温湿度信息
     * 
     * @param id 温湿度主键
     * @return 结果
     */
    public int deleteDocTemperatureById(Long id);
}
