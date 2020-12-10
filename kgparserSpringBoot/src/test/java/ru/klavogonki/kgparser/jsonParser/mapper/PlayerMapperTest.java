package ru.klavogonki.kgparser.jsonParser.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.kgparser.Car;
import ru.klavogonki.kgparser.PlayerJsonData;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;
import ru.klavogonki.kgparser.jsonParser.entity.Assertions;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.util.DateUtils;

// todo: merge it with JacksonUtilsTest (problem with module/resource dependencies), so that user data can be read from json files
class PlayerMapperTest {

    @Test
    @DisplayName("Test a user with maximum possible filled fields")
    void testUserWithMaximumData() {
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

        PlayerIndexData indexData = new PlayerIndexData();
        indexData.ok = PlayerIndexData.OK_CORRECT_VALUE; // FGJ

        indexData.bio = new PlayerIndexData.Bio();
        indexData.bio.userId = 242585;

        indexData.stats = new PlayerIndexData.Stats();

        indexData.stats.registered = new PlayerIndexData.Registered();
        indexData.stats.registered.sec = 1297852113L;
        indexData.stats.registered.usec = 0L;

        indexData.stats.achievementsCount = 225;
        indexData.stats.totalRacesCount = 60633;
        indexData.stats.bestSpeed = 626;
        indexData.stats.ratingLevel = 32;
        indexData.stats.friendsCount = 102;
        indexData.stats.vocabulariesCount = 109;
        indexData.stats.carsCount = 33;

        PlayerJsonData jsonData = new PlayerJsonData(summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(null) // todo: we should fill it!
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.err)
            .hasPlayerId(summary.user.id)
            .hasLogin(summary.user.login)
            .hasRankLevel(summary.level)
            .hasTitle(summary.title)
            .hasBlocked(summary.blocked)
            .hasRegistered(DateUtils.convertUserRegisteredTime(indexData))
            .hasAchievementsCount(indexData.stats.achievementsCount)
            .hasTotalRacesCount(indexData.stats.totalRacesCount)
            .hasBestSpeed(indexData.stats.bestSpeed)
            .hasRatingLevel(indexData.stats.ratingLevel)
            .hasFriendsCount(indexData.stats.friendsCount)
            .hasVocabulariesCount(indexData.stats.vocabulariesCount)
            .hasCarsCount(indexData.stats.carsCount)
        ;

        Assertions.assertThat(player.getCar())
            .hasId(summary.car.car)
            .hasColor(summary.car.color);
    }

    @Test
    @DisplayName("Test a new registered user - minimum possible data")
    void testJustRegisteredUser() {
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

        PlayerIndexData indexData = new PlayerIndexData();
        indexData.ok = PlayerIndexData.OK_CORRECT_VALUE; // FGJ

        indexData.bio = new PlayerIndexData.Bio();
        indexData.bio.userId = 624511;

        indexData.stats = new PlayerIndexData.Stats();

        indexData.stats.registered = new PlayerIndexData.Registered();
        indexData.stats.registered.sec = 1607554944L;
        indexData.stats.registered.usec = 0L;

        indexData.stats.achievementsCount = 0;
        indexData.stats.totalRacesCount = 0;
        indexData.stats.bestSpeed = null;
        indexData.stats.ratingLevel = 1;
        indexData.stats.friendsCount = 0;
        indexData.stats.vocabulariesCount = 0;
        indexData.stats.carsCount = 1;

        PlayerJsonData jsonData = new PlayerJsonData(summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(null) // todo: we should fill it!
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.err)
            .hasPlayerId(summary.user.id)
            .hasLogin(summary.user.login)
            .hasRankLevel(summary.level)
            .hasTitle(summary.title)
            .hasBlocked(summary.blocked)
            .hasRegistered(DateUtils.convertUserRegisteredTime(indexData))
            .hasAchievementsCount(indexData.stats.achievementsCount)
            .hasTotalRacesCount(indexData.stats.totalRacesCount)
            .hasBestSpeed(null)
            .hasRatingLevel(indexData.stats.ratingLevel)
            .hasFriendsCount(indexData.stats.friendsCount)
            .hasVocabulariesCount(indexData.stats.vocabulariesCount)
            .hasCarsCount(indexData.stats.carsCount)
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

        PlayerIndexData indexData = new PlayerIndexData();
        indexData.err = PlayerSummary.HIDDEN_PROFILE_USER_ERROR;
        indexData.ok = null; // FGJ

        PlayerJsonData jsonData = new PlayerJsonData(summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(null) // todo: we should fill it!
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.err)
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
        summary.err = PlayerSummary.INVALID_USER_ID_ERROR;

        PlayerIndexData indexData = new PlayerIndexData();
        indexData.err = PlayerSummary.INVALID_USER_ID_ERROR;
        indexData.ok = null; // FGJ

        PlayerJsonData jsonData = new PlayerJsonData(summary, indexData);

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        Assertions.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(null) // todo: we should fill it!
            .hasGetSummaryError(summary.err)
            .hasGetIndexDataError(indexData.err)
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
