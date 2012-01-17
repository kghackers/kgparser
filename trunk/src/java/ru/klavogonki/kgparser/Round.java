/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:23:35
 */
package ru.klavogonki.kgparser;

import java.util.List;
import java.util.ArrayList;

/**
 * Заезд. Содержит результаты всех игроков в этом заезде.
 */
public class Round
{
	public Round() {
		this.results = new ArrayList<PlayerRoundResult>();
	}
	public void addResult(PlayerRoundResult result) {
		for (PlayerRoundResult playerRoundResult : results)
			if (playerRoundResult.getPlayer().equals(result.getPlayer()))
				throw new IllegalArgumentException("Round already contains result for player: " + result.getPlayer().getProfileId() + " (name: " + result.getPlayer().getName() + ")");

		results.add(result);	
	}

	public Vocabulary getVocabulary() {
		return vocabulary;
	}
	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
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
	
	/**
	 * Словарь.
	 */
	private Vocabulary vocabulary;

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
