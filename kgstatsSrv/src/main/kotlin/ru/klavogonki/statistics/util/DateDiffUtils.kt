package ru.klavogonki.statistics.util

import org.apache.logging.log4j.kotlin.Logging
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

object DateDiffUtils : Logging {

    @JvmStatic
    fun formatDiff(start: OffsetDateTime, end: OffsetDateTime): String {
        // todo: maybe use Kotlin's Duration, see https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/

        val totalSecondsDiff = ChronoUnit.SECONDS.between(start, end)

        val hours = totalSecondsDiff / DateUtils.SECONDS_IN_HOUR
        val minutes = (totalSecondsDiff % DateUtils.SECONDS_IN_HOUR) / DateUtils.SECONDS_IN_MINUTE
        val seconds = totalSecondsDiff % DateUtils.SECONDS_IN_MINUTE

        val hoursFormatted = hours.toString()
        val minutesFormatted = "%02d".format(minutes)
        val secondsFormatted = "%02d".format(seconds)

        // todo: we may format single and multiple for all hour VS hours, minute VS minutes, second VS seconds
        return "$hoursFormatted hours $minutesFormatted minutes $secondsFormatted seconds"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val startDate = OffsetDateTime.now()
        val endDate = startDate.plusDays(2)

        val diff = formatDiff(startDate, endDate)

        logger.info("Start date: $startDate")
        logger.info("End date: $endDate")
        logger.info("Date diff: $diff")
    }
}