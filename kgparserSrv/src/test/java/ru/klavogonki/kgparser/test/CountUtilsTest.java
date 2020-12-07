package ru.klavogonki.kgparser.test;

import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.CountUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
class CountUtilsTest
{
	@Test
	void testGetAverageInt() {
		int[] values;
		long avg;

		values = new int[] { 100, 200, 300 };
		avg = CountUtils.getAverageInt(values);
		assertEquals(200, avg);

		values = new int[] { 100, 200, 350 };
		avg = CountUtils.getAverageInt(values);
		assertEquals(216, avg);
	}

	@Test
	void testGetAverageDouble() {
		int[] values;
		double avg;

		values = new int[] { 100, 200, 350 };
		avg = CountUtils.getAverageDouble(values);
		assertEquals(21667, Math.round(avg * 100));
	}
}
