package com.aspi.docmanage.service;

import java.util.List;
import com.aspi.docmanage.domain.DocEquipment;

/**
 * 设备Service接口
 * 
 * @author hongy
 * @date 2024-12-08
 */
public interface IDocEquipmentService 
{
    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    public DocEquipment selectDocEquipmentById(Long id);

    /**
     * 查询设备列表
     * 
     * @param docEquipment 设备
     * @return 设备集合
     */
    public List<DocEquipment> selectDocEquipmentList(DocEquipment docEquipment);

    /**
     * 新增设备
     * 
     * @param docEquipment 设备
     * @return 结果
     */
    public int insertDocEquipment(DocEquipment docEquipment);

    /**
     * 修改设备
     * 
     * @param docEquipment 设备
     * @return 结果
     */
    public int updateDocEquipment(DocEquipment docEquipment);

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键集合
     * @return 结果
     */
    public int deleteDocEquipmentByIds(Long[] ids);

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    public int deleteDocEquipmentById(Long id);
}
