package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.excel.data.OrderNumberExcelData;

import java.util.function.Function;

public class OrderNumberColumn<D extends OrderNumberExcelData> implements PlayerColumn<D, String> {

    @Override
    public String getColumnName() {
        return "#";
    }

    @Override
    public int getColumnWidth() {
        return 4000;
    }

    @Override
    public Function<D, String> playerFieldGetter() {
        return OrderNumberExcelData::getOrderNumber;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }

    @Override
    public void formatCell(final ExcelExportContext<D> context) {
        context.setTextAlignRightStyle();

        PlayerColumn.super.setCellValue(context); // call default method of the given interface
    }
}
