package ru.klavogonki.statistics.excel;

import ru.klavogonki.statistics.excel.player.AverageErrorColumn;
import ru.klavogonki.statistics.excel.player.AverageSpeedColumn;
import ru.klavogonki.statistics.excel.player.HaulColumn;
import ru.klavogonki.statistics.excel.player.LoginColumn;
import ru.klavogonki.statistics.excel.player.OrderNumberColumn;
import ru.klavogonki.statistics.excel.player.PlayerColumn;
import ru.klavogonki.statistics.excel.player.VocabularyBestSpeedColumn;
import ru.klavogonki.statistics.excel.player.VocabularyQualColumn;
import ru.klavogonki.statistics.excel.player.VocabularyRacesCountColumn;
import ru.klavogonki.statistics.excel.player.VocabularyStatsLinkColumn;
import ru.klavogonki.statistics.excel.player.VocabularyUpdatedColumn;
import ru.klavogonki.statistics.dto.PlayerVocabularyDto;

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
