package ru.klavogonki.statistics.freemarker

import ru.klavogonki.statistics.export.ExportContext
import ru.klavogonki.statistics.export.ExporterUtils

/**
 * Aggregates names/paths of all pages, so that we can pass them to FreeMarker templates
 * and change links in one place.
 */
@SuppressWarnings("TooManyFunctions")
object PageUrls {
    // html files
    const val INDEX: String = "index.html"

    @JvmField // calculated field cannot be const
    val TOP_BY_BEST_SPEED_PAGE_1 = removeLeadingSlash(
        getTopBySpeedPageFilePath("", ExporterUtils.FIRST_PAGE_NUMBER)
    )

    const val TOP_BY_BEST_SPEED = "stat-top-by-best-speed.html"

    const val TOP_BY_TOTAL_RACES = "stat-top-by-total-races.html"
    const val TOP_BY_RATING_LEVEL = "stat-top-by-rating-level.html"
    const val TOP_BY_FRIENDS_COUNT = "stat-top-by-friends-count.html"
    const val TOP_BY_ACHIEVEMENTS_COUNT = "stat-top-by-achievements-count.html"
    const val TOP_BY_VOCABULARIES_COUNT = "stat-top-by-vocabularies-count.html"
    const val TOP_BY_CARS_COUNT = "stat-top-by-cars-count.html"

    const val PLAYER_BY_RANK = "stat-players-by-rank.html"

    // xls files and their zip form
    // big top by speed, all pages
    const val TOP_BY_BEST_SPEED_ALL_PAGES_XLSX = "xlsx/stat-top-by-best-speed-all-pages.xlsx"
    const val TOP_BY_BEST_SPEED_ALL_PAGES_ZIP = "xlsx/stat-top-by-best-speed-all-pages.zip"

    const val TOP_BY_BEST_SPEED_XLSX = "xlsx/stat-top-by-best-speed.xlsx"
    const val TOP_BY_BEST_SPEED_ZIP = "xlsx/stat-top-by-best-speed.zip"

    const val TOP_BY_TOTAL_RACES_XLSX = "xlsx/stat-top-by-total-races.xlsx"
    const val TOP_BY_TOTAL_RACES_ZIP = "xlsx/stat-top-by-total-races.zip"

    const val TOP_BY_RATING_LEVEL_XLSX = "xlsx/stat-top-by-rating-level.xlsx"
    const val TOP_BY_RATING_LEVEL_ZIP = "xlsx/stat-top-by-rating-level.zip"

    const val TOP_BY_FRIENDS_COUNT_XLSX = "xlsx/stat-top-by-friends-count.xlsx"
    const val TOP_BY_FRIENDS_COUNT_ZIP = "xlsx/stat-top-by-friends-count.zip"

    const val TOP_BY_ACHIEVEMENTS_COUNT_XLSX = "xlsx/stat-top-by-achievements-count.xlsx"
    const val TOP_BY_ACHIEVEMENTS_COUNT_ZIP = "xlsx/stat-top-by-achievements-count.zip"

    const val TOP_BY_VOCABULARIES_COUNT_XLSX = "xlsx/stat-top-by-vocabularies-count.xlsx"
    const val TOP_BY_VOCABULARIES_COUNT_ZIP = "xlsx/stat-top-by-vocabularies-count.zip"

    const val TOP_BY_CARS_COUNT_XLSX = "xlsx/stat-top-by-cars-count.xlsx"
    const val TOP_BY_CARS_COUNT_ZIP = "xlsx/stat-top-by-cars-count.zip"

    // css files and images
    const val STATS_CSS = "./css/stats.css"
    const val FAVICON_ICO = "./img/favicon/favicon.ico"
    const val EXCEL_PNG = "./img/excel.png"
    const val INFO_PNG = "./img/info.png"

    // js files
    // Chart.js
    // see https://www.chartjs.org/docs/latest/getting-started/installation.html
    const val CHART_JS = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.bundle.min.js"
    const val CHART_JS_INTEGRITY =
        "sha512-SuxO9djzjML6b9w9/I07IWnLnQhgyYVSpHZx0JV97kGBfTIsUYlWflyuW4ypnvhBrslz1yJ3R+S14fdCWmSmSA=="

    const val PLAYERS_BY_RANK_CHART_JS = "./js/players-by-rank-chart.js"
    const val PLAYERS_BY_RANK_DATA_JS = "./js/players-by-rank-data.js"
    const val TOP_TABLE_JS = "./js/stats-top-table.js"
    const val TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS = "./js/stat-top-by-best-speed-login-to-page.js"

    @JvmStatic
    fun getPath(context: ExportContext, relativePath: String) =
        getPath(context.webRootDir, relativePath)

    private fun getPath(rootDir: String, relativePath: String): String {
        val sb = StringBuilder()
        sb.append(rootDir)

        if (!relativePath.startsWith("/")) {
            sb.append("/")
        }

        // todo: maybe remove "." from relativePath if every required
        sb.append(relativePath)

        return sb.toString()
    }

    private fun removeLeadingSlash(path: String): String {
        if (!path.startsWith("/")) {
            return path
        }

        return path.substring("/".length)
    }

    @JvmStatic
    fun getIndexPageFilePath(rootDir: String) =
        fullPath(rootDir, INDEX)

    @JvmStatic
    fun getTopBySpeedPageFilePath(rootDir: String, pageNumber: Int): String {
        val pageFileName = "stat-top-by-best-speed-page-$pageNumber.html"
        return fullPath(rootDir, pageFileName)
    }

    @JvmStatic
    fun getTopBySpeedAllPagesExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_BEST_SPEED_ALL_PAGES_XLSX)

    @JvmStatic
    fun getTopBySpeedAllPagesExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_BEST_SPEED_ALL_PAGES_ZIP)

    @JvmStatic
    fun getTopBySpeedLoginToPageFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_BEST_SPEED_LOGIN_TO_PAGE_JS)

    @JvmStatic
    fun getTopByTotalRacesCountFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_TOTAL_RACES)

    @JvmStatic
    fun getTopByTotalRacesCountExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_TOTAL_RACES_XLSX)

    @JvmStatic
    fun getTopByTotalRacesCountExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_TOTAL_RACES_ZIP)

    @JvmStatic
    fun getTopByBestSpeedFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_BEST_SPEED)

    @JvmStatic
    fun getTopByBestSpeedExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_BEST_SPEED_XLSX)

    @JvmStatic
    fun getTopByBestSpeedExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_BEST_SPEED_ZIP)

    @JvmStatic
    fun getTopByRatingLevelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_RATING_LEVEL)

    @JvmStatic
    fun getTopByRatingLevelExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_RATING_LEVEL_XLSX)

    @JvmStatic
    fun getTopByRatingLevelExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_RATING_LEVEL_ZIP)

    @JvmStatic
    fun getTopByFriendsCountFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_FRIENDS_COUNT)

    @JvmStatic
    fun getTopByFriendsCountExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_FRIENDS_COUNT_XLSX)

    @JvmStatic
    fun getTopByFriendsCountExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_FRIENDS_COUNT_ZIP)

    @JvmStatic
    fun getTopByAchievementsCountFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_ACHIEVEMENTS_COUNT)

    @JvmStatic
    fun getTopByAchievementsCountExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_ACHIEVEMENTS_COUNT_XLSX)

    @JvmStatic
    fun getTopByAchievementsCountExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_ACHIEVEMENTS_COUNT_ZIP)

    @JvmStatic
    fun getTopByVocabulariesCountFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_VOCABULARIES_COUNT)

    @JvmStatic
    fun getTopByVocabulariesCountExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_VOCABULARIES_COUNT_XLSX)

    @JvmStatic
    fun getTopByVocabulariesCountExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_VOCABULARIES_COUNT_ZIP)

    @JvmStatic
    fun getTopByCarsCountFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_CARS_COUNT)

    @JvmStatic
    fun getTopByCarsCountExcelFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_CARS_COUNT_XLSX)

    @JvmStatic
    fun getTopByCarsCountExcelZipFilePath(rootDir: String) =
        fullPath(rootDir, TOP_BY_CARS_COUNT_ZIP)

    @JvmStatic
    fun getPlayerByRankFilePath(rootDir: String) =
        fullPath(rootDir, PLAYER_BY_RANK)

    @JvmStatic
    fun getPlayerByRankDataFilePath(rootDir: String) =
        fullPath(rootDir, PLAYERS_BY_RANK_DATA_JS)

    private fun fullPath(rootDir: String, relativePath: String) =
        "$rootDir/$relativePath"
}