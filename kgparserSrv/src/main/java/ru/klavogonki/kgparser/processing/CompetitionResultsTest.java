package ru.klavogonki.kgparser.processing;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.http.UrlConstructor;

import java.util.Set;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionResultsTest
{
	private static final Logger logger = LogManager.getLogger(CompetitionResultsTest.class);

	public static void main(String[] args) {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);

		Competition competition = FakeDataFactory.createCompetition();
		getPlayerDictionariesResultsCount(competition);
	}

	public static void getPlayerDictionariesResultsCount(Competition competition) {
		// todo: fill results to result object and return this object

		Set<Player> players = competition.getPlayers();
		Set<Dictionary> dictionaries = competition.getDictionaries();

		// by dictionaries
		for (Dictionary dictionary : dictionaries)
		{
			int roundsCount = competition.getRoundsCount(dictionary);

			if (dictionary.isStandard()) {
				logger.info(
					"Dictionary: {} (name = \"{}\"), Rounds count: {}.",
					dictionary.getCode(),
					dictionary.getName(),
					roundsCount
				);
			}
			else {
				logger.info(
					"Dictionary: {} (name = \"{}\", dictionaryPageUrl = {}), Rounds count: {}.",
					dictionary.getCode(),
					dictionary.getName(),
					UrlConstructor.getDictionaryPageUrl( dictionary.getId() ),
					roundsCount
				);
			}
		}

		// by players
		for (Player player : players)
		{
			logger.info("================================================");
			logger.info(
				"Player {} (profileId = {}, profileLink = {})",
				player.getName(),
				player.getProfileId(),
				UrlConstructor.getProfileUrl(player)
			);

			int totalRoundsCount = competition.getRoundsCount(player);
			logger.info("Total rounds count: {}", totalRoundsCount);
			logger.info("");

			for (Dictionary dictionary : dictionaries)
			{ // player by dictionary
				int dictionaryRoundsCount = competition.getRoundsCount(player, dictionary);
				logger.info(
					"Dictionary: {} (\"{}\"), Rounds count: {}",
					dictionary.getCode(),
					dictionary.getName(),
					dictionaryRoundsCount
				);
			}
		}
	}
}
