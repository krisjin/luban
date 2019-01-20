package org.luban.common.io.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shijingui
 * Date: 2016/1/29
 */
public class StandardCellListAdapter extends CellListAdapter {
    private String[] headers;
    private String[] fields;
    private List dataList;

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public List<CellModel> getCellModeList(ExcelType excelType, Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        if (excelType == ExcelType.XLS) {
            headerStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        } else if (excelType == ExcelType.XLSX) {
            ((XSSFCellStyle) headerStyle).setFillForegroundColor(new XSSFColor(Color.BLUE));
        } else {
        }
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setColor(HSSFColor.VIOLET.index);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        //生成单元格样式
        CellStyle dataStyle = workbook.createCellStyle();
        if (excelType == ExcelType.XLS) {
            dataStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        } else if (excelType == ExcelType.XLSX) {
            ((XSSFCellStyle) dataStyle).setFillForegroundColor(new XSSFColor(Color.WHITE));
        } else {
        }
        dataStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dataStyle.setBorderRight(CellStyle.BORDER_THIN);
        dataStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // 生成单元格字体
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);
        dataStyle.setFont(dataFont);
        List<CellModel> cells = new ArrayList<CellModel>();
        CellModel cellModel;
        //行号计数
        int rowNum = 0;
        //创建列头
        for (short i = 0; i < headers.length; i++) {
            cellModel = new CellModel();
            cellModel.setStartX(i);
            cellModel.setStartY(rowNum);
            cellModel.setEndX(i);
            cellModel.setEndY(rowNum);
            cellModel.setCellStyle(headerStyle);
            cellModel.setText(headers[i]);
            cellModel.setType(Cell.CELL_TYPE_STRING);
            cells.add(cellModel);
        }
        //创建数据行
        for (int i = 0; i < dataList.size(); i++) {
            rowNum++;
            // 利用反射给每一个字段赋值
            for (int j = 0; j < fields.length; j++) {
                cellModel = new CellModel();
                cellModel.setStartX(j);
                cellModel.setStartY(rowNum);
                cellModel.setEndX(j);
                cellModel.setEndY(rowNum);
                cellModel.setCellStyle(dataStyle);
                String methodName = "get" + fields[j];
                Object value = null;
                try {
                    Class clazz = dataList.get(i).getClass();
                    Method method = clazz.getMethod(methodName, new Class[]{});
                    value = method.invoke(dataList.get(i), new Object[]{});
                } catch (Exception e) {
                    throw new RuntimeException("反射失败", e);
                }
                cellModel.setType(Cell.CELL_TYPE_STRING);
                cellModel.setText(value.toString());
                cells.add(cellModel);
            }
        }
        return cells;
    }
}