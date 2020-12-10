package ru.klavogonki.kgparser.jsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.Car;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.util.TestUtils;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonUtilsTest {
    private static final Logger logger = LogManager.getLogger(JacksonUtilsTest.class);

    // todo: add bio.oldTextRemoved validation to all indexData tests!
    // todo: missing tests:
    // - 109842 - blank login
    // - 141327 - blocked: 1 and negative registered.sec !!! date parse will most probably fail
    // - 142478 - blocked: 4
    // - 161997 - get-summary works, get-index-data returns error
    // - 368664 - bio.text == null, bio.oldText not present, bio.old_text_removed present

    @Test
    @DisplayName("Test parsing an existing user summary from a json file")
    void testPlayerSummary() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-242585.json");

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
        assertThat(summary.car.car).isEqualTo(Car.F1.id);
        assertThat(summary.car.color).isEqualTo("#BF1300");
    }

    @Test
    @DisplayName("Test parsing an existing user summary for a user with a personal car id from a json file")
    void testPlayerWithPersonalCarIdSummary() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-922.json");

        PlayerSummary summary = JacksonUtils.parse(file, PlayerSummary.class);
        logPlayerSummary(summary);

        assertThat(summary.err).isNull();
        assertThat(summary.isOnline).isFalse();
        assertThat(summary.level).isEqualTo(Rank.getLevel(Rank.maniac).intValue());
        assertThat(summary.title).isEqualTo(Rank.getDisplayName(Rank.maniac));
        assertThat(summary.blocked).isZero();

        assertThat(summary.user).isNotNull();
        assertThat(summary.user.id).isEqualTo(922);
        assertThat(summary.user.login).isEqualTo("lovermann");

        assertThat(summary.car).isNotNull();
        assertThat(summary.car.car).isEqualTo(Car.CARAVEL.personalId);
        assertThat(summary.car.color).isEqualTo("#000000");
    }

    @Test
    @DisplayName("Test parsing a brand new user summary from a json file")
    void testBrandNewPlayerSummary() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-624511.json");

        PlayerSummary summary = JacksonUtils.parse(file, PlayerSummary.class);
        logPlayerSummary(summary);

        assertThat(summary.err).isNull();
        assertThat(summary.isOnline).isTrue();
        assertThat(summary.level).isEqualTo(Rank.getLevel(Rank.novice).intValue());
        assertThat(summary.title).isEqualTo(Rank.getDisplayName(Rank.novice));
        assertThat(summary.blocked).isZero();

        assertThat(summary.user).isNotNull();
        assertThat(summary.user.id).isEqualTo(624511);
        assertThat(summary.user.login).isEqualTo("nosferatum0");

        assertThat(summary.car).isNotNull();
        assertThat(summary.car.car).isEqualTo(Car.ZAZ_965.id);
        assertThat(summary.car.color).isEqualTo("#777777");
    }

    @Test
    @DisplayName("Test parsing a summary of a klavomechanic with a hidden profile from a json file")
    void testKlavoMechanicWithHiddenProfileSummary() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-21.json");

        PlayerSummary summary = JacksonUtils.parse(file, PlayerSummary.class);
        logPlayerSummary(summary);

        assertThat(summary.err).isNull();
        assertThat(summary.isOnline).isFalse();
        assertThat(summary.level).isEqualTo(Rank.getLevel(Rank.superman).intValue());
        assertThat(summary.title).isEqualTo(Rank.KLAVO_MECHANIC_TITLE);
        assertThat(summary.blocked).isZero();

        assertThat(summary.user).isNotNull();
        assertThat(summary.user.id).isEqualTo(21);
        assertThat(summary.user.login).isEqualTo("Artch");

        assertThat(summary.car).isNotNull();
        assertThat(summary.car.car).isEqualTo(Car.AUDI_TT.id);
        assertThat(summary.car.color).isEqualTo("#893425");
    }

    @Test
    @DisplayName("Test parsing a non-existing user summary from a json file")
    void testInvalidPlayerSummary() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-30001.json");

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
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-242585.json");

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

        convertUserRegisteredTime(data);
    }

    @Test
    @DisplayName("Test parsing an existing user index data for a user with a personal car id from a json file")
    void testPlayerWithPersonalCarIdIndexData() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-922.json");

        PlayerIndexData data = JacksonUtils.parse(file, PlayerIndexData.class);
        logPlayerIndexData(data);

        assertThat(data.ok).isEqualTo(PlayerIndexData.OK_CORRECT_VALUE);
        assertThat(data.err).isNull();

        assertThat(data.bio).isNotNull();
        assertThat(data.bio.userId).isEqualTo(922);
        assertThat(data.bio.oldText).isNotBlank(); // huge html, no validation
        assertThat(data.bio.text).isNotBlank(); // huge html, no validation

        assertThat(data.stats).isNotNull();

        assertThat(data.stats.registered).isNotNull();
        assertThat(data.stats.registered.sec).isEqualTo(1211400000);
        assertThat(data.stats.registered.usec).isZero();

        assertThat(data.stats.achievementsCount).isEqualTo(171);
        assertThat(data.stats.totalRacesCount).isEqualTo(47887);
        assertThat(data.stats.bestSpeed).isEqualTo(554);
        assertThat(data.stats.ratingLevel).isEqualTo(37);
        assertThat(data.stats.friendsCount).isEqualTo(75);
        assertThat(data.stats.vocabulariesCount).isEqualTo(83);
        assertThat(data.stats.carsCount).isEqualTo(41);

        convertUserRegisteredTime(data);
    }

    // todo: move this conversion to some utils class
    private void convertUserRegisteredTime(final PlayerIndexData data) {
        // probably use ZoneOffset/ZoneId for Moscow time or use just localDate
        ZoneId moscowZoneId = ZoneId.of("Europe/Moscow");

        Instant instant = Instant.ofEpochSecond(data.stats.registered.sec, data.stats.registered.usec);
        LocalDateTime localDateTimeUtc = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        LocalDateTime localDateTimeMoscow = LocalDateTime.ofInstant(instant, moscowZoneId);
        ZonedDateTime zonedDateTimeMoscow = ZonedDateTime.ofInstant(instant, moscowZoneId);

        logger.info("registered as Instant: {}", instant);
        logger.info("registered as LocalDateTime UTC: {}", localDateTimeUtc);
        logger.info("registered as ZonedDateTime UTC: {}", zonedDateTimeUtc);
        logger.info("registered as LocalDateTime Moscow: {}", localDateTimeMoscow);
        logger.info("registered as ZonedDateTime Moscow: {}", zonedDateTimeMoscow);
    }

    @Test
    @DisplayName("Test parsing a brand new user index data from a json file")
    void testBrandNewPlayerIndexData() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-624511.json");

        PlayerIndexData data = JacksonUtils.parse(file, PlayerIndexData.class);
        logPlayerIndexData(data);

        assertThat(data.ok).isEqualTo(PlayerIndexData.OK_CORRECT_VALUE);
        assertThat(data.err).isNull();

        assertThat(data.bio).isNotNull();
        assertThat(data.bio.userId).isEqualTo(624511);
        assertThat(data.bio.oldText).isNull(); // no oldText for the new users
        assertThat(data.bio.text).isEmpty(); // empty and not null

        assertThat(data.stats).isNotNull();

        assertThat(data.stats.registered).isNotNull();
        assertThat(data.stats.registered.sec).isEqualTo(1607554944);
        assertThat(data.stats.registered.usec).isZero();

        assertThat(data.stats.achievementsCount).isZero();
        assertThat(data.stats.totalRacesCount).isZero();
        assertThat(data.stats.bestSpeed).isNull(); // no races in "Normal" -> no best speed
        assertThat(data.stats.ratingLevel).isEqualTo(1); // user is level 1 from the start
        assertThat(data.stats.friendsCount).isZero();
        assertThat(data.stats.vocabulariesCount).isZero();
        assertThat(data.stats.carsCount).isEqualTo(1); // user has 1 car from the start

        convertUserRegisteredTime(data);
    }

    @Test
    @DisplayName("Test parsing index data of a klavomechanic with a hidden profile from a json file. Request returns a \"hidden profile\" error.")
    void testKlavoMechanicWithHiddenProfileIndexData() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-21.json");

        PlayerIndexData data = JacksonUtils.parse(file, PlayerIndexData.class);
        logPlayerIndexData(data);

        assertThat(data.ok).isNull();
        assertThat(data.err).isEqualTo(PlayerSummary.HIDDEN_PROFILE_USER_ERROR);

        assertThat(data.bio).isNull();

        assertThat(data.stats).isNull();
    }

    @Test
    @DisplayName("Test parsing a non-existing user index data from a json file")
    void testInvalidPlayerIndexData() {
        File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-30001.json");

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
}
