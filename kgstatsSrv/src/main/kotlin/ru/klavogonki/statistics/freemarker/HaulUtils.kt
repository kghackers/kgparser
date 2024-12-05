package ru.klavogonki.statistics.freemarker

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.statistics.util.DateUtils

object HaulUtils : Logging {

    const val NO_HAUL_STRING = "—"

    @JvmStatic
    fun format(haul: Int?): String {
        if (haul == null || haul < 0) {
            return NO_HAUL_STRING
        }

        val hours = haul / DateUtils.SECONDS_IN_HOUR
        val minutes = (haul % DateUtils.SECONDS_IN_HOUR) / DateUtils.SECONDS_IN_MINUTE
        val seconds = haul % DateUtils.SECONDS_IN_MINUTE

        val minutesFormatted = "%02d".format(minutes)
        val secondsFormatted = "%02d".format(seconds)

        // todo: библиотека со склонением слов
        val result = "$hours ч. $minutesFormatted мин. $secondsFormatted сек."
        logger.debug("Haul $haul converted to formatted string \"$result\".")

        return result
    }
}
