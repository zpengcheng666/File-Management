package com.oaspi.system.service.impl;

import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.DocDept;
import com.oaspi.system.domain.vo.DeptTreeNodeVO;
import com.oaspi.system.mapper.DocCompanyMapper;
import com.oaspi.system.mapper.DocDeptMapper;
import com.oaspi.system.mapper.SysUserMapper;
import com.oaspi.system.service.IDocCompanyService;
import com.oaspi.system.domain.vo.CompanyTreeNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 公司Service业务层处理
 *
 * @author zoc
 * @date 2025-01-04
 */
@Service
public class DocCompanyServiceImpl implements IDocCompanyService {
    @Autowired
    private DocCompanyMapper docCompanyMapper;

    @Autowired
    private DocDeptMapper docDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询公司
     *
     * @param companyId 公司主键
     * @return 公司
     */
    @Override
    public DocCompany selectDocCompanyByCompanyId(Long companyId) {
        return docCompanyMapper.selectDocCompanyByCompanyId(companyId);
    }

    /**
     * 查询公司列表
     *
     * @param docCompany 公司
     * @return 公司
     */
    @Override
    public List<DocCompany> selectDocCompanyList(DocCompany docCompany) {
        return docCompanyMapper.selectDocCompanyList(docCompany);
    }

    /**
     * 新增公司
     *
     * @param docCompany 公司
     * @return 结果
     */
    @Override
    public int insertDocCompany(DocCompany docCompany) {
        docCompany.setHandleAuthor(SecurityUtils.getUsername());
        return docCompanyMapper.insertDocCompany(docCompany);
    }

    /**
     * 修改公司
     *
     * @param docCompany 公司
     * @return 结果
     */
    @Override
    public int updateDocCompany(DocCompany docCompany) {
        return docCompanyMapper.updateDocCompany(docCompany);
    }

    /**
     * 批量删除公司
     *
     * @param companyIds 需要删除的公司主键
     * @return 结果
     */
    @Override
    public int deleteDocCompanyByCompanyIds(Long companyIds) {
        int docCount = docCompanyMapper.docInfoCount(companyIds);
        int userCount = sysUserMapper.selectUserCountByCompanyId(companyIds);
        List<DocDept> depts = docDeptMapper.getDepartmentsByCompanyId(companyIds);//公司部门数量
        int size = depts.size();

        if (docCount > 0 || userCount > 0 || size > 0) {
            throw new RuntimeException("该公司下存在档案、用户或部门，无法删除");
        }

        return docCompanyMapper.deleteDocCompanyByCompanyId(companyIds);
    }

    /**
     * 设置公司状态
     */
    @Override
    public int editStatus(DocCompany docCompany) {
        docCompany.setStatus(docCompany.getStatus());
        return docCompanyMapper.editStatus(docCompany);
    }

    @Override
    public DocCompany selectDocCompanyByCompanyName(String companyName) {
        return docCompanyMapper.selectDocCompanyByCompanyName(companyName);
    }


    /**
     * 删除公司信息
     *
     * @param companyId 公司主键
     * @return 结果
     */
    @Override
    public int deleteDocCompanyByCompanyId(Long companyId) {
        return docCompanyMapper.deleteDocCompanyByCompanyId(companyId);
    }

    /**
     * 公司树状列表
     */
    @Override
    public List<CompanyTreeNodeVO> getCompanyDeptTreeList() {
        List<DocCompany> list = docCompanyMapper.getAllCompanies();
        List<CompanyTreeNodeVO> treeNodeList = new ArrayList<>();
        Map<Long, DocCompany> companyMap = list.stream()
                .collect(Collectors.toMap(DocCompany::getCompanyId, company -> company));
        for (DocCompany company : list) {
            if (company.getParentId() == 0) {
                treeNodeList.add(createTreeNodes(company, list, companyMap));
            }
        }
        return treeNodeList;
    }

    private CompanyTreeNodeVO createTreeNodes(DocCompany company, List<DocCompany> allCompanies, Map<Long, DocCompany> companyMap) {
        int docCount = docCompanyMapper.docInfoCount(company.getCompanyId());//公司档案数量
        int userCount = sysUserMapper.selectUserCountByCompanyId(company.getCompanyId());//公司用户数量
        List<DocDept> depts = docDeptMapper.getDepartmentsByCompanyId(company.getCompanyId());//公司部门数量
        int size = depts.size();

        CompanyTreeNodeVO node = new CompanyTreeNodeVO();
        node.setDocCount(docCount);
        node.setUserCount(userCount);
        node.setDeptCount(size);
        node.setId(company.getCompanyId());
        node.setName(company.getCompanyName());
        node.setPid(company.getParentId());
        node.setStatus(company.getStatus());
        node.setHandleTime(company.getHandleTime());
        node.setHandleAuthor(company.getHandleAuthor());
        node.setDescription(company.getDescription());
        node.setTel(company.getTel());
        node.setCompanyLinker(company.getCompanyLinker());
        node.setOrgCode(company.getOrgCode());

        if (company.getParentId() != 0) {
            DocCompany parentCompany = companyMap.get(company.getParentId());
            node.setParentName(parentCompany != null ? parentCompany.getCompanyName() : null);
        } else {
            node.setParentName(null);
        }

        List<CompanyTreeNodeVO> children = allCompanies.stream()
                .filter(c -> c.getParentId().equals(company.getCompanyId()))
                .map(c -> createTreeNodes(c, allCompanies, companyMap))
                .collect(Collectors.toList());

        node.setChildren(children);
        return node;
    }

    /**
     * 获取公司部门树关联对应部门
     * @return 公司部门树关联对应部门
     */
    @Override
    public List<CompanyTreeNodeVO> getCompanyDeptTree() {
        // 1. 获取所有公司
        List<DocCompany> list = docCompanyMapper.getAllCompanies();
        List<CompanyTreeNodeVO> treeNodeList = new ArrayList<>();

        // 2. 构建公司树
        for (DocCompany company : list) {
            //判断是否是顶级公司
            if (company.getParentId() == 0L || company.getIsTopLevel() == 1L) {
                CompanyTreeNodeVO node = createTreeNode(company, list);
                // 3. 为每个公司节点添加部门树
                addDepartmentTree(node);
                treeNodeList.add(node);
            }
        }
        return treeNodeList;
    }

    private CompanyTreeNodeVO createTreeNode(DocCompany company, List<DocCompany> list) {
        CompanyTreeNodeVO model = new CompanyTreeNodeVO();
        model.setId(company.getCompanyId());
        model.setName(company.getCompanyName());
        model.setPid(company.getParentId());
        // 递归添加子公司
        for (DocCompany tmp : list) {
            if (Objects.equals(tmp.getParentId(), company.getCompanyId())) {
                CompanyTreeNodeVO childNode = createTreeNode(tmp, list);
                // 为子公司添加部门树
                addDepartmentTree(childNode);
                model.addChild(childNode);
            }
        }
        return model;
    }

    /**
     * 为公司节点添加部门树
     */
    private void addDepartmentTree(CompanyTreeNodeVO companyNode) {
        // 1. 获取该公司的所有部门
        List<DocDept> departments = docDeptMapper.getDepartmentsByCompanyId(companyNode.getId());
        if (departments == null || departments.isEmpty()) {
            return;
        }

        // 2. 构建部门树
        List<DeptTreeNodeVO> deptTree = new ArrayList<>();
        for (DocDept dept : departments) {
            if (dept.getParentId() == 0) {  // 顶级部门
                deptTree.add(createDeptTreeNode(dept, departments));
            }
        }

        // 3. 设置部门树
        companyNode.setDepts(deptTree);
    }

    /**
     * 创建部门树节点
     */
    private DeptTreeNodeVO createDeptTreeNode(DocDept dept, List<DocDept> allDepts) {
        DeptTreeNodeVO node = new DeptTreeNodeVO();
        node.setId(dept.getDeptId());
        node.setName(dept.getDeptName());
        node.setPid(dept.getParentId());

        // 递归添加子部门
        for (DocDept childDept : allDepts) {
            if (Objects.equals(childDept.getParentId(), dept.getDeptId())) {
                node.addChild(createDeptTreeNode(childDept, allDepts));
            }
        }
        return node;
    }
}
