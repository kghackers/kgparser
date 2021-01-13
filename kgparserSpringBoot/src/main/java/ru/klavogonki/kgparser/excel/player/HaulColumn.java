package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.function.Function;

public class HaulColumn implements PlayerColumn<PlayerVocabularyDto, String> { // todo: generalize type to interface with #getHaul when required

    @Override
    public String getColumnName() {
        return "Общее время";
    }

    @Override
    public int getColumnWidth() {
        return 6000;
    }

    @Override
    public Function<PlayerVocabularyDto, String> playerFieldGetter() {
        return PlayerVocabularyDto::getHaul;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }

    @Override
    public void formatCell(final ExcelExportContext<PlayerVocabularyDto> context) {
        context.setTextAlignRightStyle();

        PlayerColumn.super.setCellValue(context); // call default method of the given interface
    }
}
