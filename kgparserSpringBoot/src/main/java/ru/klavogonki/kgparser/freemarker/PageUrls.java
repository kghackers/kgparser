package ru.klavogonki.kgparser.freemarker;

import ru.klavogonki.kgparser.export.ExportContext;

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

    public static final String PLAYER_BY_RANK = "stat-players-by-rank.html";

    // xls files and their zip form
    // big top by speed, all pages
    public static final String TOP_BY_BEST_SPEED_ALL_PAGES_XLSX = "xlsx/stat-top-by-best-speed-all-pages.xlsx";
    public static final String TOP_BY_BEST_SPEED_ALL_PAGES_ZIP = "xlsx/stat-top-by-best-speed-all-pages.zip";

    public static final String TOP_BY_BEST_SPEED_XLSX = "xlsx/stat-top-by-best-speed.xlsx";
    public static final String TOP_BY_BEST_SPEED_ZIP = "xlsx/stat-top-by-best-speed.zip";

    public static final String TOP_BY_TOTAL_RACES_XLSX = "xlsx/stat-top-by-total-races.xlsx";
    public static final String TOP_BY_TOTAL_RACES_ZIP = "xlsx/stat-top-by-total-races.zip";

    public static final String TOP_BY_RATING_LEVEL_XLSX = "xlsx/stat-top-by-rating-level.xlsx";
    public static final String TOP_BY_RATING_LEVEL_ZIP = "xlsx/stat-top-by-rating-level.zip";

    public static final String TOP_BY_FRIENDS_COUNT_XLSX = "xlsx/stat-top-by-friends-count.xlsx";
    public static final String TOP_BY_FRIENDS_COUNT_ZIP = "xlsx/stat-top-by-friends-count.zip";

    public static final String TOP_BY_ACHIEVEMENTS_COUNT_XLSX = "xlsx/stat-top-by-achievements-count.xlsx";
    public static final String TOP_BY_ACHIEVEMENTS_COUNT_ZIP = "xlsx/stat-top-by-achievements-count.zip";

    public static final String TOP_BY_VOCABULARIES_COUNT_XLSX = "xlsx/stat-top-by-vocabularies-count.xlsx";
    public static final String TOP_BY_VOCABULARIES_COUNT_ZIP = "xlsx/stat-top-by-vocabularies-count.zip";

    public static final String TOP_BY_CARS_COUNT_XLSX = "xlsx/stat-top-by-cars-count.xlsx";
    public static final String TOP_BY_CARS_COUNT_ZIP = "xlsx/stat-top-by-cars-count.zip";

    // css files and images
    public static final String STATS_CSS = "./css/stats.css";
    public static final String FAVICON_ICO = "./img/favicon/favicon.ico";
    public static final String EXCEL_PNG = "./img/excel.png";
    public static final String INFO_PNG = "./img/info.png";

    // js files
    // Chart.js
    // see https://www.chartjs.org/docs/latest/getting-started/installation.html
    public static final String CHART_JS = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.bundle.min.js";
    public static final String CHART_JS_INTEGRITY = "sha512-SuxO9djzjML6b9w9/I07IWnLnQhgyYVSpHZx0JV97kGBfTIsUYlWflyuW4ypnvhBrslz1yJ3R+S14fdCWmSmSA==";

    public static final String PLAYERS_BY_RANK_CHART_JS = "./js/players-by-rank-chart.js";
    public static final String PLAYERS_BY_RANK_DATA_JS = "./js/players-by-rank-data.js";
    public static final String TOP_TABLE_JS = "./js/stats-top-table.js";
    public static final String TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS = "./js/stat-top-by-best-speed-login-to-page.js";

    public static String getPath(final ExportContext context, final String relativePath) {
        return getPath(context.webRootDir, relativePath);
    }

    public static String getPath(final String rootDir, final String relativePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(rootDir);

        if (!relativePath.startsWith("/")) {
            sb.append("/");
        }

        // todo: maybe remove "." from relativePath if every required
        sb.append(relativePath);

        return sb.toString();
    }

    public static String getIndexPageFilePath(final String rootDir) {
        return rootDir + "/" + INDEX;
    }

    public static String getTopBySpeedPageFilePath(final String rootDir, final int pageNumber) {
        return rootDir + "stat-top-by-best-speed-page-" + pageNumber + ".html";
    }
    public static String getTopBySpeedAllPagesExcelFilePath(final String rootDir) {
        return rootDir + TOP_BY_BEST_SPEED_ALL_PAGES_XLSX;
    }
    public static String getTopBySpeedAllPagesExcelZipFilePath(final String rootDir) {
        return rootDir + TOP_BY_BEST_SPEED_ALL_PAGES_ZIP;
    }

    public static String getTopBySpeedLoginToPageFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS;
    }

    public static String getTopByTotalRacesCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_TOTAL_RACES;
    }
    public static String getTopByTotalRacesCountExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_TOTAL_RACES_XLSX;
    }
    public static String getTopByTotalRacesCountExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_TOTAL_RACES_ZIP;
    }

    public static String getTopByBestSpeedFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_BEST_SPEED;
    }
    public static String getTopByBestSpeedExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_BEST_SPEED_XLSX;
    }
    public static String getTopByBestSpeedExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_BEST_SPEED_ZIP;
    }

    public static String getTopByRatingLevelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_RATING_LEVEL;
    }
    public static String getTopByRatingLevelExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_RATING_LEVEL_XLSX;
    }
    public static String getTopByRatingLevelExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_RATING_LEVEL_ZIP;
    }

    public static String getTopByFriendsCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_FRIENDS_COUNT;
    }
    public static String getTopByFriendsCountExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_FRIENDS_COUNT_XLSX;
    }
    public static String getTopByFriendsCountExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_FRIENDS_COUNT_ZIP;
    }

    public static String getTopByAchievementsCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_ACHIEVEMENTS_COUNT;
    }
    public static String getTopByAchievementsCountExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_ACHIEVEMENTS_COUNT_XLSX;
    }
    public static String getTopByAchievementsCountExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_ACHIEVEMENTS_COUNT_ZIP;
    }

    public static String getTopByVocabulariesCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_VOCABULARIES_COUNT;
    }
    public static String getTopByVocabulariesCountExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_VOCABULARIES_COUNT_XLSX;
    }
    public static String getTopByVocabulariesCountExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_VOCABULARIES_COUNT_ZIP;
    }

    public static String getTopByCarsCountFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_CARS_COUNT;
    }
    public static String getTopByCarsCountExcelFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_CARS_COUNT_XLSX;
    }
    public static String getTopByCarsCountExcelZipFilePath(final String rootDir) {
        return rootDir + "/" + TOP_BY_CARS_COUNT_ZIP;
    }

    public static String getPlayerByRankFilePath(final String rootDir) {
        return rootDir + "/" + PLAYER_BY_RANK;
    }
    public static String getPlayerByRankDataFilePath(final String rootDir) {
        return rootDir + "/" + PLAYERS_BY_RANK_DATA_JS;
    }
}
