package ru.klavogonki.kgparser.test;

import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.CountUtils;

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(avg).isEqualTo(200);

		values = new int[] { 100, 200, 350 };
		avg = CountUtils.getAverageInt(values);
		assertThat(avg).isEqualTo(216);
	}

	@Test
	void testGetAverageDouble() {
		int[] values;
		double avg;

		values = new int[] { 100, 200, 350 };
		avg = CountUtils.getAverageDouble(values);

		assertThat(Math.round(avg * 100)).isEqualTo(21667);
	}
}
