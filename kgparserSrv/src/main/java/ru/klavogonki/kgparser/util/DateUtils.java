package ru.klavogonki.kgparser.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {
    private static final Logger logger = LogManager.getLogger(DateUtils.class);

    public static LocalDateTime convertUserRegisteredTime(final PlayerIndexData data) {
        if ((data == null) || (data.stats == null)) { // error in /get-index-data
            return null;
        }

        return convertUserRegisteredTime(data.stats.registered); // we assume it is not null
    }

    public static LocalDateTime convertUserRegisteredTime(final PlayerIndexData.Registered registered) {
        if (registered == null) {
            return null;
        }

        // probably use ZoneOffset/ZoneId for Moscow time or use just localDate
        ZoneId moscowZoneId = ZoneId.of("Europe/Moscow");

        Instant instant = Instant.ofEpochSecond(registered.sec, registered.usec);
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
}
