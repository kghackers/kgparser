package ru.klavogonki.kgparser.jsonParser.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerRankLevelAndTotalRacesCount;
import ru.klavogonki.kgparser.jsonParser.dto.PlayersByRankCount;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;

import java.util.List;
import java.util.Optional;

/**
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.core-concepts">Spring JPA core concepts</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring JPA query creation</a>
 */
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByPlayerId(int playerId);

//    List<PlayerEntity> findByTotalRacesCountGreaterThanEqualAndBlockedEqualsOrderByBestSpeedDesc(int totalRacesCount, int blocked);
    List<PlayerEntity> findByTotalRacesCountGreaterThanEqualAndBlockedEqualsOrderByBestSpeedDescTotalRacesCountDesc(int totalRacesCount, int blocked);

    // JPA query, non-native
    @Query(value =
        "select" +
        " min(p.playerId)" +
        " from PlayerEntity p" +
        " where (p.login is not null)"
    )
    Integer selectMinExistingPlayerId();

    // JPA query, non-native
    @Query(value =
        "select" +
        " max(p.playerId)" +
        " from PlayerEntity p" +
        " where (p.login is not null)"
    )
    Integer selectMaxExistingPlayerId();

    // non-existing users count, users with errors from /get-summary
    Integer countByGetSummaryErrorIsNotNull();

    // blocked users count, blocked > 0
    Integer countByBlockedIsGreaterThan(int blocked);

    // players with successful /get-summary, but failed /get-index-data
    // todo: implement a separate count method if required
    List<PlayerEntity> findByGetSummaryErrorIsNullAndGetIndexDataErrorIsNotNull();

    // actual users with given (namely 0) total texts count
    Integer countByGetSummaryErrorIsNullAndGetIndexDataErrorIsNullAndBlockedEqualsAndTotalRacesCountEquals(int blocked, int totalRacesCount);

    // actual users with at least N total texts count
    Integer countByGetSummaryErrorIsNullAndGetIndexDataErrorIsNullAndBlockedEqualsAndTotalRacesCountIsGreaterThanEqual(int blocked, int totalRacesCount);

    @Query(value =
        "select" +
        " new ru.klavogonki.kgparser.jsonParser.dto.PlayerRankLevelAndTotalRacesCount(" + // full class name required else ClassLoadingException will be thrown
        "   p.rankLevel as rankLevel," +
        "   p.totalRacesCount" + // count returns long
        " )" +
        " from PlayerEntity p" +
        " where (p.getSummaryError is null)" +
        " and (p.getIndexDataError is null)" +
        " and (p.blocked = 0)" +
        " and (p.totalRacesCount >= :minTotalRacesCount)"
    )
    List<PlayerRankLevelAndTotalRacesCount> getActualPlayersRankLevelAndTotalRacesCount(@Param("minTotalRacesCount") int minTotalRacesCount);

    @Query(value =
        "select" +
        " new ru.klavogonki.kgparser.jsonParser.dto.PlayersByRankCount(" + // full class name required else ClassLoadingException will be thrown
        "   p.rankLevel as rankLevel," +
        "   count(p.playerId) as playersCount" + // count returns long
        " )" +
        " from PlayerEntity p" +
        " where (p.getSummaryError is null)" +
        " and (p.getIndexDataError is null)" +
        " and (p.blocked = 0)" +
        " and (p.totalRacesCount >= :minTotalRacesCount)" +
        " group by p.rankLevel" +
        " order by p.rankLevel"
    )
    List<PlayersByRankCount> getActualPlayerCountByRank(@Param("minTotalRacesCount") int minTotalRacesCount);

    // JPA query, non-native
    @Query(value =
        "select" +
        " sum(p.totalRacesCount)" +
        " from PlayerEntity p" // without any limits on players
    )
    Long selectSumOfTotalRacesCountByAllPlayers();

    // JPA query, non-native
    @Query(value =
        "select" +
        " sum(p.carsCount)" +
        " from PlayerEntity p" // without any limits on players
    )
    Long selectSumOfCarsCountByAllPlayers();

    // todo: for dynamic limit in top use setMaxResults of a page requests
    PlayerEntity findTopByOrderByTotalRacesCountDesc();
    List<PlayerEntity> findTop500ByOrderByTotalRacesCountDesc();

    PlayerEntity findTopByOrderByBestSpeedDesc();
    List<PlayerEntity> findTop500ByOrderByBestSpeedDesc();

    PlayerEntity findTopByOrderByRatingLevelDesc();
    List<PlayerEntity> findTop500ByOrderByRatingLevelDesc();

    PlayerEntity findTopByOrderByAchievementsCountDesc();
    List<PlayerEntity> findTop500ByOrderByAchievementsCountDesc();

    PlayerEntity findTopByOrderByFriendsCountDesc();
    List<PlayerEntity> findTop500ByOrderByFriendsCountDesc();

    PlayerEntity findTopByOrderByVocabulariesCountDesc();
    List<PlayerEntity> findTop500ByOrderByVocabulariesCountDesc();

    PlayerEntity findTopByOrderByCarsCountDesc();
    List<PlayerEntity> findTop500ByOrderByCarsCountDesc();
}
