package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

// todo: think how to render this as hyperlink
public class ProfileLinkColumn implements PlayerColumn<String> {

    @Override
    public String getColumnName() {
        return "Профиль";
    }

    @Override
    public int getColumnWidth() {
        return 3000;
    }

    @Override
    public Function<PlayerDto, String> playerFieldGetter() {
        return PlayerDto::getProfileLink;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }
}
