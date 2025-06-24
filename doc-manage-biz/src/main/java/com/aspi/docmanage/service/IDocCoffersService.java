package com.aspi.docmanage.service;

import com.aspi.docmanage.domain.Coffers;
import com.aspi.docmanage.domain.DocCategory;
import com.aspi.docmanage.domain.DocCoffers;
import com.aspi.docmanage.domain.DocTemperature;
import com.oaspi.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 档案分类Service接口
 * 
 * @author hongy
 * @date 2024-11-02
 */
public interface IDocCoffersService
{

    /**
     * 查询库房管理列表
     *
     * @param docCoffers 库房管理
     * @return 库房管理
     */
    public List<DocCoffers> coffersList(DocCoffers docCoffers);

    /**
     * 新增库房数据
     * @param docCoffers
     * @return 结果
     */
    public AjaxResult add(DocCoffers docCoffers);

    /**
     * 删除库房
     * @param docCoffers
     * @return 结果
     */
    public AjaxResult delCoffers(DocCoffers docCoffers);

    /**
     * 修改库房表
     * @param docCoffers
     * @return 结果
     */
    public AjaxResult updCoffers(DocCoffers docCoffers);

    /**
     * 导入库房模板
     * @param file
     * @return 结果
     */
    AjaxResult importDocCoffers(MultipartFile file);

    /**
     * 根据名称查询库房
     */
    public DocCoffers getCoffersByName(String name);

}
