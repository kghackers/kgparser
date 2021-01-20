package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.excel.ExcelExportContext;
import ru.klavogonki.statistics.excel.data.ExcelExportContextData;

import java.util.function.Function;

public interface PlayerColumn<D extends ExcelExportContextData, T> { // todo: rename to ExcelColumn?

    String getColumnName();

    int getColumnWidth(); // in Excel format for Sheet#setColumnWidth

    Function<D, T> playerFieldGetter();

    Class<T> fieldClass();

    default T getValue(D player) {
        return playerFieldGetter().apply(player);
    }

    default void setCellFormat(ExcelExportContext<D> context) {
        context.setTextAlignLeftStyle();
    }

    default void formatCell(ExcelExportContext<D> context) {
        setCellFormat(context);

        setCellValue(context);
    }

    default void setCellValue(final ExcelExportContext<D> context) {
        T value = getValue(context.player);
        String cellValue = (value == null) ? "—" : value.toString();

        context.cell.setCellValue(cellValue);
    }
}
