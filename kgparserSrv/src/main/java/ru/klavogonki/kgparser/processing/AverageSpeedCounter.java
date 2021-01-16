package ru.klavogonki.kgparser.processing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.CountUtils;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import su.opencode.kefir.util.ObjectUtils;

import java.util.List;
import java.util.Set;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public final class AverageSpeedCounter {

    private AverageSpeedCounter() {
    }

    public static void logCompetitionInfo(Competition competition) {
        Set<Player> players = competition.getPlayers();
        Set<Dictionary> dictionaries = competition.getDictionaries();

        for (Player player : players) {
            logger.info("");
            logger.info("=====================================================");
            Double avgSpeed = getAvgSpeed(competition, player);
            logger.info("Player {} (profileId = {}).", player.getName(), player.getProfileId());
            logger.info(" Total average speed: {}", avgSpeed);

            for (Dictionary dictionary : dictionaries) {
                Double avgDictionarySpeed = getAvgSpeed(competition, player, dictionary);
                logger.info("Dictionary {} (code = {}). Dictionary average speed: {}.", dictionary.getName(), dictionary.getCode(), avgDictionarySpeed);
            }
        }
    }

    public static Double getAvgSpeed(Competition competition, Player player) {
        List<PlayerRoundResult> results = competition.getPlayerRoundResults(player);
        if (ObjectUtils.empty(results)) {
            return null;
        }

        return getAverageDouble(results);
    }

    public static Double getAvgSpeed(Competition competition, Player player, Dictionary dictionary) {
        List<PlayerRoundResult> results = competition.getPlayerRoundResults(player, dictionary);
        if (ObjectUtils.empty(results)) {
            return null;
        }

        return getAverageDouble(results);
    }

    private static Double getAverageDouble(List<PlayerRoundResult> results) {
        int[] speeds = new int[results.size()];

        for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
            PlayerRoundResult result = results.get(i);
            speeds[i] = result.getSpeed();
        }

        return CountUtils.getAverageDouble(speeds);
    }

    private static final Logger logger = LogManager.getLogger(AverageSpeedCounter.class);
}
