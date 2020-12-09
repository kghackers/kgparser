package ru.klavogonki.kgparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CarTest {
    private static final Logger logger = LogManager.getLogger(CarTest.class);

    @Test
    void testCarIdsAreUnique() {
        Car[] cars = Car.values();

        List<Integer> uniqueCarIds = Arrays.stream(cars)
            .map(car -> car.id)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        logger.info("unique car ids: \n{}", uniqueCarIds);

        assertThat(uniqueCarIds).hasSameSizeAs(cars);
    }

    @Test
    void testCarNamesAreUnique() {
        Car[] cars = Car.values();

        List<String> uniqueCarNames = Arrays.stream(cars)
            .map(car -> car.name)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        logger.info("unique car names: \n{}", uniqueCarNames);

        assertThat(uniqueCarNames).hasSameSizeAs(cars);
    }

    @Test
    void testAllPersonalCarIdsArePersonalIds() {
        Car[] cars = Car.values();

        List<Integer> carPersonalIds = Arrays.stream(cars)
            .filter(car -> (car.personalId != null))
            .map(car -> car.personalId)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        logger.info("personal car ids for cars that have been made public: \n{}", carPersonalIds);

        carPersonalIds.forEach(personalId -> {
            assertThat(Car.isPersonalId(personalId)).isTrue();
        });
    }
}
