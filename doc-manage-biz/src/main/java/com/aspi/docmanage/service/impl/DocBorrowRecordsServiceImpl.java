package com.aspi.docmanage.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aspi.docmanage.domain.*;
import com.oaspi.common.core.domain.AjaxResult;
import com.oaspi.common.utils.DateUtils;
import com.oaspi.common.utils.PageUtils;
import com.oaspi.common.utils.SecurityUtils;
import com.oaspi.common.utils.bean.BeanUtils;
import com.oaspi.common.utils.poi.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspi.docmanage.mapper.DocBorrowRecordsMapper;
import com.aspi.docmanage.service.IDocBorrowRecordsService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 借阅记录Service业务层处理
 * 
 * @author hongy
 * @date 2024-12-08
 */
@Service
public class DocBorrowRecordsServiceImpl implements IDocBorrowRecordsService 
{
    @Autowired
    private DocBorrowRecordsMapper docBorrowRecordsMapper;

    @Autowired
    private DocBorrowArchiveServiceImpl docBorrowArchiveService;

    @Autowired
    private DocBorrowApproveServiceImpl docBorrowApproveService;

    @Autowired
    private DocRenewRecordsServiceImpl docRenewRecordsService;

    /**
     * 查询借阅记录
     * 
     * @param borrowId 借阅记录主键
     * @return 借阅记录
     */
    @Override
    public DocBorrowRecords selectDocBorrowRecordsByBorrowId(Long borrowId)
    {
        return docBorrowRecordsMapper.selectDocBorrowRecordsByBorrowId(borrowId);
    }

    /**
     * 查询借阅基本信息
     *
     * @return 借阅记录
     */
    @Override
    public DocBorrowRecords getInfo()
    {
        DocBorrowRecords docBorrowRecords = new DocBorrowRecords();
        //借阅时间
        docBorrowRecords.setBorrowDate(DateUtils.getNowDate());
        //借阅人id
        docBorrowRecords.setBorrowerUserId(SecurityUtils.getUserId());
        //借阅人
        String nickName = docBorrowRecordsMapper.selUserName(String.valueOf(SecurityUtils.getUserId()));
        docBorrowRecords.setBorrower(nickName);
        //借阅部门id
        String deptId =  docBorrowRecordsMapper.selDeptId(String.valueOf(SecurityUtils.getUserId()));
        docBorrowRecords.setDeptId(Long.valueOf(deptId));
        //借阅部门
        String dept = docBorrowRecordsMapper.selDept(deptId);
        docBorrowRecords.setDepartment(dept);
        return docBorrowRecords;
    }


    /**
     * 查询借阅记录列表
     * 
     * @param docBorrowRecords 借阅记录
     * @return 借阅记录
     */
    @Override
    public List<DocBorrowRecords> selectDocBorrowRecordsList(DocBorrowRecords docBorrowRecords)
    {
        return docBorrowRecordsMapper.selectDocBorrowRecordsList(docBorrowRecords);
    }

    /**
     * 我的收藏查询
     *
     * @param docBorrowRecords 借阅记录
     * @return 借阅记录
     */
    @Override
    public List<DocBorrowRecords> collectionTypeList(DocBorrowRecords docBorrowRecords)
    {
        return docBorrowRecordsMapper.collectionTypeList(docBorrowRecords);
    }

    /**
     * 新增借阅记录
     * 
     * @param docBorrowRecords 借阅记录
     * @return 结果
     */
    @Override
    public AjaxResult insertDocBorrowRecords(DocBorrowRecords docBorrowRecords)
    {
        //创建时间
        docBorrowRecords.setCreateTime(DateUtils.getNowDate());
        //创建人
        docBorrowRecords.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        //插入借阅信息表
        docBorrowRecordsMapper.insertDocBorrowRecords(docBorrowRecords);
        //插入审批表
/*        DocBorrowApprove docBorrowApprove = new DocBorrowApprove();
        docBorrowApprove.setCreateTime(DateUtils.getNowDate());
        docBorrowApprove.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        docBorrowApprove.setBorrowId(docBorrowRecords.getBorrowId());
        docBorrowApproveService.insertDocBorrowApprove(docBorrowApprove);*/
        if(docBorrowRecords.getDocIdList() == null){
            //插入档案借阅关联
            DocBorrowArchive borrowArchive = new DocBorrowArchive();
            //创建时间
            borrowArchive.setCreateTime(DateUtils.getNowDate());
            //创建人
            borrowArchive.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            //档案编号
            borrowArchive.setDocInfoId(Long.valueOf(docBorrowRecords.getDocId()));
            //借阅单号
            borrowArchive.setBorrowId(docBorrowRecords.getBorrowId());
            //插入
            docBorrowArchiveService.insertDocBorrowArchive(borrowArchive);
        }else{
            //循环取档案号
            for (Integer id : docBorrowRecords.getDocIdList()){
                //插入档案借阅关联
                DocBorrowArchive borrowArchive = new DocBorrowArchive();
                //创建时间
                borrowArchive.setCreateTime(DateUtils.getNowDate());
                //创建人
                borrowArchive.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
                //档案编号
                borrowArchive.setDocInfoId(Long.valueOf(id));
                //借阅单号
                borrowArchive.setBorrowId(docBorrowRecords.getBorrowId());
                //插入
                docBorrowArchiveService.insertDocBorrowArchive(borrowArchive);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 修改借阅记录
     * 
     * @param docBorrowRecords 借阅记录
     * @return 结果
     */
    @Override
    public int updateDocBorrowRecords(DocBorrowRecords docBorrowRecords)
    {
        docBorrowRecords.setUpdateTime(DateUtils.getNowDate());
        return docBorrowRecordsMapper.updateDocBorrowRecords(docBorrowRecords);
    }

    /**
     * 批量删除借阅记录
     * 
     * @param borrowIds 需要删除的借阅记录主键
     * @return 结果
     */
    @Override
    public int deleteDocBorrowRecordsByBorrowIds(Long[] borrowIds)
    {
        return docBorrowRecordsMapper.deleteDocBorrowRecordsByBorrowIds(borrowIds);
    }

    /**
     * 删除借阅记录信息
     * 
     * @param borrowId 借阅记录主键
     * @return 结果
     */
    @Override
    public int deleteDocBorrowRecordsByBorrowId(Long borrowId)
    {
        return docBorrowRecordsMapper.deleteDocBorrowRecordsByBorrowId(borrowId);
    }

    /**
     * 导入借阅模板
     *
     * @param file
     * @return 结果
     */
    @Override
    public AjaxResult importArchives(MultipartFile file) {
        try {
            //试试此方法，能否满足，并且简化操作
//            ExcelUtil<DocBorrowRecords> util = new ExcelUtil<>(DocBorrowRecords.class);
//            List<DocBorrowRecords> DocBorrowRecordsList = util.importExcel(file.getInputStream());

            List<DocBorrowRecords> imports = readWarehouses(file.getInputStream());

            for (DocBorrowRecords imp : imports) {
                DocBorrowRecords warehouse = getInfo();
                warehouse.setDocId(imp.getDocId());
                warehouse.setBorrowDate(imp.getBorrowDate());
                warehouse.setReturnDate(imp.getReturnDate());
                warehouse.setBorrowReason(imp.getBorrowReason());
                warehouse.setRemark(imp.getRemark());
                warehouse.setCreateTime(DateUtils.getNowDate());
                warehouse.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
                insertDocBorrowRecords(warehouse);
            }

            return AjaxResult.success("导入成功 " + imports.size() + "条 !");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败！");
        }
    }

    /**
     * 解析借阅excel
     *
     * @param inputStream
     * @return 结果
     */
    public static List<DocBorrowRecords> readWarehouses(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<DocBorrowRecords> warehouses = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) { // Skip header
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            DocBorrowRecords warehouse = new DocBorrowRecords();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIdx) {
                    case 0:
                        warehouse.setDocId((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    case 1:
                        warehouse.setBorrowDate(currentCell.getDateCellValue());
                        break;
                    case 2:
                        warehouse.setReturnDate(currentCell.getDateCellValue());
                        break;
                    case 3:
                        warehouse.setBorrowReason(ExcelUtil.getCellValueAsString(currentCell));
                        break;
                    case 4:
                        warehouse.setRemark(ExcelUtil.getCellValueAsString(currentCell));
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

    /**
     * 加入收藏
     *
     * @param docBorrowRecords 档案架
     * @return 结果
     */
    @Override
    public AjaxResult updCollectionType(DocBorrowRecords docBorrowRecords)
    {
        for (Integer docId : docBorrowRecords.getDocIdList()){
            docBorrowRecords.setDocId(Long.valueOf(docId));
            docBorrowRecordsMapper.updCollectionType(docBorrowRecords);
        }

        return AjaxResult.success();
    }


    /**
     * 移除收藏
     *
     * @param docBorrowRecords 档案架
     * @return 结果
     */
    @Override
    public AjaxResult delCollectionType(DocBorrowRecords docBorrowRecords)
    {
        for (Integer docId : docBorrowRecords.getDocIdList()){
            docBorrowRecords.setDocId(Long.valueOf(docId));
            docBorrowRecordsMapper.delCollectionType(docBorrowRecords);
        }

        return AjaxResult.success();
    }


    /**
     * 借阅待审批查询
     * @return 结果
     */
    @Override
    public List<DocBorrowRecords> archivesApplovalList()
    {
        PageUtils.startPage();
        return docBorrowRecordsMapper.archivesApplovalList();
    }

    /**
     * 借阅待审批查询
     * @return 结果
     */
    @Override
    public List<DocBorrowRecords> selarchivesApplovalList(DocBorrowRecords docBorrowRecords)
    {
        PageUtils.startPage();
        return docBorrowRecordsMapper.selarchivesApplovalList(docBorrowRecords);
    }

    /**
     * 借阅审批查询
     * @return 结果
     */
    @Override
    public List<DocBorrowRecords> archivesApploval()
    {
        PageUtils.startPage();
        return docBorrowRecordsMapper.archivesApploval();
    }

    /**
     * 同意
     *
     * @param docBorrowRecords
     * @return 结果
     */
    @Override
    public AjaxResult approve(DocBorrowRecords docBorrowRecords)
    {
        //查询对应得借阅id
        Long borrowId = docBorrowRecordsMapper.selBorrowId(docBorrowRecords.getDocId());
        docBorrowRecords.setBorrowId(borrowId);
        //更新信息表审批状态
        docBorrowRecordsMapper.updApprove(docBorrowRecords);
        //往审批表插入数据
        DocBorrowApprove docBorrowApprove = new DocBorrowApprove();
        docBorrowApprove.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        docBorrowApprove.setCreateTime(DateUtils.getNowDate());
        docBorrowApprove.setBorrowId(docBorrowRecords.getBorrowId());
        docBorrowApprove.setApproveUserId(SecurityUtils.getUserId());
        docBorrowApprove.setApprovalResult(docBorrowRecords.getApprovalResult());
        docBorrowApprove.setApprovalSuggest(docBorrowRecords.getApprovalSuggest());
        docBorrowApprove.setApprovalTime(DateUtils.getNowDate());
        docBorrowApproveService.insertDocBorrowApprove(docBorrowApprove);
        return AjaxResult.success();
    }

    /**
     * 归还
     *
     * @param docBorrowRecords
     * @return 结果
     */
    @Override
    public AjaxResult returnDoc(DocBorrowRecords docBorrowRecords)
    {
        //查询对应得借阅id
        Long borrowId = docBorrowRecordsMapper.selBorrowId(docBorrowRecords.getDocId());
        docBorrowRecords.setBorrowId(borrowId);
        docBorrowRecords.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        docBorrowRecords.setCreateTime(DateUtils.getNowDate());
        String nickName = docBorrowRecordsMapper.selUserName(docBorrowRecords.getCreateBy());
        docBorrowRecords.setReturnConfirmer(nickName);
        //归还
        docBorrowRecordsMapper.updReturn(docBorrowRecords);
        return AjaxResult.success();
    }

    /**
     * 导入归还
     *
     * @param file
     * @return 结果
     */
    @Override
    public AjaxResult importReturn(MultipartFile file) {
        try {

            List<DocBorrowRecords> imports = readReturn(file.getInputStream());

            for (DocBorrowRecords imp : imports) {
                DocBorrowRecords warehouse = new DocBorrowRecords();
                warehouse.setDocId(imp.getDocId());
                returnDoc(warehouse);
            }

            return AjaxResult.success("导入成功 " + imports.size() + "条 !");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败！");
        }
    }

    /**
     * 解析借阅excel
     *
     * @param inputStream
     * @return 结果
     */
    public static List<DocBorrowRecords> readReturn(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<DocBorrowRecords> warehouses = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) { // Skip header
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            DocBorrowRecords warehouse = new DocBorrowRecords();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIdx) {
                    case 0:
                        warehouse.setDocId((long) ExcelUtil.getNumericCellValue(currentCell));
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

    /**
     * 续借
     *
     * @param docBorrowRecords
     * @return 结果
     */
    @Override
    public AjaxResult renew(DocBorrowRecords docBorrowRecords)
    {
        //查询对应得借阅id
        Long borrowId = docBorrowRecordsMapper.selBorrowId(docBorrowRecords.getDocId());
        DocRenewRecords docRenewRecords = new DocRenewRecords();
        docRenewRecords.setBorrowId(borrowId);
        docRenewRecords.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        docRenewRecords.setCreateTime(DateUtils.getNowDate());
        docRenewRecords.setRenewDay(docBorrowRecords.getRenewDay());
        docRenewRecordsService.insertDocRenewRecords(docRenewRecords);
        docBorrowRecords.setBorrowId(borrowId);
        docBorrowRecords.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        docBorrowRecords.setCreateTime(DateUtils.getNowDate());
        //更新借阅单号状态
        docBorrowRecordsMapper.updRenew(docBorrowRecords);
        return AjaxResult.success();
    }

    /**
     * 导入续借
     *
     * @param file
     * @return 结果
     */
    @Override
    public AjaxResult importRenew(MultipartFile file) {
        try {

            List<DocBorrowRecords> imports = readRenew(file.getInputStream());
            String msg = null;
            for (DocBorrowRecords imp : imports) {
                DocBorrowRecords warehouse = new DocBorrowRecords();
                warehouse.setDocId(imp.getDocId());
                warehouse.setRenewDay(imp.getRenewDay());
                //判断是否已经续借
                int a = docBorrowRecordsMapper.selArchives(warehouse);
                if(a == 0){
                    renew(warehouse);
                }else{
                    msg = "档案编号"+imp.getDocId()+"已经续借;"+msg;
                }
            }
            if(msg == null){
                return AjaxResult.success("导入成功 " + imports.size() + "条 !");
            }else{
                return AjaxResult.success(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败！");
        }
    }

    /**
     * 解析续借excel
     *
     * @param inputStream
     * @return 结果
     */
    public static List<DocBorrowRecords> readRenew(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<DocBorrowRecords> warehouses = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) { // Skip header
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            DocBorrowRecords warehouse = new DocBorrowRecords();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIdx) {
                    case 0:
                        warehouse.setDocId((long) ExcelUtil.getNumericCellValue(currentCell));
                        break;
                    case 1:
                        warehouse.setRenewDay((long) ExcelUtil.getNumericCellValue(currentCell));
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

    /**
     * 查询待归还记录
     *
     * @param docBorrowRecords
     * @return 库房管理
     */
    @Override
    public List<DocBorrowRecords> beReturnList(DocBorrowRecords docBorrowRecords) {
        PageUtils.startPage();
        return docBorrowRecordsMapper.beReturnList(docBorrowRecords);
    }

    /**
     * 查询归还记录
     *
     * @param docBorrowRecords
     * @return 库房管理
     */
    @Override
    public List<DocBorrowRecords> returnList(DocBorrowRecords docBorrowRecords) {
        PageUtils.startPage();
        return docBorrowRecordsMapper.returnList(docBorrowRecords);
    }

    /**
     * 超期
     */
    @Override
    public List<DocBorrowRecords> extended(DocBorrowRecords docBorrowRecords) {
        PageUtils.startPage();
        return docBorrowRecordsMapper.extended(docBorrowRecords);
    }

    /**
     * 查询是否借阅
     * @return 结果
     */
    @Override
    public AjaxResult selArchives(DocBorrowRecords docBorrowRecords)
    {
        int a = docBorrowRecordsMapper.selArchives(docBorrowRecords);
        return AjaxResult.success(a);
    }

    /**
     * 查询档案信息
     * @return 结果
     */
    @Override
    public List<DocBorrowRecords> selDocInfoList(DocBorrowRecords docBorrowRecords)
    {
        return docBorrowRecordsMapper.selDocInfoList(docBorrowRecords);
    }
}
