package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;

public interface DoubleColumn<D extends ExcelExportContextData> extends PlayerColumn<D, Double> {

    @Override
    default int getColumnWidth() {
        return 4000;
    }

    @Override
    default void setCellFormat(final ExcelExportContext<D> context) {
        context.setDoubleStyle();
    }

    @Override
    default void formatCell(ExcelExportContext<D> context) {
        Double value = getValue(context.player);

        if (value == null) {
            context.setTextAlignRightStyle(); // align right to be consistent with number values
            context.cell.setCellValue("—");
        }
        else {
            context.setDoubleStyle();
            context.cell.setCellValue(value);
        }
    }

    @Override
    default Class<Double> fieldClass() {
        return Double.class;
    }
}
