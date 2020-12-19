package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.jsonParser.dto.PlayersByRankCount;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;

import java.util.List;

@SuppressWarnings("unchecked")
@Log4j2
public class IndexPageTemplate extends FreemarkerTemplate {

    // todo: keys from config: minPlayerId, maxPlayerId, exportStartDate, exportEndDate

    // global players metrics
    private static final String MIN_EXISTING_PLAYER_ID_KEY = "minExistingPlayerId";
    private static final String MAX_EXISTING_PLAYER_ID_KEY = "maxExistingPlayerId";

    private static final String NON_EXISTING_PLAYER_COUNT_KEY = "nonExistingPlayersCount";
    private static final String BLOCKED_PLAYERS_COUNT_KEY = "blockedPlayersCount";
    private static final String PLAYERS_WITH_GET_INDEX_DATA_ERROR_KEY = "playersWithGetIndexDataError";
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

    @Override
    public String getTemplatePath() {
        return "ftl/index-page.ftl";
    }

    public IndexPageTemplate minExistingPlayerId(int minExistingPlayerId) {
        templateData.put(MIN_EXISTING_PLAYER_ID_KEY, minExistingPlayerId);
        return this;
    }

    public int getMinExistingPlayerId() {
        return (int) templateData.get(MIN_EXISTING_PLAYER_ID_KEY);
    }

    public IndexPageTemplate maxExistingPlayerId(int maxExistingPlayerId) {
        templateData.put(MAX_EXISTING_PLAYER_ID_KEY, maxExistingPlayerId);
        return this;
    }

    public int getMaxExistingPlayerId() {
        return (int) templateData.get(MAX_EXISTING_PLAYER_ID_KEY);
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

    public IndexPageTemplate playersByRankWithAtLeast1Race(List<PlayersByRankCount> playersByRankWithAtLeast1Race) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_1_RACE_KEY, playersByRankWithAtLeast1Race);
        return this;
    }

    public List<PlayersByRankCount> getPlayersByRankWithAtLeast1Race() {
        return (List<PlayersByRankCount>) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_1_RACE_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast10Races(List<PlayersByRankCount> playersByRankWithAtLeast10Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_10_RACES_KEY, playersByRankWithAtLeast10Races);
        return this;
    }

    public List<PlayersByRankCount> getPlayersByRankWithAtLeast10Races() {
        return (List<PlayersByRankCount>) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_10_RACES_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast100Races(List<PlayersByRankCount> playersByRankWithAtLeast100Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_100_RACES_KEY, playersByRankWithAtLeast100Races);
        return this;
    }

    public List<PlayersByRankCount> getPlayersByRankWithAtLeast100Races() {
        return (List<PlayersByRankCount>) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_100_RACES_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast1000Races(List<PlayersByRankCount> playersByRankWithAtLeast1000Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_1000_RACES_KEY, playersByRankWithAtLeast1000Races);
        return this;
    }

    public List<PlayersByRankCount> getPlayersByRankWithAtLeast1000Races() {
        return (List<PlayersByRankCount>) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_1000_RACES_KEY);
    }

    public IndexPageTemplate playersByRankWithAtLeast10000Races(List<PlayersByRankCount> playersByRankWithAtLeast10000Races) {
        templateData.put(PLAYERS_BY_RANK_WITH_AT_LEAST_10000_RACES_KEY, playersByRankWithAtLeast10000Races);
        return this;
    }

    public List<PlayersByRankCount> getPlayersByRankWithAtLeast10000Races() {
        return (List<PlayersByRankCount>) templateData.get(PLAYERS_BY_RANK_WITH_AT_LEAST_10000_RACES_KEY);
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

    @Override
    public void export(final String filePath) {
        super.export(filePath);

        logger.debug("Exported index page to file {}", filePath);
    }
}
