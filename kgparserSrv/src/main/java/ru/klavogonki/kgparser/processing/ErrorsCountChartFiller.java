package ru.klavogonki.kgparser.processing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.RankDto;
import ru.klavogonki.kgparser.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public final class ErrorsCountChartFiller {

    private ErrorsCountChartFiller() {
    }

    public static HighChartValue fillData(Competition competition) {
        List<String> categories = new ArrayList<>();

        Set<Player> players = competition.getPlayers();

        // fill present ranks
        logger.info("===========================================");
        SortedSet<Rank> ranks = competition.getRanks();
        logger.info("Present player ranks (normal ranks): {}", ranks);

        List<RankDto> ranksDto = new ArrayList<>();
        for (Rank rank : ranks)
            ranksDto.add(new RankDto(rank));

        List<Round> rounds = competition.getRounds();

        List<HighChartSeries> seriesList = new ArrayList<>();

        for (Round round : rounds) {
            categories.add(round.getNumber().toString());
        }


        for (Player player : players) {
            if (player.isGuest())
                continue; // do not add guests to the chart

/*
			if ( player.getName().equals("Juicee") ) // todo: remove this
				continue; // remove cheater
*/

            List<Integer> errorsCounts = new ArrayList<>();

            for (Round round : rounds) {
                PlayerRoundResult playerResult = round.getPlayerResult(player);

                if (playerResult == null) {
                    errorsCounts.add(null);
                }
                else {
                    errorsCounts.add(playerResult.getErrorsCount());
                }
            }

            HighChartSeries series = new HighChartSeries();
            series.setName(player.getName());
            series.setRank(player.getRank().toString());
            series.setRankDisplayName(Rank.getDisplayName(player.getRank()));
            series.setColor(Rank.getColor(player.getRank()));
            series.setData(errorsCounts);
            seriesList.add(series);
        }

        seriesList.sort(new HighChartSeriesNameComparator()); // order players by name

        HighChartValue value = new HighChartValue();
        value.setCompetitionName(competition.getName());
        value.setCategories(categories);
        value.setSeries(seriesList);
        value.setRanks(ranksDto);
        return value;
    }

    private static final Logger logger = LogManager.getLogger(ErrorsCountChartFiller.class);
}
