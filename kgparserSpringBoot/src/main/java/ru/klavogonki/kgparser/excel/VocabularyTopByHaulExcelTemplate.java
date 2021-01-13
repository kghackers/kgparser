package ru.klavogonki.kgparser.excel;

import ru.klavogonki.kgparser.excel.player.AverageErrorColumn;
import ru.klavogonki.kgparser.excel.player.AverageSpeedColumn;
import ru.klavogonki.kgparser.excel.player.HaulColumn;
import ru.klavogonki.kgparser.excel.player.LoginColumn;
import ru.klavogonki.kgparser.excel.player.OrderNumberColumn;
import ru.klavogonki.kgparser.excel.player.PlayerColumn;
import ru.klavogonki.kgparser.excel.player.VocabularyBestSpeedColumn;
import ru.klavogonki.kgparser.excel.player.VocabularyQualColumn;
import ru.klavogonki.kgparser.excel.player.VocabularyRacesCountColumn;
import ru.klavogonki.kgparser.excel.player.VocabularyStatsLinkColumn;
import ru.klavogonki.kgparser.excel.player.VocabularyUpdatedColumn;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.List;

public class VocabularyTopByHaulExcelTemplate extends ExcelTemplate<PlayerVocabularyDto> {

    private String sheetName;

    public VocabularyTopByHaulExcelTemplate(final String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public List<? extends PlayerColumn<PlayerVocabularyDto, ?>> getColumns() {
        return List.of(
            new OrderNumberColumn<>(),
            new LoginColumn<>(),
            new VocabularyStatsLinkColumn(),
            new HaulColumn(),
            new VocabularyBestSpeedColumn(),
            new VocabularyRacesCountColumn(),
            new AverageSpeedColumn(),
            new AverageErrorColumn(),
            new VocabularyQualColumn(),
            new VocabularyUpdatedColumn()
        );
    }
}
