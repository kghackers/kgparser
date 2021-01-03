package ru.klavogonki.kgparser.jsonParser.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.kgparser.Car;
import ru.klavogonki.kgparser.PlayerJsonData;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.jsonParser.ApiErrors;
import ru.klavogonki.kgparser.jsonParser.Assertions;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.util.DateUtils;
import ru.klavogonki.openapi.model.Bio;
import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetIndexDataStats;
import ru.klavogonki.openapi.model.GetSummaryResponse;
import ru.klavogonki.openapi.model.GetSummaryUser;
import ru.klavogonki.openapi.model.Microtime;

import java.time.LocalDateTime;

// todo: merge it with JacksonUtilsTest (problem with module/resource dependencies), so that user data can be read from json files
class PlayerMapperTest {

    @Test
    @DisplayName("Test a user with maximum possible filled fields")
    void testUserWithMaximumData() {
        // summary
        GetSummaryUser user = new GetSummaryUser()
            .id(242585)
            .login("nosferatum");

        ru.klavogonki.openapi.model.Car car = new ru.klavogonki.openapi.model.Car()
            .car(Car.F1.id)
            .color("#BF1300");

        GetSummaryResponse summary = new GetSummaryResponse()
            .level(Rank.getLevel(Rank.superman).intValue())
            .title(Rank.getDisplayName(Rank.superman))
            .blocked(0)
            .user(user)
            .car(car);

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

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData, null); // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.getErr())
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(user.getId())
            .hasLogin(user.getLogin())
            .hasRankLevel(summary.getLevel())
            .hasTitle(summary.getTitle())
            .hasBlocked(summary.getBlocked())
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
            .hasId(car.getCar())
            .hasColor(car.getColor());
    }

    @Test
    @DisplayName("Test a new registered user - minimum possible data")
    void testJustRegisteredUser() {
        // summary
        GetSummaryUser user = new GetSummaryUser()
            .id(624511)
            .login("nosferatum0");

        ru.klavogonki.openapi.model.Car car = new ru.klavogonki.openapi.model.Car()
            .car(Car.ZAZ_965.id)
            .color("#777777");

        GetSummaryResponse summary = new GetSummaryResponse()
            .level(Rank.getLevel(Rank.novice).intValue())
            .title(Rank.getDisplayName(Rank.novice))
            .blocked(0)
            .user(user)
            .car(car);

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

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData, null);  // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.getErr())
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(user.getId())
            .hasLogin(user.getLogin())
            .hasRankLevel(summary.getLevel())
            .hasTitle(summary.getTitle())
            .hasBlocked(summary.getBlocked())
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
            .hasId(car.getCar())
            .hasColor(car.getColor());
    }

    @Test
    @DisplayName("Test a user for which /get-summary works but /get-index-data returns an error")
    void testUserWithoutIndexData() {
        // summary
        GetSummaryUser user = new GetSummaryUser()
            .id(21)
            .login("Artch");

        ru.klavogonki.openapi.model.Car car = new ru.klavogonki.openapi.model.Car()
            .car(Car.AUDI_TT.id)
            .color("#893425");

        GetSummaryResponse summary = new GetSummaryResponse()
            .level(Rank.getLevel(Rank.superman).intValue())
            .title(Rank.KLAVO_MECHANIC_TITLE)
            .blocked(0)
            .user(user)
            .car(car);

        // index-data
        GetIndexDataResponse indexData = new GetIndexDataResponse()
            .err(ApiErrors.HIDDEN_PROFILE_USER_ERROR)
            .ok(null); // FGJ

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData, null); // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.getErr())
            .hasGetIndexDataError(indexData.getErr())
            .hasPlayerId(user.getId())
            .hasLogin(user.getLogin())
            .hasRankLevel(summary.getLevel())
            .hasTitle(summary.getTitle())
            .hasBlocked(summary.getBlocked())
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
            .hasId(car.getCar())
            .hasColor(car.getColor());
    }

    @Test
    @DisplayName("Test a non-existing user for which both /get-summary and /get-index-data returns return \"invalid user id\" error")
    void testNonExistingUser() {
        // summary
        GetSummaryResponse summary = new GetSummaryResponse()
            .err(ApiErrors.INVALID_USER_ID_ERROR);

        // index-data
        GetIndexDataResponse indexData = new GetIndexDataResponse()
            .err(ApiErrors.INVALID_USER_ID_ERROR)
            .ok(null); // FGJ

        PlayerJsonData jsonData = new PlayerJsonData(LocalDateTime.now(), summary, indexData, null);  // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(jsonData.importDate)
            .hasGetSummaryError(summary.getErr())
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
