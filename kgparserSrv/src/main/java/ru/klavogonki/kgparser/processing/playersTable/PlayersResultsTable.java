package ru.klavogonki.kgparser.processing.playersTable;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */

import org.apache.log4j.Logger;
import ru.klavogonki.kgparser.*;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Результирующая таблица игроков.
 * Присутствуют все {@linkplain ru.klavogonki.kgparser.Player игроки},
 * проехавшие хотя бы один {@linkplain ru.klavogonki.kgparser.Round заезд} в {@linkplain ru.klavogonki.kgparser.Competition соревновании}.
 */
public class PlayersResultsTable extends JsonObject
{
	public void fillTable(Competition competition) {
		fillHeaderRows(competition);
		fillPlayersRows(competition);
	}

	private void fillHeaderRows(Competition competition) {
		StringBuilder sb = new StringBuilder();
		HeaderRow firstRow = new HeaderRow();


		// player data
		firstRow.addEmptyCell(4);

		firstRow.addEmptyCell();
		firstRow.addEmptyCell();
		firstRow.addEmptyCell();
		firstRow.addEmptyCell();
		// todo: нужные пробеги

		// rounds data
		for (Round round : competition.getRounds())
		{
			firstRow.addCell( concat(sb, "Заезд № ", round.getNumber()), 3 );
		}

		// total
		firstRow.addCell("Итог", 4);

		HeaderRow secondRow = new HeaderRow();

		// player data
		secondRow.addCell("id");
		secondRow.addCell("Логин");
		secondRow.addCell("Ранг");
		secondRow.addCell("Рекорд в обычном");
		// todo: нужные пробеги

		// rounds data
		for (Round round : competition.getRounds())
		{
			secondRow.addCell("Скорость");
			secondRow.addCell("Количество ошибок");
			secondRow.addCell("Процент ошибок");
		}

		// total
		secondRow.addCell("Всего заездов");
		secondRow.addCell("Средняя скорость");
		secondRow.addCell("Средний процент ошибок");
		secondRow.addCell("Всего ошибок");

		this.headerRows = new ArrayList<>();
		this.headerRows.add(firstRow);
		this.headerRows.add(secondRow);
	}

	private void fillPlayersRows(Competition competition) {
		StringBuilder sb = new StringBuilder();

		Set<Player> players = competition.getPlayers();
		// todo: order players as needed

		List<Round> rounds = competition.getRounds();

		this.playerRows = new ArrayList<>();

		for (Player player : players)
		{
			if (player.isGuest()) // do not add guests to the table
				continue;

			int playerRoundsCount = 0;
			long speedSum = 0;
			int totalErrorsCount = 0;
			double errorsPercentageSum = 0;

			PlayerRow row = new PlayerRow();
			row.addCell( player.getProfileId().toString() );
			row.addCell( player.getName() );
			row.addCell( Rank.getDisplayName(player.getRank()) );
			row.addCell( (player.getNormalRecord() == null) ? null : player.getNormalRecord().toString() );

			// todo: count average params

			for (Round round : rounds)
			{
				PlayerRoundResult playerResult = round.getPlayerResult(player);
				if (playerResult == null)
				{ // игрок не участвовал в заезде
					logger.info( concat(sb, "Player \"", player.getName(), "\" (profileId = ", player.getProfileId(), ") has not finished in round number ", round.getNumber() ) );

					row.addEmptyCell();
					row.addEmptyCell();
					row.addEmptyCell();
				}
				else
				{ // игрок участвовал в заезде
					playerRoundsCount++;
					speedSum += playerResult.getSpeed();
					totalErrorsCount += playerResult.getErrorsCount();
					errorsPercentageSum += playerResult.getErrorPercentage();

					row.addCell( playerResult.getSpeed().toString() );
					row.addCell( playerResult.getErrorsCount().toString() );
					row.addCell( StringUtils.formatDouble( playerResult.getErrorPercentage()) );
				}
			}

			double averageSpeed = speedSum / playerRoundsCount;
			double averageErrorPercentage = errorsPercentageSum / playerRoundsCount;

			row.addCell( Integer.toString(playerRoundsCount) );
			row.addCell( StringUtils.formatDouble(averageSpeed) );
			row.addCell( StringUtils.formatDouble(averageErrorPercentage) );
			row.addCell( Integer.toString(totalErrorsCount) );

			this.playerRows.add(row);
		}
	}

	public List<HeaderRow> getHeaderRows() {
		return headerRows;
	}
	public void setHeaderRows(List<HeaderRow> headerRows) {
		this.headerRows = headerRows;
	}
	public List<PlayerRow> getPlayerRows() {
		return playerRows;
	}
	public void setPlayerRows(List<PlayerRow> playerRows) {
		this.playerRows = playerRows;
	}

	private List<HeaderRow> headerRows;

	private List<PlayerRow> playerRows;

	private static final Logger logger = Logger.getLogger(PlayersResultsTable.class);
}