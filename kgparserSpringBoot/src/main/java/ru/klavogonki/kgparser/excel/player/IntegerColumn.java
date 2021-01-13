package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;

public interface IntegerColumn<D extends ExcelExportContextData> extends PlayerColumn<D, Integer> {

    @Override
    default int getColumnWidth() {
        return 4000;
    }

    @Override
    default void setCellFormat(final ExcelExportContext<D> context) {
        context.setIntegerStyle();
    }

    @Override
    default void formatCell(ExcelExportContext<D> context) {
        Integer value = getValue(context.player);

        if (value == null) {
            context.setTextAlignRightStyle(); // align right to be consistent with number values
            context.cell.setCellValue("—");
        }
        else {
            context.setIntegerStyle();
            context.cell.setCellValue(value);
        }
    }

    @Override
    default Class<Integer> fieldClass() {
        return Integer.class;
    }
}
