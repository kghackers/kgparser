package ru.klavogonki.statistics.freemarker

import org.apache.logging.log4j.kotlin.Logging

object HaulUtils : Logging {
    // todo: use some library with these constants
    // see https://stackoverflow.com/questions/2442486/time-consts-in-java
    private const val SECONDS_IN_MINUTE = 60
    private const val MINUTES_IN_HOUR = 60
    private const val SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR

    const val NO_HAUL_STRING = "—"

    @JvmStatic
    fun format(haul: Int?): String {
        if (haul == null || haul < 0) {
            return NO_HAUL_STRING
        }

        val hours = haul / SECONDS_IN_HOUR
        val minutes = (haul % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
        val seconds = haul % SECONDS_IN_MINUTE

        val minutesFormatted = "%02d".format(minutes)
        val secondsFormatted = "%02d".format(seconds)

        // todo: библиотека со склонением слов
        val result = "$hours ч. $minutesFormatted мин. $secondsFormatted сек."
        logger.debug("Haul $haul converted to formatted string \"$result\".")

        return result
    }
}
