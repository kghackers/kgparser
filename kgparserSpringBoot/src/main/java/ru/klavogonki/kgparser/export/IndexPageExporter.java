package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

        // todo: group players by rank (see stats-data.js)
        // todo: make it parameterizable by totalRacesCount and execute for: >= 1, >= 10 >=100 >=1000 >=10000

        // todo: total races (aggregate)
        // todo: total cars (aggregate)

        // todo: top 1 by each count metric
        // todo: make the query parameterizable (for top-500 links)
    }
}
