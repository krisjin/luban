package org.bscl.common.io.excel;


import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: shijingui
 * Date: 2016/1/29
 */
public class Excel {
    private String outputPath;
    private ExcelType excelType;
    private String[] sheetNames;

    private Workbook workbook;
    private Sheet[] sheets;
    private CellListAdapter[] cellListAdapters;


    private String templatePath;
    private Map<String, String>[] variableMaps;

    FileInputStream inputStream = null;
    FileOutputStream outputStream = null;


    public Excel(String outputPath, ExcelType excelType, String[] sheetNames) {

        if (outputPath == null || outputPath.equals(""))
            throw new IllegalArgumentException("excel cannot without a output path");

        this.outputPath = outputPath;
        this.excelType = excelType;
        this.sheetNames = sheetNames;
    }

    public void setCellListAdapters(CellListAdapter[] cellListAdapters) {
        this.cellListAdapters = cellListAdapters;
    }

    /**
     * 使用Excel模板方式生成Excel
     *
     * @param templatePath
     * @param variableMaps
     */
    public void setTemplate(String templatePath, Map<String, String>[] variableMaps) {
        this.templatePath = templatePath;
        this.variableMaps = variableMaps;
    }

    /**
     * 初始化需要的实例变量
     */
    private void init() {

        if (null != this.templatePath && !"".equals(this.templatePath)) {
            File file = new File(this.templatePath);
            if (file.exists()) {
                try {
                    inputStream = new FileInputStream(this.templatePath);
                    ByteArrayInputStream in = new ByteArrayInputStream(readInputStream(inputStream));

                    switch (this.excelType) {
                        case XLS:
                            this.workbook = new HSSFWorkbook(in);
                            break;
                        case XLSX:
                            this.workbook = new XSSFWorkbook(in);
                            break;
                        default:
                            throw new IllegalArgumentException("can not recognized excel type");
                    }
                    if (this.variableMaps != null && this.variableMaps.length > 0) {
                        for (int i = 0; i < this.variableMaps.length; i++) {
                            this.sheets = new Sheet[this.sheetNames.length];
                            this.sheets[i] = workbook.getSheetAt(i);
                        }
                    }
                } catch (FileNotFoundException e) {
                    throw new IllegalArgumentException("can't find path [" + this.templatePath + "]", e);
                } catch (IOException e) {
                    throw new IllegalArgumentException("can't create workbook!", e);
                }
            }
        }

        if (null != this.outputPath && !"".equals(this.outputPath)) {
            try {
                outputStream = new FileOutputStream(this.outputPath);
                if (null == workbook) {
                    switch (this.excelType) {
                        case XLS:
                            this.workbook = new HSSFWorkbook();
                            break;
                        case XLSX:
                            this.workbook = new SXSSFWorkbook(); //new XSSFWorkbook() ;
                            break;
                        default:
                            throw new IllegalArgumentException("can not recognized excel type");
                    }
                    if (this.sheetNames != null && this.sheetNames.length > 0) {
                        this.sheets = new Sheet[this.sheetNames.length];
                        for (int i = 0; i < this.sheetNames.length; i++) {
                            this.sheets[i] = workbook.createSheet(this.sheetNames[i]);
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("can't create workbook!", e);
            }
        }

        if (null == this.workbook) {
            throw new IllegalArgumentException("can't create workbook!");
        }

    }

    /**
     * 输入生成的excel
     *
     * @throws Exception
     */
    public void outputExcel() throws Exception {
        init();

        try {
            List<CellModel> cells;
            Sheet sheet;
            Row row;
            Cell cell;

            for (int k = 0; k < this.sheets.length; k++) {
                sheet = this.sheets[k];
                sheet.setDefaultColumnWidth(15); // 设置单元格长度为15
                sheet.setDefaultRowHeightInPoints(20);

                if (null != variableMaps && null != variableMaps[k]) {
                    Map<String, String> params = getCellParams(sheet);
                    Set<Map.Entry<String, String>> paramSet = params.entrySet();
                    for (Map.Entry<String, String> entry : paramSet) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        String[] pos = value.split("#");
                        cell = sheet.getRow(Short.parseShort(pos[0])).getCell(Short.parseShort(pos[1]));
                        cell.setCellValue(variableMaps[k].get(key));
                    }
                }

                cells = this.cellListAdapters[k].getCellModeList(this.excelType, this.workbook);
                if (null != cells && cells.size() > 0) {
                    CellModel cellModel;
                    for (int i = 0; i < cells.size(); i++) {
                        cellModel = cells.get(i);
                        sheet.addMergedRegion(new CellRangeAddress(cellModel.getStartY(),
                                cellModel.getEndY(), (short) cellModel.getStartX(),
                                (short) cellModel.getEndX()));
                        row = sheet.getRow(cellModel.getStartY());
                        if (null == row) {
                            row = sheet.createRow(cellModel.getStartY());
                        }
                        cell = row.getCell(cellModel.getStartX());
                        if (null == cell) {
                            cell = row.createCell(cellModel.getStartX());
                        }
                        if (cellModel.getCellStyle() != null) {
                            cell.setCellStyle(cellModel.getCellStyle());
                        }
                        convertAndSetCellValue(cell, this.excelType, cellModel.getType(), cellModel.getText());
                    }
                }
            }

            this.workbook.write(outputStream);

            outputStream.close();
            if (inputStream != null) inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) outputStream.close();
            if (inputStream != null) inputStream.close();
        }
    }

    /**
     * 根据cell的类型，赋值cell
     *
     * @param cell
     * @param excelType
     * @param type
     * @param value
     */
    private void convertAndSetCellValue(Cell cell, ExcelType excelType, int type, String value) {
        cell.setCellType(type);
        switch (type) {
            case Cell.CELL_TYPE_NUMERIC:
                //excel中数值全是double类型
                cell.setCellValue(Double.parseDouble(value));
                break;
            case Cell.CELL_TYPE_FORMULA:
                cell.setCellFormula(value);
                break;
            default:
                if (excelType == ExcelType.XLS) {
                    RichTextString text = new HSSFRichTextString(value);
                    cell.setCellValue(text);
                } else if (excelType == ExcelType.XLSX) {
                    RichTextString text = new XSSFRichTextString(value);
                    cell.setCellValue(text);
                } else {
                    throw new IllegalArgumentException("can not recognized excel type");
                }
                break;
        }
    }

    /**
     * 读取流缓存到字节数组中
     *
     * @param inputStream
     * @return
     */
    public static byte[] readInputStream(InputStream inputStream) {
        ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) > 0) {
                byteOs.write(buffer, 0, len);
            }
            byteOs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteOs.toByteArray();
    }

    /**
     * 获取Excel中的变量
     *
     * @param sheet
     * @return
     */
    public static Map<String, String> getCellParams(Sheet sheet) {
        Map<String, String> params = new HashMap<String, String>();

        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();

        Row row;
        Cell cell;

        Pattern p = Pattern.compile("\\$\\{(.*)\\}");
        Matcher matcher;
        for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {
            row = sheet.getRow(rowIndex);
            if (null == row) {
                continue;
            }
            int firstCell = row.getFirstCellNum();
            int lastCell = row.getLastCellNum();

            for (int cellIndex = firstCell; cellIndex < lastCell; cellIndex++) {
                cell = row.getCell(cellIndex);
                if (null == cell) {
                    continue;
                }
                String text = cell.getStringCellValue();
                if (null != text && !"".equals(text)) {
                    text = text.trim();
                    matcher = p.matcher(text);
                    String variable = "";
                    while (matcher.find()) {
                        variable = matcher.group(1);
                    }
                    if (!"".equals(variable)) {
                        params.put(variable, rowIndex + "#" + cellIndex);
                    }
                }
            }
        }
        return params;
    }

}