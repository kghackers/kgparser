package ru.klavogonki.common

import org.apache.logging.log4j.kotlin.Logging
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class CarTest : Logging {

    @Test
    fun testCarIdsAreUnique() {
        val cars = Car.entries

        val uniqueCarIds = cars
            .map { car: Car -> car.id }
            .distinct()
            .sorted()

        logger.info("Unique car ids: \n$uniqueCarIds")

        assertThat(uniqueCarIds).hasSameSizeAs(cars)
    }

    @Test
    fun testCarNamesAreUnique() {
        val cars = Car.entries

        val uniqueCarNames = cars
            .map { it.name }
            .distinct()
            .sorted()

        logger.info("Unique car names: \n$uniqueCarNames")

        assertThat(uniqueCarNames).hasSameSizeAs(cars)
    }

    @Test
    fun testAllPersonalCarIdsArePersonalIds() {
        val cars = Car.entries

        val carPersonalIds = cars
            .filter { it.personalId != null }
            .map { it.personalId!! }
            .distinct()
            .sorted()

        logger.info("Personal car ids for cars that have been made public: \n$carPersonalIds")

        carPersonalIds.forEach {
            assertThat(Car.isPersonalId(it)).isTrue()
        }
    }

    @Test
    fun testAllPersonalCarsThatWereMadePublicHaveOriginalOwnerFilled() {
        val cars = Car.entries

        val personalCarsThatWereMadePublic = cars
            .filter { it.wasPersonalButMadePublic() }
            .distinct()
            .sorted()

        logger.info("Cars that were personal but have been made public: \n$personalCarsThatWereMadePublic")

        personalCarsThatWereMadePublic.forEach {
            assertThat(it.ownerId).isNotNull()
            assertThat(it.personalId).isNotNull()
        }
    }

    @Test
    fun testCarById() {
        val publicCarById = Car.getById(Car.NISSAN_ROUND_BOX.id)
        assertThat(publicCarById).isEqualTo(Car.NISSAN_ROUND_BOX)

        val personalCarById = Car.getById(Car.DUCATI_848_2010.id)
        assertThat(personalCarById).isEqualTo(Car.DUCATI_848_2010)

        val publishedPersonalCarById = Car.getById(Car.TRAM.id)
        assertThat(publishedPersonalCarById).isEqualTo(Car.TRAM)

        assertThat(Car.TRAM.personalId).isNotNull()
        val publishedPersonalCarByPersonalId = Car.getById(Car.TRAM.personalId!!)
        assertThat(publishedPersonalCarByPersonalId).isEqualTo(Car.TRAM)

        assertThatThrownBy {
            Car.getById(0) // non-existing car id
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("0 cars")
            .hasMessageContaining(" = 0")
    }
}
