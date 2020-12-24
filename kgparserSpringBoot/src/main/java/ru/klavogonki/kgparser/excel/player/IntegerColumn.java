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
        Integer value = getValue(context.player);

        if (value == null) {
            context.setTextFormat();
            context.setAlignRight(); // align right to be consistent with number values
            context.cell.setCellValue("—");
        }
        else {
            setCellFormat(context); // todo: if null, than text format?
            context.cell.setCellValue(value);
        }
    }

    @Override
    default Class<Integer> fieldClass() {
        return Integer.class;
    }
}
