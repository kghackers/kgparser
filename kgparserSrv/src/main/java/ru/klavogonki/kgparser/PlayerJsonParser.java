package ru.klavogonki.kgparser;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;

import java.io.File;
import java.util.Optional;

/**
 * Parses the json files saved by {@link PlayerSummaryDownloader}.
 */
public class PlayerJsonParser {
    private static final Logger logger = LogManager.getLogger(PlayerJsonParser.class);

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
        // todo: get date as input
        // todo: get rootDir as input
        // todo: get fromPlayer, toPlayer as input

        // todo: iterate players

        // todo: check on brand new players: what will be the stats?
/*
        int playerId = ;
        File summaryFile = ;
        File indexDataFile = ;

        Optional<PlayerJsonData> player = readPlayerData(playerId, summaryFile, indexDataFile);
*/

        // todo: validate over all users
        // todo: all users must have unique id
        // todo: should login be unique over all users?
    }

    static Optional<PlayerJsonData> readPlayerData(final int playerId, final File summaryFile, final File indexDataFile) {
        String summaryFilePath = summaryFile.getPath();
        String indexDataFilePath = indexDataFile.getPath();

        // parse summary file
        PlayerSummary summary = JacksonUtils.parse(summaryFile, PlayerSummary.class);

        // parse index-data file
        PlayerIndexData data = JacksonUtils.parse(indexDataFile, PlayerIndexData.class);

        // validate expected data
        // todo: use some validation framework instead of this manual code hell
        validate(playerId, summary, summaryFilePath);
        validate(playerId, data, indexDataFilePath);

        // check whether this is a parse error

        // validate erratic cases
        boolean isErrorCase = validateErrorCase(summaryFilePath, indexDataFilePath, summary, data);
        if (isErrorCase) {
            // both files contain same errors -> return empty result, there is no such player
            logger.info("Player with id = {} is not found according to both summary file {} and index data file {}.", playerId, summaryFilePath, indexDataFilePath);
            return Optional.empty();
        }

        // player validation passed -> return parsed player object
        logger.info("Player {} was successfully parsed from summary file {} and index data file {}.", playerId, summaryFilePath, indexDataFilePath);
        PlayerJsonData result = new PlayerJsonData(summary, data);
        return Optional.of(result);
    }

    private static boolean validateErrorCase( // true if error, false if no error
        final String summaryFilePath,
        final String indexDataFilePath,
        final PlayerSummary summary,
        final PlayerIndexData data
    ) {
        if (StringUtils.isBlank(summary.err) && StringUtils.isBlank(data.err)) {
            logger.info("Neither summary file {} nor index data file {} contain an error.", summaryFilePath, indexDataFilePath);
            return false;
        }

        if (StringUtils.isNotBlank(summary.err) && StringUtils.isBlank(data.err)) { // error only in summary
            throw new ParserException(
                "Summary file %s contains error \"%s\", but index data file %s contains no error",
                summaryFilePath,
                summary.err,
                indexDataFilePath
            );
        }

        if (StringUtils.isNotBlank(data.err) && StringUtils.isBlank(summary.err)) { // error only in index-data
            throw new ParserException(
                "Summary file %s contains no error, but index data file %s contains error \"%s\"",
                summaryFilePath,
                indexDataFilePath,
                data.err
            );
        }

        if (!summary.err.equals(data.err)) { // different errors in summary and index-data files
            throw new ParserException(
                "Summary file %s contains error \"%s\", but index data file %s contains different error \"%s\"",
                summaryFilePath,
                summary.err,
                indexDataFilePath,
                data.err
            );
        }

        // both files contain same errors -> return empty result, there is no such player
        logger.info("Both summary file {} and index data file {} contain the same correct error \"{}\".", summaryFilePath, indexDataFilePath, summary.err);
        return true;
    }

    private static void validate(int playerId, PlayerSummary summary, String summaryFilePath) {
        if (StringUtils.isNotBlank(summary.err)) { // error case
            if (!summary.err.equals(PlayerSummary.INVALID_USER_ID_ERROR)) {
                throw new ParserException("Summary file %s: Unknown error: %s", summaryFilePath, summary.err);
            }

            return;
        }

        // no-error

        // isOnline
        if (summary.isOnline == null) {
            throw new ParserException("Summary file %s: summary.isOnline is null", summaryFilePath);
        }

        // level
        if (summary.level == null) {
            throw new ParserException("Summary file %s: summary.level is null", summary.level);
        }

        Rank rank = Rank.getRank(summary.level);// this method will throw on incorrect input

        // level title
        if (StringUtils.isBlank(summary.title)) {
            throw new ParserException("Summary file %s: summary.title is null or blank", summary.title);
        }

        String expectedRankTitle = Rank.getDisplayName(rank);
        if (!summary.title.equals(expectedRankTitle)) {
            throw new ParserException("Summary file %s: summary.level has incorrect value %s, must be %s", summary.level, expectedRankTitle);
        }

        // blocked
        if (summary.isOnline == null) {
            throw new ParserException("Summary file %s: summary.isOnline is null", summaryFilePath);
        }

        // user
        if (summary.user == null) {
            throw new ParserException("Summary file %s: summary.user is null", summaryFilePath);
        }

        if (summary.user.id != playerId) {
            throw new ParserException("Summary file %s contains incorrect playerId %s. Expected playerId: %s", summaryFilePath, summary.user.id, playerId);
        }

        if (StringUtils.isBlank(summary.user.login)) {
            throw new ParserException("Summary file %s: summary.user.login is null or blank", summary.title);
        }

        // car
        if (summary.car == null) {
            throw new ParserException("Summary file %s: summary.car is null", summaryFilePath);
        }

        Car.getById(summary.car.car); // this method will throw on incorrect input

        if (StringUtils.isBlank(summary.car.color)) {
            throw new ParserException("Summary file %s: summary.car.color is null or blank", summaryFilePath);
        }
    }

    private static void validate(int playerId, PlayerIndexData data, String indexDataFilePath) {
        if (StringUtils.isNotBlank(data.err)) { // error case
            if (!data.err.equals(PlayerSummary.INVALID_USER_ID_ERROR)) {
                throw new ParserException("Index data file %s: Unknown error: %s", indexDataFilePath, data.err);
            }

            return;
        }

        // non-error case
        // ok
        if (data.ok != PlayerIndexData.OK_CORRECT_VALUE) { // should be null-safe
            throw new ParserException(
                "Index data file %s contains no error, but data.ok = %s. It must be = %s",
                indexDataFilePath,
                data.ok,
                PlayerIndexData.OK_CORRECT_VALUE
            );
        }

        // bio
        if (data.bio == null) {
            throw new ParserException("Index data file %s: data.bio is null", indexDataFilePath);
        }

        if (data.bio.userId != playerId) {
            throw new ParserException("Index data file %s contains incorrect playerId %s. Expected playerId: %s", indexDataFilePath, data.bio.userId, playerId);
        }

        // bio.oldText can be null / empty - no need to validate

        // bio.text can be empty, but cannot be null
        if (data.bio.text == null) {
            throw new ParserException("Index data file %s: data.text is null", indexDataFilePath);
        }

        // stats
        if (data.stats == null) {
            throw new ParserException("Index data file %s: data.stats is null", indexDataFilePath);
        }

        // stats.registered
        if (data.stats.registered == null) {
            throw new ParserException("Index data file %s: data.registered is null", indexDataFilePath);
        }

        if (data.stats.registered.sec == null) {
            throw new ParserException("Index data file %s: data.registered.sec is null", indexDataFilePath);
        }

        // todo: validate that (data.stats.registered.sec + data.stats.registered.usec) can parse to date
        if (data.stats.registered.sec <= 0) { // todo: compare to minimal unix date
            throw new ParserException("Index data file %s: data.registered.sec has invalid value: %d", indexDataFilePath, data.stats.registered.sec);
        }

        if (data.stats.registered.usec == null) {
            throw new ParserException("Index data file %s: data.registered.usec is null", indexDataFilePath);
        }

        if (data.stats.registered.usec < 0) { // todo: validate range, from 0 to 1000?
            throw new ParserException("Index data file %s: data.stats.registered.usec has invalid value: %d", indexDataFilePath, data.stats.registered.usec);
        }

        // statistics in stats
        if (data.stats.achievementsCount == null || data.stats.achievementsCount < 0) {
            throw new ParserException("Index data file %s: data.stats.achievementsCount has invalid value: %d", indexDataFilePath, data.stats.achievementsCount);
        }

        if (data.stats.totalRacesCount == null || data.stats.totalRacesCount < 0) {
            throw new ParserException("Index data file %s: data.stats.totalRacesCount has invalid value: %d", indexDataFilePath, data.stats.totalRacesCount);
        }

        // best speed can be null for player without races in Normal
        if (data.stats.bestSpeed != null && data.stats.bestSpeed <= 0) {
            throw new ParserException("Index data file %s: data.stats.bestSpeed has invalid value: %d", indexDataFilePath, data.stats.bestSpeed);
        }

        if (data.stats.ratingLevel == null || data.stats.ratingLevel < 1) { // todo: move 1 to constant in Player etc
            throw new ParserException("Index data file %s: data.stats.ratingLevel has invalid value: %d", indexDataFilePath, data.stats.ratingLevel);
        }

        if (data.stats.friendsCount == null || data.stats.friendsCount < 0) {
            throw new ParserException("Index data file %s: data.stats.friendsCount has invalid value: %d", indexDataFilePath, data.stats.friendsCount);
        }

        if (data.stats.vocabulariesCount == null || data.stats.vocabulariesCount < 0) {
            throw new ParserException("Index data file %s: data.stats.vocabulariesCount has invalid value: %d", indexDataFilePath, data.stats.vocabulariesCount);
        }

        // cannot own no cars or more than total cars
        if (data.stats.carsCount == null || data.stats.carsCount < 1 || data.stats.carsCount > Car.values().length) {
            throw new ParserException("Index data file %s: data.stats.carsCount has invalid value: %d", indexDataFilePath, data.stats.carsCount);
        }
    }
}
