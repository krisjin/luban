package org.luban.common.io.excel;

import com.google.common.base.Strings;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.luban.common.annotations.ExcelEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/30
 * Time: 22:37
 */
public final class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String STATDATE_CELL = "STATDATE";

    private static final String APPID_CELL = "APPID";

    private static final String ACTIVECOUNT_CELL = "ACTIVECOUNT";

    private static Class<?> excelEntity;


    private ExcelUtil() {
    }


    /**
     * @param is
     * @return
     */
    public static List<?> read(InputStream is) {
        Workbook workbook = null;
        InputStream fisNew = is;
        List<Object> beanList = new ArrayList();
        try {
            if (!is.markSupported()) {
                fisNew = new PushbackInputStream(is, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(fisNew)) {
                //excel2003
                workbook = new HSSFWorkbook(fisNew);
            } else if (POIXMLDocument.hasOOXMLHeader(fisNew)) {
                //excel2007
                workbook = new XSSFWorkbook(OPCPackage.open(fisNew));
            } else {
                // LOGGER.info("The import file type is not correct！");
                return new ArrayList<Class<?>>();
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<String> props = parseSheetProps(sheet);
            int rows = sheet.getLastRowNum();

            for (int i = 2; i <= rows; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    try {
                        Object obj = excelEntity.newInstance();
                        for (int p = 0; p < props.size(); p++) {
                            Object param = getCellValue(row.getCell(p));
                            addData(props.get(p), param, obj);
                        }
                        beanList.add(obj);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return beanList;
    }


    /**
     * @param clazz
     */
    public static void initBean(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ExcelEntity.class)) {
            ExcelEntity excelEntity = clazz.getAnnotation(ExcelEntity.class);
            String cls = excelEntity.value();
            try {
                ExcelUtil.excelEntity = Class.forName(cls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析Sheet中的属性定义
     *
     * @param sheet
     * @return
     */
    private static List parseSheetProps(Sheet sheet) {
        List props = new ArrayList();
        if (sheet == null) {
            return props;
        }
        Row row = sheet.getRow(1);
        int cells = row.getLastCellNum();
        for (int i = 0; i < cells; i++) {
            Cell cell = row.getCell(i);
            String cellVal = cell.getStringCellValue();
            if (!Strings.isNullOrEmpty(cellVal)) {
                cellVal = cellVal.trim().substring(1);
            }
            props.add(cellVal);
        }
        return props;
    }

    /**
     * @param prop
     * @param param
     * @param obj
     */
    private static void addData(String prop, Object param, Object obj) {
        Method[] methods = excelEntity.getMethods();
        String setterMehtod = setterPropMethod(prop);
        for (Method method : methods) {
            String name = method.getName();
            if (name.equals(setterMehtod)) {
                try {
                    method.invoke(obj, param);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    try {
                        method.invoke(obj, String.valueOf(param));
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    /**
     * 获取setter方法
     *
     * @param prop
     * @return
     */
    private static String setterPropMethod(String prop) {
        String setMethod;
        setMethod = "set" + Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
        return setMethod;
    }


    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                return cell.getStringCellValue();
        }
    }
}
