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
        assertThat(summary.level).isEqualTo(7);
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

    private void logPlayerSummary(final PlayerSummary summary) {
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


    private static File readResourceFile(final String resourceName) {
        ClassLoader classLoader = JacksonUtilsTest.class.getClassLoader();
        URL resource = classLoader.getResource(resourceName);
        assertThat(resource).isNotNull();

        return new File(resource.getFile());
    }
}
