package ru.klavogonki.kgparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        carPersonalIds.forEach(personalId ->
            assertThat(Car.isPersonalId(personalId)).isTrue()
        );
    }

    @Test
    void testAllPersonalCarsThatWereMadePublicHaveOriginalOwnerFilled() {
        Car[] cars = Car.values();

        List<Car> personalCarsThatWereMadePublic = Arrays.stream(cars)
            .filter(Car::wasPersonalButMadePublic)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        logger.info("cars that were personal but have been made public: \n{}", personalCarsThatWereMadePublic);

        personalCarsThatWereMadePublic.forEach(car -> {
            assertThat(car.ownerId).isNotNull();
            assertThat(car.personalId).isNotNull();
        });
    }

    @Test
    void testCarById() {
        Car publicCarById = Car.getById(Car.NISSAN_ROUND_BOX.id);
        assertThat(publicCarById).isEqualTo(Car.NISSAN_ROUND_BOX);

        Car personalCarById = Car.getById(Car.DUCATI_848_2010.id);
        assertThat(personalCarById).isEqualTo(Car.DUCATI_848_2010);

        Car publishedPersonalCarById = Car.getById(Car.TRAM.id);
        assertThat(publishedPersonalCarById).isEqualTo(Car.TRAM);

        assertThat(Car.TRAM.personalId).isNotNull();
        Car publishedPersonalCarByPersonalId = Car.getById(Car.TRAM.personalId);
        assertThat(publishedPersonalCarByPersonalId).isEqualTo(Car.TRAM);

        assertThatThrownBy(() -> {
            Car.getById(0);// non-existing car id
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("0 cars")
            .hasMessageContaining(" = 0");
    }
}
