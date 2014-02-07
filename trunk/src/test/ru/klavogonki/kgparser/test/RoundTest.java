package ru.klavogonki.kgparser.test;

import org.junit.Test;
import ru.klavogonki.kgparser.*;

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
public class RoundTest
{
	@Test
	public void testHasPlayerResult() {
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

		assertEquals(true, normalRound1.hasPlayerResult(nosferatum));
		assertEquals(true, normalRound1.hasPlayerResult(ToNick));
		assertEquals(true, normalRound1.hasPlayerResult(Elena));
		assertEquals(false, normalRound1.hasPlayerResult(alanen));

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, ToNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, Elena) );

		assertEquals(true, normalRound2.hasPlayerResult(nosferatum));
		assertEquals(true, normalRound2.hasPlayerResult(ToNick));
		assertEquals(false, normalRound2.hasPlayerResult(Elena));
		assertEquals(false, normalRound2.hasPlayerResult(alanen));


		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		assertEquals(true, charsRound1.hasPlayerResult(nosferatum));
		assertEquals(false, charsRound1.hasPlayerResult(ToNick));
		assertEquals(false, charsRound1.hasPlayerResult(Elena));
		assertEquals(false, charsRound1.hasPlayerResult(alanen));
	}
}