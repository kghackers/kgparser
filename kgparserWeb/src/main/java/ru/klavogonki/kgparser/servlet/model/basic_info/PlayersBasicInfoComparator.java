package ru.klavogonki.kgparser.servlet.model.basic_info;

import java.util.Comparator;

/**
 * Упорядочивает игроков сначала по убыванию количества доездов в соревновании,
 *  а внутри одинакового количества заездов &mdash; по имени.
 */
public class PlayersBasicInfoComparator implements Comparator<PlayerBasicInfo>
{
	@Override
	public int compare(PlayerBasicInfo o1, PlayerBasicInfo o2) {
		Integer count1 = o1.getTotalRoundsCount();
		Integer count2 = o2.getTotalRoundsCount();

		if ( count1.equals(count2) ) {
			return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() ); // asc
		}

		return count2.compareTo(count1); // desc
	}
}
