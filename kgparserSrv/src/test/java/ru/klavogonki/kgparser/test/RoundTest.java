package ru.klavogonki.kgparser.test;

import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Round;
import ru.klavogonki.kgparser.StandardDictionary;

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
class RoundTest
{
	@Test
	void testHasPlayerResult() {
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

		assertThat(normalRound1.hasPlayerResult(nosferatum)).isTrue();
		assertThat(normalRound1.hasPlayerResult(ToNick)).isTrue();
		assertThat(normalRound1.hasPlayerResult(Elena)).isTrue();
		assertThat(normalRound1.hasPlayerResult(alanen)).isFalse();

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, ToNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, Elena) );

		assertThat(normalRound2.hasPlayerResult(nosferatum)).isTrue();
		assertThat(normalRound2.hasPlayerResult(ToNick)).isTrue();
		assertThat(normalRound2.hasPlayerResult(Elena)).isFalse();
		assertThat(normalRound2.hasPlayerResult(alanen)).isFalse();


		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		assertThat(charsRound1.hasPlayerResult(nosferatum)).isTrue();
		assertThat(charsRound1.hasPlayerResult(ToNick)).isFalse();
		assertThat(charsRound1.hasPlayerResult(Elena)).isFalse();
		assertThat(charsRound1.hasPlayerResult(alanen)).isFalse();
	}
}
