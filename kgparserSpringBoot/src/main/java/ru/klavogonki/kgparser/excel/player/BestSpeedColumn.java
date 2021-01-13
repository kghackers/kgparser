package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

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
