package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerDto;

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
