package com.oaspi.system.mapper;

import com.oaspi.system.domain.DocDept;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门Mapper接口
 *
 * @author ruoyi
 * @date 2025-01-04
 */
public interface DocDeptMapper
{
    /**
     * 查询部门
     *
     * @param deptId 部门主键
     * @return 部门
     */
    public DocDept selectDocDeptByDeptId(Long deptId);

    /**
     * 查询部门列表
     *
     * @param docDept 部门
     * @return 部门集合
     */
    public List<DocDept> selectDocDeptList(DocDept docDept);

    /**
     * 新增部门
     *
     * @param docDept 部门
     * @return 结果
     */
    public int insertDocDept(DocDept docDept);

    /**
     * 修改部门
     *
     * @param docDept 部门
     * @return 结果
     */
    public int updateDocDept(DocDept docDept);

    /**
     * 删除部门
     *
     * @param deptId 部门主键
     * @return 结果
     */
    public int deleteDocDeptByDeptId(Long deptId);

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocDeptByDeptIds(Long[] deptIds);

    List<DocDept> getDepartmentsByCompanyId(Long companyId);

    DocDept selectDocDeptByDeptName(String deptName ,Long companyId);
}
