package ru.klavogonki.kgparser.processing;

import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.http.UrlConstructor;

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
public class CompetitionResultsTest
{
	public static void main(String[] args) {
		Competition competition = FakeDataFactory.createCompetition();
		getPlayerDictionariesResultsCount(competition);
	}

	public static void getPlayerDictionariesResultsCount(Competition competition) {
		// todo: fill results to result object and return this object

		Set<Player> players = competition.getPlayers();
		Set<Dictionary> dictionaries = competition.getDictionaries();

		StringBuilder sb = new StringBuilder();

		// by dictionaries
		for (Dictionary dictionary : dictionaries)
		{
			int roundsCount = competition.getRoundsCount(dictionary);

			if (dictionary.isStandard())
				System.out.println( concat(sb, "Dictionary: ", dictionary.getCode(), " (name = \"", dictionary.getName(), "\"), Rounds count: ", roundsCount) );
			else
				System.out.println( concat(sb, "Dictionary: ", dictionary.getCode(), " (name = \"", dictionary.getName(), "\", dictionaryPageUrl = ", UrlConstructor.getDictionaryPageUrl( dictionary.getId() ), "), Rounds count: ", roundsCount) );
		}

		// by players
		for (Player player : players)
		{
			System.out.println("================================================");
			System.out.println( concat(sb, "Player ", player.getName(), " (profileId = ", player.getProfileId(), ", profileLink = ", UrlConstructor.getProfileUrl(player), ")") );

			int totalRoundsCount = competition.getRoundsCount(player);
			System.out.println( concat(sb, "Total rounds count: ", totalRoundsCount) );
			System.out.println();

			for (Dictionary dictionary : dictionaries)
			{ // player by dictionary
				int dictionaryRoundsCount = competition.getRoundsCount(player, dictionary);
				System.out.println( concat(sb, "Dictionary: ", dictionary.getCode(), " (\"", dictionary.getName(), "\"), Rounds count: ", dictionaryRoundsCount) );
			}
		}
	}
}