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