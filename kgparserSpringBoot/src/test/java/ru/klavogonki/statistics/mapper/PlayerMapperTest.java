package ru.klavogonki.statistics.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.openapi.model.Car;
import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetIndexDataStats;
import ru.klavogonki.openapi.model.GetSummaryResponse;
import ru.klavogonki.openapi.model.GetSummaryUser;
import ru.klavogonki.statistics.download.PlayerJsonData;
import ru.klavogonki.statistics.entity.CarEntityAssert;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.entity.PlayerEntityAssert;
import ru.klavogonki.statistics.util.DateUtils;
import ru.klavogonki.statistics.util.JacksonUtils;
import ru.klavogonki.statistics.util.TestUtils;

import java.io.File;
import java.time.OffsetDateTime;

class PlayerMapperTest {

    @Test
    @DisplayName("Test a user with maximum possible filled fields")
    void testUserWithMaximumData() {
        // summary
        File getSummaryFile = TestUtils.readFromStatisticsDownload("get-summary-242585.json");
        GetSummaryResponse summary = JacksonUtils.parse(getSummaryFile, GetSummaryResponse.class);

        GetSummaryUser user = summary.getUser();
        Car car = summary.getCar();

        // index-data
        File getIndexDataFile = TestUtils.readFromStatisticsDownload("get-index-data-242585.json");
        GetIndexDataResponse indexData = JacksonUtils.parse(getIndexDataFile, GetIndexDataResponse.class);

        GetIndexDataStats stats = indexData.getStats();

        // map
        PlayerJsonData jsonData = new PlayerJsonData(OffsetDateTime.now(), summary, indexData, null); // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        // assert
        PlayerEntityAssert.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(DateUtils.convertToUtcLocalDateTime(jsonData.importDate))
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

        CarEntityAssert.assertThat(player.getCar())
            .hasId(car.getCar())
            .hasColor(car.getColor());
    }

    @Test
    @DisplayName("Test a new registered user - minimum possible data")
    void testJustRegisteredUser() {
        // summary
        File getSummaryFile = TestUtils.readFromStatisticsDownload("get-summary-624511.json");
        GetSummaryResponse summary = JacksonUtils.parse(getSummaryFile, GetSummaryResponse.class);

        GetSummaryUser user = summary.getUser();
        Car car = summary.getCar();

        // index-data
        File getIndexDataFile = TestUtils.readFromStatisticsDownload("get-index-data-624511.json");
        GetIndexDataResponse indexData = JacksonUtils.parse(getIndexDataFile, GetIndexDataResponse.class);

        GetIndexDataStats stats = indexData.getStats();

        // map
        PlayerJsonData jsonData = new PlayerJsonData(OffsetDateTime.now(), summary, indexData, null);  // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        // assert
        PlayerEntityAssert.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(DateUtils.convertToUtcLocalDateTime(jsonData.importDate))
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

        CarEntityAssert.assertThat(player.getCar())
            .hasId(car.getCar())
            .hasColor(car.getColor());
    }

    @Test
    @DisplayName("Test a user for which /get-summary works but /get-index-data returns an error")
    void testUserWithoutIndexData() {
        // summary
        File getSummaryFile = TestUtils.readFromStatisticsDownload("get-summary-21.json");
        GetSummaryResponse summary = JacksonUtils.parse(getSummaryFile, GetSummaryResponse.class);

        GetSummaryUser user = summary.getUser();
        Car car = summary.getCar();

        // index-data
        File getIndexDataFile = TestUtils.readFromStatisticsDownload("get-index-data-21.json");
        GetIndexDataResponse indexData = JacksonUtils.parse(getIndexDataFile, GetIndexDataResponse.class);

        // map
        PlayerJsonData jsonData = new PlayerJsonData(OffsetDateTime.now(), summary, indexData, null); // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        // assert
        PlayerEntityAssert.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(DateUtils.convertToUtcLocalDateTime(jsonData.importDate))
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

        CarEntityAssert.assertThat(player.getCar())
            .hasId(car.getCar())
            .hasColor(car.getColor());
    }

    @Test
    @DisplayName("Test a non-existing user for which both /get-summary and /get-index-data returns return \"invalid user id\" error")
    void testNonExistingUser() {
        // summary
        File getSummaryFile = TestUtils.readFromStatisticsDownload("get-summary-30001.json");
        GetSummaryResponse summary = JacksonUtils.parse(getSummaryFile, GetSummaryResponse.class);

        // index-data
        File getIndexDataFile = TestUtils.readFromStatisticsDownload("get-index-data-30001.json");
        GetIndexDataResponse indexData = JacksonUtils.parse(getIndexDataFile, GetIndexDataResponse.class);

        // map
        PlayerJsonData jsonData = new PlayerJsonData(OffsetDateTime.now(), summary, indexData, null);  // todo: fill statsOverview?

        PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

        PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

        // assert
        PlayerEntityAssert.assertThat(player)
            .hasDbId(null) // id not yet filled, entity not yet saved to the database
            .hasImportDate(DateUtils.convertToUtcLocalDateTime(jsonData.importDate))
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

        CarEntityAssert.assertThat(player.getCar())
            .hasId(null)
            .hasColor(null);
    }
}
