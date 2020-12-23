package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class FriendsCountColumn implements PlayerColumn<Integer> {

    @Override
    public String getColumnName() {
        return "Друзей";
    }

    @Override
    public int getColumnWidth() {
        return 4000;
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getFriendsCount;
    }

    @Override
    public Class<Integer> fieldClass() {
        return Integer.class;
    }
}
