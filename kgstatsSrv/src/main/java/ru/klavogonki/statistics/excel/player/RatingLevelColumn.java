package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerDto;

import java.util.function.Function;

public class RatingLevelColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Уровень";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getRatingLevel;
    }
}
