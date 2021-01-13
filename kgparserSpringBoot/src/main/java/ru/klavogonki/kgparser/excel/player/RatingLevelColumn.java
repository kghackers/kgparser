package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

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
