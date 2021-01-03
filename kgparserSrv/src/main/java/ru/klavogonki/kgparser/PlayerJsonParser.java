package ru.klavogonki.kgparser;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.jsonParser.ApiErrors;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.util.DateUtils;
import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetIndexDataStats;
import ru.klavogonki.openapi.model.GetStatsOverviewGameType;
import ru.klavogonki.openapi.model.GetStatsOverviewGameTypeInfo;
import ru.klavogonki.openapi.model.GetStatsOverviewResponse;
import ru.klavogonki.openapi.model.GetSummaryResponse;
import ru.klavogonki.openapi.model.GetSummaryUser;
import ru.klavogonki.openapi.model.Microtime;
import ru.klavogonki.openapi.model.NonStandardVocabularyType;
import ru.klavogonki.openapi.model.VocabularyMode;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Parses the json files saved by {@link PlayerDataDownloader}.
 */
public class PlayerJsonParser {
    private static final Logger logger = LogManager.getLogger(PlayerJsonParser.class);

    public static final int REQUIRED_ARGUMENTS_COUNT = 4;

    static class ParserException extends RuntimeException {
        public ParserException(final String message, final Object... messageArguments) {
            super(String.format(message, messageArguments));
        }

        public ParserException(final String message) {
            super(message);
        }

        public ParserException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] args) {
        // todo: pass a path to a json file with config instead

        if (args.length != REQUIRED_ARGUMENTS_COUNT) {
            // todo: use logger instead of System.out??
            System.out.printf("Usage: %s <rootJsonDir> <minPlayerId> <maxPlayerId> <yyyy-MM-dd HH-mm-ss> %n", PlayerJsonParser.class.getSimpleName());
            return;
        }

        PlayerDataDownloader.Config config = PlayerDataDownloader.Config.parseFromArguments(args);
        config.setStartDate(args[3]);
        config.log();

        List<PlayerJsonData> players = new ArrayList<>();
        List<Integer> nonExistingPlayerIds = new ArrayList<>();

        BiConsumer<Integer, Optional<PlayerJsonData>> playerHandler = (playerId, playerOptional) -> {
            if (playerOptional.isPresent()) {
                PlayerJsonData player = playerOptional.get();
                players.add(player);

                if (player.summary.getErr().equals(ApiErrors.INVALID_USER_ID_ERROR)) {
                    nonExistingPlayerIds.add(playerId);
                }
            }
            else {
                nonExistingPlayerIds.add(playerId);
            }
        };

        handlePlayers(config, playerHandler);

        logger.info("=======================================================");
        logger.info("Total player ids handled: {}", config.maxPlayerId - config.minPlayerId + 1);
        logger.info("Total existing players parsed: {}", players.size());
        logger.info("Total non existing players: {}", nonExistingPlayerIds.size());

        // todo: validate over all users
        // todo: all users must have unique id
        // todo: should login be unique over all users?
    }

    public static void handlePlayers(final PlayerDataDownloader.Config config, final BiConsumer<Integer, Optional<PlayerJsonData>> playerHandler) {
        int totalPlayersToHandle = config.maxPlayerId - config.minPlayerId + 1;

        for (int playerId = config.minPlayerId; playerId <= config.maxPlayerId; playerId++) {
            logger.info("=======================================================");
            int indexOfCurrentPlayer = playerId - config.minPlayerId + 1; // starting from 1
            logger.info("Handling player {} (player {} / {})...", playerId, indexOfCurrentPlayer, totalPlayersToHandle);

            File summaryFile = new File(config.getPlayerSummaryFilePath(playerId));
            File indexDataFile = new File(config.getPlayerIndexDataFilePath(playerId));
            File statsOverviewFile = new File(config.getStatsOverviewFilePath(playerId));

            Optional<PlayerJsonData> playerOptional = readPlayerData(config.startDate, playerId, summaryFile, indexDataFile, statsOverviewFile);

            playerHandler.accept(playerId, playerOptional);
        }
    }

    static Optional<PlayerJsonData> readPlayerData(
        final LocalDateTime importDate,
        final int playerId,
        final File summaryFile,
        final File indexDataFile,
        final File statsOverviewFile
    ) {
        String summaryFilePath = summaryFile.getPath();
        String indexDataFilePath = indexDataFile.getPath();
        String statsOverviewFilePath = statsOverviewFile.getPath();

        // parse summary file
        GetSummaryResponse summary = JacksonUtils.parse(summaryFile, GetSummaryResponse.class);

        // parse index-data file
        GetIndexDataResponse indexData = JacksonUtils.parse(indexDataFile, GetIndexDataResponse.class);

        // parse stats-overview file
        GetStatsOverviewResponse statsOverview = JacksonUtils.parse(statsOverviewFile, GetStatsOverviewResponse.class);

        // validate expected data
        // todo: use some validation framework instead of this manual code hell
        validate(playerId, summary, summaryFilePath);
        validate(playerId, summary.getBlocked(), indexData, indexDataFilePath);
        validate(playerId, statsOverview, statsOverviewFilePath);
        // todo: we can check that summary.stats.totalNumRaces = sum(stats.gametypes.num_races) , but this is not very reliable since data can change between 2 requests

        // check whether this is a parse error

        // validate erratic cases
        boolean isErrorCase = validateErrorCase(summaryFilePath, indexDataFilePath, summary, indexData);
        if (isErrorCase) {
            // both files contain same errors -> return empty result, there is no such player
            logger.info("Player with id = {} is not found according to both summary file {} and index data file {}.", playerId, summaryFilePath, indexDataFilePath);
            PlayerJsonData result = new PlayerJsonData(importDate, summary, indexData, statsOverview);
            return Optional.of(result); // we will save not found players as well, for the database consistency (and FGJ as well!)
        }

        // player validation passed -> return parsed player object
        logger.info("Player {} was successfully parsed from summary file {} and index data file {}.", playerId, summaryFilePath, indexDataFilePath);
        PlayerJsonData result = new PlayerJsonData(importDate, summary, indexData, statsOverview);
        return Optional.of(result);
    }

    private static boolean validateErrorCase( // true if user does not exist, false if user exists
        final String summaryFilePath,
        final String indexDataFilePath,
        final GetSummaryResponse summary,
        final GetIndexDataResponse data
    ) {
        String summaryError = summary.getErr();
        String indexDataError = data.getErr();

        if (StringUtils.isBlank(summaryError) && StringUtils.isBlank(indexDataError)) {
            logger.info("Neither summary file {} nor index data file {} contain an error.", summaryFilePath, indexDataFilePath);
            return false;
        }

        if (StringUtils.isBlank(summaryError) && indexDataError.equals(ApiErrors.HIDDEN_PROFILE_USER_ERROR)) { // hidden profile -> ok user, there will be no index data
            logger.info("Summary file {} contains no error, index data file {} contain a error. User exists, but will have no index data.", summaryFilePath, indexDataFilePath);
            return false;
        }

        if (StringUtils.isNotBlank(summaryError) && StringUtils.isBlank(indexDataError)) { // error only in summary
            throw new ParserException(
                "Summary file %s contains error \"%s\", but index data file %s contains no error",
                summaryFilePath,
                summaryError,
                indexDataFilePath
            );
        }

        if (StringUtils.isNotBlank(indexDataError) && StringUtils.isBlank(summaryError)) { // error only in index-data
            // https://klavogonki.ru/u/#/161997/ - possible case: /get-summary works, /get-index-data fails. Not blocked.
            logger.warn(
                "Summary file {} contains no error, but index data file {} contains error \"{}\". Player: {}",
                summaryFilePath,
                indexDataFilePath,
                indexDataError,
                summary.getUser().getId()
            );

            return false; // todo: a very-tricky case, but the user exists and his/her page can be accessed

/*
            throw new ParserException(
                "Summary file %s contains no error, but index data file %s contains error \"%s\"",
                summaryFilePath,
                indexDataFilePath,
                data.err
            );
*/
        }

        if (!summaryError.equals(indexDataError)) { // different errors in summary and index-data files
            throw new ParserException(
                "Summary file %s contains error \"%s\", but index data file %s contains different error \"%s\"",
                summaryFilePath,
                summaryError,
                indexDataFilePath,
                indexDataError
            );
        }

        // both files contain same errors -> return empty result, there is no such player
        logger.info("Both summary file {} and index data file {} contain the same correct error \"{}\".", summaryFilePath, indexDataFilePath, summaryError);
        return true;
    }

    private static void validate(int playerId, GetSummaryResponse summary, String summaryFilePath) {
        String err = summary.getErr();
        if (StringUtils.isNotBlank(err)) { // error case
            if (!err.equals(ApiErrors.INVALID_USER_ID_ERROR)) {
                throw new ParserException("Summary file %s: Unknown error: %s", summaryFilePath, err);
            }

            return;
        }

        // no-error

        // isOnline
        if (summary.getIsOnline() == null) {
            throw new ParserException("Summary file %s: summary.isOnline is null", summaryFilePath);
        }

        // level
        if (summary.getLevel() == null) {
            throw new ParserException("Summary file %s: summary.level is null", summaryFilePath);
        }

        Rank rank = Rank.getRank(summary.getLevel());// this method will throw on incorrect input

        // level title
        String title = summary.getTitle();
        if (StringUtils.isBlank(title)) {
            throw new ParserException("Summary file %s: summary.title is null or blank", summaryFilePath);
        }

        String expectedRankTitle = Rank.getDisplayName(rank);
        if (!title.equals(expectedRankTitle) && !title.equals(Rank.KLAVO_MECHANIC_TITLE)) {
            throw new ParserException("Summary file %s: summary.title has incorrect value %s, must be %s", summaryFilePath, title, expectedRankTitle);
        }

        // blocked
        Integer blocked = summary.getBlocked();
        if (
               (blocked == null)
            || ((blocked != 0) && (blocked != 1) && (blocked != 4))
        ) {
            // https://klavogonki.ru/u/#/141327/ - blocked == 1
            // https://klavogonki.ru/u/#/142478/ - blocked == 4
            throw new ParserException("Summary file %s: summary.blocked has incorrect value: %s", summaryFilePath, blocked);
        }

        // user
        GetSummaryUser user = summary.getUser();
        if (user == null) {
            throw new ParserException("Summary file %s: summary.user is null", summaryFilePath);
        }

        Integer userId = user.getId();
        if (userId != playerId) {
            throw new ParserException("Summary file %s contains incorrect summary.user.id %s. Expected playerId: %s", summaryFilePath, userId, playerId);
        }

        String login = user.getLogin();
        if (login == null) { // login CAN be blank, see https://klavogonki.ru/u/#/109842/
            throw new ParserException("Summary file %s: summary.user.login is null", summaryFilePath);
        }

        if (StringUtils.isBlank(login)) { // login CAN be blank, see https://klavogonki.ru/u/#/109842/
            logger.warn("Summary file {}: summary.user.login is blank: \"{}\".", summaryFilePath, login);
        }

        // car
        ru.klavogonki.openapi.model.Car car = summary.getCar();
        if (car == null) {
            throw new ParserException("Summary file %s: summary.car is null", summaryFilePath);
        }

        Car.getById(car.getCar()); // this method will throw on incorrect input

        if (StringUtils.isBlank(car.getColor())) {
            throw new ParserException("Summary file %s: summary.car.color is null or blank", summaryFilePath);
        }
    }

    private static void validate(int playerId, Integer blocked, GetIndexDataResponse data, String indexDataFilePath) {
        String err = data.getErr();
        if (StringUtils.isNotBlank(err)) { // error case
            if (
                !err.equals(ApiErrors.INVALID_USER_ID_ERROR)
                && !err.equals(ApiErrors.HIDDEN_PROFILE_USER_ERROR)
                && !err.equals(ApiErrors.MONGO_REFS_ERROR_USER_498727)
            ) {
                throw new ParserException("Index data file %s: Unknown error: %s", indexDataFilePath, err);
            }

            return;
        }

        // non-error case
        // ok
        Integer ok = data.getOk();
        if (ok != ApiErrors.OK_CORRECT_VALUE) { // should be null-safe
            throw new ParserException(
                "Index data file %s contains no error, but data.ok = %s. It must be = %s",
                indexDataFilePath,
                ok,
                ApiErrors.OK_CORRECT_VALUE
            );
        }

        // bio
        if (data.getBio() == null) {
            throw new ParserException("Index data file %s: data.bio is null", indexDataFilePath);
        }

        if (data.getBio().getUserId() != playerId) {
            throw new ParserException("Index data file %s contains incorrect data.bio.userId %s. Expected playerId: %s", indexDataFilePath, data.getBio().getUserId(), playerId);
        }

        // bio.oldText can be null / empty - no need to validate
        // bio.oldTextRemoved can be null / empty - no need to validate
        // bio.text can be null / empty - see https://klavogonki.ru/u/#/368664/ - no need to validate

        // stats
        GetIndexDataStats stats = data.getStats();

        if (stats == null) {
            throw new ParserException("Index data file %s: data.stats is null", indexDataFilePath);
        }

        // stats.registered
        Microtime registered = stats.getRegistered();
        if (registered == null) {
            throw new ParserException("Index data file %s: data.registered is null", indexDataFilePath);
        }

        if (registered.getSec() == null) {
            throw new ParserException("Index data file %s: data.registered.sec is null", indexDataFilePath);
        }

        // todo: validate that (data.stats.registered.sec + data.stats.registered.usec) can parse to date
        if (registered.getSec() <= 0) { // todo: compare to minimal unix date
            if ((blocked != null) && (blocked == 1)) { // todo: maybe other blocked != 0 values can also contain crazy register dates
                logger.warn("Index data file {}: User {} is blocked, data.registered.sec has invalid value: {}", indexDataFilePath, playerId, registered.getSec());
            }
            else {
                throw new ParserException("Index data file %s: User is not blocked, but data.registered.sec has invalid value: %d", indexDataFilePath, registered.getSec());
            }
        }

        if (registered.getUsec() == null) {
            throw new ParserException("Index data file %s: data.registered.usec is null", indexDataFilePath);
        }

        if (registered.getUsec() < 0) { // todo: validate range, from 0 to 1000?
            throw new ParserException("Index data file %s: data.stats.registered.usec has invalid value: %d", indexDataFilePath, registered.getUsec());
        }

        // statistics in stats
        Integer achievementsCount = stats.getAchievesCnt();
        if (achievementsCount == null || achievementsCount < 0) {
            throw new ParserException("Index data file %s: data.stats.achievementsCount has invalid value: %d", indexDataFilePath, achievementsCount);
        }

        Integer totalRacesCount = stats.getTotalNumRaces();
        if (totalRacesCount == null || totalRacesCount < 0) {
            throw new ParserException("Index data file %s: data.stats.totalRacesCount has invalid value: %d", indexDataFilePath, totalRacesCount);
        }

        // best speed can be null for player without races in Normal
        Integer bestSpeed = stats.getBestSpeed();
        if (bestSpeed != null && bestSpeed <= 0) {
            throw new ParserException("Index data file %s: data.stats.bestSpeed has invalid value: %d", indexDataFilePath, bestSpeed);
        }

        Integer ratingLevel = stats.getRatingLevel();
        if (ratingLevel == null || ratingLevel < 1) { // todo: move 1 to constant in Player etc
            throw new ParserException("Index data file %s: data.stats.ratingLevel has invalid value: %d", indexDataFilePath, ratingLevel);
        }

        Integer friendsCount = stats.getFriendsCnt();
        if (friendsCount == null || friendsCount < 0) {
            throw new ParserException("Index data file %s: data.stats.friendsCount has invalid value: %d", indexDataFilePath, friendsCount);
        }

        Integer vocabulariesCount = stats.getVocsCnt();
        if (vocabulariesCount == null || vocabulariesCount < 0) {
            throw new ParserException("Index data file %s: data.stats.vocabulariesCount has invalid value: %d", indexDataFilePath, vocabulariesCount);
        }

        // cannot own no cars or more than total cars
        Integer carsCount = stats.getCarsCnt();
        if (carsCount == null || carsCount < 1 || carsCount > Car.values().length) {
            throw new ParserException("Index data file %s: data.stats.carsCount has invalid value: %d", indexDataFilePath, carsCount);
        }
    }

    private static void validate(int playerId, GetStatsOverviewResponse response, String statsOverviewFilePath) {
        String err = response.getErr();

        if (StringUtils.isNotBlank(err)) {
            if (!err.equals(ApiErrors.INVALID_USER_ID_ERROR) && !err.equals(ApiErrors.PERMISSION_BLOCKED_ERROR)) {
                throw new ParserException("Stats overview file %s: Unknown error: %s", statsOverviewFilePath, err);
            }

            logger.debug("Stats overview file {} contains a valid error \"{}\".", statsOverviewFilePath, err);

            return;
        }

        Map<String, GetStatsOverviewGameType> gameTypes = response.getGametypes();
        for (Map.Entry<String, GetStatsOverviewGameType> entry : gameTypes.entrySet()) {
            String vocabularyCode = entry.getKey();

            GetStatsOverviewGameType vocabularyStats = entry.getValue();

            if (!Dictionary.isValid(vocabularyCode)) { // we have dictionaries with "" code
                logger.warn("Stats overview file {}: Invalid vocabulary code: {}. Cannot validate it as standard or non-standard.", statsOverviewFilePath, vocabularyCode);
                // todo: we may write some heuristic determination whether this dictionary is standard or non-standard
//                throw new ParserException("Stats overview file %s: Incorrect vocabulary code in gametypes: \"%s\"", statsOverviewFilePath, vocabularyCode);
            }
            else if (Dictionary.isStandard(vocabularyCode)) {
                validateCommon(statsOverviewFilePath, vocabularyCode, vocabularyStats);
                validateStandardVocabulary(statsOverviewFilePath, vocabularyCode, vocabularyStats);
                validateInfo(playerId, statsOverviewFilePath, vocabularyCode, vocabularyStats);
            }
            else {
                validateCommon(statsOverviewFilePath, vocabularyCode, vocabularyStats);
                validateNonStandardVocabulary(statsOverviewFilePath, vocabularyCode, vocabularyStats);
                validateInfo(playerId, statsOverviewFilePath, vocabularyCode, vocabularyStats);
            }
        }

        List<String> recentGameTypes = response.getRecentGametypes();


        // todo: recent gametypes - all types must be present in gametypes
        // todo: recent gametypes - size from 0 to 10
    }

    private static void validateCommon(final String statsOverviewFilePath, final String vocabularyCode, final GetStatsOverviewGameType vocabularyStats) {
        if (StringUtils.isBlank(vocabularyStats.getName())) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: name is not present.", statsOverviewFilePath, vocabularyCode);
        }

        Integer numRaces = vocabularyStats.getNumRaces();
        if (numRaces == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: numRaces is not present.", statsOverviewFilePath, vocabularyCode);
        }

        if (numRaces < 0) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: numRaces %d is negative.", statsOverviewFilePath, vocabularyCode, numRaces);
        }
    }

    private static void validateStandardVocabulary(final String statsOverviewFilePath, final String vocabularyCode, final GetStatsOverviewGameType vocabularyStats) {
        Integer id = vocabularyStats.getId();
        if (id != null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is standard, but id = %d is present.", statsOverviewFilePath, vocabularyCode, id);
        }

        NonStandardVocabularyType type = vocabularyStats.getType();
        if (type != null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is standard, but type = %s is present.", statsOverviewFilePath, vocabularyCode, type);
        }

        Integer symbols = vocabularyStats.getSymbols();
        if (symbols != null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is standard, but symbols = %d is present.", statsOverviewFilePath, vocabularyCode, symbols);
        }

        Integer rows = vocabularyStats.getRows();
        if (rows != null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is standard, but rows = %d is present.", statsOverviewFilePath, vocabularyCode, rows);
        }

        Boolean bookDone = vocabularyStats.getBookDone();
        if (bookDone != null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is standard, but bookDone = %s is present.", statsOverviewFilePath, vocabularyCode, bookDone);
        }
    }

    private static void validateNonStandardVocabulary(final String statsOverviewFilePath, final String vocabularyCode, final GetStatsOverviewGameType vocabularyStats) {
        // id
        Integer id = vocabularyStats.getId();
        if (id == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but id is not present.", statsOverviewFilePath, vocabularyCode);
        }

        int expectedId = Dictionary.getDictionaryId(vocabularyCode);
        if (id != expectedId) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but id = %d is not equal to expectedId = %d.", statsOverviewFilePath, vocabularyCode, id, expectedId);
        }

        // type
        NonStandardVocabularyType type = vocabularyStats.getType();
        if (type == null) {
            // get-stats-overview-80523.json, voc-107263, type: "" (deleted vocabulary of player fischca)
            logger.warn("Stats overview file {}: Vocabulary {} is non-standard, but type is not present.", statsOverviewFilePath, vocabularyCode);

//            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but type is not present.", statsOverviewFilePath, vocabularyCode);
        }

        // symbols
        Integer symbols = vocabularyStats.getSymbols();
        if (symbols == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but symbols is not present.", statsOverviewFilePath, vocabularyCode);
        }

        // ! there exist vocabularies with symbols = 0, e.g. voc-73806 (words), voc-62586 (texts)
//        if (symbols <= 0) {
//            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but symbols %d is non-positive.", statsOverviewFilePath, vocabularyCode, symbols);
//        }
        if (symbols < 0) {
            // https://klavogonki.ru/vocs/186079 - has -56 symbols!
            logger.warn("Stats overview file {}: Vocabulary {} is non-standard, but symbols {} is negative.", statsOverviewFilePath, vocabularyCode, symbols);

//            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but symbols %d is negative.", statsOverviewFilePath, vocabularyCode, symbols);
        }
        if (symbols == 0) {
            logger.warn("Stats overview file {}: Vocabulary {} is non-standard, but symbols {} is 0.", statsOverviewFilePath, vocabularyCode, symbols);
        }

        Integer rows = vocabularyStats.getRows();
        if (rows == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but rows is not present.", statsOverviewFilePath, vocabularyCode);
        }

        // ! there exist vocabularies rows = 0, e.g. voc-53 (RSS)
//        if (rows <= 0) {
//            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but rows %d is non-positive.", statsOverviewFilePath, vocabularyCode, rows);
//        }
        if (rows < 0) {
            throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard, but rows %d is negative.", statsOverviewFilePath, vocabularyCode, rows);
        }
        if (rows == 0) {
            logger.warn("Stats overview file {}: Vocabulary {} is non-standard, but rows {} is 0.", statsOverviewFilePath, vocabularyCode, rows);
        }

        // book_done - must be present for books only
        Boolean bookDone = vocabularyStats.getBookDone();
        if (type == NonStandardVocabularyType.BOOK) { // book vocabulary -> book_done must be present
            if (bookDone == null) {
                throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard and has type = %s, but bookDone is not present.", statsOverviewFilePath, vocabularyCode, type);
            }
        }
        else { // non-book vocabulary -> book_done must NOT be present
            if (bookDone != null) {
                throw new ParserException("Stats overview file %s: Vocabulary %s is non-standard and has type = %s, but bookDone = %s is present.", statsOverviewFilePath, vocabularyCode, type, bookDone);
            }
        }
    }

    private static void validateInfo(final int playerId, final String statsOverviewFilePath, final String vocabularyCode, final GetStatsOverviewGameType vocabularyStats) {
        GetStatsOverviewGameTypeInfo info = vocabularyStats.getInfo();

        if (info == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info is not present.", statsOverviewFilePath, vocabularyCode);
        }

        // id
        Integer id = info.getId();
        if (id == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.id is not present.", statsOverviewFilePath, vocabularyCode);
        }

        if (id <= 0) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.id %d is non-positive.", statsOverviewFilePath, vocabularyCode, id);
        }

        // user_id
        Integer userId = info.getUserId();
        if (userId == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.userId is not present.", statsOverviewFilePath, vocabularyCode);
        }

        if (userId != playerId) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.userId = %d is not equal to playerId = %d.", statsOverviewFilePath, vocabularyCode, userId, playerId);
        }

        // mode
        VocabularyMode mode = info.getMode();
        if (mode == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.mode is not present.", statsOverviewFilePath, vocabularyCode);
        }

        DictionaryMode expectedMode = DictionaryMode.getDictionaryMode(vocabularyCode);

        // todo: mapper conversion VocabularyMode -> DictionaryMode?
        if (!mode.toString().equals(expectedMode.toString())) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.mode %s is not equal to expected mode = %d.", statsOverviewFilePath, vocabularyCode, mode, expectedMode);
        }

        // texttype
        Integer textType = info.getTexttype();
        if (textType == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.texttype is not present.", statsOverviewFilePath, vocabularyCode);
        }

        int expectedTextType = Dictionary.getTextType(vocabularyCode);
        if (!textType.equals(expectedTextType)) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.texttype %s is not equal to expected textType = %d.", statsOverviewFilePath, vocabularyCode, textType, expectedTextType);
        }

        // num_races
        Integer numRaces = info.getNumRaces();
        if (numRaces == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.num_races is not present.", statsOverviewFilePath, vocabularyCode);
        }

        Integer expectedNumRaces = vocabularyStats.getNumRaces();
        if (!numRaces.equals(expectedNumRaces)) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.num_races %s is not equal to expected num_races = %d.", statsOverviewFilePath, vocabularyCode, numRaces, expectedNumRaces);
        }

        // avg_speed
        Double avgSpeed = info.getAvgSpeed();
        if (avgSpeed == null) {
            logger.warn("Stats overview file {}: Vocabulary {}: info.avg_speed is not present. NumRaces = {}.", statsOverviewFilePath, vocabularyCode, numRaces);

            // player 24646 has ugly data: 1 race in marathon, but avg_speed = 0 and avg_error = 0 // todo: maybe validate that numRaces <= 1 and voc in (normal, marathon)
/*
            if (!vocabularyCode.equals(StandardDictionary.normal.name())) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.avg_speed is not present and vocabularyCode != %s.", statsOverviewFilePath, vocabularyCode, StandardDictionary.normal);
            }

            if (numRaces > 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.avg_speed %s is null, but numRaces = %d != 0.", statsOverviewFilePath, vocabularyCode, avgSpeed, numRaces);
            }
*/
        }
        else { // bestSpeed != 0
            if (avgSpeed < 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.avg_speed %s is negative.", statsOverviewFilePath, vocabularyCode, avgSpeed);
            }
        }

        // best_speed - nullable
        Integer bestSpeed = info.getBestSpeed();
        if (bestSpeed == null) {
            logger.warn("Stats overview file {}: Vocabulary {}: info.best_speed is not present. NumRaces = {}.", statsOverviewFilePath, vocabularyCode, numRaces);

            // player 215941 has ugly data: 1 race in "voc-11315", but avg_speed = null, avg_error = null, best_speed = null  // todo: maybe validate that numRaces <= 1 and voc in (voc-11315)

/*
            if (!vocabularyCode.equals(StandardDictionary.normal.name())) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.best_speed is not present and vocabularyCode != %s.", statsOverviewFilePath, vocabularyCode, StandardDictionary.normal);
            }

            if (numRaces > 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.best_speed %s is null, but numRaces = %d != 0.", statsOverviewFilePath, vocabularyCode, bestSpeed, numRaces);
            }
*/
        }
        else { // bestSpeed != 0
            if (bestSpeed < 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.best_speed %s is negative.", statsOverviewFilePath, vocabularyCode, bestSpeed);
            }
        }

        // avg_error - nullable, eg players 142478, 141327, 109842
        Double avgError = info.getAvgError();
        if (avgError == null) {
            logger.warn("Stats overview file {}: Vocabulary {}: info.avg_error is not present. NumRaces = {}.", statsOverviewFilePath, vocabularyCode, numRaces);

            // player 24646 has ugly data: 1 race in marathon, but avg_speed = 0 and avg_error = 0 // todo: maybe validate that numRaces <= 1 and voc in (normal, marathon)
/*
            if (!vocabularyCode.equals(StandardDictionary.normal.name())) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.avg_error is not present and vocabularyCode != %s.", statsOverviewFilePath, vocabularyCode, StandardDictionary.normal);
            }

            if (numRaces > 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.avg_error %s is null, but numRaces = %d != 0.", statsOverviewFilePath, vocabularyCode, bestSpeed, numRaces);
            }
*/
        }
        else { // avgSpeed != 0
            if (avgError < 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.avgError %s is negative.", statsOverviewFilePath, vocabularyCode, avgError);
            }
        }

        // haul
        Integer haul = info.getHaul();
        if (haul == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.haul is not present.", statsOverviewFilePath, vocabularyCode);
        }

        if (haul < 0) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.haul %s is negative.", statsOverviewFilePath, vocabularyCode, haul);
        }

        // qual
        Integer qual = info.getQual();
        if (qual == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.qual is not present.", statsOverviewFilePath, vocabularyCode);
        }

        if (qual < 0) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.qual %s is negative.", statsOverviewFilePath, vocabularyCode, qual);
        }

        // dirty
        Integer dirty = info.getDirty();
        if (dirty == null) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.dirty is not present.", statsOverviewFilePath, vocabularyCode);
        }

        if (dirty < 0) {
            throw new ParserException("Stats overview file %s: Vocabulary %s: info.dirty %s is negative.", statsOverviewFilePath, vocabularyCode, dirty);
        }

        // updated - nullable
        String updated = info.getUpdated();
        if (updated == null) {
            if (!vocabularyCode.equals(StandardDictionary.normal.name())) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.updated is not present and vocabularyCode != %s.", statsOverviewFilePath, vocabularyCode, StandardDictionary.normal);
            }

            if (numRaces > 0) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.updated %s is null, but numRaces = %d != 0.", statsOverviewFilePath, vocabularyCode, updated, numRaces);
            }
        }
        else { // updated != 0
            try {
                DateUtils.parseLocalDateTimeWithUiDateFormat(updated);
            }
            catch (Exception e) {
                throw new ParserException("Stats overview file %s: Vocabulary %s: info.updated %s cannot be parsed to LocalDateTime using %s format.", statsOverviewFilePath, vocabularyCode, updated, DateUtils.DATE_TIME_FORMAT_FOR_UI);
            }
        }
    }
}
