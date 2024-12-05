package ru.klavogonki.statistics.util

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class DateDiffUtilsTest {

    @Test
    fun testTwoDays() {
        val startDate = OffsetDateTime.now()
        val endDate = startDate.plusDays(2)

        val diff = DateDiffUtils.formatDiff(startDate, endDate)

        Assertions.assertThat(diff).isEqualTo("48 hours 00 minutes 00 seconds")
    }

    @Test
    fun testHoursMinutesSeconds() {
        val startDate = OffsetDateTime.now()
        val endDate = startDate.plusHours(1).plusMinutes(2).plusSeconds(3)

        val diff = DateDiffUtils.formatDiff(startDate, endDate)

        Assertions.assertThat(diff).isEqualTo("1 hours 02 minutes 03 seconds")
    }
}