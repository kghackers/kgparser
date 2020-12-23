package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class LoginColumn implements PlayerColumn<String> {

    @Override
    public String getColumnName() {
        return "Логин";
    }

    @Override
    public int getColumnWidth() {
        return 6000;
    }

    @Override
    public Function<PlayerDto, String> playerFieldGetter() {
        return PlayerDto::getLogin;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }
}
