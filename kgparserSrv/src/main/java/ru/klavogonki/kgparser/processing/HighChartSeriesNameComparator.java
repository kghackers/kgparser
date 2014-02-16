package ru.klavogonki.kgparser.processing;

import java.util.Comparator;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class HighChartSeriesNameComparator implements Comparator<HighChartSeries>
{
	@Override
	public int compare(HighChartSeries o1, HighChartSeries o2) {
		return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() );
	}
}