package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.export.vocabulary.VocabularyTopExporter;
import ru.klavogonki.kgparser.export.vocabulary.VocabularyTopUtils;

public interface NonStandardVocabularyTopExporter extends VocabularyTopExporter {

    @Override
    default boolean isStandard() {
        return false;
    }

    NonStandardDictionary vocabulary();

    @Override
    default String vocabularyCode() {
        return vocabulary().code;
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
        return VocabularyTopUtils.topByRacesCountIn(vocabulary()); // 30 chars :)
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
        return VocabularyTopUtils.topByHaulIn(vocabulary());
    }

    // override compared to default to not compare "voc-" 2 times, since vocabularyCode() already contains "voc-"
    // todo: extract all the construction to a common, take "best-speed" etc as parameters as well
    @Override
    default String topByBestSpeedPageFilePath(final int pageNumber) {
        return String.format("./%s-top-by-best-speed-page-%d.html", vocabularyCode(), pageNumber);
    }
    @Override
    default String topByBestSpeedPageFileTemplate() { // for js paging
        return String.format("./%s-top-by-best-speed-page-${%s}.html", vocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    @Override
    default String topByBestSpeedLoginToPageFilePath() {
        return String.format("./js/%s-top-by-best-speed-login-to-page.js", vocabularyCode());
    }
    @Override
    default String topByBestSpeedExcelFilePath() {
        return String.format("./xlsx/%s-top-by-best-speed.xlsx", vocabularyCode());
    }
    @Override
    default String topByBestSpeedExcelZipFilePath() {
        return String.format("./xlsx/%s-top-by-best-speed.zip", vocabularyCode());
    }

    @Override
    default String topByRacesCountPageFilePath(final int pageNumber) {
        return String.format("./%s-top-by-races-count-page-%d.html", vocabularyCode(), pageNumber);
    }
    @Override
    default String topByRacesCountPageFileTemplate() { // for js paging
        return String.format("./%s-top-by-races-count-page-${%s}.html", vocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    @Override
    default String topByRacesCountLoginToPageFilePath() {
        return String.format("./js/%s-top-by-races-count-login-to-page.js", vocabularyCode());
    }
    @Override
    default String topByRacesCountExcelFilePath() {
        return String.format("./xlsx/%s-top-by-races-count.xlsx", vocabularyCode());
    }
    @Override
    default String topByRacesCountExcelZipFilePath() {
        return String.format("./xlsx/%s-top-by-races-count.zip", vocabularyCode());
    }

    @Override
    default String topByHaulPageFilePath(final int pageNumber) {
        return String.format("./%s-top-by-haul-page-%d.html", vocabularyCode(), pageNumber);
    }
    @Override
    default String topByHaulPageFileTemplate() { // for js paging
        return String.format("./%s-top-by-haul-page-${%s}.html", vocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    @Override
    default String topByHaulLoginToPageFilePath() {
        return String.format("./js/%s-top-by-haul-login-to-page.js", vocabularyCode());
    }
    @Override
    default String topByHaulExcelFilePath() {
        return String.format("./xlsx/%s-top-by-haul.xlsx", vocabularyCode());
    }
    @Override
    default String topByHaulExcelZipFilePath() {
        return String.format("./xlsx/%s-top-by-haul.zip", vocabularyCode());
    }
}
