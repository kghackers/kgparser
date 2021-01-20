package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerDto;

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
