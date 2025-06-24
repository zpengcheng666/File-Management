package com.oaspi.system.service;

import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.vo.CompanyTreeNodeVO;

import java.util.List;

/**
 * 公司Service接口
 *
 * @author zpc
 * @date 2025-01-04
 */
public interface IDocCompanyService
{
    /**
     * 查询公司
     *
     * @param companyId 公司主键
     * @return 公司
     */
    public DocCompany selectDocCompanyByCompanyId(Long companyId);

    /**
     * 查询公司列表
     *
     * @param docCompany 公司
     * @return 公司集合
     */
    public List<DocCompany> selectDocCompanyList(DocCompany docCompany);

    /**
     * 新增公司
     *
     * @param docCompany 公司
     * @return 结果
     */
    public int insertDocCompany(DocCompany docCompany);

    /**
     * 修改公司
     *
     * @param docCompany 公司
     * @return 结果
     */
    public int updateDocCompany(DocCompany docCompany);

    /**
     * 批量删除公司
     *
     * @param companyIds 需要删除的公司主键集合
     * @return 结果
     */
    public int deleteDocCompanyByCompanyIds(Long companyIds);

    /**
     * 删除公司信息
     *
     * @param companyId 公司主键
     * @return 结果
     */
    public int deleteDocCompanyByCompanyId(Long companyId);

    /**
     * 获取公司部门树关联对应部门
     * @return
     */
    List<CompanyTreeNodeVO> getCompanyDeptTree();

    /**
     * 公司树状列表
     */
    List<CompanyTreeNodeVO> getCompanyDeptTreeList();

    /**
     * 设置公司状态
     */
    int editStatus(DocCompany docCompany);

    /**
     * 根据公司名称查询公司
     * @param companyName
     * @return
     */
    DocCompany selectDocCompanyByCompanyName(String companyName);
}
