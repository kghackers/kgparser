package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;

public interface IntegerColumn extends PlayerColumn<Integer> {

    @Override
    default int getColumnWidth() {
        return 4000;
    }

    @Override
    default void setCellFormat(final ExcelExportContext context) {
        context.setIntegerFormat();
    }

    @Override
    default void formatCell(ExcelExportContext context) {
        setCellFormat(context); // todo: if null, than text format?

        Integer value = getValue(context.player);

        if (value == null) {
            context.cell.setCellValue("—");
        }
        else {
            context.cell.setCellValue(value);
        }
    }

    @Override
    default Class<Integer> fieldClass() {
        return Integer.class;
    }
}
