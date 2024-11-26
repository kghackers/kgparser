package ru.klavogonki.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UrlConstructorTest {

    @Test
    fun testUserProfileLink() {
        val link = UrlConstructor.userProfileLink(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/u/#/242585/")
    }

    @Test
    fun testUserProfileLinkWithoutHash() {
        val link = UrlConstructor.userProfileLinkWithoutHash(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/profile/242585/")
    }

    @Test
    fun testUserAchievements() {
        val link = UrlConstructor.userAchievements(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/u/#/242585/achievements/")
    }

    @Test
    fun testUserCars() {
        val link = UrlConstructor.userCars(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/u/#/242585/car/")
    }

    @Test
    fun testUserJournalLink() {
        val link = UrlConstructor.userJournalLink(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/u/#/242585/journal/")
    }

    @Test
    fun testUserFriendsList() {
        val link = UrlConstructor.userFriendsList(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/u/#/242585/friends/list/")
    }

    @Test
    fun testUserStatistics() {
        val link = UrlConstructor.userStatistics(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/u/#/242585/stats/")
    }

    @Test
    fun testUserStatsByVocabulary() {
        // standard vocabulary
        val standardVocabularyLink = UrlConstructor.userStatsByVocabulary(
            NOSFERATUM_PROFILE_ID,
            STANDARD_DICTIONARY_CODE
        )

        assertThat(standardVocabularyLink).isEqualTo("https://klavogonki.ru/u/#/242585/stats/normal/")

        // non-standard vocabulary -> pass code without "voc-" prefix
        val nonStandardVocabularyLinkById = UrlConstructor.userStatsByVocabulary(
            NOSFERATUM_PROFILE_ID,
            NON_STANDARD_DICTIONARY_ID_STRING
        )

        assertThat(nonStandardVocabularyLinkById).isEqualTo("https://klavogonki.ru/u/#/242585/stats/voc-5539/")

        // non-standard vocabulary -> pass code with "voc-" prefix
        val nonStandardVocabularyLinkByCode = UrlConstructor.userStatsByVocabulary(
            NOSFERATUM_PROFILE_ID,
            NON_STANDARD_DICTIONARY_CODE
        )

        assertThat(nonStandardVocabularyLinkByCode).isEqualTo("https://klavogonki.ru/u/#/242585/stats/voc-5539/")
    }

    @Test
    fun testUserStatsByVocabularyWithoutHash() {
        // standard vocabulary
        val standardVocabularyLnk = UrlConstructor.userStatsByVocabularyWithoutHash(
            NOSFERATUM_PROFILE_ID,
            STANDARD_DICTIONARY_CODE
        )

        assertThat(standardVocabularyLnk).isEqualTo(
            "https://klavogonki.ru/profile/242585/stats/?gametype=normal"
        )

        // non-standard vocabulary -> pass code without "voc-" prefix
        val nonStandardVocabularyLinkById = UrlConstructor.userStatsByVocabularyWithoutHash(
            NOSFERATUM_PROFILE_ID,
            NON_STANDARD_DICTIONARY_ID_STRING
        )

        assertThat(nonStandardVocabularyLinkById).isEqualTo(
            "https://klavogonki.ru/profile/242585/stats/?gametype=voc-5539"
        )

        // non-standard vocabulary -> pass code with "voc-" prefix
        val nonStandardVocabularyLinkByCode = UrlConstructor.userStatsByVocabularyWithoutHash(
            NOSFERATUM_PROFILE_ID,
            NON_STANDARD_DICTIONARY_CODE
        )

        assertThat(nonStandardVocabularyLinkByCode).isEqualTo(
            "https://klavogonki.ru/profile/242585/stats/?gametype=voc-5539"
        )
    }

    @Test
    fun testNonStandardDictionaryPage() {
        val link = UrlConstructor.dictionaryPage(NON_STANDARD_DICTIONARY_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/vocs/5539")
    }

    @Test
    fun testBasicCarImage() {
        val publicCarLink = UrlConstructor.basicCarImage(Car.TRAM)

        assertThat(publicCarLink).isEqualTo("https://klavogonki.ru/img/cars/31.png")

        val personalCarLink = UrlConstructor.basicCarImage(Car.DE_LOREAN_TIME_MACHINE)

        assertThat(personalCarLink).isEqualTo("https://klavogonki.ru/img/cars/1002.png")
    }

    @Test
    fun testGetSummary() {
        val link = UrlConstructor.getSummary(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/api/profile/get-summary?id=242585")
    }

    @Test
    fun testGetIndexData() {
        val link = UrlConstructor.getIndexData(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/api/profile/get-index-data?userId=242585")
    }

    @Test
    fun testGetStatsOverview() {
        val link = UrlConstructor.getStatsOverview(NOSFERATUM_PROFILE_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/api/profile/get-stats-overview?userId=242585")
    }

    @Test
    fun testGetStatsDetail() {
        // standard vocabulary
        val standardVocabularyLnk = UrlConstructor.getStatsDetail(
            NOSFERATUM_PROFILE_ID,
            STANDARD_DICTIONARY_CODE
        )

        assertThat(standardVocabularyLnk).isEqualTo(
            "https://klavogonki.ru/api/profile/get-stats-details?userId=242585&gametype=normal"
        )

        // non-standard vocabulary -> pass code without "voc-" prefix
        val nonStandardVocabularyLinkById = UrlConstructor.getStatsDetail(
            NOSFERATUM_PROFILE_ID,
            NON_STANDARD_DICTIONARY_ID_STRING
        )

        assertThat(nonStandardVocabularyLinkById).isEqualTo(
            "https://klavogonki.ru/api/profile/get-stats-details?userId=242585&gametype=voc-5539"
        )

        // non-standard vocabulary -> pass code with "voc-" prefix
        val nonStandardVocabularyLinkByCode = UrlConstructor.getStatsDetail(
            NOSFERATUM_PROFILE_ID,
            NON_STANDARD_DICTIONARY_CODE
        )

        assertThat(nonStandardVocabularyLinkByCode).isEqualTo(
            "https://klavogonki.ru/api/profile/get-stats-details?userId=242585&gametype=voc-5539"
        )
    }

    @Test
    fun testGetUserBookProgress() {
        val link = UrlConstructor.getUserBookProgress(NOSFERATUM_PROFILE_ID, BOOK_DICTIONARY_ID)

        assertThat(link).isEqualTo("https://klavogonki.ru/api/profile/get-book-parts?userId=242585&vocId=30149")
    }

    companion object {
        const val NOSFERATUM_PROFILE_ID = 242585

        val STANDARD_DICTIONARY_CODE = StandardDictionary.normal.klavogonkiName // cannot use const here

        const val NON_STANDARD_DICTIONARY_ID = 5539 // todo: use constant from NonStandardDictionary
        const val NON_STANDARD_DICTIONARY_ID_STRING = NON_STANDARD_DICTIONARY_ID.toString()
        val NON_STANDARD_DICTIONARY_CODE = DictionaryUtils.getDictionaryCode(NON_STANDARD_DICTIONARY_ID)

        const val BOOK_DICTIONARY_ID = 30149; // https://klavogonki.ru/vocs/30149
    }
}