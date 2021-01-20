package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerDto;

import java.util.function.Function;

public class VocabulariesCountColumn implements IntegerColumn<PlayerDto> {

    @Override
    public String getColumnName() {
        return "Словарей";
    }

    @Override
    public Function<PlayerDto, Integer> playerFieldGetter() {
        return PlayerDto::getVocabulariesCount;
    }
}
