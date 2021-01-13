package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.function.Function;

public class VocabularyQualColumn implements IntegerColumn<PlayerVocabularyDto> { // todo: generalize type to interface with #getQual when required

    @Override
    public String getColumnName() {
        return "Квала";
    }

    @Override
    public Function<PlayerVocabularyDto, Integer> playerFieldGetter() {
        return PlayerVocabularyDto::getQual;
    }
}
