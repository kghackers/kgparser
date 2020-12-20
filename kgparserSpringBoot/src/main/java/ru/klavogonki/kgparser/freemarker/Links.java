package ru.klavogonki.kgparser.freemarker;

import lombok.Data;

/**
 * Object with page and files urls to pass to FreeMarker templates.
 */
@Data
@SuppressWarnings("unused")
public class Links {

    // html files
    private String index = PageUrls.INDEX;

    private String topByBestSpeedPage1 = PageUrls.TOP_BY_BEST_SPEED_PAGE_1;
    private String topByBestSpeed = PageUrls.TOP_BY_BEST_SPEED;

    private String topByTotalRaces = PageUrls.TOP_BY_TOTAL_RACES;
    private String topByRatingLevel = PageUrls.TOP_BY_RATING_LEVEL;
    private String topByFriendsCount = PageUrls.TOP_BY_FRIENDS_COUNT;
    private String topByAchievementsCount = PageUrls.TOP_BY_ACHIEVEMENTS_COUNT;
    private String topByVocabulariesCount = PageUrls.TOP_BY_VOCABULARIES_COUNT;
    private String topByCarsCount = PageUrls.TOP_BY_CARS_COUNT;

    // css files
    private String statsCss = PageUrls.STATS_CSS;

    // js files
    private String chartJs = PageUrls.CHART_JS;
    private String chartJsIntegrity = PageUrls.CHART_JS_INTEGRITY;

    private String playersByRankChartJs = PageUrls.PLAYERS_BY_RANK_CHART_JS;
    private String topTableJs = PageUrls.TOP_TABLE_JS;
    private String topByBestSpeedLoginToPageJs = PageUrls.TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS;
}
