package ru.klavogonki.statistics.freemarker;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import ru.klavogonki.common.NonStandardDictionary;
import ru.klavogonki.statistics.export.vocabulary.NonStandardVocabularyGeneratorContext;
import ru.klavogonki.statistics.export.vocabulary.standard.AbraTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.CharsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.DigitsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.MarathonTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.NoErrorTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.NormalTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.ReferatsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.SprintTopExporter;

/**
 * Object with page and files urls to pass to FreeMarker templates.
 */
@Data
@SuppressWarnings("unused")
@Log4j2
public class Links {

    // html files
    private String index = PageUrls.INDEX;

    private String topByBestSpeedPage1 = PageUrls.TOP_BY_BEST_SPEED_PAGE_1;
    private String topByBestSpeedAllPagesXlsx = PageUrls.TOP_BY_BEST_SPEED_ALL_PAGES_XLSX;
    private String topByBestSpeedAllPagesZip = PageUrls.TOP_BY_BEST_SPEED_ALL_PAGES_ZIP;

    private String topByBestSpeed = PageUrls.TOP_BY_BEST_SPEED;
    private String topByBestSpeedXlsx = PageUrls.TOP_BY_BEST_SPEED_XLSX;
    private String topByBestSpeedZip = PageUrls.TOP_BY_BEST_SPEED_ZIP;

    private String topByTotalRaces = PageUrls.TOP_BY_TOTAL_RACES;
    private String topByTotalRacesXlsx = PageUrls.TOP_BY_TOTAL_RACES_XLSX;
    private String topByTotalRacesZip = PageUrls.TOP_BY_TOTAL_RACES_ZIP;

    private String topByRatingLevel = PageUrls.TOP_BY_RATING_LEVEL;
    private String topByRatingLevelXlsx = PageUrls.TOP_BY_RATING_LEVEL_XLSX;
    private String topByRatingLevelZip = PageUrls.TOP_BY_RATING_LEVEL_ZIP;

    private String topByFriendsCount = PageUrls.TOP_BY_FRIENDS_COUNT;
    private String topByFriendsCountXlsx = PageUrls.TOP_BY_FRIENDS_COUNT_XLSX;
    private String topByFriendsCountZip = PageUrls.TOP_BY_FRIENDS_COUNT_ZIP;

    private String topByAchievementsCount = PageUrls.TOP_BY_ACHIEVEMENTS_COUNT;
    private String topByAchievementsCountXlsx = PageUrls.TOP_BY_ACHIEVEMENTS_COUNT_XLSX;
    private String topByAchievementsCountZip = PageUrls.TOP_BY_ACHIEVEMENTS_COUNT_ZIP;

    private String topByVocabulariesCount = PageUrls.TOP_BY_VOCABULARIES_COUNT;
    private String topByVocabulariesCountXlsx = PageUrls.TOP_BY_VOCABULARIES_COUNT_XLSX;
    private String topByVocabulariesCountZip = PageUrls.TOP_BY_VOCABULARIES_COUNT_ZIP;

    private String topByCarsCount = PageUrls.TOP_BY_CARS_COUNT;
    private String topByCarsCountXlsx = PageUrls.TOP_BY_CARS_COUNT_XLSX;
    private String topByCarsCountZip = PageUrls.TOP_BY_CARS_COUNT_ZIP;

    private String playersByRank = PageUrls.PLAYER_BY_RANK;

    // vocabularies landing pages
    // standard vocabularies
    private String normalTopBySpeedPage1 = new NormalTopExporter().topByBestSpeedFirstPageFilePath();
    private String abraTopBySpeedPage1 = new AbraTopExporter().topByBestSpeedFirstPageFilePath();
    private String referatsTopBySpeedPage1 = new ReferatsTopExporter().topByBestSpeedFirstPageFilePath();
    private String noErrorTopBySpeedPage1 = new NoErrorTopExporter().topByBestSpeedFirstPageFilePath();
    private String marathonTopBySpeedPage1 = new MarathonTopExporter().topByBestSpeedFirstPageFilePath();
    private String charsTopBySpeedPage1 = new CharsTopExporter().topByBestSpeedFirstPageFilePath();
    private String digitsTopBySpeedPage1 = new DigitsTopExporter().topByBestSpeedFirstPageFilePath();
    private String sprintTopBySpeedPage1 = new SprintTopExporter().topByBestSpeedFirstPageFilePath();

    private String normalTopHeader = new NormalTopExporter().headerName();
    private String abraTopHeader = new AbraTopExporter().headerName();
    private String referatsTopHeader = new ReferatsTopExporter().headerName();
    private String noErrorTopHeader = new NoErrorTopExporter().headerName();
    private String marathonTopHeader = new MarathonTopExporter().headerName();
    private String charsTopHeader = new CharsTopExporter().headerName();
    private String digitsTopHeader = new DigitsTopExporter().headerName();
    private String sprintTopHeader = new SprintTopExporter().headerName();

    // non-standard vocabularies
    private String normalInEnglishTopBySpeedPage1;
    private String miniMarathonTopBySpeedPage1;
    private String shortTextsTopBySpeedPage1;
    private String frequencyVocabularyTopBySpeedPage1;
    private String oneHundredRussianTopBySpeedPage1;
    private String digitsOneHundredTopBySpeedPage1;
    private String trainingIndexFingersTopBySpeedPage1;
    private String ringFingersTopBySpeedPage1;
    private String pinkiesPlusTopBySpeedPage1;

    private String normalInEnglishTopHeader;
    private String miniMarathonTopHeader;
    private String shortTextsTopHeader;
    private String frequencyVocabularyTopHeader;
    private String oneHundredRussianTopHeader;
    private String digitsOneHundredTopHeader;
    private String trainingIndexFingersTopHeader;
    private String ringFingersTopHeader;
    private String pinkiesPlusTopHeader;

    // css files and images
    private String statsCss = PageUrls.STATS_CSS;
    private String faviconIco = PageUrls.FAVICON_ICO;
    private String excelPng = PageUrls.EXCEL_PNG;
    private String infoPng = PageUrls.INFO_PNG;

    // js files
    private String chartJs = PageUrls.CHART_JS;
    private String chartJsIntegrity = PageUrls.CHART_JS_INTEGRITY;

    private String playersByRankChartJs = PageUrls.PLAYERS_BY_RANK_CHART_JS;
    private String playersByRankDataJs = PageUrls.PLAYERS_BY_RANK_DATA_JS;
    private String topTableJs = PageUrls.TOP_TABLE_JS;
    private String topByBestSpeedLoginToPageJs = PageUrls.TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS;

    private Links() {
    }

    public static Links create(NonStandardVocabularyGeneratorContext context) {
        Links links = new Links();

        // todo: pass a map to Links instead of multiple separate hardcoded fields

        links.normalInEnglishTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.NORMAL_IN_ENGLISH);
        links.miniMarathonTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.MINI_MARATHON);
        links.shortTextsTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.SHORT_TEXTS);
        links.frequencyVocabularyTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.FREQUENCY_VOCABULARY);
        links.oneHundredRussianTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.ONE_HUNDRED_RUSSIAN);
        links.digitsOneHundredTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.DIGITS_ONE_HUNDRED);
        links.trainingIndexFingersTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.TRAINING_INDEX_FINGERS);
        links.ringFingersTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.RING_FINGERS);
        links.pinkiesPlusTopBySpeedPage1 = context.getTopBySpeedFirstPageFilePath(NonStandardDictionary.PINKIES_PLUS);

        links.normalInEnglishTopHeader = context.getHeaderName(NonStandardDictionary.NORMAL_IN_ENGLISH);
        links.miniMarathonTopHeader = context.getHeaderName(NonStandardDictionary.MINI_MARATHON);
        links.shortTextsTopHeader = context.getHeaderName(NonStandardDictionary.SHORT_TEXTS);
        links.frequencyVocabularyTopHeader = context.getHeaderName(NonStandardDictionary.FREQUENCY_VOCABULARY);
        links.oneHundredRussianTopHeader = context.getHeaderName(NonStandardDictionary.ONE_HUNDRED_RUSSIAN);
        links.digitsOneHundredTopHeader = context.getHeaderName(NonStandardDictionary.DIGITS_ONE_HUNDRED);
        links.trainingIndexFingersTopHeader = context.getHeaderName(NonStandardDictionary.TRAINING_INDEX_FINGERS);
        links.ringFingersTopHeader = context.getHeaderName(NonStandardDictionary.RING_FINGERS);
        links.pinkiesPlusTopHeader = context.getHeaderName(NonStandardDictionary.PINKIES_PLUS);

        return links;
    }
}
