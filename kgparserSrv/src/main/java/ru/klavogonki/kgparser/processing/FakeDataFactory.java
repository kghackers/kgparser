package ru.klavogonki.kgparser.processing;

import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Round;
import ru.klavogonki.kgparser.StandardDictionary;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public final class FakeDataFactory {

    private FakeDataFactory() {
    }

    public static Competition createCompetition() {
		Dictionary normal = getDictionary(StandardDictionary.normal);
		Dictionary chars = getDictionary(StandardDictionary.chars);
		Dictionary frequent = getDictionary(192, "Частотный");

		Player nosferatum = createPlayer(242585, "nosferatum");
		Player toNick = createPlayer(1210, "ToNick");
		Player elena = createPlayer(233067, "E_l_e_n_a");
		Player alanen = createPlayer(139052, "alanen");

		// todo: already set player results (speed etc.)
		Round normalRound1 = createRound(normal);
		normalRound1.addResult( new PlayerRoundResult(normalRound1, nosferatum) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, toNick) );
		normalRound1.addResult( new PlayerRoundResult(normalRound1, elena) );

		Round normalRound2 = createRound(normal);
		normalRound2.addResult( new PlayerRoundResult(normalRound2, nosferatum) );
		normalRound2.addResult( new PlayerRoundResult(normalRound2, toNick) );
//		normalRound2.addResult( new PlayerRoundResult(normalRound2, elena) );

		Round charsRound1 = createRound(chars);
		charsRound1.addResult( new PlayerRoundResult(charsRound1, nosferatum) );

		Round frequentRound1 = createRound(frequent);
		frequentRound1.addResult( new PlayerRoundResult(frequentRound1, nosferatum) );
		frequentRound1.addResult( new PlayerRoundResult(frequentRound1, toNick) );
		frequentRound1.addResult( new PlayerRoundResult(frequentRound1, elena) );
		frequentRound1.addResult( new PlayerRoundResult(frequentRound1, alanen) );

		Competition competition = new Competition();
		competition.addRound(normalRound1);
		competition.addRound(normalRound2);
		competition.addRound(charsRound1);
		competition.addRound(frequentRound1);

		return competition;
	}

	// todo: move to constructors
	public static Round createRound(Dictionary dictionary) {
		Round round = new Round();
		round.setDictionary(dictionary);

		return round;
	}

	// todo: move to constructors
	public static Dictionary getDictionary(StandardDictionary dictionary) {
		return getDictionary( dictionary.toString(), dictionary.displayName );
	}
	public static Dictionary getDictionary(int dictionaryId, String name) {
		return getDictionary( Dictionary.getDictionaryCode(dictionaryId), name );
	}
	public static Dictionary getDictionary(String code, String name) {
		Dictionary dictionary = new Dictionary();
		dictionary.setCode(code);
		dictionary.setName(name);

		return dictionary;
	}

	// todo: move to constructor
	public static Player createPlayer(int profileId, String name) {
		Player player = new Player();
		player.setProfileId(profileId);
		player.setName(name);

		return player;
	}
}
