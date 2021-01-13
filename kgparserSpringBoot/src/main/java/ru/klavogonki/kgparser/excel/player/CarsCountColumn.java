package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class CarsCountColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Машин";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getCarsCount;
    }
}
