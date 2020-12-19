package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.jsonParser.dto.PlayersByRankCount;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;

import java.util.List;

@Log4j2
@Component
public class IndexPageExporter {

    @Autowired
    private PlayerRepository playerRepository;

    public void export() {
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

        Integer actualPlayersWithoutRacesCount = playerRepository.countByGetSummaryErrorIsNullAndGetIndexDataErrorIsNullAndBlockedEqualsAndTotalRacesCountEquals(0, 0);
        logger.debug("Actual players with no total races: {}", actualPlayersWithoutRacesCount);

        int minTotalRacesCount = 1;
        Integer actualPlayersWithAtLeast1RaceCount = playerRepository.countByGetSummaryErrorIsNullAndGetIndexDataErrorIsNullAndBlockedEqualsAndTotalRacesCountIsGreaterThanEqual(0, minTotalRacesCount);
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
    }
}
