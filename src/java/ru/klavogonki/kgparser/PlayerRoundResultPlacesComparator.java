package ru.klavogonki.kgparser;

import java.util.Comparator;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br/>
 * <br/>
 * Компаратор, сравнивающий результаты игроков согласно занятым ими в заезде местам.
 * Игроки, с неопределенными местами, идут неупорядоченно в конце.
 */
public class PlayerRoundResultPlacesComparator implements Comparator<PlayerRoundResult>
{
	@Override
	public int compare(PlayerRoundResult o1, PlayerRoundResult o2) {
		Integer place1 = o1.getPlace();
		Integer place2 = o2.getPlace();

		// check place1 validity
		if ( (place1 != null) && (!PlayerRoundResult.isCorrectPlace(place1)) )
			throw new IllegalArgumentException( concat("Incorrect player1 place: ", place1) );

		// check place2 validity
		if ( (place2 != null) && (!PlayerRoundResult.isCorrectPlace(place2)) )
			throw new IllegalArgumentException( concat("Incorrect player2 place: ", place2) );

		if ( (place1 == null) && (place2 == null) )
			return 0; // undefined order

		if ( (place1 != null) && (place2 == null) )
			return -1; // null is after a defined place

		if ( (place1 == null) && (place2 != null) )
			return 1; // null is after a defined place

		return place1.compareTo(place2);
	}
}