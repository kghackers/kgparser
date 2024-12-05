package ru.klavogonki.statistics.freemarker

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HaulUtilsTest {

    @Test
    fun testNull() {
        val haul = HaulUtils.format(null)
        assertThat(haul).isEqualTo(HaulUtils.NO_HAUL_STRING)
    }

    @Test
    fun testOneSecond() {
        val haul = HaulUtils.format(1)
        assertThat(haul).isEqualTo("0 ч. 00 мин. 01 сек.")
    }

    @Test
    fun testOneMinute() {
        val haul = HaulUtils.format(60)
        assertThat(haul).isEqualTo("0 ч. 01 мин. 00 сек.")
    }

    @Test
    fun testOneHour() {
        val haul = HaulUtils.format(3600)
        assertThat(haul).isEqualTo("1 ч. 00 мин. 00 сек.")
    }

    @Test
    fun testTenMinutes() {
        val haul = HaulUtils.format(600)
        assertThat(haul).isEqualTo("0 ч. 10 мин. 00 сек.")
    }

    @Test
    fun testSecondsLessThanMinute() {
        val haul = HaulUtils.format(59)
        assertThat(haul).isEqualTo("0 ч. 00 мин. 59 сек.")
    }

    @Test
    fun testMoreThan1Hour() {
        val haul = HaulUtils.format(3735)
        assertThat(haul).isEqualTo("1 ч. 02 мин. 15 сек.")
    }

    @Test
    fun testRealHaul() {
        val haul = HaulUtils.format(1029641)
        assertThat(haul).isEqualTo("286 ч. 00 мин. 41 сек.")
    }
}
