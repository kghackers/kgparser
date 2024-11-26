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
    fun testSecondsLessThanMinute() {
        val haul = HaulUtils.format(59)
        assertThat(haul).isEqualTo("0 ч. 0 мин. 59 сек.")
    }

    @Test
    fun testMoreThan1Hour() {
        val haul = HaulUtils.format(3735)
        assertThat(haul).isEqualTo("1 ч. 2 мин. 15 сек.")
    }

    @Test
    fun testRealHaul() {
        val haul = HaulUtils.format(1029641)
        assertThat(haul).isEqualTo("286 ч. 0 мин. 41 сек.")
    }
}
