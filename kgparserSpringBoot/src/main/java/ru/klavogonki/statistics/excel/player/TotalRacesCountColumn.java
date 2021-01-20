package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerDto;

import java.util.function.Function;

public class TotalRacesCountColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Общий пробег";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getTotalRacesCount;
    }
}
