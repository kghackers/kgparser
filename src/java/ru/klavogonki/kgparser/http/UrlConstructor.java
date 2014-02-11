package ru.klavogonki.kgparser.http;

import ru.klavogonki.kgparser.Player;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class UrlConstructor
{
	/**
	 * @param playerId идентификатор игрока
	 * @return урл профиля игрока
	 */
	public static String getProfileUrl(int playerId) {
		return concat(DOMAIN_NAME,  "/profile/", Integer.toString(playerId) + "/");
//		return "http://klavogonki.ru/u/#/" + Integer.toString(playerId) + "/"; // new profile
	}
	/**
	 * @param player игрок
	 * @return урл профиля игрока
	 */
	public static String getProfileUrl(Player player) {
		return getProfileUrl( player.getProfileId() );
	}

	public static String getGetPlayerSummaryUrl(int playerId) {
		return concat(DOMAIN_NAME, "/api/profile/get-summary?id=", playerId);
	}

	public static String getGetPlayerStatsOverviewUrl(int playerId) {
		return concat(DOMAIN_NAME, "/api/profile/get-stats-overview?userId=", playerId);
	}
	public static String getGetPlayerStatsDetailsUrl(int playerId, String dictionaryCode) {
		return concat(DOMAIN_NAME, "/api/profile/get-stats-details?userId=", playerId, "&gametype=", dictionaryCode);
	}

	/**
	 * @param dictionaryId идентификатор нестандартного словаря.
	 * @return урл страницы словаря на клавогонках
	 */
	public static String getDictionaryPageUrl(int dictionaryId) {
		return concat(DOMAIN_NAME, "/vocs/", dictionaryId);
	}

	public static final String DOMAIN_NAME = "http://klavogonki.ru";
}