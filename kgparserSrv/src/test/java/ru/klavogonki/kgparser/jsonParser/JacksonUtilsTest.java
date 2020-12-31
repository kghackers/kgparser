package ru.klavogonki.kgparser.jsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.Car;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.StandardDictionary;
import ru.klavogonki.kgparser.util.DateUtils;
import ru.klavogonki.kgparser.util.TestUtils;
import ru.klavogonki.openapi.model.Bio;
import ru.klavogonki.openapi.model.BioAssert;
import ru.klavogonki.openapi.model.CarAssert;
import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetIndexDataResponseAssert;
import ru.klavogonki.openapi.model.GetIndexDataStats;
import ru.klavogonki.openapi.model.GetIndexDataStatsAssert;
import ru.klavogonki.openapi.model.GetStatsOverviewGameType;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeAssert;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeInfo;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeInfoAssert;
import ru.klavogonki.openapi.model.GetStatsOverviewResponse;
import ru.klavogonki.openapi.model.GetStatsOverviewResponseAssert;
import ru.klavogonki.openapi.model.GetSummaryResponse;
import ru.klavogonki.openapi.model.GetSummaryResponseAssert;
import ru.klavogonki.openapi.model.GetSummaryUser;
import ru.klavogonki.openapi.model.GetSummaryUserAssert;
import ru.klavogonki.openapi.model.Microtime;
import ru.klavogonki.openapi.model.MicrotimeAssert;
import ru.klavogonki.openapi.model.VocabularyMode;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonUtilsTest {
    private static final Logger logger = LogManager.getLogger(JacksonUtilsTest.class);

    // todo: add bio.oldTextRemoved validation to all indexData tests!
    // todo: missing tests:
    // - 109842 - blank login
    // - 141327 - blocked: 1 and negative registered.sec !!! date parse will most probably fail
    // - 142478 - blocked: 4
    // - 161997 - /get-summary works, /get-index-data returns error
    // - 368664 - bio.text == null, bio.oldText not present, bio.old_text_removed present
    // - 498727 - /get-summary works, /get-index-data returns error (special MongoDB error)

    @Nested
    @DisplayName("Test parsing of /get-summary responses")
    class GetSummary {

        @Test
        @DisplayName("Test parsing an existing user summary from a json file")
        void existingPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-242585.json");

            GetSummaryResponse summary = JacksonUtils.parse(file, GetSummaryResponse.class);
            logPlayerSummary(summary);

            GetSummaryResponseAssert
                .assertThat(summary)
                .hasErr(null)
                .hasIsOnline(Boolean.TRUE)
                .hasLevel(Rank.getLevel(Rank.superman).intValue())
                .hasTitle(Rank.getDisplayName(Rank.superman))
                .hasBlocked(0);

            GetSummaryUser user = summary.getUser();
            GetSummaryUserAssert
                .assertThat(user)
                .isNotNull()
                .hasId(242585)
                .hasLogin("nosferatum")
                .hasMigrateBookDone(Boolean.TRUE)
                .hasMigrateDone(Boolean.TRUE);

            Microtime avatar = user.getAvatar();
            MicrotimeAssert
                .assertThat(avatar)
                .hasSec(1388213197L)
                .hasUsec(738000L);

            ru.klavogonki.openapi.model.Car car = summary.getCar();
            CarAssert
                .assertThat(car)
                .isNotNull()
                .hasCar(Car.F1.id)
                .hasColor("#BF1300");

            Map<String, Integer> tuning = (Map<String, Integer>) car.getTuning();
            assertThat(tuning)
                .hasSize(2)
                .containsEntry("0", 3)
                .containsEntry("3", 0);
        }

        @Test
        @DisplayName("Test parsing an existing user summary for a user with a personal car id from a json file")
        void playerWithPersonalCarId() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-922.json");

            GetSummaryResponse summary = JacksonUtils.parse(file, GetSummaryResponse.class);
            logPlayerSummary(summary);

            GetSummaryResponseAssert
                .assertThat(summary)
                .hasErr(null)
                .hasIsOnline(Boolean.FALSE)
                .hasLevel(Rank.getLevel(Rank.maniac).intValue())
                .hasTitle(Rank.getDisplayName(Rank.maniac))
                .hasBlocked(0);

            GetSummaryUser user = summary.getUser();
            GetSummaryUserAssert
                .assertThat(user)
                .isNotNull()
                .hasId(922)
                .hasLogin("lovermann")
                .hasMigrateBookDone(Boolean.TRUE)
                .hasMigrateDone(Boolean.TRUE);

            Microtime avatar = user.getAvatar();
            MicrotimeAssert
                .assertThat(avatar)
                .hasSec(1388204842L)
                .hasUsec(467000L);

            ru.klavogonki.openapi.model.Car car = summary.getCar();
            CarAssert
                .assertThat(car)
                .isNotNull()
                .hasCar(Car.CARAVEL.personalId)
                .hasColor("#000000");

            List<Integer> tuning = (List<Integer>) car.getTuning();
            assertThat(tuning)
                .isEmpty();
        }

        @Test
        @DisplayName("Test parsing a brand new user summary from a json file")
        void brandNewPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-624511.json");

            GetSummaryResponse summary = JacksonUtils.parse(file, GetSummaryResponse.class);
            logPlayerSummary(summary);


            GetSummaryResponseAssert
                .assertThat(summary)
                .hasErr(null)
                .hasIsOnline(Boolean.TRUE)
                .hasLevel(Rank.getLevel(Rank.novice).intValue())
                .hasTitle(Rank.getDisplayName(Rank.novice))
                .hasBlocked(0);

            GetSummaryUser user = summary.getUser();
            GetSummaryUserAssert
                .assertThat(user)
                .isNotNull()
                .hasId(624511)
                .hasLogin("nosferatum0")
                .hasMigrateBookDone(null) // no migrated flags for the new users
                .hasMigrateDone(null); // no migrated flags for the new users

            Microtime avatar = user.getAvatar();
            MicrotimeAssert
                .assertThat(avatar)
                .isNull(); // new user without avatar -> no avatar date

            ru.klavogonki.openapi.model.Car car = summary.getCar();
            CarAssert
                .assertThat(car)
                .isNotNull()
                .hasCar(Car.ZAZ_965.id)
                .hasColor("#777777");

            List<Integer> tuning = (List<Integer>) car.getTuning();
            assertThat(tuning)
                .isEmpty();
        }

        @Test
        @DisplayName("Test parsing a summary of a klavomechanic with a hidden profile from a json file")
        void klavoMechanicWithHiddenProfile() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-21.json");

            GetSummaryResponse summary = JacksonUtils.parse(file, GetSummaryResponse.class);
            logPlayerSummary(summary);

            GetSummaryResponseAssert
                .assertThat(summary)
                .hasErr(null)
                .hasIsOnline(Boolean.FALSE)
                .hasLevel(Rank.getLevel(Rank.superman).intValue())
                .hasTitle(Rank.KLAVO_MECHANIC_TITLE)
                .hasBlocked(0);

            GetSummaryUser user = summary.getUser();
            GetSummaryUserAssert
                .assertThat(user)
                .isNotNull()
                .hasId(21)
                .hasLogin("Artch")
                .hasMigrateBookDone(Boolean.TRUE)
                .hasMigrateDone(Boolean.TRUE);

            Microtime avatar = user.getAvatar();
            MicrotimeAssert
                .assertThat(avatar)
                .hasSec(1388650379L)
                .hasUsec(184000L);

            ru.klavogonki.openapi.model.Car car = summary.getCar();
            CarAssert
                .assertThat(car)
                .isNotNull()
                .hasCar(Car.AUDI_TT.id)
                .hasColor("#893425");

            Map<String, Integer> tuning = (Map<String, Integer>) car.getTuning();
            assertThat(tuning)
                .hasSize(1)
                .containsEntry("1", 1);
        }

        @Test
        @DisplayName("Test parsing a non-existing user summary from a json file")
        void invalidPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-summary-30001.json");

            GetSummaryResponse summary = JacksonUtils.parse(file, GetSummaryResponse.class);
            logPlayerSummary(summary);

            GetSummaryResponseAssert
                .assertThat(summary)
                .hasErr(ApiErrors.INVALID_USER_ID_ERROR)
                .hasIsOnline(null)
                .hasLevel(null)
                .hasTitle(null)
                .hasBlocked(null);

            GetSummaryUser user = summary.getUser();
            GetSummaryUserAssert
                .assertThat(user)
                .isNull();

            ru.klavogonki.openapi.model.Car car = summary.getCar();
            CarAssert
                .assertThat(car)
                .isNull();
        }

        private void logPlayerSummary(final GetSummaryResponse summary) {
            logger.info("Player summary: ");
            logger.info(summary);
        }
    }

    @Nested
    @DisplayName("Test parsing of /get-index-data responses")
    class GetIndexData {

        @Test
        @DisplayName("Test parsing an existing user index data from a json file")
        void existingPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-242585.json");

            GetIndexDataResponse data = JacksonUtils.parse(file, GetIndexDataResponse.class);
            logPlayerIndexData(data);

            GetIndexDataResponseAssert
                .assertThat(data)
                .hasOk(ApiErrors.OK_CORRECT_VALUE)
                .hasErr(null);

            // bio
            Bio bio = data.getBio();
            BioAssert
                .assertThat(bio)
                .isNotNull()
                .hasUserId(242585);

            assertThat(bio.getOldText()).isNotBlank(); // huge html, no validation
            assertThat(bio.getText()).isNotBlank(); // huge html, no validation

            // stats
            GetIndexDataStats stats = data.getStats();
            GetIndexDataStatsAssert
                .assertThat(stats)
                .isNotNull()
                .hasAchievesCnt(225)
                .hasTotalNumRaces(60633)
                .hasBestSpeed(626)
                .hasRatingLevel(32)
                .hasFriendsCnt(102)
                .hasVocsCnt(109)
                .hasCarsCnt(33);

            // stats.registered
            Microtime registered = stats.getRegistered();
            MicrotimeAssert
                .assertThat(registered)
                .isNotNull()
                .hasSec(1297852113L)
                .hasUsec(0L);

            DateUtils.convertUserRegisteredTime(data);
        }

        @Test
        @DisplayName("Test parsing an existing user index data for a user with a personal car id from a json file")
        void playerWithPersonalCarId() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-922.json");

            GetIndexDataResponse data = JacksonUtils.parse(file, GetIndexDataResponse.class);
            logPlayerIndexData(data);

            GetIndexDataResponseAssert
                .assertThat(data)
                .hasOk(ApiErrors.OK_CORRECT_VALUE)
                .hasErr(null);

            // bio
            Bio bio = data.getBio();
            BioAssert
                .assertThat(bio)
                .isNotNull()
                .hasUserId(922);

            assertThat(bio.getOldText()).isNotBlank(); // huge html, no validation
            assertThat(bio.getText()).isNotBlank(); // huge html, no validation

            // bio.editedDate
            Microtime editedData = bio.getEditedDate();
            MicrotimeAssert
                .assertThat(editedData)
                .isNotNull()
                .hasSec(1508143960L)
                .hasUsec(314000L);

            // stats
            GetIndexDataStats stats = data.getStats();
            GetIndexDataStatsAssert
                .assertThat(stats)
                .isNotNull()
                .hasAchievesCnt(171)
                .hasTotalNumRaces(47887)
                .hasBestSpeed(554)
                .hasRatingLevel(37)
                .hasFriendsCnt(75)
                .hasVocsCnt(83)
                .hasCarsCnt(41);

            // stats.registered
            Microtime registered = stats.getRegistered();
            MicrotimeAssert
                .assertThat(registered)
                .isNotNull()
                .hasSec(1211400000L)
                .hasUsec(0L);

            DateUtils.convertUserRegisteredTime(data);
        }

        @Test
        @DisplayName("Test parsing a brand new user index data from a json file")
        void brandNewPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-624511.json");

            GetIndexDataResponse data = JacksonUtils.parse(file, GetIndexDataResponse.class);
            logPlayerIndexData(data);

            GetIndexDataResponseAssert
                .assertThat(data)
                .hasOk(ApiErrors.OK_CORRECT_VALUE)
                .hasErr(null);

            Bio bio = data.getBio();
            BioAssert
                .assertThat(bio)
                .isNotNull()
                .hasUserId(624511)
                .hasOldText(null) // no oldText for the new users
                .hasText(""); // empty and not null

            // stats
            GetIndexDataStats stats = data.getStats();
            GetIndexDataStatsAssert
                .assertThat(stats)
                .isNotNull()
                .hasAchievesCnt(0)
                .hasTotalNumRaces(0)
                .hasBestSpeed(null) // no races in "Normal" -> no best speed
                .hasRatingLevel(1) // user is level 1 from the start
                .hasFriendsCnt(0)
                .hasVocsCnt(0)
                .hasCarsCnt(1); // user has 1 car from the start

            // stats.registered
            Microtime registered = stats.getRegistered();
            MicrotimeAssert
                .assertThat(registered)
                .isNotNull()
                .hasSec(1607554944L)
                .hasUsec(0L);

            DateUtils.convertUserRegisteredTime(data);
        }

        @Test
        @DisplayName("Test parsing index data of a klavomechanic with a hidden profile from a json file. Request returns a \"hidden profile\" error.")
        void klavoMechanicWithHiddenProfile() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-21.json");

            GetIndexDataResponse data = JacksonUtils.parse(file, GetIndexDataResponse.class);
            logPlayerIndexData(data);

            GetIndexDataResponseAssert
                .assertThat(data)
                .hasOk(null)
                .hasErr(ApiErrors.HIDDEN_PROFILE_USER_ERROR);

            BioAssert
                .assertThat(data.getBio())
                .isNull();

            GetIndexDataStatsAssert
                .assertThat(data.getStats())
                .isNull();
        }

        @Test
        @DisplayName("Test parsing a non-existing user index data from a json file")
        void invalidPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-index-data-30001.json");

            GetIndexDataResponse data = JacksonUtils.parse(file, GetIndexDataResponse.class);
            logPlayerIndexData(data);

            GetIndexDataResponseAssert
                .assertThat(data)
                .hasOk(null)
                .hasErr(ApiErrors.INVALID_USER_ID_ERROR);

            BioAssert
                .assertThat(data.getBio())
                .isNull();

            GetIndexDataStatsAssert
                .assertThat(data.getStats())
                .isNull();
        }

        private void logPlayerIndexData(final GetIndexDataResponse data) {
            logger.info("Player index data: ");
            logger.info(data);
        }
    }

    @Nested
    @DisplayName("Test parsing of /get-stats-overview responses")
    class GetStatsOverview {
        @Test
        @DisplayName("Test parsing an existing user stats overview from a json file")
        void existingPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-242585.json");

            GetStatsOverviewResponse stats = JacksonUtils.parse(file, GetStatsOverviewResponse.class);
            logPlayerStatsOverview(stats);

            GetStatsOverviewResponseAssert
                .assertThat(stats)
                .hasOk(ApiErrors.OK_CORRECT_VALUE);

            Map<String, GetStatsOverviewGameType> gameTypes = stats.getGametypes();
            // todo: validate size

            // validate player stats in "normal"
            GetStatsOverviewGameType normalStats = gameTypes.get(StandardDictionary.normal.name());// no getValue, but toString works
            GetStatsOverviewGameTypeAssert
                .assertThat(normalStats)
                .isNotNull()
                .hasId(null)
                .hasName(StandardDictionary.getDisplayName(StandardDictionary.normal))
                .hasNumRaces(29445)
                .hasType(null) // type set for non-standard dictionaries only
                .hasRows(null) // rows set for non-standard dictionaries only
                .hasSymbols(null) // symbols set for non-standard dictionaries only
                .hasBookDone(null) // bookDone set for book dictionaries only
            ;

            GetStatsOverviewGameTypeInfo normalStatsInfo = normalStats.getInfo();
            GetStatsOverviewGameTypeInfoAssert
                .assertThat(normalStatsInfo)
                .hasId(1826608)
                .hasUserId(242585)
                .hasMode(VocabularyMode.NORMAL)
                .hasTexttype(StandardDictionary.getTextType(StandardDictionary.normal))
                .hasNumRaces(29445)
                .hasAvgSpeed(453.123)
                .hasBestSpeed(626)
                .hasAvgError(2.33079)
                .hasHaul(1027636)
                .hasQual(531)
                .hasDirty(0)
                .hasUpdated("2020-12-27 02:02:24")
            ;

            // todo: validate player stats in "chars" (negative text type)
            // todo: validate player stats in non-standard "words" dictionary
            // todo: validate player stats in non-standard "phrases" dictionary
            // todo: validate player stats in non-standard "texts" dictionary
            // todo: validate player stats in non-standard "url" dictionary
            // todo: validate player stats in non-standard "book" dictionary
            // todo: validate player stats in non-standard "generator" dictionary

            // validate recent game types
            List<String> recentGameTypes = stats.getRecentGametypes();
            assertThat(recentGameTypes)
                .hasSize(10)
                .containsExactly(
                    "normal",
                    "chars",
                    "voc-1432",
                    "voc-203",
                    "voc-13589",
                    "voc-21357",
                    "voc-13656",
                    "voc-1141",
                    "voc-17499",
                    "voc-103209"
                );
        }

        @Test
        @DisplayName("Test parsing a brand new user stats overview from a json file")
        void brandNewPlayer() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-624511.json");

            GetStatsOverviewResponse stats = JacksonUtils.parse(file, GetStatsOverviewResponse.class);
            logPlayerStatsOverview(stats);

            GetStatsOverviewResponseAssert
                .assertThat(stats)
                .hasOk(ApiErrors.OK_CORRECT_VALUE);

            Map<String, GetStatsOverviewGameType> gameTypes = stats.getGametypes();
            assertThat(gameTypes)
                .hasSize(1); // for new player, only empty result for "normal" is present

            // validate player stats in "normal"
            GetStatsOverviewGameType normalStats = gameTypes.get(StandardDictionary.normal.name());// no getValue, but toString works
            GetStatsOverviewGameTypeAssert
                .assertThat(normalStats)
                .isNotNull()
                .hasId(null)
                .hasName(StandardDictionary.getDisplayName(StandardDictionary.normal))
                .hasNumRaces(0)
                .hasType(null) // type set for non-standard dictionaries only
                .hasRows(null) // rows set for non-standard dictionaries only
                .hasSymbols(null) // symbols set for non-standard dictionaries only
                .hasBookDone(null) // bookDone set for book dictionaries only
            ;

            // minimal 0/null info for a brand new user
            GetStatsOverviewGameTypeInfo normalStatsInfo = normalStats.getInfo();
            GetStatsOverviewGameTypeInfoAssert
                .assertThat(normalStatsInfo)
                .hasId(30914229)
                .hasUserId(624511)
                .hasMode(VocabularyMode.NORMAL)
                .hasTexttype(StandardDictionary.getTextType(StandardDictionary.normal))
                .hasNumRaces(0)
                .hasAvgSpeed(0d)
                .hasBestSpeed(null)
                .hasAvgError(0d)
                .hasHaul(0)
                .hasQual(0)
                .hasDirty(0)
                .hasUpdated(null)
            ;

            // validate recent game types - empty for the new playre
            List<String> recentGameTypes = stats.getRecentGametypes();
            assertThat(recentGameTypes)
                .isEmpty();
        }

        @Test
        @DisplayName("Test parsing stats overview for a player who is hidden or denied the access to his/her statistics")
        void permissionBlocked() {
            File file = TestUtils.readResourceFile("ru/klavogonki/kgparser/jsonParser/get-stats-overview-21.json");

            GetStatsOverviewResponse stats = JacksonUtils.parse(file, GetStatsOverviewResponse.class);
            logPlayerStatsOverview(stats);

            GetStatsOverviewResponseAssert
                .assertThat(stats)
                .isNotNull()
                .hasErr(ApiErrors.PERMISSION_BLOCKED_ERROR)
                .hasOk(null);

            assertThat(stats.getGametypes())
                .isEmpty();

            assertThat(stats.getRecentGametypes())
                .isEmpty();
        }

        private void logPlayerStatsOverview(final GetStatsOverviewResponse response) {
            logger.info("Player stats overview: ");
            logger.info(response);
        }
    }
}
