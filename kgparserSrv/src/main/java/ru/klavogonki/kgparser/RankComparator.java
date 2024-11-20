package ru.klavogonki.kgparser;

import java.util.Comparator;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br>
 * <br>
 * Компаратор рангов по возрастанию.
 */
public class RankComparator implements Comparator<Rank>
{
	@Override
	public int compare(Rank o1, Rank o2) {
		return Integer.compare(o1.level, o2.level);
	}
}
