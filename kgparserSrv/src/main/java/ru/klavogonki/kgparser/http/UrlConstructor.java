package ru.klavogonki.kgparser.http;

import ru.klavogonki.kgparser.Car;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
@SuppressWarnings("unused")
public class UrlConstructor
{
	public static final String DOMAIN_NAME = "http://klavogonki.ru";
	public static final String DOMAIN_NAME_HTTPS = "https://klavogonki.ru";

	public static String getLink(String relativeUrl, Object... relativeUrlArguments) {
		return DOMAIN_NAME_HTTPS + String.format(relativeUrl, relativeUrlArguments);
	}

	public static String getLinkHttps(String relativeUrl, Object... relativeUrlArguments) {
		return DOMAIN_NAME_HTTPS + String.format(relativeUrl, relativeUrlArguments);
	}

	// web page links
	/**
	 * @param player игрок
	 * @return урл профиля игрока
	 */
	public static String userProfileLink(Player player) {
		return userProfileLink( player.getProfileId() );
	}

	/**
	 * Сводка
	 * @param playerId id игрока
	 * @return ссылка на сводку игрока (корневая страница пользователя, саммари)
	 */
	public static String userProfileLink(final int playerId) {
		return getLinkHttps("/u/#/%d/", playerId);
	}

	/**
	 * @param playerId id игрока
	 * @return ссылка на профиль игрока без символа {@code #}. Ссылки с {@code #} не открываются из Excel.
	 */
	public static String userProfileLinkWithNoHash(final int playerId) {
		return getLinkHttps("/profile/%d/", playerId);
	}

	/**
	 * Достижения
	 * @param playerId id игрока
	 * @return ссылка на достижения игрока
	 */
	public static String userAchievements(final int playerId) {
		return getLink("/u/#/%d/achievements/", playerId);
	}

	/**
	 * Гараж
	 * @param playerId id игрока
	 * @return ссылка на гараж игрока
	 */
	public static String userCars(final int playerId) {
		return getLink("/u/#/%d/car/", playerId);
	}

	/**
	 * Бортжурнал
	 * @param playerId id игрока
	 * @return ссылка на бортжурнал игрока
	 */
	public static String userJournalLink(final int playerId) {
		return getLink("/u/#/%d/journal/", playerId);
	}

	/**
	 * Друзья
	 * @param playerId id игрока
	 * @return ссылка на список друзей игрока
	 */
	public static String userFriendsList(final int playerId) {
		return getLink("/u/#/%d/friends/list/", playerId);
	}

	/**
	 * Статистика
	 * @param playerId id игрока
	 * @return ссылка на статистику игрока
	 */
	public static String userStatistics(final int playerId) {
		return getLink("/u/#/%d/stats/", playerId);
	}

	/**
	 * Статистика по словарю
	 * @param playerId id игрока
	 * @param vocabularyCode {@link Dictionary#getCode код словаря}
	 * @return ссылка на статистику игрока по словарю
	 */
	public static String userStatsByVocabulary(final int playerId, final String vocabularyCode) {
		return getLink("/u/#/%s/stats/%s/", playerId, vocabularyCode);
	}

	/**
	 * Статистика по словарю
	 * @param playerId id игрока
	 * @param vocabularyCode {@link Dictionary#getCode код словаря}
	 * @return ссылка на статистику игрока по словарю без символа {@code #}. Ссылки с {@code #} не открываются из Excel.
	 */
	public static String userStatsByVocabularyWithoutHash(final int playerId, final String vocabularyCode) {
		return getLink("/profile/%s/stats/?gametype=%s", playerId, vocabularyCode);
	}

	// dictionaries
	/**
	 * @param dictionaryId идентификатор нестандартного словаря.
	 * @return урл страницы словаря на клавогонках
	 */
	public static String dictionaryPage(int dictionaryId) {
		return getLink("/vocs/%d", dictionaryId);
	}

	// car images
	public static String basicCarImage(Car car) {
		return getLink("/img/cars/%d.png", car.id);
	}

	// todo: maybe extract API to a separate class
	// todo: write a OAS for the API
	// API calls
	// /api/profile
	public static String getSummary(final int playerId) {
		return getLink("/api/profile/get-summary?id=%d", playerId);
	}

	public static String getIndexData(final int playerId) {
		return getLink("/api/profile/get-index-data?userId=%d", playerId);
	}

	public static String getStatsOverview(int playerId) {
		return getLink("/api/profile/get-stats-overview?userId=%d", playerId);
	}

	public static String getStatsDetail(int playerId, String dictionaryCode) {
		return getLink("/api/profile/get-stats-details?userId=%d&gametype=%s", playerId, dictionaryCode);
	}

	/**
	 * @param playerLogin логин игрока
	 * @return json вида {@code {"id":242585}} для корректного логина
	 * либо {@code {"id":false}} для некорректного логина
	 */
	public static String getUserIdByLogin(String playerLogin) {
		return getLink(".fetchuser?login=%s", playerLogin);
	}
}
