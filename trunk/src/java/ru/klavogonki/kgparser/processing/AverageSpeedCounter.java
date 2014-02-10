package ru.klavogonki.kgparser.processing;

import org.apache.log4j.Logger;
import ru.klavogonki.kgparser.*;
import su.opencode.kefir.util.ObjectUtils;

import java.util.List;
import java.util.Set;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class AverageSpeedCounter
{
	public static void logCompetitionInfo(Competition competition) {
		StringBuilder sb = new StringBuilder();

		Set<Player> players = competition.getPlayers();
		Set<Dictionary> dictionaries = competition.getDictionaries();

		for (Player player : players)
		{
			logger.info("");
			logger.info("=====================================================");
			Double avgSpeed = getAvgSpeed(competition, player);
			logger.info( concat(sb, "Player ", player.getName(), " (profileId = ", player.getProfileId(), ").") );
			logger.info( concat(sb, " Total average speed: ", avgSpeed) );

			for (Dictionary dictionary : dictionaries)
			{
				Double avgDictionarySpeed = getAvgSpeed(competition, player, dictionary);
				logger.info( concat(sb, "Dictionary ", dictionary.getName(), " (code = ", dictionary.getCode(), "). Dictionary average speed: ", avgDictionarySpeed) );
			}
		}
	}

	public static Double getAvgSpeed(Competition competition, Player player) {
		List<PlayerRoundResult> results = competition.getPlayerRoundResults(player);
		if (ObjectUtils.empty(results) )
			return null;

		return getAverageDouble(results);
	}
	public static Double getAvgSpeed(Competition competition, Player player, Dictionary dictionary) {
		List<PlayerRoundResult> results = competition.getPlayerRoundResults(player, dictionary);
		if (ObjectUtils.empty(results) )
			return null;

		return getAverageDouble(results);
	}

	private static Double getAverageDouble(List<PlayerRoundResult> results) {
		int[] speeds = new int[ results.size() ];

		for (int i = 0, resultsSize = results.size(); i < resultsSize; i++)
		{
			PlayerRoundResult result = results.get(i);
			speeds[i] = result.getSpeed();
		}

		return CountUtils.getAverageDouble(speeds);
	}

	private static final Logger logger = Logger.getLogger(AverageSpeedCounter.class);
}