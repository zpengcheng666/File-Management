package com.aspi.docmanage.mapper;


import com.aspi.docmanage.domain.DocTurn;

import java.util.List;

/**
 * 档案移交信息Mapper接口
 *
 * @author zpc
 * @date 2025-01-09
 */
public interface DocTurnMapper
{
    /**
     * 查询档案移交信息
     *
     * @param id 档案移交信息主键
     * @return 档案移交信息
     */
    public DocTurn selectDocTurnById(Long id);

    /**
     * 查询档案移交信息列表
     *
     * @param docTurn 档案移交信息
     * @return 档案移交信息集合
     */
    public List<DocTurn> selectDocTurnList(DocTurn docTurn);

    /**
     * 新增档案移交信息
     *
     * @param docTurn 档案移交信息
     * @return 结果
     */
    public int insertDocTurn(DocTurn docTurn);

    /**
     * 修改档案移交信息
     *
     * @param docTurn 档案移交信息
     * @return 结果
     */
    public int updateDocTurn(DocTurn docTurn);

    /**
     * 删除档案移交信息
     *
     * @param id 档案移交信息主键
     * @return 结果
     */
    public int deleteDocTurnById(Long id);

    /**
     * 批量删除档案移交信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocTurnByIds(Long[] ids);
}

