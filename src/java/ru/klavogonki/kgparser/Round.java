/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:23:35
 */
package ru.klavogonki.kgparser;

import java.util.ArrayList;
import java.util.List;

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
}