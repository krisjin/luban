package org.luban.common.io.excel;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * 一个合并单元格属性集合Model
 * User: shijingui
 * Date: 2016/1/29
 */
public class CellModel {
    /**
     * Col坐标开始
     */
    private int startX;
    /**
     * Row坐标开始
     */
    private int startY;
    /**
     * Col坐标结束
     */
    private int endX;
    /**
     * Row坐标结束
     */
    private int endY;
    /**
     * 单元格类型
     */
    private int type;
    /**
     * 单元格内容
     */
    private String text;
    /**
     * 单元格样式
     */
    private CellStyle cellStyle;

    public CellModel() {
        super();
    }

    public CellModel(int startX, int startY, int endX, int endY, int type,
                     String text, CellStyle cellStyle) {
        super();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.type = type;
        this.text = text;
        this.cellStyle = cellStyle;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
