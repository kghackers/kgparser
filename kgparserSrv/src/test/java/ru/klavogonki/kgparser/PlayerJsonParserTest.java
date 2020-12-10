package ru.klavogonki.kgparser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.util.TestUtils;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerJsonParserTest {

    @Test
    @DisplayName("Data of brand new user with minimum possible statistics must be successfully parsed")
    void testSuccessfulParseOfBrandNewUser() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-624511.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-624511.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(624511, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();
    }

    @Test
    @DisplayName("Data of non-existing user must return an empty result")
    void testNonExistingPlayerParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-30001.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-30001.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(30001, summaryFile, indexDataFile);
        assertThat(playerOptional).isNotPresent();
    }

    @Test
    @DisplayName("Data of an existing user must be successfully parsed")
    void testExistingUserParse() {
        File summaryFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-242585.json");
        File indexDataFile = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-242585.json");

        Optional<PlayerJsonData> playerOptional = PlayerJsonParser.readPlayerData(242585, summaryFile, indexDataFile);
        assertThat(playerOptional).isPresent();
    }
}
