package org.luban.common.io;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * User: krisjin
 * Date: 2016/4/15
 */
public class CsvExportUtil {
    private static final Logger log = Logger.getLogger(CsvExportUtil.class);
    public static int MAX_NUM = 10000; //每次读取一万条
    public static int MAX_TOTAL_NUM = 200000; //一次最多导出二十万条


    public static <T> void createCSVFile(File tmpFile, String[] properties, String[] titles, List<T> list) {
        BufferedWriter csvFileOutputStream = null;
        try {
            // GB2312使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile), "GB2312"), 1024);
            //显示标题
            for (int i = 0; i < titles.length - 1; i++) {
                csvFileOutputStream.write("\"" + titles[i] + "\",");
            }
            csvFileOutputStream.write("\"" + titles[titles.length - 1] + "\"");
            csvFileOutputStream.newLine();

            // 写入文件内容
            addContentRows(list, csvFileOutputStream, properties);

            csvFileOutputStream.flush();
        } catch (Exception e) {
            log.error("导出失败", e);
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("BufferedWriter 关闭异常", e);
            }
        }
    }

    public static <T> void appendCSVFile(File tmpFile, String[] properties, List<T> list) {
        BufferedWriter csvFileOutputStream = null;
        try {
            // GB2312使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile, true), "GB2312"), 1024);
            // 写入文件内容
            addContentRows(list, csvFileOutputStream, properties);
            csvFileOutputStream.flush();
        } catch (Exception e) {
            log.error("订单导出失败", e);
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("BufferedWriter 关闭异常", e);
            }
        }
    }


    private static <T> void addContentRows(List<T> list, BufferedWriter csvFileOutputStream, String[] properties) {
        if (!CollectionUtils.isEmpty(list)) {
            try {
                //实体类处理
                for (int i = 0; i < list.size(); i++) {
                    T data = list.get(i);
                    for (int j = 0; j < properties.length; j++) {
                        PropertyDescriptor pd = new PropertyDescriptor(properties[j], data.getClass());
                        Method getMethod = pd.getReadMethod();//获得get方法
                        Object field = getMethod.invoke(data);
                        if (null == field) {
                            field = "";
                        } else if (field instanceof Date) {
                            //field = DateTimeUtil.format((Date) field, "yyyy-MM-dd HH:mm:ss");
                        }
                        //写入，已英文,分割
                        if (j == properties.length - 1) {
                            csvFileOutputStream.write("\"" + field + "\"");
                        } else {
                            csvFileOutputStream.write("\"" + field + "\",");
                        }
                    }
                    csvFileOutputStream.newLine();
                }
            } catch (Exception e) {
                log.error("导出失败", e);
            }
        }
    }


    /**
     * 实体类处理
     *
     * @param <T>
     * @param fieldNames
     * @param fieldClass
     * @return
     * @throws NoSuchFieldException
     */
    public static <T> Field[] createColumnFileds(String[] fieldNames, Class<T> fieldClass) throws NoSuchFieldException {
        Field[] fields = new Field[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            Field field = fieldClass.getDeclaredField(fieldNames[i]);
            field.setAccessible(true);
            fields[i] = field;
        }
        return fields;
    }

    public static void writeResponse(InputStream is, OutputStream os) {
        try {
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = is.read(b)) > 0) {
                os.write(b, 0, i);
            }
            os.flush();
        } catch (Exception e) {
            log.error("导出失败", e);
        }
    }

    /**
     * 导出设置参数
     *
     * @param response
     * @param fileName
     */
    public static void setResponseParam(HttpServletResponse response, String fileName) {
        response.setContentType("application/csv;charset=GBK");
        fileName = fileName + ".csv";
        try {
            response.setHeader("Content-Disposition", "attachment;filename=".concat(new String(fileName.getBytes("GBK"), "iso8859-1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/csv");
    }
}
