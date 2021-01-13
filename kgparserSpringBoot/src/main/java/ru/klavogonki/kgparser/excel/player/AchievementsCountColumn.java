package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public class AchievementsCountColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Достижений";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getAchievementsCount;
    }
}
