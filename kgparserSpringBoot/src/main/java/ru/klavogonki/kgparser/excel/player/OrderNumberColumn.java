package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class OrderNumberColumn implements PlayerColumn<String> {

    @Override
    public String getColumnName() {
        return "#";
    }

    @Override
    public int getColumnWidth() {
        return 4000;
    }

    @Override
    public Function<PlayerDto, String> playerFieldGetter() {
        return PlayerDto::getOrderNumber;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }

    @Override
    public void formatCell(final ExcelExportContext context) {
        PlayerColumn.super.formatCell(context); // call default method of the given interface

        context.setAlignRight();
    }
}
