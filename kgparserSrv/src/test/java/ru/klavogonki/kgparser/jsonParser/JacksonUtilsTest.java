package ru.klavogonki.kgparser.jsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.Rank;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonUtilsTest {
    private static final Logger logger = LogManager.getLogger(JacksonUtilsTest.class);

    @Test
    @DisplayName("Test parsing an existing user summary from a json file")
    void testPlayerSummary() {
        File file = readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-242585.json");

        PlayerSummary summary = JacksonUtils.parse(file, PlayerSummary.class);
        logPlayerSummary(summary);

        assertThat(summary.err).isNull();
        assertThat(summary.isOnline).isTrue();
        assertThat(summary.level).isEqualTo(Rank.getLevel(Rank.superman).intValue());
        assertThat(summary.title).isEqualTo(Rank.getDisplayName(Rank.superman));
        assertThat(summary.blocked).isZero();

        assertThat(summary.user).isNotNull();
        assertThat(summary.user.id).isEqualTo(242585);
        assertThat(summary.user.login).isEqualTo("nosferatum");

        assertThat(summary.car).isNotNull();
        assertThat(summary.car.car).isEqualTo(15);
        assertThat(summary.car.color).isEqualTo("#BF1300");
    }

    @Test
    @DisplayName("Test parsing a non-existing user summary from a json file")
    void testInvalidPlayerSummary() {
        File file = readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-30001.json");

        PlayerSummary summary = JacksonUtils.parse(file, PlayerSummary.class);
        logPlayerSummary(summary);

        assertThat(summary.err).isEqualTo(PlayerSummary.INVALID_USER_ID_ERROR);
        assertThat(summary.isOnline).isNull();
        assertThat(summary.level).isNull();
        assertThat(summary.title).isNull();
        assertThat(summary.blocked).isNull();

        assertThat(summary.user).isNull();

        assertThat(summary.car).isNull();
    }

    @Test
    @DisplayName("Test parsing an existing user index data from a json file")
    void testPlayerIndexData() {
        File file = readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-242585.json");

        PlayerIndexData data = JacksonUtils.parse(file, PlayerIndexData.class);
        logPlayerIndexData(data);

        assertThat(data.ok).isEqualTo(PlayerIndexData.OK_CORRECT_VALUE);
        assertThat(data.err).isNull();

        assertThat(data.bio).isNotNull();
        assertThat(data.bio.userId).isEqualTo(242585);
        assertThat(data.bio.oldText).isNotBlank(); // huge html, no validation
        assertThat(data.bio.text).isNotBlank(); // huge html, no validation

        assertThat(data.stats).isNotNull();

        assertThat(data.stats.registered).isNotNull();
        assertThat(data.stats.registered.sec).isEqualTo(1297852113);
        assertThat(data.stats.registered.usec).isZero();

        assertThat(data.stats.achievementsCount).isEqualTo(225);
        assertThat(data.stats.totalRacesCount).isEqualTo(60633);
        assertThat(data.stats.bestSpeed).isEqualTo(626);
        assertThat(data.stats.ratingLevel).isEqualTo(32);
        assertThat(data.stats.friendsCount).isEqualTo(102);
        assertThat(data.stats.vocabulariesCount).isEqualTo(109);
        assertThat(data.stats.carsCount).isEqualTo(33);
    }

    @Test
    @DisplayName("Test parsing a non-existing user index data from a json file")
    void testInvalidPlayerIndexData() {
        File file = readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-30001.json");

        PlayerIndexData data = JacksonUtils.parse(file, PlayerIndexData.class);
        logPlayerIndexData(data);

        assertThat(data.ok).isNull();
        assertThat(data.err).isEqualTo(PlayerSummary.INVALID_USER_ID_ERROR);

        assertThat(data.bio).isNull();

        assertThat(data.stats).isNull();
    }

    private static void logPlayerSummary(final PlayerSummary summary) {
        logger.info("Player summary: ");
        logger.info("- err: {}", summary.err);
        logger.info("- isOnline: {}", summary.isOnline);
        logger.info("- level: {}", summary.level);
        logger.info("- title: {}", summary.title);
        logger.info("- blocked: {}", summary.blocked);

        logger.info("");

        if (summary.user != null) {
            logger.info("User:");
            logger.info("- id: {}", summary.user.id);
            logger.info("- login: {}", summary.user.login);
        }
        else {
            logger.info("User: null");
        }

        logger.info("");

        if (summary.car != null) {
            logger.info("Car:");
            logger.info("- car: {}", summary.car.car);
            logger.info("- color: {}", summary.car.color);
        }
        else {
            logger.info("User: null");
        }
    }

    private static void logPlayerIndexData(final PlayerIndexData data) {
        logger.info("Player index data: ");
        logger.info("- ok: {}", data.ok);
        logger.info("- err: {}", data.err);

        logger.info("");

        if (data.bio != null) {
            logger.info("Bio:");
            logger.info("- userId: {}", data.bio.userId);
            logger.info("- oldText: {}", data.bio.oldText);
            logger.info("- text: {}", data.bio.text);
        }
        else {
            logger.info("Bio: null");
        }

        logger.info("");

        if (data.stats != null) {
            logger.info("Stats:");
            logger.info("- registered.sec: {}", data.stats.registered.sec);
            logger.info("- registered.usec: {}", data.stats.registered.usec);

            logger.info("- achievementsCount: {}", data.stats.achievementsCount);
            logger.info("- totalRacesCount: {}", data.stats.totalRacesCount);
            logger.info("- bestSpeed: {}", data.stats.bestSpeed);
            logger.info("- ratingLevel: {}", data.stats.ratingLevel);
            logger.info("- friendsCount: {}", data.stats.friendsCount);
            logger.info("- vocabulariesCount: {}", data.stats.vocabulariesCount);
            logger.info("- carsCount: {}", data.stats.carsCount);
        }
        else {
            logger.info("Stats: null");
        }
    }

    private static File readResourceFile(final String resourceName) {
        ClassLoader classLoader = JacksonUtilsTest.class.getClassLoader();
        URL resource = classLoader.getResource(resourceName);
        assertThat(resource).isNotNull();

        return new File(resource.getFile());
    }
}
