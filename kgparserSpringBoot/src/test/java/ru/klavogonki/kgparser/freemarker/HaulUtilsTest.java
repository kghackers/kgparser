package ru.klavogonki.kgparser.freemarker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HaulUtilsTest {

    @Test
    void testNull() {
        String haul = HaulUtils.format(null);
        assertThat(haul).isEqualTo("—");
    }

    @Test
    void testSecondsLessThanMinute() {
        String haul = HaulUtils.format(59);
        assertThat(haul).isEqualTo("0 ч. 0 мин. 59 сек.");
    }

    @Test
    void testMoreThan1Hour() {
        String haul = HaulUtils.format(3735);
        assertThat(haul).isEqualTo("1 ч. 2 мин. 15 сек.");
    }

    @Test
    void testRealHaul() {
        String haul = HaulUtils.format(1029641);
        assertThat(haul).isEqualTo("286 ч. 0 мин. 41 сек.");
    }
}
