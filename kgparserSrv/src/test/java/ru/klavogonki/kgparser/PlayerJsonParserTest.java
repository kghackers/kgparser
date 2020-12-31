package ru.klavogonki.kgparser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.jsonParser.ApiErrors;
import ru.klavogonki.kgparser.util.TestUtils;
import ru.klavogonki.openapi.model.Bio;
import ru.klavogonki.openapi.model.BioAssert;
import ru.klavogonki.openapi.model.GetStatsOverviewGameType;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeAssert;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeInfo;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeInfoAssert;
import ru.klavogonki.openapi.model.Microtime;
import ru.klavogonki.openapi.model.MicrotimeAssert;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerJsonParserTest {

    @Test
    @DisplayName("Data of a brand new user with minimum possible statistics must be successfully parsed")
    void testSuccessfulParseOfBrandNewUser() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-624511.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-624511.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-624511.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 624511, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of a non-existing user must return an result with appropriate errors")
    void testNonExistingPlayerParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-30001.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-30001.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-30001.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 30001, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getErr()).isEqualTo(ApiErrors.INVALID_USER_ID_ERROR);
        assertThat(player.indexData.getErr()).isEqualTo(ApiErrors.INVALID_USER_ID_ERROR);
    }

    @Test
    @DisplayName("Data of a user with a hidden profile must be successfully parsed")
    void testKlavoMechanicWithHiddenProfileParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-21.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-21.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-21.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 21, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.indexData.getErr()).isEqualTo(ApiErrors.HIDDEN_PROFILE_USER_ERROR);
    }

    @Test
    @DisplayName("Data of an existing user must be successfully parsed")
    void testExistingUserParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-242585.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-242585.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-242585.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 242585, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of an existing user with personal car id must be successfully parsed")
    void testExistingUserWithPersonalCarIdParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-922.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-922.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-922.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(),922, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of an existing user with blank login must be successfully parsed")
    void testExistingUserWithBlankLoginParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-109842.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-109842.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-109842.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(),109842, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of an existing blocked user with negative registered date must be successfully parsed")
    void testBlockedUserWithNegativeRegisteredDateParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-141327.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-141327.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-141327.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 141327, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getBlocked()).isEqualTo(1);

        Microtime registered = player.indexData.getStats().getRegistered();
        MicrotimeAssert
            .assertThat(registered)
            .hasSec(-62169993079L) // Mein Gott, muss das sein?! So ein Bockmist aber auch!
            .hasUsec(0L);
    }

    @Test
    @DisplayName("Data of an existing blocked user with blocked: 4 must be successfully parsed")
    void testBlockedUserWithBlocked4Parse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-142478.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-142478.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-142478.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(),142478, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getBlocked()).isEqualTo(4);
    }

    @Test
    @DisplayName("Data of an existing user with successful /get-summary but failing /get-index-data must be successfully parsed")
    void testUserWithSuccessfulGetSummaryAndInvalidUserIdErrorInGetIndexData() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-161997.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-161997.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-161997.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(),161997, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getBlocked()).isZero();
        assertThat(player.summary.getErr()).isNull();
        assertThat(player.indexData.getErr()).isEqualTo(ApiErrors.INVALID_USER_ID_ERROR);
    }

    @Test
    @DisplayName("Data of an existing user with successful /get-summary but failing /get-index-data on a MongoDB error must be successfully parsed")
    void testUserWithSuccessfulGetSummaryAndMongoDbErrorInGetIndexData() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-498727.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-498727.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-498727.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 498727, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getBlocked()).isZero();
        assertThat(player.summary.getErr()).isNull();
        assertThat(player.indexData.getErr()).isEqualTo(ApiErrors.MONGO_REFS_ERROR_USER_498727);
    }

    @Test
    @DisplayName("Data of an existing user with indexData.bio.text == null must be successfully parsed")
    void testUserWithNullBioText() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-368664.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-368664.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-368664.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 368664, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getBlocked()).isZero(); // user is not blocked

        Bio bio = player.indexData.getBio();
        BioAssert
            .assertThat(bio)
            .hasText(null) // null text
            .hasOldText(null)
            .hasOldTextRemoved("<img  src=\"http://iplogger.ru/1LZq3.jpg\" border=\"0\" class=\"linked-image\" />");
    }

    @Test
    @DisplayName("User with 1 race in marathon, but avg_speed == null and avg_error == null")
    void testUserWith1MarathonRaceButWithNullAvgSpeedAndAvgError() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-24646.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-24646.json");
        File statsOverviewFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-24646.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(LocalDateTime.now(), 24646, summaryFile, indexDataFile, statsOverviewFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.getBlocked()).isZero(); // user is not blocked

        GetStatsOverviewGameType marathonStats = player.statsOverview.getGametypes().get(StandardDictionary.marathon.name());

        GetStatsOverviewGameTypeAssert
            .assertThat(marathonStats)
            .hasNumRaces(1);

        GetStatsOverviewGameTypeInfo marathonStatsInfo = marathonStats.getInfo();
        GetStatsOverviewGameTypeInfoAssert
            .assertThat(marathonStatsInfo)
            .hasUserId(24646)
            .hasNumRaces(1)
            .hasAvgSpeed(null)
            .hasBestSpeed(0)
            .hasAvgError(null)
            .hasHaul(299)
            .hasQual(0)
            .hasDirty(0)
            .hasUpdated("2008-08-16 15:30:36")
        ;
    }
}
