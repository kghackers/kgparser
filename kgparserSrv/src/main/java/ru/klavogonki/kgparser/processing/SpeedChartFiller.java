package ru.klavogonki.kgparser.processing;

import org.apache.log4j.Logger;
import ru.klavogonki.kgparser.*;
import ru.klavogonki.kgparser.http.HttpClientTest;

import java.util.*;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class SpeedChartFiller
{
//	public static void main(String[] args) {
//		fillData()
//		System.out.println( value.toJson() );
//	}
	public static HighChartValue fillData(Competition competition) {
		List<String> categories = new ArrayList<>();

		Set<Player> players = competition.getPlayers();

/*
		for (Player player : players)
		{ // set ranks to normal mode ranks
			if (player.isGuest())
				continue;

			player.setRank(HttpClientTest.getUserRank(player.getProfileId()));
		}
*/

		// fill present ranks
		logger.info("===========================================");
		SortedSet<Rank> ranks = competition.getRanks();
		logger.info( concat("Present player ranks (normal ranks): ", ranks.toString()) );

		List<RankDto> ranksDto = new ArrayList<>();
		for (Rank rank : ranks)
			ranksDto.add( new RankDto(rank) );


		List<Round> rounds = competition.getRounds();

		List<HighChartSeries> seriesList = new ArrayList<>();

		for (Round round : rounds)
		{
			categories.add( round.getNumber().toString() );
		}

		for (Player player : players)
		{
			if ( player.isGuest() )
				continue; // do not add guests to the chart

/*
			if ( player.getName().equals("Juicee") ) // todo: remove this
				continue; // remove cheater
*/

			List<Integer> speeds = new ArrayList<>();

			for (Round round : rounds)
			{
				PlayerRoundResult playerResult = round.getPlayerResult(player);

				if (playerResult == null)
					speeds.add(null);
				else
					speeds.add(playerResult.getSpeed());
			}

			HighChartSeries series = new HighChartSeries();
			series.setName( player.getName() );
			series.setRank(player.getRank().toString());
			series.setRankDisplayName(Rank.getDisplayName(player.getRank()));
			series.setColor( Rank.getColor( player.getRank()) );
			series.setData(speeds);
			seriesList.add(series);
		}

		Collections.sort(seriesList, new HighChartSeriesNameComparator() ); // order by name

		HighChartValue value = new HighChartValue();
		value.setCategories(categories);
		value.setSeries(seriesList);
		value.setRanks(ranksDto);
		return value;
	}

	private static final Logger logger = Logger.getLogger(SpeedChartFiller.class);
}