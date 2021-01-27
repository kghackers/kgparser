package ru.klavogonki.kgparser.processing.players_table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Round;

/**
 * Оболочка для агрегатных данных игрока в соревновании.
 */
public class PlayerResult {

    public PlayerResult(Player player, Competition competition) {
        if (player.isGuest()) {
            throw new IllegalArgumentException("Cannot count player results for guest.");
        }

        this.player = player;
        this.roundsCount = competition.getRoundsCount(player);
        if (this.roundsCount <= 0) {
            String errorMessage = String.format(
                "Player \"%s\" (profileId = %d) has no results in Competition \"%s\".",
                player.getName(),
                player.getProfileId(),
                competition.getName()
            );

            throw new IllegalArgumentException(errorMessage);
        }

        long speedSum = 0;
        int competitionTotalErrorsCount = 0;
        double errorsPercentageSum = 0;

        for (Round round : competition.getRounds()) {
            PlayerRoundResult playerResult = round.getPlayerResult(player);
            if (playerResult == null) {
                logger.info(
                    "Player \"{}\" (profileId = {}}) has not finished in round number {}.",
                    player.getName(),
                    player.getProfileId(),
                    round.getNumber()
                );

                continue;
            }

            speedSum += playerResult.getSpeed();
            competitionTotalErrorsCount += playerResult.getErrorsCount();
            errorsPercentageSum += playerResult.getErrorPercentage();
        }

        double competitionAverageSpeed = ((double) speedSum) / this.roundsCount;
        double competitionAverageErrorPercentage = errorsPercentageSum / this.roundsCount;

        this.averageSpeed = competitionAverageSpeed;
        this.averageErrorPercentage = competitionAverageErrorPercentage;
        this.totalErrorsCount = competitionTotalErrorsCount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getRoundsCount() {
        return roundsCount;
    }

    public void setRoundsCount(int roundsCount) {
        this.roundsCount = roundsCount;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getAverageErrorPercentage() {
        return averageErrorPercentage;
    }

    public void setAverageErrorPercentage(double averageErrorPercentage) {
        this.averageErrorPercentage = averageErrorPercentage;
    }

    public int getTotalErrorsCount() {
        return totalErrorsCount;
    }

    public void setTotalErrorsCount(int totalErrorsCount) {
        this.totalErrorsCount = totalErrorsCount;
    }

    /**
     * Игрок.
     */
    private Player player;

    /**
     * Количество заездов игрока в соревновании.
     */
    private int roundsCount;

    /**
     * Средняя скорость игрока в соревновании.
     */
    private double averageSpeed;

    /**
     * Средний процент ошибок игрока в соревновании.
     */
    private double averageErrorPercentage;

    /**
     * Общее число ошибок игрока в соревновании.
     */
    private int totalErrorsCount;

    private static final Logger logger = LogManager.getLogger(PlayerResult.class);
}
