package ru.klavogonki.kgparser.processing.players_table;

import java.util.Comparator;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class PlayerResultsComparator implements Comparator<PlayerResult>
{
	@Override
	public int compare(PlayerResult o1, PlayerResult o2) {
		// сравнить количества заездов по убыванию
		int roundsCount1 = o1.getRoundsCount();
		int roundsCount2 = o2.getRoundsCount();
		if (roundsCount1 != roundsCount2) {
			return Integer.compare(roundsCount2, roundsCount1);
		}

		// количества заездов равны -> сравнить среднюю по убыванию
		double averageSpeed1 = o1.getAverageSpeed();
		double averageSpeed2 = o2.getAverageSpeed();
		if (averageSpeed1 != averageSpeed2) {
			return Double.compare(averageSpeed2, averageSpeed1);
		}

		// средние скорости равны -> сравнить процент ошибок по возрастанию
		double averageErrorPercentage1 = o1.getAverageErrorPercentage();
		double averageErrorPercentage2 = o2.getAverageErrorPercentage();
		if (averageErrorPercentage1 != averageErrorPercentage2) {
			return Double.compare(averageErrorPercentage1, averageErrorPercentage2);
		}

		// средние проценты ошибок равны -> сравнить количество ошибок по возрастанию
		int totalErrorsCount1 = o1.getTotalErrorsCount();
		int totalErrorsCount2 = o2.getTotalErrorsCount();
		if (totalErrorsCount1 != totalErrorsCount2) {
			return Integer.compare(totalErrorsCount1, totalErrorsCount2);
		}

		// все одинаково -> результаты игроков равны
		return 0;
	}
}
