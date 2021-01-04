package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Log4j2
public class IndexPageTemplate extends FreemarkerTemplate {

    // todo: keys from config: minPlayerId, maxPlayerId, exportStartDate, exportEndDate
    // fields from ExportContext
    private static final String MIN_PLAYER_ID_KEY = "minPlayerId";
    private static final String MAX_PLAYER_ID_KEY = "maxPlayerId";
    private static final String DATA_DOWNLOAD_START_DATE_KEY = "dataDownloadStartDate";
    private static final String DATA_DOWNLOAD_END_DATE_KEY = "dataDownloadEndDate";

    // example player
    private static final String EXAMPLE_PLAYER_ID_KEY = "examplePlayerId";
    private static final String EXAMPLE_PLAYER_PROFILE_LINK_KEY = "examplePlayerProfileLink";
    private static final String EXAMPLE_PLAYER_GET_SUMMARY_URL_KEY = "examplePlayerGetSummaryUrl";
    private static final String EXAMPLE_PLAYER_GET_INDEX_DATA_URL_KEY = "examplePlayerGetIndexDataUrl";
    private static final String EXAMPLE_PLAYER_GET_STATS_OVERVIEW_URL_KEY = "examplePlayerGetStatsOverviewUrl";

    // global players metrics
    private static final String MIN_EXISTING_PLAYER_ID_KEY = "minExistingPlayerId";
    private static final String MIN_EXISTING_PLAYER_PROFILE_LINK_KEY = "minExistingPlayerProfileLink";
    private static final String MAX_EXISTING_PLAYER_ID_KEY = "maxExistingPlayerId";
    private static final String MAX_EXISTING_PLAYER_PROFILE_LINK_KEY = "maxExistingPlayerProfileLink";

    private static final String NON_EXISTING_PLAYER_COUNT_KEY = "nonExistingPlayersCount";
    private static final String BLOCKED_PLAYERS_COUNT_KEY = "blockedPlayersCount";
    private static final String PLAYERS_WITH_GET_INDEX_DATA_ERROR_KEY = "playersWithGetIndexDataError";
    private static final String PLAYERS_WITH_GET_INDEX_DATA_ERROR_GROUPED_BY_ERROR_KEY = "playersWithIndexDataErrorGroupedByError";
    private static final String ACTUAL_PLAYERS_WITHOUT_RACES_COUNT_KEY = "actualPlayersWithoutRacesCount";
    private static final String ACTUAL_PLAYERS_WITH_AT_LEAST_1_RACE_COUNT_KEY = "actualPlayersWithAtLeast1RaceCount";
    private static final String TOTAL_USERS_IN_DATABASE_KEY = "totalUsersInDatabase";

    // players by rank
    private static final String PLAYERS_BY_RANK_WITH_AT_LEAST_1_RACE_KEY = "playersByRankWithAtLeast1Race";
    private static final String PLAYERS_BY_RANK_WITH_AT_LEAST_10_RACES_KEY = "playersByRankWithAtLeast10Races";
    private static final String PLAYERS_BY_RANK_WITH_AT_LEAST_100_RACES_KEY = "playersByRankWithAtLeast100Races";
    private static final String PLAYERS_BY_RANK_WITH_AT_LEAST_1000_RACES_KEY = "playersByRankWithAtLeast1000Races";
    private static final String PLAYERS_BY_RANK_WITH_AT_LEAST_10000_RACES_KEY = "playersByRankWithAtLeast10000Races";

    // aggregates
    private static final String TOTAL_RACES_COUNT_BY_ALL_PLAYERS_KEY = "totalRacesCountByAllPlayers";
    private static final String TOTAL_CARS_COUNT_BY_ALL_PLAYERS_KEY = "totalCarsCountByAllPlayers";

    // top 1
    private static final String TOP_1_PLAYER_BY_TOTAL_RACES_COUNT_KEY = "top1PlayerByTotalRacesCount";
    private static final String TOP_1_PLAYER_BY_BEST_SPEED_KEY = "top1PlayerByBestSpeed";
    private static final String TOP_1_PLAYER_BY_RATING_LEVEL_KEY = "top1PlayerByRatingLevel";
    private static final String TOP_1_PLAYER_BY_ACHIEVEMENTS_COUNT_KEY = "top1PlayerByAchievementsCount";
    private static final String TOP_1_PLAYER_BY_FRIENDS_COUNT_KEY = "top1PlayerByFriendsCount";
    private static final String TOP_1_PLAYER_BY_VOCABULARIES_COUNT_KEY = "top1PlayerByVocabulariesCount";
    private static final String TOP_1_PLAYER_BY_CARS_COUNT_KEY = "top1PlayerByCarsCount";
    private static final String TOP_1_PLAYER_BY_RATING_EXPERIENCE_IN_ONE_MONTH_KEY = "top1PlayerByRatingExperienceInOneMonth";

    @Override
    public String getTemplatePath() {
        return "ftl/index-page.ftl";
    }


    public IndexPageTemplate minPlayerId(int minPlayerId) {
        templateData.put(MIN_PLAYER_ID_KEY, minPlayerId);
        return this;
    }

    public int getMinPlayerId() {
        return (int) templateData.get(MIN_PLAYER_ID_KEY);
    }

    public IndexPageTemplate maxPlayerId(int maxPlayerId) {
        templateData.put(MAX_PLAYER_ID_KEY, maxPlayerId);
        return this;
    }

    public int getMaxPlayerId() {
        return (int) templateData.get(MAX_PLAYER_ID_KEY);
    }

    public IndexPageTemplate dataDownloadStartDate(String dataDownloadStartDate) {
        templateData.put(DATA_DOWNLOAD_START_DATE_KEY, dataDownloadStartDate);
        return this;
    }

    public String getDataDownloadStartDate() {
        return (String) templateData.get(DATA_DOWNLOAD_START_DATE_KEY);
    }

    public IndexPageTemplate dataDownloadEndDate(String dataDownloadEndDate) {
        templateData.put(DATA_DOWNLOAD_END_DATE_KEY, dataDownloadEndDate);
        return this;
    }

    public String getDataDownloadEndDate() {
        return (String) templateData.get(DATA_DOWNLOAD_END_DATE_KEY);
    }

    public IndexPageTemplate examplePlayerId(int examplePlayerId) {
        templateData.put(EXAMPLE_PLAYER_ID_KEY, examplePlayerId);
        return this;
    }

    public int getExamplePlayerId() {
        return (int) templateData.get(EXAMPLE_PLAYER_ID_KEY);
    }

    public IndexPageTemplate examplePlayerProfileLink(String examplePlayerProfileLink) {
        templateData.put(EXAMPLE_PLAYER_PROFILE_LINK_KEY, examplePlayerProfileLink);
        return this;
    }

    public String getExamplePlayerProfileLink() {
        return (String) templateData.get(EXAMPLE_PLAYER_PROFILE_LINK_KEY);
    }

    public IndexPageTemplate examplePlayerGetSummaryUrl(String examplePlayerGetSummaryUrl) {
        templateData.put(EXAMPLE_PLAYER_GET_SUMMARY_URL_KEY, examplePlayerGetSummaryUrl);
        return this;
    }

    public String getExamplePlayerGetSummaryUrl() {
        return (String) templateData.get(EXAMPLE_PLAYER_GET_SUMMARY_URL_KEY);
    }

    public IndexPageTemplate examplePlayerGetIndexDataUrl(String examplePlayerGetIndexDataUrl) {
        templateData.put(EXAMPLE_PLAYER_GET_INDEX_DATA_URL_KEY, examplePlayerGetIndexDataUrl);
        return this;
    }

    public String getExamplePlayerGetIndexDataUrl() {
        return (String) templateData.get(EXAMPLE_PLAYER_GET_INDEX_DATA_URL_KEY);
    }

    public IndexPageTemplate examplePlayerGetStatsOverviewUrl(String examplePlayerGetStatsOverviewUrl) {
        templateData.put(EXAMPLE_PLAYER_GET_STATS_OVERVIEW_URL_KEY, examplePlayerGetStatsOverviewUrl);
        return this;
    }

    public String getExamplePlayerGetStatsOverviewUrl() {
        return (String) templateData.get(EXAMPLE_PLAYER_GET_STATS_OVERVIEW_URL_KEY);
    }

    public IndexPageTemplate minExistingPlayerId(int minExistingPlayerId) {
        templateData.put(MIN_EXISTING_PLAYER_ID_KEY, minExistingPlayerId);
        return this;
    }

    public int getMinExistingPlayerId() {
        return (int) templateData.get(MIN_EXISTING_PLAYER_ID_KEY);
    }

    public IndexPageTemplate minExistingPlayerProfileLink(String minExistingPlayerProfileLink) {
        templateData.put(MIN_EXISTING_PLAYER_PROFILE_LINK_KEY, minExistingPlayerProfileLink);
        return this;
    }

    public String getMinExistingPlayerProfileLink() {
        return (String) templateData.get(MIN_EXISTING_PLAYER_PROFILE_LINK_KEY);
    }

    public IndexPageTemplate maxExistingPlayerId(int maxExistingPlayerId) {
        templateData.put(MAX_EXISTING_PLAYER_ID_KEY, maxExistingPlayerId);
        return this;
    }

    public int getMaxExistingPlayerId() {
        return (int) templateData.get(MAX_EXISTING_PLAYER_ID_KEY);
    }

    public IndexPageTemplate maxExistingPlayerProfileLink(String maxExistingPlayerProfileLink) {
        templateData.put(MAX_EXISTING_PLAYER_PROFILE_LINK_KEY, maxExistingPlayerProfileLink);
        return this;
    }

    public String getMaxExistingPlayerProfileLink() {
        return (String) templateData.get(MAX_EXISTING_PLAYER_PROFILE_LINK_KEY);
    }

    public IndexPageTemplate nonExistingPlayersCount(int nonExistingPlayersCount) {
        templateData.put(NON_EXISTING_PLAYER_COUNT_KEY, nonExistingPlayersCount);
        return this;
    }

    public int getNonExistingPlayersCount() {
        return (int) templateData.get(NON_EXISTING_PLAYER_COUNT_KEY);
    }

    public IndexPageTemplate blockedPlayersCount(int blockedPlayersCount) {
        templateData.put(BLOCKED_PLAYERS_COUNT_KEY, blockedPlayersCount);
        return this;
    }

    public int getBlockedPlayersCount() {
        return (int) templateData.get(BLOCKED_PLAYERS_COUNT_KEY);
    }

    public IndexPageTemplate playersWithGetIndexDataError(List<PlayerEntity> playersWithGetIndexDataError) {
        templateData.put(PLAYERS_WITH_GET_INDEX_DATA_ERROR_KEY, playersWithGetIndexDataError);
        return this;
    }

    public List<PlayerEntity> getPlayersWithGetIndexDataError() {
        return (List<PlayerEntity>) templateData.get(BLOCKED_PLAYERS_COUNT_KEY);
    }

    public IndexPageTemplate playersWithIndexDataErrorGroupedByError(Map<String, List<PlayerEntity>> playersWithIndexDataErrorGroupedByError) {
        templateData.put(PLAYERS_WITH_GET_INDEX_DATA_ERROR_GROUPED_BY_ERROR_KEY, playersWithIndexDataErrorGroupedByError);
        return this;
    }

    public Map<String, List<PlayerEntity>> getPlayersWithIndexDataErrorGroupedByError() {
        return (Map<String, List<PlayerEntity>>) templateData.get(PLAYERS_WITH_GET_INDEX_DATA_ERROR_GROUPED_BY_ERROR_KEY);
    }

    public IndexPageTemplate actualPlayersWithoutRacesCount(int actualPlayersWithoutRacesCount) {
        templateData.put(ACTUAL_PLAYERS_WITHOUT_RACES_COUNT_KEY, actualPlayersWithoutRacesCount);
        return this;
    }

    public int getActualPlayersWithoutRacesCount() {
        return (int) templateData.get(ACTUAL_PLAYERS_WITHOUT_RACES_COUNT_KEY);
    }

    public IndexPageTemplate actualPlayersWithAtLeast1RaceCount(int actualPlayersWithAtLeast1RaceCount) {
        templateData.put(ACTUAL_PLAYERS_WITH_AT_LEAST_1_RACE_COUNT_KEY, actualPlayersWithAtLeast1RaceCount);
        return this;
    }

    public int getActualPlayersWithAtLeast1RaceCount() {
        return (int) templateData.get(ACTUAL_PLAYERS_WITH_AT_LEAST_1_RACE_COUNT_KEY);
    }

    public IndexPageTemplate totalUsersInDatabase(long totalUsersInDatabase) {
        templateData.put(TOTAL_USERS_IN_DATABASE_KEY, totalUsersInDatabase);
        return this;
    }

    public long getTotalUsersInDatabase() {
        return (long) templateData.get(TOTAL_USERS_IN_DATABASE_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast1Race(String playersByRankWithAtLeast1Race) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_1_RACE_KEY, playersByRankWithAtLeast1Race);
        return this;
    }

    public String getPlayersByRankWithAtLeast1Race() {
        return (String) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_1_RACE_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast10Races(String playersByRankWithAtLeast10Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_10_RACES_KEY, playersByRankWithAtLeast10Races);
        return this;
    }

    public String getPlayersByRankWithAtLeast10Races() {
        return (String) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_10_RACES_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast100Races(String playersByRankWithAtLeast100Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_100_RACES_KEY, playersByRankWithAtLeast100Races);
        return this;
    }

    public String getPlayersByRankWithAtLeast100Races() {
        return (String) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_100_RACES_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast1000Races(String playersByRankWithAtLeast1000Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_1000_RACES_KEY, playersByRankWithAtLeast1000Races);
        return this;
    }

    public String getPlayersByRankWithAtLeast1000Races() {
        return (String) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_1000_RACES_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast10000Races(String playersByRankWithAtLeast10000Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_10000_RACES_KEY, playersByRankWithAtLeast10000Races);
        return this;
    }

    public String getPlayersByRankWithAtLeast10000Races() {
        return (String) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_10000_RACES_KEY);
    }

    public IndexPageTemplate totalRacesCountByAllPlayers(long totalRacesCountByAllPlayers) {
        templateData.put(TOTAL_RACES_COUNT_BY_ALL_PLAYERS_KEY, totalRacesCountByAllPlayers);
        return this;
    }

    public long getTotalRacesCountByAllPlayers() {
        return (long) templateData.get(TOTAL_RACES_COUNT_BY_ALL_PLAYERS_KEY);
    }

    public IndexPageTemplate totalCarsCountByAllPlayers(long totalCarsCountByAllPlayers) {
        templateData.put(TOTAL_CARS_COUNT_BY_ALL_PLAYERS_KEY, totalCarsCountByAllPlayers);
        return this;
    }

    public long getTotalCarsCountByAllPlayers() {
        return (long) templateData.get(TOTAL_CARS_COUNT_BY_ALL_PLAYERS_KEY);
    }

    public IndexPageTemplate top1PlayerByTotalRacesCount(PlayerEntity top1PlayerByTotalRacesCount) {
        templateData.put(TOP_1_PLAYER_BY_TOTAL_RACES_COUNT_KEY, top1PlayerByTotalRacesCount);
        return this;
    }

    public PlayerEntity getTop1PlayerByTotalRacesCount() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_TOTAL_RACES_COUNT_KEY);
    }

    public IndexPageTemplate top1PlayerByBestSpeed(PlayerEntity top1PlayerByBestSpeed) {
        templateData.put(TOP_1_PLAYER_BY_BEST_SPEED_KEY, top1PlayerByBestSpeed);
        return this;
    }

    public PlayerEntity getTop1PlayerByBestSpeed() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_BEST_SPEED_KEY);
    }

    public IndexPageTemplate top1PlayerByRatingLevel(PlayerEntity top1PlayerByRatingLevel) {
        templateData.put(TOP_1_PLAYER_BY_RATING_LEVEL_KEY, top1PlayerByRatingLevel);
        return this;
    }

    public PlayerEntity getTop1PlayerByRatingLevel() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_RATING_LEVEL_KEY);
    }

    public IndexPageTemplate top1PlayerByAchievementsCount(PlayerEntity top1PlayerByAchievementsCount) {
        templateData.put(TOP_1_PLAYER_BY_ACHIEVEMENTS_COUNT_KEY, top1PlayerByAchievementsCount);
        return this;
    }

    public PlayerEntity getTop1PlayerByAchievementsCount() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_ACHIEVEMENTS_COUNT_KEY);
    }

    public IndexPageTemplate top1PlayerByFriendsCount(PlayerEntity top1PlayerByFriendsCount) {
        templateData.put(TOP_1_PLAYER_BY_FRIENDS_COUNT_KEY, top1PlayerByFriendsCount);
        return this;
    }

    public PlayerEntity getTop1PlayerByFriendsCount() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_FRIENDS_COUNT_KEY);
    }

    public IndexPageTemplate top1PlayerByVocabulariesCount(PlayerEntity top1PlayerByVocabulariesCount) {
        templateData.put(TOP_1_PLAYER_BY_VOCABULARIES_COUNT_KEY, top1PlayerByVocabulariesCount);
        return this;
    }

    public PlayerEntity getTop1PlayerByVocabulariesCount() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_VOCABULARIES_COUNT_KEY);
    }

    public IndexPageTemplate top1PlayerByCarsCount(PlayerEntity top1PlayerByCarsCount) {
        templateData.put(TOP_1_PLAYER_BY_CARS_COUNT_KEY, top1PlayerByCarsCount);
        return this;
    }

    public PlayerEntity getTop1PlayerByCarsCount() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_CARS_COUNT_KEY);
    }

    public IndexPageTemplate top1PlayerByRatingExperienceInOneMonth(PlayerEntity top1PlayerByRatingExperienceInOneMonth) {
        templateData.put(TOP_1_PLAYER_BY_RATING_EXPERIENCE_IN_ONE_MONTH_KEY, top1PlayerByRatingExperienceInOneMonth);
        return this;
    }

    public PlayerEntity getTop1PlayerByRatingExperienceInOneMonth() {
        return (PlayerEntity) templateData.get(TOP_1_PLAYER_BY_RATING_EXPERIENCE_IN_ONE_MONTH_KEY);
    }

    @Override
    public void export(final String filePath) {
        super.export(filePath);

        logger.debug("Exported index page to file {}", filePath);
    }
}
