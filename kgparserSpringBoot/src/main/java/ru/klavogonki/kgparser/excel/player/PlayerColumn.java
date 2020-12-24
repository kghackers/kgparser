package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public interface PlayerColumn<T> {

    String getColumnName();

    int getColumnWidth(); // in excel format for Sheet#setColumnWidth

    Function<PlayerDto, T> playerFieldGetter();

    Class<T> fieldClass();

    default T getValue(PlayerDto player) {
        return playerFieldGetter().apply(player);
    }

    default void setCellFormat(ExcelExportContext context) {
        context.setTextFormat();
    }

    default void formatCell(ExcelExportContext context) {
        setCellFormat(context);

        T value = getValue(context.player);
        String cellValue = (value == null) ? "—" : value.toString();

        context.cell.setCellValue(cellValue);
    }
}
