package ru.klavogonki.kgparser.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.Microtime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateUtils {
    private static final Logger logger = LogManager.getLogger(DateUtils.class);

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH-mm-ss";
    public static final String DATE_TIME_FORMAT_FOR_UI = "yyyy-MM-dd HH:mm:ss";

    private static ZoneId getMoscowZoneId() {
        return ZoneId.of("Europe/Moscow");
    }

    public static LocalDateTime convertUserRegisteredTime(final GetIndexDataResponse data) {
        if ((data == null) || (data.getStats() == null)) { // error in /get-index-data
            return null;
        }

        Microtime registered = data.getStats().getRegistered();
        return convertUserRegisteredTime(registered); // we assume it is not null
    }

    public static LocalDateTime convertUserRegisteredTime(final Microtime registered) {
        if (registered == null) {
            return null;
        }

        return convertUserRegisteredTime(registered.getSec(), registered.getUsec());
    }

    public static LocalDateTime convertUserRegisteredTime(final Long sec, final Long usec) {
        // probably use ZoneOffset/ZoneId for Moscow time or use just localDate
        ZoneId moscowZoneId = getMoscowZoneId();

        Instant instant = Instant.ofEpochSecond(sec, usec);
//        LocalDateTime localDateTimeUtc = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
//        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        LocalDateTime localDateTimeMoscow = LocalDateTime.ofInstant(instant, moscowZoneId);
        ZonedDateTime zonedDateTimeMoscow = ZonedDateTime.ofInstant(instant, moscowZoneId);

        logger.info("registered as Instant: {}", instant);
//        logger.info("registered as LocalDateTime UTC: {}", localDateTimeUtc);
//        logger.info("registered as ZonedDateTime UTC: {}", zonedDateTimeUtc);
        logger.info("registered as LocalDateTime Moscow: {}", localDateTimeMoscow);
        logger.info("registered as ZonedDateTime Moscow: {}", zonedDateTimeMoscow);

        return localDateTimeMoscow;
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String formatDateTimeForUi(LocalDateTime localDateTime) {
        if (localDateTime == null) { // https://klavogonki.ru/u/#/166851/stats/ - has statistics, but /get-index-data returns error
            return "—";
        }

        // convert LocalDateTime -> ZonedDateTime
        ZoneId moscowZoneId = getMoscowZoneId();
        ZonedDateTime zonedDateTime = localDateTime.atZone(moscowZoneId);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FOR_UI);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"); // will be +0300 (Moscow Winter Time) / +0400 (Moscow Summer Time)
        return zonedDateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeString) {
        Objects.requireNonNull(dateTimeString);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public static LocalDateTime parseLocalDateTimeWithUiDateFormat(String dateTimeString) {
        Objects.requireNonNull(dateTimeString);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FOR_UI);
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }
}
