package com.oaspi.system.mapper;

import com.oaspi.system.domain.DocCompany;
import com.oaspi.system.domain.DocDept;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 公司Mapper接口
 *
 * @author zpc
 * @date 2025-01-04
 */
public interface DocCompanyMapper
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
     * 删除公司
     *
     * @param companyId 公司主键
     * @return 结果
     */
    public int deleteDocCompanyByCompanyId(Long companyId);

    /**
     * 批量删除公司
     *
     * @param companyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocCompanyByCompanyIds(Long[] companyIds);

    /**
     * 查询所有公司
     * @return
     */
    List<DocCompany> getAllCompanies();

    /**
     * 设置公司状态
     */
    int editStatus(DocCompany docCompany);

    /**
     * 根据公司id查询公司档案数量
     */
    int docInfoCount(Long companyId);

    DocCompany selectDocCompanyByCompanyName(String companyName);
}
