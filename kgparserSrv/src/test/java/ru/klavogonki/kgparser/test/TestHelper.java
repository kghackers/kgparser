package ru.klavogonki.kgparser.test;

import ru.klavogonki.common.StandardDictionary;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.Round;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class TestHelper
{
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