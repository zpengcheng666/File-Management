package com.oaspi.system.service;

import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.DocDept;
import com.oaspi.system.domain.vo.DeptTreeNodeVO;
import com.oaspi.system.domain.vo.DeptVO;

import java.util.List;

/**
 * 部门Service接口
 *
 * @author zpc
 * @date 2025-01-04
 */
public interface IDocDeptService
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
     * 批量删除部门
     *
     * @param deptIds 需要删除的部门主键集合
     * @return 结果
     */
    public int deleteDocDeptByDeptIds(Long[] deptIds);

    /**
     * 删除部门信息
     *
     * @param deptId 部门主键
     * @return 结果
     */
    public int deleteDocDeptByDeptId(Long deptId);

    /**
     * 部门列表
     * @param docDept
     * @return
     */
    List<DeptVO> deptList(DocDept docDept);

    /**
     * 根据公司id查询部门列表
     */
    List<DeptTreeNodeVO> getCompanyByDeptList(Long companyId);

    DocDept selectDocDeptByDeptName(String deptName,Long companyId);
}
