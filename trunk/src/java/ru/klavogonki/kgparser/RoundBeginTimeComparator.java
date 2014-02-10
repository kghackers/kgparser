package ru.klavogonki.kgparser;

import java.util.Comparator;
import java.util.Date;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br/>
 * <br/>
 * Компаратор, сравнивающий заезды по их {@linkplain Round#beginTime дате начала}.
 * Если у какого-либо заезда нет даты начала, бросается исключение.
 */
public class RoundBeginTimeComparator implements Comparator<Round>
{
	@Override
	public int compare(Round o1, Round o2) {
		Date beginTime1 = o1.getBeginTime();
		Date beginTime2 = o2.getBeginTime();

		if ( (beginTime1 == null) || (beginTime2 == null) )
			throw new IllegalArgumentException("Round has no beginTime");

		return beginTime1.compareTo(beginTime2);
	}
}