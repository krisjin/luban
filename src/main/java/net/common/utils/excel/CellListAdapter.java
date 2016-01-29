package net.common.utils.excel;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * Sheet中所有单元格加载的列表适配器
 * User: shijingui
 * Date: 2016/1/29
 */
public abstract class CellListAdapter {
    /**
     * 子类Adapter必须实现该方法，返回合并单元格的列表
     *
     * @return
     */
    public abstract List<CellModel> getCellModeList(ExcelType excelType, Workbook workbook);
}
