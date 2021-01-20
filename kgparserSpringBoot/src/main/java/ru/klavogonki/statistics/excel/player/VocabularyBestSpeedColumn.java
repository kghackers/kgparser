package ru.klavogonki.statistics.excel.player;

import ru.klavogonki.statistics.dto.PlayerVocabularyDto;

import java.util.function.Function;

public class VocabularyBestSpeedColumn implements IntegerColumn<PlayerVocabularyDto> {

    @Override
    public String getColumnName() {
        return "Рекорд";
    }

    @Override
    public Function<PlayerVocabularyDto, Integer> playerFieldGetter() {
        return PlayerVocabularyDto::getBestSpeed;
    }
}
