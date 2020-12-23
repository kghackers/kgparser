package ru.klavogonki.kgparser.freemarker;

/**
 * Aggregates names/paths of all pages, so that we can pass them to FreeMarker templates
 * and change links in one place.
 */
public class PageUrls {

    // html files
    public static final String INDEX = "stats.html";

    public static final String TOP_BY_BEST_SPEED_PAGE_1 = getTopBySpeedPageFilePath("", 1);
    public static final String TOP_BY_BEST_SPEED = "stat-top-by-best-speed.html";

    public static final String TOP_BY_TOTAL_RACES = "stat-top-by-total-races.html";
    public static final String TOP_BY_RATING_LEVEL = "stat-top-by-rating-level.html";
    public static final String TOP_BY_FRIENDS_COUNT = "stat-top-by-friends-count.html";
    public static final String TOP_BY_ACHIEVEMENTS_COUNT = "stat-top-by-achievements-count.html";
    public static final String TOP_BY_VOCABULARIES_COUNT = "stat-top-by-vocabularies-count.html";
    public static final String TOP_BY_CARS_COUNT = "stat-top-by-cars-count.html";

    // css files
    public static final String STATS_CSS = "css/stats.css";

    // js files
    // Chart.js
    // see https://www.chartjs.org/docs/latest/getting-started/installation.html
    public static final String CHART_JS = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.bundle.min.js";
    public static final String CHART_JS_INTEGRITY = "sha512-SuxO9djzjML6b9w9/I07IWnLnQhgyYVSpHZx0JV97kGBfTIsUYlWflyuW4ypnvhBrslz1yJ3R+S14fdCWmSmSA==";

    public static final String PLAYERS_BY_RANK_CHART_JS = "js/players-by-rank-chart.js";
    public static final String TOP_TABLE_JS = "js/stats-top-table.js";
    public static final String TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS = "js/stat-top-by-best-speed-login-to-page.js";

    public static String getIndexPageFilePath(final String rootDir) {
        return rootDir + "/" + INDEX;
    }

    public static String getTopBySpeedPageFilePath(final String rootDir, final int pageNumber) {
        return rootDir + "stat-top-by-best-speed-page-" + pageNumber + ".html";
    }

    public static String getTopBySpeedLoginToPageFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS;
    }

    public static String getTopByTotalRacesCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_TOTAL_RACES;
    }

    public static String getTopByBestSpeedFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_BEST_SPEED;
    }

    public static String getTopByRatingLevelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_RATING_LEVEL;
    }

    public static String getTopByFriendsCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_FRIENDS_COUNT;
    }

    public static String getTopByAchievementsCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_ACHIEVEMENTS_COUNT;
    }

    public static String getTopByVocabulariesCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_VOCABULARIES_COUNT;
    }

    public static String getTopByCarsCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_CARS_COUNT;
    }
}