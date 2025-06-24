package com.aspi.docmanage.service.impl;

import com.aspi.docmanage.domain.*;
import com.aspi.docmanage.mapper.DocCategoryMapper;
import com.aspi.docmanage.mapper.DocCoffersMapper;
import com.aspi.docmanage.service.IDocCategoryService;
import com.aspi.docmanage.service.IDocCoffersService;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.PageUtils;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * 档案分类Service业务层处理
 * 
 * @author hongy
 * @date 2024-11-02
 */
@Service
public class DocCoffersServiceImpl implements IDocCoffersService
{
    @Autowired
    private DocCoffersMapper docCoffersMapper;

    @Autowired
    private DocShelvesServiceImpl docShelvesService;

    @Autowired
    private DocBoxServiceImpl docBoxService;

    /**
     * 查询库房管理列表
     *
     * @param docCoffers 库房管理
     * @return 库房管理
     */
    @Override
    public List<DocCoffers> coffersList(DocCoffers docCoffers)
    {
        return docCoffersMapper.coffersList(docCoffers);
    }

    /**
     * 新增库房管理
     * 
     * @param docCoffers 库房管理
     * @return 结果
     */
    @Override
    public AjaxResult add(DocCoffers docCoffers)
    {
        //插入时间
        docCoffers.setCreateTime(DateUtils.getNowDate());
        docCoffers.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        //插入库房数据
        docCoffersMapper.instCoffers(docCoffers);
        int a = 0;
        //根据档案架数量，插入对应的档案架数据
        for (int i = 1; i < docCoffers.getShelvesNum()+1; i++) {
            DocShelves docShelves = new DocShelves();
            // 生成随机的UUID
            UUID uuids = UUID.randomUUID();
            docShelves.setId(uuids.toString());
            //获取档案架名称
            String name = docCoffers.getShelvesName()+i;
            docShelves.setShelvesName(name);
            docShelves.setCreateTime(DateUtils.getNowDate());
            docShelves.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            docShelves.setBox(docCoffers.getBox());
            docShelves.setCoffersNo(String.valueOf(docCoffers.getCoffersNo()));
            //插入档案架数据
            docShelvesService.insertDocShelves(docShelves);
            //行
            for (int j = 1; j < docCoffers.getShelvesRow()+1; j++) {
                //列
                for (int k = 1; k < docCoffers.getShelvesColumn()+1; k++) {
                    // 生成随机的UUID
                    UUID uuid = UUID.randomUUID();
                    DocBox docBox = new DocBox();
                    docBox.setBoxId(uuid.toString());
                    a = a +1;
                    String boxName = "第"+a+"盒";
                    docBox.setBoxName(boxName);
                    //根据盒数，插入对应的盒子数据
                    docBox.setBoxRow(Long.valueOf(j));
                    docBox.setBoxColumn(Long.valueOf(k));
                    docBox.setCoffersNo(docCoffers.getCoffersNo());
                    docBox.setShelvesNo(String.valueOf(docShelves.getShelvesNo()));
                    docBoxService.insertDocBox(docBox);
                }
            }
        }
        return AjaxResult.success();
    }



    /**
     * 删除库房
     *
     * @param docCoffers
     * @return 结果
     */
    @Override
    public AjaxResult delCoffers(DocCoffers docCoffers)
    {
        //插入时间
        docCoffers.setCreateTime(DateUtils.getNowDate());
        docCoffers.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        //下架档案
        docCoffersMapper.down(docCoffers);
        //把库房数据逻辑删除
        docCoffersMapper.delCoffers(docCoffers);
        //把档案架数据逻辑删除
        docCoffersMapper.delShelves(docCoffers);
        //把档案盒数据逻辑删除
        docCoffersMapper.delBox(docCoffers);
        return AjaxResult.success();
    }

    /**
     * 修改库房
     *
     * @param docCoffers
     * @return 结果
     */
    @Override
    public AjaxResult updCoffers(DocCoffers docCoffers)
    {
        //修改库房
        docCoffersMapper.updCoffers(docCoffers);
        return AjaxResult.success();
    }

    /**
     * 导入库房模板
     *
     * @param file
     * @return 结果
     */
    @Override
    public AjaxResult importDocCoffers(MultipartFile file) {
        try {
            //试试此方法，能否满足，并且简化操作
//            ExcelUtil<DocCoffers> util = new ExcelUtil<>(DocCoffers.class);
//            List<DocCoffers> DocBorrowRecordsList = util.importExcel(file.getInputStream());

            List<DocCoffers> imports = readWarehouses(file.getInputStream());

            for (DocCoffers imp : imports) {
                DocCoffers warehouse = new DocCoffers();
                //通过此方法实现类拷贝，简化操作；
//                BeanUtils.copyProperties(imp, warehouse);

                warehouse.setName(imp.getName());
                warehouse.setShelvesNum(imp.getShelvesNum());
                warehouse.setShelvesRow(imp.getShelvesRow());
                warehouse.setShelvesColumn(imp.getShelvesColumn());
                warehouse.setBox(imp.getBox());
                warehouse.setShelvesName(imp.getShelvesName());
                add(warehouse);
            }

            return AjaxResult.success("导入成功 " + imports.size() + "条 !");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败！");
        }
    }

    /**
     * 解析库房excel
     *
     * @param inputStream
     * @return 结果
     */
    public static List<DocCoffers> readWarehouses(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<DocCoffers> warehouses = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) { // Skip header
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            DocCoffers warehouse = new DocCoffers();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIdx) {
                    case 0:
                        warehouse.setName(ExcelUtil.getCellValueAsString(currentCell));
                        break;
                    case 1:
                        warehouse.setShelvesName(ExcelUtil.getCellValueAsString(currentCell));
                        break;
                    case 2:
                        warehouse.setShelvesNum((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    case 3:
                        warehouse.setShelvesRow((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    case 4:
                        warehouse.setShelvesColumn((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    case 5:
                        warehouse.setBox((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    default:
                        break;
                }
                cellIdx++;
            }

            warehouses.add(warehouse);
            rowNumber++;
        }
        workbook.close();
        return warehouses;
    }

    @Override
    public DocCoffers getCoffersByName(String name) {
        return docCoffersMapper.getCoffersByName(name.trim());
    }

}
