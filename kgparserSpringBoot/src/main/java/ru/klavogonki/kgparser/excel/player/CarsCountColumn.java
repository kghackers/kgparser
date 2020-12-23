package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class CarsCountColumn implements PlayerColumn<Integer> {

    @Override
    public String getColumnName() {
        return "Машин";
    }

    @Override
    public int getColumnWidth() {
        return 4000;
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getCarsCount;
    }

    @Override
    public Class<Integer> fieldClass() {
        return Integer.class;
    }
}
