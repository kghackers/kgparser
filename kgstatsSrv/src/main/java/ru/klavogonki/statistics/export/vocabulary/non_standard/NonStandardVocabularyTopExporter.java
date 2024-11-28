package ru.klavogonki.statistics.export.vocabulary.non_standard;

import ru.klavogonki.common.NonStandardDictionary;
import ru.klavogonki.statistics.dictionaries.NonStandardDictionariesCache;
import ru.klavogonki.statistics.dictionaries.NonStandardDictionaryData;
import ru.klavogonki.statistics.dictionaries.NonStandardDictionaryTopData;
import ru.klavogonki.statistics.export.vocabulary.VocabularyTopExporter;
import ru.klavogonki.statistics.export.vocabulary.VocabularyTopUtils;

import java.util.Objects;

// todo: this object is basically the same as impelemented by NonStandardVocabularyTopExporterGenerator
public interface NonStandardVocabularyTopExporter extends VocabularyTopExporter {

    @Override
    default boolean isStandard() {
        return false;
    }

    int dictionaryId();

    @Deprecated(since = "use vocabularyData() instead")
    default NonStandardDictionary vocabulary() {
        return NonStandardDictionary.getByDictionaryId(
            dictionaryId()
        );
    }

    default NonStandardDictionaryData vocabularyData() {
        int code = dictionaryId();

        NonStandardDictionaryData dictionaryData = NonStandardDictionariesCache.INSTANCE.getDictionary(code);

        assertDictionaryHasTopSet(dictionaryData);

        return dictionaryData;
    }

    default NonStandardDictionaryTopData vocabularyTopData() {
        return assertDictionaryHasTopSet(vocabularyData());
    }

    @Override
    default int minRacesCount() {
        return vocabularyTopData().minRacesCount;
    }

    @Override
    default String loggerName() {
        return vocabularyTopData().loggerName;
    }

    private static NonStandardDictionaryTopData assertDictionaryHasTopSet(NonStandardDictionaryData dictionaryData) {
        Objects.requireNonNull(
            dictionaryData.top,
            String.format(
                "Non-standard dictionary with code = %d, name = \"%s\" has no top configured.",
                dictionaryData.code,
                dictionaryData.displayName
            )
        );

        return dictionaryData.top;
    }

    @Override
    default String vocabularyCode() { // full code. in "voc-123" format
        return vocabularyData().getFullCode();
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
        // same logic as in NonStandardVocabularyTopExporterGenerator
        return Objects.requireNonNullElseGet(
            vocabularyTopData().topByBestSpeedExcelSheetName,
            () -> VocabularyTopUtils.topByBestSpeedExcelSheetName(vocabulary())
        );
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
        // same logic as in NonStandardVocabularyTopExporterGenerator
        return Objects.requireNonNullElseGet(
            vocabularyTopData().topByRacesCountExcelSheetName,
            () -> VocabularyTopUtils.topByRacesCountIn(vocabulary())
        );
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
        // same logic as in NonStandardVocabularyTopExporterGenerator
        return Objects.requireNonNullElseGet(
            vocabularyTopData().topByHaulExcelSheetName,
            () -> VocabularyTopUtils.topByHaulIn(vocabulary())
        );
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
