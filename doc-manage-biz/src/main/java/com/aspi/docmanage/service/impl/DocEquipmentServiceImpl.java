package com.aspi.docmanage.service.impl;

import java.util.List;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocEquipmentMapper;
import com.aspi.docmanage.domain.DocEquipment;
import com.aspi.docmanage.service.IDocEquipmentService;

/**
 * 设备Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocEquipmentServiceImpl implements IDocEquipmentService 
{
    @Autowired
    private DocEquipmentMapper docEquipmentMapper;

    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    @Override
    public DocEquipment selectDocEquipmentById(Long id)
    {
        return docEquipmentMapper.selectDocEquipmentById(id);
    }

    /**
     * 查询设备列表
     * 
     * @param docEquipment 设备
     * @return 设备
     */
    @Override
    public List<DocEquipment> selectDocEquipmentList(DocEquipment docEquipment)
    {
        PageUtils.startPage();
        return docEquipmentMapper.selectDocEquipmentList(docEquipment);
    }

    /**
     * 新增设备
     * 
     * @param docEquipment 设备
     * @return 结果
     */
    @Override
    public int insertDocEquipment(DocEquipment docEquipment)
    {
        docEquipment.setCreateTime(DateUtils.getNowDate());
        return docEquipmentMapper.insertDocEquipment(docEquipment);
    }

    /**
     * 修改设备
     * 
     * @param docEquipment 设备
     * @return 结果
     */
    @Override
    public int updateDocEquipment(DocEquipment docEquipment)
    {
        docEquipment.setUpdateTime(DateUtils.getNowDate());
        return docEquipmentMapper.updateDocEquipment(docEquipment);
    }

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteDocEquipmentByIds(Long[] ids)
    {
        return docEquipmentMapper.deleteDocEquipmentByIds(ids);
    }

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    @Override
    public int deleteDocEquipmentById(Long id)
    {
        return docEquipmentMapper.deleteDocEquipmentById(id);
    }
}
