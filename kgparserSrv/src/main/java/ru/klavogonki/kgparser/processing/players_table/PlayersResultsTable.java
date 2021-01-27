package ru.klavogonki.kgparser.processing.players_table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.Round;
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
		this.competitionName = competition.getName();

		fillHeaderRows(competition);
		fillPlayersRows(competition);
	}

	private void fillHeaderRows(Competition competition) {
		StringBuilder sb = new StringBuilder();
		HeaderRow firstRow = new HeaderRow();

		// player data
		firstRow.addEmptyCell(4);
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
		Set<Player> players = competition.getPlayers();

		List<PlayerResult> playerResults = new ArrayList<>();
		for (Player player : players)
		{
			if (player.isGuest())
			{
				continue;
			}

			playerResults.add( new PlayerResult(player, competition) );
		}
		playerResults.sort(new PlayerResultsComparator());


		this.playersRows = new ArrayList<>();

		for (PlayerResult playerResult : playerResults)
		{
			Player player = playerResult.getPlayer();
			if (player.isGuest()) // do not add guests to the table
			{
				continue;
			}

			PlayerRow row = new PlayerRow();
			row.addCell( player.getProfileId().toString() );
			row.addCell( player.getName() );
			row.addCell( Rank.getDisplayName(player.getRank()) );
			row.addCell( (player.getNormalRecord() == null) ? null : player.getNormalRecord().toString() );

			for (Round round : competition.getRounds())
			{
				PlayerRoundResult playerRoundResult = round.getPlayerResult(player);
				if (playerRoundResult == null)
				{ // игрок не участвовал в заезде
					logger.info(
						"Player \"{}\" (profileId = {}) has not finished in round number {}.",
						player.getName(),
						player.getProfileId(),
						round.getNumber()
					);

					row.addEmptyCell();
					row.addEmptyCell();
					row.addEmptyCell();
				}
				else
				{ // игрок участвовал в заезде
					row.addCell( playerRoundResult.getSpeed().toString() );
					row.addCell( playerRoundResult.getErrorsCount().toString() );
					row.addCell( StringUtils.formatDouble(playerRoundResult.getErrorPercentage()) );
				}
			}

			row.addCell( Integer.toString( playerResult.getRoundsCount()) );
			row.addCell( StringUtils.formatDouble( playerResult.getAverageSpeed()) );
			row.addCell( StringUtils.formatDouble( playerResult.getAverageErrorPercentage()) );
			row.addCell( Integer.toString( playerResult.getTotalErrorsCount() ) );

			this.playersRows.add(row);
		}
	}

	public String getCompetitionName() {
		return competitionName;
	}
	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}
	public List<HeaderRow> getHeaderRows() {
		return headerRows;
	}
	public void setHeaderRows(List<HeaderRow> headerRows) {
		this.headerRows = headerRows;
	}
	public List<PlayerRow> getPlayersRows() {
		return playersRows;
	}
	public void setPlayersRows(List<PlayerRow> playersRows) {
		this.playersRows = playersRows;
	}

	private String competitionName;

	private List<HeaderRow> headerRows;

	private List<PlayerRow> playersRows;

	private static final Logger logger = LogManager.getLogger(PlayersResultsTable.class);
}
