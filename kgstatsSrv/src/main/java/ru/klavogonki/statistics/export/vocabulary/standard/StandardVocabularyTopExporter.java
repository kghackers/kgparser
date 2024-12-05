package ru.klavogonki.statistics.export.vocabulary.standard;

import ru.klavogonki.common.StandardDictionary;
import ru.klavogonki.statistics.export.vocabulary.VocabularyTopExporter;
import ru.klavogonki.statistics.export.vocabulary.VocabularyTopUtils;

public interface StandardVocabularyTopExporter extends VocabularyTopExporter {

    @Override
    default boolean isStandard() {
        return true;
    }

    StandardDictionary vocabulary();

    @Override
    default String vocabularyCode() {
        return vocabulary().name();
    }

    @Override
    default String topByBestSpeedPageTitle() {
        return VocabularyTopUtils.topByBestSpeedIn(vocabulary());
    }
    @Override
    default String topByBestSpeedHeader() {
        return VocabularyTopUtils.topByBestSpeedIn(vocabulary());
    }
    @Override
    default String topByBestSpeedAdditionalHeader() {
        return VocabularyTopUtils.takenPlayersWithMinimalRacesCount(vocabulary(), minRacesCount());
    }
    @Override
    default String topByBestSpeedExcelSheetName() {
        return VocabularyTopUtils.topByBestSpeedExcelSheetName(vocabulary());
    }

    @Override
    default String topByRacesCountPageTitle() {
        return VocabularyTopUtils.topByRacesCountIn(vocabulary());
    }
    @Override
    default String topByRacesCountHeader() {
        return VocabularyTopUtils.topByRacesCountIn(vocabulary());
    }
    @Override
    default String topByRacesCountAdditionalHeader() {
        return VocabularyTopUtils.takenPlayersWithMinimalRacesCount(vocabulary(), minRacesCount());
    }
    @Override
    default String topByRacesCountExcelSheetName() {
        return VocabularyTopUtils.topByRacesCountExcelSheetName(vocabulary());
    }

    @Override
    default String topByHaulPageTitle() {
        return VocabularyTopUtils.topByHaulIn(vocabulary());
    }
    @Override
    default String topByHaulHeader() {
        return VocabularyTopUtils.topByHaulIn(vocabulary());
    }
    @Override
    default String topByHaulAdditionalHeader() {
        return VocabularyTopUtils.takenPlayersWithMinimalRacesCount(vocabulary(), minRacesCount());
    }
    @Override
    default String topByHaulExcelSheetName() {
        return VocabularyTopUtils.topByHaulExcelSheetName(vocabulary());
    }
}
