package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;

@Log4j2
public final class HaulUtils {

    // todo: use some library with these constants
    // see https://stackoverflow.com/questions/2442486/time-consts-in-java
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;

    private HaulUtils() {
    }

    public static String format(Integer haul) {
        if (haul == null || haul < 0) {
            return "—";
        }

        int hours = haul / SECONDS_IN_HOUR;
        int minutes = (haul % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        int seconds = haul % SECONDS_IN_MINUTE;

        // todo: библиотека со склонением слов
        String result = String.format("%d ч. %d мин. %d сек.", hours, minutes, seconds);
        logger.debug("Haul {} converted to formatted string: {}", haul, result);
        return result;
    }
}
