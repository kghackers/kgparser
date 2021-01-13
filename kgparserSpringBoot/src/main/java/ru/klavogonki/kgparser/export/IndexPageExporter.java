package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.freemarker.IndexPageTemplate;
import ru.klavogonki.kgparser.freemarker.PageUrls;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.jsonParser.dto.PlayersByRankCount;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;
import ru.klavogonki.kgparser.util.DateUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
public class IndexPageExporter implements DataExporter {

    private static final int EXAMPLE_PLAYER_ID = 242585; // nosferatum :)

    private static final int TOP_1_BY_RATING_EXPERIENCE_IN_ONE_MONTH_PLAYER_ID = 379843; // iforrest, see https://klavogonki.ru/forum/software/59/page6/#post116

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public void export(ExportContext context) {
        Integer minExistingPlayerId = playerRepository.selectMinExistingPlayerId();
        logger.debug("Min existing player id: {}", minExistingPlayerId);

        Integer maxExistingPlayerId = playerRepository.selectMaxExistingPlayerId();
        logger.debug("Max existing player id: {}", maxExistingPlayerId);

        Integer nonExistingPlayersCount = playerRepository.countByGetSummaryErrorIsNotNull();
        logger.debug("Non-existing players count: {}", nonExistingPlayersCount);

        // todo: blocked = 0 to constant?
        Integer blockedPlayersCount = playerRepository.countByBlockedIsGreaterThan(0);
        logger.debug("Blocked players count: {}", blockedPlayersCount);

        List<PlayerEntity> playersWithGetIndexDataError = playerRepository.findByGetSummaryErrorIsNullAndGetIndexDataErrorIsNotNull();
        logger.debug("Players with error on /get-index-data count: {}", playersWithGetIndexDataError.size());
        logger.debug("Players with error on /get-index-data: {}", playersWithGetIndexDataError);

        Map<String, List<PlayerEntity>> playersWithIndexDataErrorGroupedByError = playersWithGetIndexDataError
            .stream()
            .collect(Collectors.groupingBy(PlayerEntity::getGetIndexDataError));

        logger.debug("Players with error on /get-index-data grouped by error: {}", playersWithIndexDataErrorGroupedByError);

        Integer actualPlayersWithoutRacesCount = playerRepository.countByGetSummaryErrorIsNullAndGetIndexDataErrorIsNullAndBlockedEqualsAndTotalRacesCountEquals(PlayerEntity.NOT_BLOCKED, 0);
        logger.debug("Actual players with no total races: {}", actualPlayersWithoutRacesCount);

        int minTotalRacesCount = 1;
        Integer actualPlayersWithAtLeast1RaceCount = playerRepository.countByGetSummaryErrorIsNullAndGetIndexDataErrorIsNullAndBlockedEqualsAndTotalRacesCountIsGreaterThanEqual(PlayerEntity.NOT_BLOCKED, minTotalRacesCount);
        logger.debug("Actual players with at least {} total races count: {}", minTotalRacesCount, actualPlayersWithAtLeast1RaceCount);

        long totalUsersInDatabase = playerRepository.count();
        logger.debug("Total users in the database: {}", totalUsersInDatabase);

        // group players by rank (see stats-data.js)
        // player with total races: >= 1, >= 10 >=100 >=1000 >=10000
        List<PlayersByRankCount> playersByRankWithAtLeast1Race = playerRepository.getActualPlayerCountByRank(1);
        logger.debug("Players by rank with at least 1 race: {}", playersByRankWithAtLeast1Race);

        List<PlayersByRankCount> playersByRankWithAtLeast10Races = playerRepository.getActualPlayerCountByRank(10);
        logger.debug("Players by rank with at least 10 races: {}", playersByRankWithAtLeast10Races);

        List<PlayersByRankCount> playersByRankWithAtLeast100Races = playerRepository.getActualPlayerCountByRank(100);
        logger.debug("Players by rank with at least 100 races: {}", playersByRankWithAtLeast100Races);

        List<PlayersByRankCount> playersByRankWithAtLeast1000Races = playerRepository.getActualPlayerCountByRank(1000);
        logger.debug("Players by rank with at least 1000 races: {}", playersByRankWithAtLeast1000Races);

        List<PlayersByRankCount> playersByRankWithAtLeast10000Races = playerRepository.getActualPlayerCountByRank(10000);
        logger.debug("Players by rank with at least 10000 races: {}", playersByRankWithAtLeast10000Races);

        // total races (aggregate)
        Long totalRacesCountByAllPlayers = playerRepository.selectSumOfTotalRacesCountByAllPlayers();
        logger.debug("Total races by all players: {}", totalRacesCountByAllPlayers);

        // total cars (aggregate)
        Long totalCarsCountByAllPlayers = playerRepository.selectSumOfCarsCountByAllPlayers();
        logger.debug("Total cars by all players: {}", totalCarsCountByAllPlayers);

        // top 1 by each count metric
        // todo: hardcoded top1 and top500 are ok for now,
        // todo: or implement with setMaxResults or something like this
        PlayerEntity top1PlayerByTotalRacesCount = playerRepository.findTopByOrderByTotalRacesCountDesc();
        logger.debug("Top 1 player by total races count: {}", top1PlayerByTotalRacesCount);

        PlayerEntity top1PlayerByBestSpeed = playerRepository.findTopByOrderByBestSpeedDesc();
        logger.debug("Top 1 player by best speed: {}", top1PlayerByBestSpeed);

        PlayerEntity top1PlayerByRatingLevel = playerRepository.findTopByOrderByRatingLevelDesc();
        logger.debug("Top 1 player by rating level: {}", top1PlayerByRatingLevel);

        PlayerEntity top1PlayerByAchievementsCount = playerRepository.findTopByOrderByAchievementsCountDesc();
        logger.debug("Top 1 player by achievements count: {}", top1PlayerByAchievementsCount);

        PlayerEntity top1PlayerByFriendsCount = playerRepository.findTopByOrderByFriendsCountDesc();
        logger.debug("Top 1 player by friends count: {}", top1PlayerByFriendsCount);

        PlayerEntity top1PlayerByVocabulariesCount = playerRepository.findTopByOrderByVocabulariesCountDesc();
        logger.debug("Top 1 player by vocabularies count: {}", top1PlayerByVocabulariesCount);

        PlayerEntity top1PlayerByCarsCount = playerRepository.findTopByOrderByCarsCountDesc();
        logger.debug("Top 1 player by cars count: {}", top1PlayerByCarsCount);

        PlayerEntity top1PlayerByRatingExperienceInOneMonth = playerRepository.findByPlayerId(TOP_1_BY_RATING_EXPERIENCE_IN_ONE_MONTH_PLAYER_ID)
            .orElseThrow(() -> new IllegalStateException(String.format("Player with playerId = %d is not found.", TOP_1_BY_RATING_EXPERIENCE_IN_ONE_MONTH_PLAYER_ID)));

        logger.debug("Top 1 player by rating experience in one month: {}", top1PlayerByRatingExperienceInOneMonth);

        String indexPageFilePath = PageUrls.getIndexPageFilePath(context.webRootDir);

        new IndexPageTemplate()
            .minPlayerId(context.minPlayerId)
            .maxPlayerId(context.maxPlayerId)
            .dataDownloadStartDate(DateUtils.formatDateTimeForUi(context.dataDownloadStartDate))
            .dataDownloadEndDate(DateUtils.formatDateTimeForUi(context.dataDownloadEndDate))

            // example player
            .examplePlayerId(EXAMPLE_PLAYER_ID)
            .examplePlayerProfileLink(UrlConstructor.userProfileLink(EXAMPLE_PLAYER_ID))
            .examplePlayerGetSummaryUrl(UrlConstructor.getSummary(EXAMPLE_PLAYER_ID))
            .examplePlayerGetIndexDataUrl(UrlConstructor.getIndexData(EXAMPLE_PLAYER_ID))
            .examplePlayerGetStatsOverviewUrl(UrlConstructor.getStatsOverview(EXAMPLE_PLAYER_ID))

            // global players metrics
            .minExistingPlayerId(minExistingPlayerId)
            .minExistingPlayerProfileLink(UrlConstructor.userProfileLink(minExistingPlayerId))
            .maxExistingPlayerId(maxExistingPlayerId)
            .maxExistingPlayerProfileLink(UrlConstructor.userProfileLink(maxExistingPlayerId))
            .nonExistingPlayersCount(nonExistingPlayersCount)
            .blockedPlayersCount(blockedPlayersCount)
            .playersWithGetIndexDataError(playersWithGetIndexDataError)
            .playersWithIndexDataErrorGroupedByError(playersWithIndexDataErrorGroupedByError)
            .actualPlayersWithoutRacesCount(actualPlayersWithoutRacesCount)
            .actualPlayersWithAtLeast1RaceCount(actualPlayersWithAtLeast1RaceCount)
            .totalUsersInDatabase(totalUsersInDatabase)

            // players by rank
            .playersByRankWithAtLeast1Race(JacksonUtils.serializeToString(playersByRankWithAtLeast1Race))
            .playersByRankWithAtLeast10Races(JacksonUtils.serializeToString(playersByRankWithAtLeast10Races))
            .playersByRankWithAtLeast100Races(JacksonUtils.serializeToString(playersByRankWithAtLeast100Races))
            .playersByRankWithAtLeast1000Races(JacksonUtils.serializeToString(playersByRankWithAtLeast1000Races))
            .playersByRankWithAtLeast10000Races(JacksonUtils.serializeToString(playersByRankWithAtLeast10000Races))

            // aggregates
            .totalRacesCountByAllPlayers(totalRacesCountByAllPlayers)
            .totalCarsCountByAllPlayers(totalCarsCountByAllPlayers)

            // top 1
            .top1PlayerByTotalRacesCount(top1PlayerByTotalRacesCount)
            .top1PlayerByBestSpeed(top1PlayerByBestSpeed)
            .top1PlayerByRatingLevel(top1PlayerByRatingLevel)
            .top1PlayerByAchievementsCount(top1PlayerByAchievementsCount)
            .top1PlayerByFriendsCount(top1PlayerByFriendsCount)
            .top1PlayerByVocabulariesCount(top1PlayerByVocabulariesCount)
            .top1PlayerByCarsCount(top1PlayerByCarsCount)
            .top1PlayerByRatingExperienceInOneMonth(top1PlayerByRatingExperienceInOneMonth)

            .export(indexPageFilePath);
    }
}
