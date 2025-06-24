package com.oaspi.system.service.impl;

import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.DocDept;
import com.oaspi.system.domain.vo.CompanyTreeNodeVO;
import com.oaspi.system.domain.vo.DeptTreeNodeVO;
import com.oaspi.system.domain.vo.DeptVO;
import com.oaspi.system.mapper.DocDeptMapper;
import com.oaspi.system.service.IDocDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门Service业务层处理
 *
 * @author ruoyi
 * @date 2025-01-04
 */
@Service
public class DocDeptServiceImpl implements IDocDeptService
{
    @Autowired
    private DocDeptMapper docDeptMapper;

    /**
     * 查询部门
     *
     * @param deptId 部门主键
     * @return 部门
     */
    @Override
    public DocDept selectDocDeptByDeptId(Long deptId)
    {
        return docDeptMapper.selectDocDeptByDeptId(deptId);
    }

    /**
     * 查询部门列表
     *
     * @param docDept 部门
     * @return 部门
     */
    @Override
    public List<DocDept> selectDocDeptList(DocDept docDept)
    {
        return docDeptMapper.selectDocDeptList(docDept);
    }

    /**
     * 新增部门
     *
     * @param docDept 部门
     * @return 结果
     */
    @Override
    public int insertDocDept(DocDept docDept)
    {
        return docDeptMapper.insertDocDept(docDept);
    }

    /**
     * 修改部门
     *
     * @param docDept 部门
     * @return 结果
     */
    @Override
    public int updateDocDept(DocDept docDept)
    {
        return docDeptMapper.updateDocDept(docDept);
    }

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的部门主键
     * @return 结果
     */
    @Override
    public int deleteDocDeptByDeptIds(Long[] deptIds)
    {
        return docDeptMapper.deleteDocDeptByDeptIds(deptIds);
    }

    /**
     * 删除部门信息
     *
     * @param deptId 部门主键
     * @return 结果
     */
    @Override
    public int deleteDocDeptByDeptId(Long deptId)
    {
        return docDeptMapper.deleteDocDeptByDeptId(deptId);
    }

    /**
     * 部门树状列表
     */
    @Override
    public List<DeptVO> deptList(DocDept docDept) {
        List<DocDept> list = docDeptMapper.selectDocDeptList(docDept);
        List<DeptVO> vos = new ArrayList<>();
        for (DocDept tmp : list) {
            DeptVO model = new DeptVO();
            model.setDeptId(tmp.getDeptId());
            model.setDeptName(tmp.getDeptName());
            vos.add(model);
        }
        List<DeptVO> collect = vos.stream().filter(e -> e.getDeptId() != 0).collect(Collectors.toList());
        return  collect;
    }

    /**
     * 根据公司id查询部门列表
     */
    @Override
    public List<DeptTreeNodeVO> getCompanyByDeptList(Long companyId) {
        List<DocDept> list = docDeptMapper.getDepartmentsByCompanyId(companyId);
        List<DeptTreeNodeVO> treeNodeList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DocDept tmp = list.get(i);
            //如果parentid=0则为父级，添加子节点
            if (tmp.getParentId() == 0){
                treeNodeList.add(createTreeNode(tmp, list));
            }
        }
        return  treeNodeList;
    }

    @Override
    public DocDept selectDocDeptByDeptName(String deptName,Long companyId) {
        return docDeptMapper.selectDocDeptByDeptName(deptName,companyId);
    }

    /**
     * 递归创建树形节点
     * @param dept
     * @param list
     * @return
     */
    private DeptTreeNodeVO createTreeNode(DocDept dept, List<DocDept> list){
        DeptTreeNodeVO model = new DeptTreeNodeVO();
        model.setId(dept.getDeptId());
        model.setName(dept.getDeptName());
        model.setPid(dept.getParentId());
        model.setDeptCode(dept.getDeptCode());
        for (int i = 0; i < list.size(); i++) {
            DocDept tmp = list.get(i);
            if (Objects.equals(tmp.getParentId(), dept.getDeptId())){
                model.addChild(createTreeNode(tmp, list));
            }
        }
        return model;
    }
}
