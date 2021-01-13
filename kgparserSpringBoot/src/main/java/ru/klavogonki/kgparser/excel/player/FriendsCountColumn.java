package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class FriendsCountColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Друзей";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getFriendsCount;
    }
}
