package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class RegisteredColumn implements PlayerColumn<String> {

    @Override
    public String getColumnName() {
        return "Зарегистрирован";
    }

    @Override
    public int getColumnWidth() {
        return 8000;
    }

    @Override
    public Function<PlayerDto, String> playerFieldGetter() { // todo: we should use Excel date instead
        return PlayerDto::getRegistered;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }
}
