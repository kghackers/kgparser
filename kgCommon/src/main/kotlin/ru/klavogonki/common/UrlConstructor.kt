package ru.klavogonki.common

/**
 * Copyright 2014 [Dmitriy Popov](mailto:dmitry.weirdo@gmail.com).
 */
@Suppress("unused", "TooManyFunctions")
object UrlConstructor {
    const val DOMAIN_NAME = "https://klavogonki.ru" // always use the https:// link

    private fun getLink(relativeUrl: String, vararg relativeUrlArguments: Any?): String {
        return DOMAIN_NAME + String.format(relativeUrl, *relativeUrlArguments)
    }

    // web page links
    /**
     * Сводка.
     *
     * @param playerId id игрока
     * @return ссылка на сводку игрока (корневая страница пользователя, саммари)
     */
    @JvmStatic
    fun userProfileLink(playerId: Int) =
        getLink("/u/#/%d/", playerId)

    /**
     * @param playerId id игрока
     * @return ссылка на профиль игрока без символа `#`. Ссылки с `#` не открываются из Excel.
     */
    @JvmStatic
    fun userProfileLinkWithoutHash(playerId: Int) =
        getLink("/profile/%d/", playerId)

    /**
     * Достижения.
     *
     * @param playerId id игрока
     * @return ссылка на достижения игрока
     */
    @JvmStatic
    fun userAchievements(playerId: Int) =
        getLink("/u/#/%d/achievements/", playerId)

    /**
     * Гараж.
     *
     * @param playerId id игрока
     * @return ссылка на гараж игрока
     */
    @JvmStatic
    fun userCars(playerId: Int) =
        getLink("/u/#/%d/car/", playerId)

    /**
     * Бортжурнал.
     *
     * @param playerId id игрока
     * @return ссылка на бортжурнал игрока
     */
    @JvmStatic
    fun userJournalLink(playerId: Int) =
        getLink("/u/#/%d/journal/", playerId)

    /**
     * Друзья.
     *
     * @param playerId id игрока
     * @return ссылка на список друзей игрока
     */
    @JvmStatic
    fun userFriendsList(playerId: Int) =
        getLink("/u/#/%d/friends/list/", playerId)

    /**
     * Статистика.
     *
     * @param playerId id игрока
     * @return ссылка на статистику игрока
     */
    @JvmStatic
    fun userStatistics(playerId: Int) =
        getLink("/u/#/%d/stats/", playerId)

    /**
     * Статистика по словарю.
     *
     * @param playerId       id игрока
     * @param vocabularyCode код словаря
     * @return ссылка на статистику игрока по словарю
     */
    @JvmStatic
    fun userStatsByVocabulary(playerId: Int, vocabularyCode: String): String {
        val validCode = DictionaryUtils.getValidCode(vocabularyCode)

        return getLink("/u/#/%s/stats/%s/", playerId, validCode)
    }

    /**
     * Статистика по словарю.
     *
     * @param playerId       id игрока
     * @param vocabularyCode код словаря
     * @return ссылка на статистику игрока по словарю без символа `#`. Ссылки с `#` не открываются из Excel.
     */
    @JvmStatic
    fun userStatsByVocabularyWithoutHash(playerId: Int, vocabularyCode: String): String {
        val validCode = DictionaryUtils.getValidCode(vocabularyCode)

        return getLink("/profile/%s/stats/?gametype=%s", playerId, validCode)
    }

    // dictionaries
    /**
     * @param dictionaryId идентификатор нестандартного словаря.
     * @return урл страницы словаря на клавогонках
     */
    @JvmStatic
    fun dictionaryPage(dictionaryId: Int) =
        getLink("/vocs/%d", dictionaryId)

    // car images
    @JvmStatic
    fun basicCarImage(car: Car) =
        getLink("/img/cars/%d.png", car.id)

    // todo: maybe extract API to a separate class
    // todo: write a OAS for the API
    // API calls
    // /api/profile
    @JvmStatic
    fun getSummary(playerId: Int) =
        getLink("/api/profile/get-summary?id=%d", playerId)

    @JvmStatic
    fun getIndexData(playerId: Int) =
        getLink("/api/profile/get-index-data?userId=%d", playerId)

    @JvmStatic
    fun getStatsOverview(playerId: Int) =
        getLink("/api/profile/get-stats-overview?userId=%d", playerId)

    @JvmStatic
    fun getStatsDetail(playerId: Int, vocabularyCode: String): String {
        val validCode = DictionaryUtils.getValidCode(vocabularyCode)

        return getLink("/api/profile/get-stats-details?userId=%d&gametype=%s", playerId, validCode)
    }

    /**
     * @param playerLogin логин игрока
     * @return json вида `{"id":242585}` для корректного логина
     * либо `{"id":false}` для некорректного логина
     */
    @JvmStatic
    fun getUserIdByLogin(playerLogin: String) =
        getLink(".fetchuser?login=%s", playerLogin)
}
