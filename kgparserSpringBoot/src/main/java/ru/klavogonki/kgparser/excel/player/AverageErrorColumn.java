package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.function.Function;

public class AverageErrorColumn implements DoubleColumn<PlayerVocabularyDto> { // todo: generalize type to interface with #getAverageError when required

    @Override
    public String getColumnName() {
        return "Ошибки, %";
    }

    @Override
    public Function<PlayerVocabularyDto, Double> playerFieldGetter() {
        return PlayerVocabularyDto::getAverageError;
    }
}
