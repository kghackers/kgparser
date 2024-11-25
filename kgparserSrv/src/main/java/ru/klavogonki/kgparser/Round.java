package ru.klavogonki.kgparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.common.StandardDictionary;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Заезд. Содержит результаты всех игроков в этом заезде.
 */
public class Round extends JsonObject
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
	public Integer getNumberInDictionary() {
		return numberInDictionary;
	}
	public void setNumberInDictionary(Integer numberInDictionary) {
		this.numberInDictionary = numberInDictionary;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public List<PlayerRoundResult> getResults() {
		return results;
	}
	public void setResults(List<PlayerRoundResult> results) {
		this.results = results;

		if ( ObjectUtils.notEmpty(this.results) )
			for (PlayerRoundResult result : results)
				result.setRound(this);
	}

	@Json(exclude = true) // todo: think about this
	public int getTextLength() {
		return text.length();
	}

	@Json(exclude = true)
	public PlayerRoundResult getPlayerResult(Player player) {
		if (player == null)
			throw new IllegalArgumentException("player cannot be null");

		Integer profileId = player.getProfileId();
		if (profileId == null)
			throw new IllegalArgumentException("player has no profileId");

		if ( ObjectUtils.empty(results) )
			return null;

		for (PlayerRoundResult result : results)
		{
			Player roundPlayer = result.getPlayer();
			if (roundPlayer == null)
				throw new IllegalStateException("PlayerRoundResult has no player");

			if ( roundPlayer.isGuest() )
				continue;

			Integer roundPlayerProfileId = roundPlayer.getProfileId();

			if ( roundPlayerProfileId.equals(profileId) )
				return result;
		}

		return null;
	}

	/**
	 * @param player игрок
	 * @return <code>true</code> &mdash; если игрок участвовал в заезде (имеет результат в нем).
	 * <br>
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
	 * <br>
	 * <code>false</code> &mdash; в противном случае
	 */
	public boolean hasDictionary(Dictionary dictionary) {
		if (dictionary == null)
			throw new IllegalArgumentException("dictionary cannot be null");

		return this.dictionary.isSame(dictionary);
	}

	/**
	 * @return минимальное место игрока в заезде или -1, если ни в одном из результатов игроков нет установленного места.
	 * Должно быть равно {@linkplain ru.klavogonki.kgparser.PlayerRoundResult#FIRST_PLACE первому месту}.
	 */
	@Json(exclude = true)
	public int getMinPlace() {
		int minPlace = -1;

		for (PlayerRoundResult result : results)
		{
			Integer playerPlace = result.getPlace();
			if (playerPlace == null)
			{ // player place not set -> ignore this result
				logger.info("PlayerRoundResult for player has no place set."); // todo: add player info
				continue;
			}

			if (minPlace == -1)
			{
				minPlace = playerPlace;
				continue;
			}

			if (playerPlace < minPlace)
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
	@Json(exclude = true)
	public int getMaxPlace() {
		int maxPlace = -1;

		for (PlayerRoundResult result : results)
		{
			Integer playerPlace = result.getPlace();
			if (playerPlace == null)
			{
				logger.info("PlayerRoundResult for player has no place set."); // todo: add player info
				continue; // player place not set -> ignore this result
			}

			if (maxPlace == -1)
			{
				maxPlace = playerPlace;
				continue;
			}

			if (playerPlace > maxPlace)
			{
				maxPlace = playerPlace;
			}
		}

		return maxPlace;
	}

	/**
	 * @return список результатов заезда, упорядоченных по месту игрока в заезде
	 */
	@Json(exclude = true)
	public List<PlayerRoundResult> getResultsSortedByPlace() {
		if ( ObjectUtils.empty(results) )
			return Collections.emptyList();

		List<PlayerRoundResult> sortedResults = new ArrayList<>(results);
		sortedResults.sort(new PlayerRoundResultPlacesComparator());

		// check places
		int minPlace = getMinPlace();
		int maxPlace = getMaxPlace();

		for (int correctPlace = minPlace; correctPlace <= maxPlace; correctPlace++)
		{
			Integer place = sortedResults.get(correctPlace - 1).getPlace();
			if ( (place == null) || (place != correctPlace) ) {
				String errorMessage = String.format("Incorrect PlayerRoundResult place: must be %d, but is %d.", correctPlace, place);
				throw new IllegalStateException(errorMessage); // todo: add player info
			}
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
	 * Номер заезда (в рамках соревнования целиком).
	 */
	private Integer number;

	/**
	 * Номер заезда в рамках конкретного словаря.
	 */
	private Integer numberInDictionary;

	/**
	 * Текст заезда без ошибок.
	 */
	private String text;

	/**
	 * Автор книги, из которой взят отрывок, по которому проходит заезд.
	 * <br>
	 * Заполняется только для следующих стандартных режимов:
	 * <ul>
	 *   <li>{@linkplain StandardDictionary#normal Обычный}</li>
	 *   <li>{@linkplain StandardDictionary#noerror Безошибочный}</li>
	 *   <li>{@linkplain StandardDictionary#sprint Спринт}</li>
	 *   <li>{@linkplain StandardDictionary#marathon Марафон}</li>
	 * </ul>
	 */
	private String bookAuthor;

	/**
	 * Название книги, из которой взят отрывок, по которому проходит заезд.
	 * <br>
	 * Заполняется только для следующих стандартных режимов:
	 * <ul>
	 *   <li>{@linkplain StandardDictionary#normal Обычный}</li>
	 *   <li>{@linkplain StandardDictionary#noerror Безошибочный}</li>
	 *   <li>{@linkplain StandardDictionary#sprint Спринт}</li>
	 *   <li>{@linkplain StandardDictionary#marathon Марафон}</li>
	 * </ul>
	 */
	private String bookName;

	/**
	 * Результаты игроков в заезде.
	 */
	private List<PlayerRoundResult> results;

	/**
	 * Номер первого заезда.
	 */
	public static final int FIRST_ROUND_NUMBER = 1;

	private static final Logger logger = LogManager.getLogger(Round.class);
}
