package ru.klavogonki.kgparser.jsonParser.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.kgparser.Car;
import ru.klavogonki.kgparser.PlayerJsonData;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.jsonParser.ApiErrors;
import ru.klavogonki.kgparser.jsonParser.Assertions;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.util.DateUtils;
import ru.klavogonki.openapi.model.Bio;
import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetIndexDataStats;
import ru.klavogonki.openapi.model.Microtime;

import java.time.LocalDateTime;

// todo: merge it with JacksonUtilsTest (problem with module/resource dependencies), so that user data can be read from json files
class PlayerMapperTest {

    @Test
    @DisplayName("Test a user with maximum possible filled fields")
    void testUserWithMaximumData() {
        // summary
        PlayerSummary summary = new PlayerSummary();

        summary.level = Rank.getLevel(Rank.superman).intValue();
        summary.title = Rank.getDisplayName(Rank.superman);
        summary.blocked = 0;

        summary.user = new PlayerSummary.User();
        summary.user.id = 242585;
        summary.user.login = "nosferatum";

        summary.car = new PlayerSummary.Car();
        summary.car.car = Car.F1.id;
        summary.car.color = "#BF1300";

        // index-data
        Bio bio = new Bio()
            .userId(242585);

        Microtime registered = new Microtime()
            .sec(1297852113L)
            .usec(0L);

        GetIndexDataStats stats = new GetIndexDataStats()
            .registered(registered)
            .achievesCnt(225)
            .totalNumRaces(60633)
            .bestSpeed(626)
            .ratingLevel(32)
            .friendsCnt(102)
            .vocsCnt(109)
            .carsCnt(33);

        GetIndexDataResponse indexData = new GetIndexDataResponse()
            .ok(ApiErrors.OK_CORRECT_VALUE) // FGJ
            .bio(bio)
            .stats(stats);

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(summary.user.id)
            .hasLogin(summary.user.login)
            .hasRankLevel(summary.level)
            .hasTitle(summary.title)
            .hasBlocked(summary.blocked)
            .hasRegistered(DateUtils.convertUserRegisteredTime(indexData))
            .hasAchievementsCount(stats.getAchievesCnt())
            .hasTotalRacesCount(stats.getTotalNumRaces())
            .hasBestSpeed(stats.getBestSpeed())
            .hasRatingLevel(stats.getRatingLevel())
            .hasFriendsCount(stats.getFriendsCnt())
            .hasVocabulariesCount(stats.getVocsCnt())
            .hasCarsCount(stats.getCarsCnt())
        ;

        Assertions.assertThat(player.getCar())
            .hasId(summary.car.car)
            .hasColor(summary.car.color);
    }

    @Test
    @DisplayName("Test a new registered user - minimum possible data")
    void testJustRegisteredUser() {
        // summary
        PlayerSummary summary = new PlayerSummary();

        summary.level = Rank.getLevel(Rank.novice).intValue();
        summary.title = Rank.getDisplayName(Rank.novice);
        summary.blocked = 0;

        summary.user = new PlayerSummary.User();
        summary.user.id = 624511;
        summary.user.login = "nosferatum0";

        summary.car = new PlayerSummary.Car();
        summary.car.car = Car.ZAZ_965.id;
        summary.car.color = "#777777";

        // index-data
        Bio bio = new Bio()
            .userId(624511);

        Microtime registered = new Microtime()
            .sec(1607554944L)
            .usec(0L);

        GetIndexDataStats stats = new GetIndexDataStats()
            .registered(registered)
            .achievesCnt(0)
            .totalNumRaces(0)
            .bestSpeed(null)
            .ratingLevel(1)
            .friendsCnt(0)
            .vocsCnt(0)
            .carsCnt(1);

        GetIndexDataResponse indexData = new GetIndexDataResponse()
            .ok(ApiErrors.OK_CORRECT_VALUE) // FGJ
            .bio(bio)
            .stats(stats);

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(summary.user.id)
            .hasLogin(summary.user.login)
            .hasRankLevel(summary.level)
            .hasTitle(summary.title)
            .hasBlocked(summary.blocked)
            .hasRegistered(DateUtils.convertUserRegisteredTime(indexData))
            .hasAchievementsCount(stats.getAchievesCnt())
            .hasTotalRacesCount(stats.getTotalNumRaces())
            .hasBestSpeed(null)
            .hasRatingLevel(stats.getRatingLevel())
            .hasFriendsCount(stats.getFriendsCnt())
            .hasVocabulariesCount(stats.getVocsCnt())
            .hasCarsCount(stats.getCarsCnt())
        ;

        Assertions.assertThat(player.getCar())
            .hasId(summary.car.car)
            .hasColor(summary.car.color);
    }

    @Test
    @DisplayName("Test a user for which /get-summary works but /get-index-data returns an error")
    void testUserWithoutIndexData() {
        PlayerSummary summary = new PlayerSummary();

        summary.level = Rank.getLevel(Rank.superman).intValue();
        summary.title = Rank.KLAVO_MECHANIC_TITLE;
        summary.blocked = 0;

        summary.user = new PlayerSummary.User();
        summary.user.id = 21;
        summary.user.login = "Artch";

        summary.car = new PlayerSummary.Car();
        summary.car.car = Car.AUDI_TT.id;
        summary.car.color = "#893425";

        GetIndexDataResponse indexData = new GetIndexDataResponse()
            .err(ApiErrors.HIDDEN_PROFILE_USER_ERROR)
            .ok(null); // FGJ

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(summary.user.id)
            .hasLogin(summary.user.login)
            .hasRankLevel(summary.level)
            .hasTitle(summary.title)
            .hasBlocked(summary.blocked)
            .hasRegistered(null)
            .hasAchievementsCount(null)
            .hasTotalRacesCount(null)
            .hasBestSpeed(null)
            .hasRatingLevel(null)
            .hasFriendsCount(null)
            .hasVocabulariesCount(null)
            .hasCarsCount(null)
        ;

        Assertions.assertThat(player.getCar())
            .hasId(summary.car.car)
            .hasColor(summary.car.color);
    }

    @Test
    @DisplayName("Test a non-existing user for which both /get-summary and /get-index-data returns return \"invalid user id\" error")
    void testNonExistingUser() {
        PlayerSummary summary = new PlayerSummary();
        summary.err = ApiErrors.INVALID_USER_ID_ERROR;

        GetIndexDataResponse indexData = new GetIndexDataResponse()
            .err(ApiErrors.INVALID_USER_ID_ERROR)
            .ok(null); // FGJ

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(null)
            .hasLogin(null)
            .hasRankLevel(null)
            .hasTitle(null)
            .hasBlocked(null)
            .hasRegistered(null)
            .hasAchievementsCount(null)
            .hasTotalRacesCount(null)
            .hasBestSpeed(null)
            .hasRatingLevel(null)
            .hasFriendsCount(null)
            .hasVocabulariesCount(null)
            .hasCarsCount(null)
        ;

        Assertions.assertThat(player.getCar())
            .hasId(null)
            .hasColor(null);
    }
}
