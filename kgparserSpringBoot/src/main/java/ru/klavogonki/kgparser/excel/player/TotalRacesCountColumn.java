package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class TotalRacesCountColumn implements IntegerColumn {

    @Override
    public String getColumnName() {
        return "Общий пробег";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getTotalRacesCount;
    }
}
