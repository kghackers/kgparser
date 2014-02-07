package ru.klavogonki.kgparser.test;

import org.junit.Test;
import ru.klavogonki.kgparser.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static ru.klavogonki.kgparser.test.TestHelper.*;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionTest
{
	@Test
	public void testGetPlayers() {
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
		assertEquals(3, players.size());
		assertEquals(true, players.contains(nosferatum));
		assertEquals(true, players.contains(ToNick));
		assertEquals(true, players.contains(Elena));
		assertEquals(false, players.contains(alanen));
	}

	@Test
	public void testGetDictionariesAndContainsDictionary() {
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
		assertEquals(2, dictionaries.size());
		assertEquals(true, dictionaries.contains(normal));
		assertEquals(true, dictionaries.contains(chars));
		assertEquals(false, dictionaries.contains(frequent));

		assertEquals(true, competition.containsDictionary(normal));
		assertEquals(true, competition.containsDictionary(chars));
		assertEquals(false, competition.containsDictionary(frequent));

		assertEquals(true, competition.containsDictionary(normal.getCode()));
		assertEquals(true, competition.containsDictionary(chars.getCode()));
		assertEquals(false, competition.containsDictionary(frequent.getCode()));
	}

	@Test
	public void testGetRoundsCount() {
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
		assertEquals(2, competition.getRoundsCount(normal));
		assertEquals(1, competition.getRoundsCount(chars));
		assertEquals(0, competition.getRoundsCount(frequent));

		// by dictionary code
		assertEquals(2, competition.getRoundsCount(normal.getCode()));
		assertEquals(1, competition.getRoundsCount(chars.getCode()));
		assertEquals(0, competition.getRoundsCount(frequent.getCode()));

		// by player
		assertEquals(3, competition.getRoundsCount(nosferatum));
		assertEquals(2, competition.getRoundsCount(ToNick));
		assertEquals(1, competition.getRoundsCount(Elena));
		assertEquals(0, competition.getRoundsCount(alanen));

		// by player "nosferatum" and dictionaries
		assertEquals(2, competition.getRoundsCount(nosferatum, normal));
		assertEquals(1, competition.getRoundsCount(nosferatum, chars));
		assertEquals(0, competition.getRoundsCount(nosferatum, frequent));

		// by player "ToNick" and dictionaries
		assertEquals(2, competition.getRoundsCount(ToNick, normal));
		assertEquals(0, competition.getRoundsCount(ToNick, chars));
		assertEquals(0, competition.getRoundsCount(ToNick, frequent));

		// by player "Elena" and dictionaries
		assertEquals(1, competition.getRoundsCount(Elena, normal));
		assertEquals(0, competition.getRoundsCount(Elena, chars));
		assertEquals(0, competition.getRoundsCount(Elena, frequent));

		// by player "alanen" and dictionaries
		assertEquals(0, competition.getRoundsCount(alanen, normal));
		assertEquals(0, competition.getRoundsCount(alanen, chars));
		assertEquals(0, competition.getRoundsCount(alanen, frequent));
	}

	@Test
	public void testGetRoundsByDictionariesMap() {
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

		Map<String,List<Round>> map = competition.getRoundsByDictionariesMap();
		assertEquals(2, map.keySet().size());
		assertEquals( true, map.keySet().contains(normal.getCode()) );
		assertEquals( true, map.keySet().contains(chars.getCode()) );
		assertEquals( false, map.keySet().contains(frequent.getCode()) );

		List<Round> normalRounds = map.get(normal.getCode());
		assertEquals(2, normalRounds.size());

		List<Round> charRounds = map.get(chars.getCode());
		assertEquals(1, charRounds.size());

		// todo: also check PlayerRoundResult inner values
	}
}