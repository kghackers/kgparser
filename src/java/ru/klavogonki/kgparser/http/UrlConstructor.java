package ru.klavogonki.kgparser.http;

import ru.klavogonki.kgparser.Player;

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
		return DOMAIN_NAME + "/profile/" + Integer.toString(playerId) + "/";
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
		return DOMAIN_NAME + "/api/profile/get-summary?id=" + playerId;
	}

	public static String getGetPlayerStatsDetailsUrl(int playerId, String dictionaryCode) {
		return DOMAIN_NAME + "/api/profile/get-stats-details?userId="+ playerId + "&gametype=" + dictionaryCode;
	}

	/**
	 * @param dictionaryId идентификатор нестандартного словаря.
	 * @return урл страницы словаря на клавогонках
	 */
	public static String getDictionaryPageUrl(int dictionaryId) {
		return DOMAIN_NAME + "/vocs/" + Integer.toString(dictionaryId); // todo: use concat
	}


	public static final String DOMAIN_NAME = "http://klavogonki.ru";
}