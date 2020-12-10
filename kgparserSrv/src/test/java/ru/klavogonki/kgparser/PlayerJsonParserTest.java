package ru.klavogonki.kgparser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;
import ru.klavogonki.kgparser.util.TestUtils;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerJsonParserTest {

    @Test
    @DisplayName("Data of a brand new user with minimum possible statistics must be successfully parsed")
    void testSuccessfulParseOfBrandNewUser() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-624511.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-624511.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(624511, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of a non-existing user must return an empty result")
    void testNonExistingPlayerParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-30001.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-30001.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(30001, summaryFile, indexDataFile);
        assertThat(playerOptional).isNotPresent();
    }

    @Test
    @DisplayName("Data of a user with a hidden profile must be successfully parsed")
    void testKlavoMechanicWithHiddenProfileParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-21.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-21.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(21, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.indexData.err).isEqualTo(PlayerSummary.HIDDEN_PROFILE_USER_ERROR);
    }

    @Test
    @DisplayName("Data of an existing user must be successfully parsed")
    void testExistingUserParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-242585.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-242585.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(242585, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of an existing user with personal car id must be successfully parsed")
    void testExistingUserWithPersonalCarIdParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-922.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-922.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(922, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of an existing user with blank login must be successfully parsed")
    void testExistingUserWithBlankLoginParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-109842.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-109842.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(109842, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of an existing blocked user with negative registered date must be successfully parsed")
    void testBlockedUserWithNegativeRegisteredDateParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-141327.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-141327.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(141327, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.blocked).isEqualTo(1);
        assertThat(player.indexData.stats.registered.sec).isEqualTo(-62169993079L); // Mein Gott, muss das sein?! So ein Bockmist aber auch!
        assertThat(player.indexData.stats.registered.usec).isZero();
    }

    @Test
    @DisplayName("Data of an existing blocked user with blocked: 4 must be successfully parsed")
    void testBlockedUserWithBlocked4Parse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-142478.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-142478.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(142478, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.blocked).isEqualTo(4);
    }

    @Test
    @DisplayName("Data of an existing user with successful /get-summary but failing /get-index-data must be successfully parsed")
    void testUserWithSuccessfulGetSummaryAndInvalidUserIdErrorInGetIndexData() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-161997.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-161997.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(161997, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.blocked).isZero();
        assertThat(player.summary.err).isNull();
        assertThat(player.indexData.err).isEqualTo(PlayerSummary.INVALID_USER_ID_ERROR);
    }

    @Test
    @DisplayName("Data of an existing user with successful /get-summary but failing /get-index-data on a MongoDB error must be successfully parsed")
    void testUserWithSuccessfulGetSummaryAndMongoDbErrorInGetIndexData() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-498727.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-498727.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(498727, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.blocked).isZero();
        assertThat(player.summary.err).isNull();
        assertThat(player.indexData.err).isEqualTo(PlayerSummary.MONGO_REFS_ERROR_USER_498727);
    }

    @Test
    @DisplayName("Data of an existing user with indexData.bio.text == null must be successfully parsed")
    void testUserWithNullBioText() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-368664.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-368664.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(368664, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();

        PlayerJsonData player = playerOptional.get();
        assertThat(player.summary.blocked).isZero(); // user is not blocked
        assertThat(player.indexData.bio.text).isNull(); // null text
        assertThat(player.indexData.bio.oldText).isNull();
        assertThat(player.indexData.bio.oldTextRemoved).isEqualTo("<img  src=\"http://iplogger.ru/1LZq3.jpg\" border=\"0\" class=\"linked-image\" />");
    }
}
