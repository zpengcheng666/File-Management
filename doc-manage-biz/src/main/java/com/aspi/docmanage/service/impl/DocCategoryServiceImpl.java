package com.aspi.docmanage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.aspi.docmanage.web.api.vo.ClassificationVO;
import com.aspi.docmanage.web.api.vo.SelectTreeNode;
import com.oaspi.common.core.domain.entity.SysDictData;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.DictUtils;
import com.oaspi.system.service.ISysDictDataService;
import com.oaspi.system.service.ISysDictTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocCategoryMapper;
import com.aspi.docmanage.domain.DocCategory;
import com.aspi.docmanage.service.IDocCategoryService;

/**
 * 档案分类Service业务层处理
 * 
 * @author hongy
 * @date 2024-11-02
 */
@Service
public class DocCategoryServiceImpl implements IDocCategoryService
{
    @Autowired
    private DocCategoryMapper docCategoryMapper;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询档案分类
     * 
     * @param id 档案分类主键
     * @return 档案分类
     */
    @Override
    public DocCategory selectDocCategoryById(Long id)
    {
        return docCategoryMapper.selectDocCategoryById(id);
    }

    /**
     * 查询档案分类列表
     * 
     * @param docCategory 档案分类
     * @return 档案分类
     */
    @Override
    public List<DocCategory> selectDocCategoryList(DocCategory docCategory)
    {
        return docCategoryMapper.selectDocCategoryList(docCategory);
    }

    /**
     * 新增档案分类
     * 
     * @param docCategory 档案分类
     * @return 结果
     */
    @Override
    public int insertDocCategory(DocCategory docCategory)
    {
        docCategory.setCreateTime(DateUtils.getNowDate());
        return docCategoryMapper.insertDocCategory(docCategory);
    }

    /**
     * 修改档案分类
     * 
     * @param docCategory 档案分类
     * @return 结果
     */
    @Override
    public int updateDocCategory(DocCategory docCategory)
    {
        docCategory.setUpdateTime(DateUtils.getNowDate());
        return docCategoryMapper.updateDocCategory(docCategory);
    }

    /**
     * 批量删除档案分类
     * 
     * @param ids 需要删除的档案分类主键
     * @return 结果
     */
    @Override
    public int deleteDocCategoryByIds(Long[] ids)
    {
        return docCategoryMapper.deleteDocCategoryByIds(ids);
    }

    /**
     * 删除档案分类信息
     * 
     * @param id 档案分类主键
     * @return 结果
     */
    @Override
    public int deleteDocCategoryById(Long id)
    {
        return docCategoryMapper.deleteDocCategoryById(id);
    }

    /**
     * 档案门类树形列表
     * @param docCategory
     * @return
     */
    @Override
    public List<SelectTreeNode> treeList(DocCategory docCategory) {
        List<DocCategory> list = docCategoryMapper.selectDocCategoryList(docCategory);
        List<SelectTreeNode> treeNodeList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DocCategory tmp = list.get(i);
            //如果parentid=0则为父级，添加子节点
            if (tmp.getParentId() == 0){
                treeNodeList.add(createTreeNode(tmp, list));
            }
        }
        return  treeNodeList;
    }

    /**
     * 递归创建树形节点
     * @param docCategory
     * @param list
     * @return
     */
    private SelectTreeNode createTreeNode(DocCategory docCategory, List<DocCategory> list){
        SelectTreeNode model = new SelectTreeNode();
        model.setId(docCategory.getId());
        model.setName(docCategory.getName());
        model.setPid(docCategory.getParentId());
        for (int i = 0; i < list.size(); i++) {
            DocCategory tmp = list.get(i);
            if (Objects.equals(tmp.getParentId(), docCategory.getId())){
                model.addChild(createTreeNode(tmp, list));
            }
        }
        return model;
    }

    /**
     * 获取所有分类
     * @return
     */
    @Override
    public List<ClassificationVO> fullClassification(SysDictData sysDictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(sysDictData);
        List<SysDictData> dictData = list.stream().filter(item -> item.getDictType().equals("doc_qbfl")).collect(Collectors.toList());
        return getDictLable(dictData);
    }

    /**
     * 密级及保管期限
     * @return
     */
    @Override
    public List<ClassificationVO> secretDuration(SysDictData sysDictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(sysDictData);
        List<SysDictData> dictData = list.stream().filter(item -> item.getDictType().equals("doc_sec_period")).collect(Collectors.toList());
        return getDictLable(dictData);
    }

    @Override
    public DocCategory selectDocCategoryByIds(Long[] docIds) {
        return null;
    }

    @Override
    public DocCategory selectDocCategoryByCategory(String categoryName) {
        return docCategoryMapper.selectDocCategoryByCategory(categoryName);
    }

    @NotNull
    private List<ClassificationVO> getDictLable(List<SysDictData> dictData) {
        List<ClassificationVO> collect = dictData.stream().map(e -> {
            ClassificationVO vo = new ClassificationVO();
            vo.setId(e.getDictCode());
            vo.setName(e.getDictLabel());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }
}
