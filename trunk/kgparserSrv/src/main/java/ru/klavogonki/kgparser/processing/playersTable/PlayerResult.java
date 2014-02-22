package ru.klavogonki.kgparser.processing.playersTable;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */

import org.apache.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Round;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Оболочка для агрегатных данных игрока в соревновании.
 */
public class PlayerResult
{
	public PlayerResult(Player player, Competition competition) {
		StringBuilder sb = new StringBuilder();

		if (player.isGuest())
			throw new IllegalArgumentException( "Cannot count player results for guest." );

		this.player = player;
		this.roundsCount = competition.getRoundsCount(player);
		if (this.roundsCount <= 0)
			throw new IllegalArgumentException( concat(sb, "Player \"", player.getName(), "\" (profileId = ", player.getProfileId(), ") has no results in Competition \"", competition.getName(), "\".") );

		long speedSum = 0;
		int totalErrorsCount = 0;
		double errorsPercentageSum = 0;

		for (Round round : competition.getRounds())
		{
			PlayerRoundResult playerResult = round.getPlayerResult(player);
			if (playerResult == null)
			{
				logger.info( concat(sb, "Player \"", player.getName(), "\" (profileId = ", player.getProfileId(), ") has not finished in round number ", round.getNumber() ) );
				continue;
			}

			speedSum += playerResult.getSpeed();
			totalErrorsCount += playerResult.getErrorsCount();
			errorsPercentageSum += playerResult.getErrorPercentage();
		}

		double averageSpeed = ((double) speedSum) / this.roundsCount;
		double averageErrorPercentage = errorsPercentageSum / this.roundsCount;

		this.averageSpeed = averageSpeed;
		this.averageErrorPercentage = averageErrorPercentage;
		this.totalErrorsCount = totalErrorsCount;
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

	private static final Logger logger = Logger.getLogger(PlayerResult.class);
}