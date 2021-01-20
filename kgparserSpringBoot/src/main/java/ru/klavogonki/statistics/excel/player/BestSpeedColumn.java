package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerDto;

import java.util.function.Function;

public class BestSpeedColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Рекорд в «Обычном»";
    }

    @Override
    public int getColumnWidth() {
        return 6000;
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getBestSpeed;
    }
}
