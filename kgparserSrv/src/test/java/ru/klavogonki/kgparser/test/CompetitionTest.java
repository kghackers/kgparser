package ru.klavogonki.kgparser.test;

import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Round;
import ru.klavogonki.kgparser.StandardDictionary;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.klavogonki.kgparser.test.TestHelper.createPlayer;
import static ru.klavogonki.kgparser.test.TestHelper.createRound;
import static ru.klavogonki.kgparser.test.TestHelper.getDictionary;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
class CompetitionTest
{
	@Test
	void testGetPlayers() {
		Dictionary normal = getDictionary(StandardDictionary.normal);
		Dictionary chars = getDictionary(StandardDictionary.chars);
		Dictionary frequent = getDictionary(192, "Частотный");

		Player nosferatum = createPlayer(242585, "nosferatum");
		Player ToNick = createPlayer(1210, "ToNick");
		Player Elena = createPlayer(233067, "E_l_e_n_a");
		Player alanen = createPlayer(139052, "alanen");

		// todo: already set player results (speed etc.)
		Round normalRound1 = createRound(normal);
		normalRound1.addResult( new PlayerRoundResult(normalRound1, nosferatum) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, ToNick) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, Elena) );

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, ToNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, Elena) );

		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		Competition competition = new Competition();
		competition.addRound(normalRound1);
		competition.addRound(normalRound2);
		competition.addRound(charsRound1);

		Set<Player> players = competition.getPlayers();

		assertThat(players)
			.hasSize(3)
			.contains(nosferatum, ToNick, Elena)
			.doesNotContain(alanen);
	}

	@Test
	void testGetDictionariesAndContainsDictionary() {
		Dictionary normal = getDictionary(StandardDictionary.normal);
		Dictionary chars = getDictionary(StandardDictionary.chars);
		Dictionary frequent = getDictionary(192, "Частотный");

		Player nosferatum = createPlayer(242585, "nosferatum");
		Player ToNick = createPlayer(1210, "ToNick");
		Player Elena = createPlayer(233067, "E_l_e_n_a");
		Player alanen = createPlayer(139052, "alanen");

		// todo: already set player results (speed etc.)
		Round normalRound1 = createRound(normal);
		normalRound1.addResult( new PlayerRoundResult(normalRound1, nosferatum) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, ToNick) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, Elena) );

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, ToNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, Elena) );

		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		Competition competition = new Competition();
		competition.addRound(normalRound1);
		competition.addRound(normalRound2);
		competition.addRound(charsRound1);

		Set<Dictionary> dictionaries = competition.getDictionaries();

		assertThat(dictionaries)
			.hasSize(2)
			.contains(normal, chars)
			.doesNotContain(frequent);

		assertThat(competition.containsDictionary(normal)).isTrue();
		assertThat(competition.containsDictionary(chars)).isTrue();
		assertThat(competition.containsDictionary(frequent)).isFalse();

		assertThat(competition.containsDictionary(normal.getCode())).isTrue();
		assertThat(competition.containsDictionary(chars.getCode())).isTrue();
		assertThat(competition.containsDictionary(frequent.getCode())).isFalse();
	}

	@Test
	void testGetRoundsCount() {
		Dictionary normal = getDictionary(StandardDictionary.normal);
		Dictionary chars = getDictionary(StandardDictionary.chars);
		Dictionary frequent = getDictionary(192, "Частотный");

		Player nosferatum = createPlayer(242585, "nosferatum");
		Player ToNick = createPlayer(1210, "ToNick");
		Player Elena = createPlayer(233067, "E_l_e_n_a");
		Player alanen = createPlayer(139052, "alanen");

		// todo: already set player results (speed etc.)
		Round normalRound1 = createRound(normal);
		normalRound1.addResult( new PlayerRoundResult(normalRound1, nosferatum) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, ToNick) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, Elena) );

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, ToNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, Elena) );

		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		Competition competition = new Competition();
		competition.addRound(normalRound1);
		competition.addRound(normalRound2);
		competition.addRound(charsRound1);

		// by dictionary
		assertThat(competition.getRoundsCount(normal)).isEqualTo(2);
		assertThat(competition.getRoundsCount(chars)).isEqualTo(1);
		assertThat(competition.getRoundsCount(frequent)).isZero();

		// by dictionary code
		assertThat(competition.getRoundsCount(normal.getCode())).isEqualTo(2);
		assertThat(competition.getRoundsCount(chars.getCode())).isEqualTo(1);
		assertThat(competition.getRoundsCount(frequent.getCode())).isZero();

		// by player
		assertThat(competition.getRoundsCount(nosferatum)).isEqualTo(3);
		assertThat(competition.getRoundsCount(ToNick)).isEqualTo(2);
		assertThat(competition.getRoundsCount(Elena)).isEqualTo(1);
		assertThat(competition.getRoundsCount(alanen)).isZero();

		// by player "nosferatum" and dictionaries
		assertThat(competition.getRoundsCount(nosferatum, normal)).isEqualTo(2);
		assertThat(competition.getRoundsCount(nosferatum, chars)).isEqualTo(1);
		assertThat(competition.getRoundsCount(nosferatum, frequent)).isZero();

		// by player "ToNick" and dictionaries
		assertThat(competition.getRoundsCount(ToNick, normal)).isEqualTo(2);
		assertThat(competition.getRoundsCount(ToNick, chars)).isZero();
		assertThat(competition.getRoundsCount(ToNick, frequent)).isZero();

		// by player "Elena" and dictionaries
		assertThat(competition.getRoundsCount(Elena, normal)).isEqualTo(1);
		assertThat(competition.getRoundsCount(Elena, chars)).isZero();
		assertThat(competition.getRoundsCount(Elena, frequent)).isZero();

		// by player "alanen" and dictionaries
		assertThat(competition.getRoundsCount(alanen, normal)).isZero();
		assertThat(competition.getRoundsCount(alanen, chars)).isZero();
		assertThat(competition.getRoundsCount(alanen, frequent)).isZero();
	}

	@Test
	void testGetRoundsByDictionariesMap() {
		Dictionary normal = getDictionary(StandardDictionary.normal);
		Dictionary chars = getDictionary(StandardDictionary.chars);
		Dictionary frequent = getDictionary(192, "Частотный");

		Player nosferatum = createPlayer(242585, "nosferatum");
		Player ToNick = createPlayer(1210, "ToNick");
		Player Elena = createPlayer(233067, "E_l_e_n_a");
		Player alanen = createPlayer(139052, "alanen");

		// todo: already set player results (speed etc.)
		Round normalRound1 = createRound(normal);
		normalRound1.addResult( new PlayerRoundResult(normalRound1, nosferatum) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, ToNick) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, Elena) );

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, ToNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, Elena) );

		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		Competition competition = new Competition();
		competition.addRound(normalRound1);
		competition.addRound(normalRound2);
		competition.addRound(charsRound1);

		Map<String, List<Round>> map = competition.getRoundsByDictionariesMap();
		assertThat(map)
			.hasSize(2)
			.containsKeys(normal.getCode(), chars.getCode())
			.doesNotContainKey(frequent.getCode());

		List<Round> normalRounds = map.get(normal.getCode());
		assertThat(normalRounds).hasSize(2);

		List<Round> charRounds = map.get(chars.getCode());
		assertThat(charRounds).hasSize(1);

		// todo: also check PlayerRoundResult inner values
	}
}
