/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:23:35
 */
package ru.klavogonki.kgparser;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Заезд. Содержит результаты всех игроков в этом заезде.
 */
public class Round
{
	public Round() {
		this.results = new ArrayList<>();
	}
	public void addResult(PlayerRoundResult result) {
		for (PlayerRoundResult playerRoundResult : results)
			if (playerRoundResult.getPlayer().equals(result.getPlayer()))
				throw new IllegalArgumentException("Round already contains result for player: " + result.getPlayer().getProfileId() + " (name: " + result.getPlayer().getName() + ")");

		result.setRound(this);
		results.add(result);
	}

	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public long getBeginTimeMilliseconds() {
		return beginTimeMilliseconds;
	}
	public void setBeginTimeMilliseconds(long beginTimeMilliseconds) {
		this.beginTimeMilliseconds = beginTimeMilliseconds;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Dictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<PlayerRoundResult> getResults() {
		return results;
	}
	public void setResults(List<PlayerRoundResult> results) {
		this.results = results;
	}

	public int getTextLength() {
		return text.length();
	}

	public PlayerRoundResult getPlayerResult(Player player) {
		if (player == null)
			throw new IllegalArgumentException("player cannot be null");

		Integer profileId = player.getProfileId();
		if (profileId == null)
			throw new IllegalArgumentException("player has no profileId");

		for (PlayerRoundResult result : results)
		{
			Player roundPlayer = result.getPlayer();
			if (roundPlayer == null)
				throw new IllegalStateException("PlayerRoundResult has no player");

			Integer roundPlayerProfileId = roundPlayer.getProfileId();
			if (roundPlayerProfileId == null)
				throw new IllegalStateException("PlayerRoundResult has player with no profileId");

			if ( roundPlayerProfileId.equals(profileId) )
				return result;
		}

		return null;
	}

	/**
	 * @param player игрок
	 * @return <code>true</code> &mdash; если игрок участвовал в заезде (имеет результат в нем).
	 * <br/>
	 * <code>false</code> &mdash; в противном случае.
	 */
	public boolean hasPlayerResult(Player player) {
		PlayerRoundResult playerResult = getPlayerResult(player);
		if (playerResult == null)
			return false;

		// todo: maybe check result emptiness and return false if result is empty
		return true;
	}

	/**
	 * @param dictionary словарь
	 * @return <code>true</code> &mdash; если заезд проходил по указанному словарю
	 * <br/>
	 * <code>false</code> &mdash; в противном случае
	 */
	public boolean hasDictionary(Dictionary dictionary) {
		if (dictionary == null)
			throw new IllegalArgumentException("dictionary cannot be null");

		return this.dictionary.isSame(dictionary);
	}

	/**
	 * @return минимальное место игрока в заезде или -1, если ни в одном из результатов игроков нет установленного места.
	 * Должно быть равно {@linkplain PlayerRoundResult#FIRST_PLACE первому месту}.
	 */
	public int getMinPlace() {
		int minPlace = -1;

		for (PlayerRoundResult result : results)
		{
			Integer playerPlace = result.getPlace();
			if (playerPlace == null)
			{
				logger.info( concat("PlayerRoundResult for player has no place set") ); // todo: add player info
				continue; // player place not set -> ignore this result
			}
			else if (minPlace == -1)
			{
				minPlace = playerPlace;
			}
			else if (playerPlace < minPlace)
			{
				minPlace = playerPlace;
			}
		}

		return minPlace;
	}

	/**
	 * @return минимальное место доехавшего игрока в заезде или -1, если ни в одном из результатов игроков нет установленного места
	 * Должно быть равно количеству доехавших игроков. // todo: ссылка на метод, определяющий finished
	 */
	public int getMaxPlace() {
		int maxPlace = -1;

		for (PlayerRoundResult result : results)
		{
			Integer playerPlace = result.getPlace();
			if (playerPlace == null)
			{
				logger.info( concat("PlayerRoundResult for player has no place set") ); // todo: add player info
				continue; // player place not set -> ignore this result
			}
			else if (maxPlace == -1)
			{
				maxPlace = playerPlace;
			}
			else if (playerPlace > maxPlace)
			{
				maxPlace = playerPlace;
			}
		}

		return maxPlace;
	}

	/**
	 * @return список результатов заезда, упорядоченных по месту игрока в заезде
	 */
	public List<PlayerRoundResult> getResultsSortedByPlace() {
		List<PlayerRoundResult> sortedResults = new ArrayList<>();
		for (PlayerRoundResult result : results)
			sortedResults.add(result);

		Collections.sort(sortedResults, new PlayerRoundResultPlacesComparator());

		// check places
		int minPlace = getMinPlace();
		int maxPlace = getMaxPlace();

		for (int correctPlace = minPlace; correctPlace <= maxPlace; correctPlace++)
		{
			Integer place = sortedResults.get(correctPlace - 1).getPlace();
			if ( (place == null) || (place != correctPlace) )
				throw new IllegalStateException( concat("Incorrect PlayerRoundResult place: must be ", correctPlace, ", but is ", place) ); // todo: add player info
		}

		return sortedResults;
	}

	/**
	 * Идентификатор игры.
	 */
	private int gameId;

	/**
	 * Время начала времени в Unix-time (миллисекунды).
	 */
	private long beginTimeMilliseconds;

	/**
	 * Дата и время начала игры
	 */
	private Date beginTime;

	/**
	 * Словарь.
	 */
	private Dictionary dictionary;

	/**
	 * Номер заезда (в рамках игры).
	 * // todo: порешить, как нумеруются заезды в случае, когда соревнование состоит из нескольких режимов
	 */
	private Integer number;

	/**
	 * Текст заезда без ошибок.
	 */
	private String text;

	/**
	 * Результаты игроков в заезде.
	 */
	private List<PlayerRoundResult> results;

	private static final Logger logger = Logger.getLogger(Round.class);
}