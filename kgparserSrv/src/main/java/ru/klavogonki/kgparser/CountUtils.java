package ru.klavogonki.kgparser;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br>
 * <br>
 * Утилитный класс для выполнения типичных расчетов.
 */
public class CountUtils
{
	public static long getAverageInt(int... speeds) {
		long sum = 0;
		for (int speed : speeds)
		{
			sum += speed;
		}

		return sum / speeds.length; // todo: use round from double division
	}
	public static double getAverageDouble(int... speeds) {
		long sum = 0;
		for (int speed : speeds)
		{
			sum += speed;
		}

		return sum / (speeds.length * 1.0);
	}
}
