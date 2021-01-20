package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerVocabularyDto;

import java.util.function.Function;

public class VocabularyRacesCountColumn implements IntegerColumn<PlayerVocabularyDto> {

    @Override
    public String getColumnName() {
        return "Общий пробег";
    }

    @Override
    public Function<PlayerVocabularyDto, Integer> playerFieldGetter() {
        return PlayerVocabularyDto::getRacesCount;
    }
}
