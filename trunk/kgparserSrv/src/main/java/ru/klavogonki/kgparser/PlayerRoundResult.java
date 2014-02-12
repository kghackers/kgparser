/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:25:37
 */
package ru.klavogonki.kgparser;

import java.util.Date;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Результат игрока в конкретном заезде.
 */
public class PlayerRoundResult
{
	public PlayerRoundResult() {
	}
	public PlayerRoundResult(Round round, Player player) {
		this.round = round;
		this.player = player;
	}

	public Round getRound() {
		return round;
	}
	public void setRound(Round round) {
		this.round = round;
	}

	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getPlace() {
		if (place == null)
			return null;

		if (place < FIRST_PLACE)
			throw new IllegalArgumentException( concat("Incorrect place: ", place) );

		return place;
	}
	public void setPlace(Integer place) {
		if (place < FIRST_PLACE)
			throw new IllegalArgumentException( concat("Incorrect place: ", place) );

		this.place = place;
	}

	public long getFinishedTimeMilliseconds() {
		return finishedTimeMilliseconds;
	}
	public void setFinishedTimeMilliseconds(long finishedTimeMilliseconds) {
		this.finishedTimeMilliseconds = finishedTimeMilliseconds;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		if (speed <= 0)
			throw new IllegalArgumentException( concat("Incorrect speed: ", speed) );

		this.speed = speed;
	}

	public Integer getCharsTotal() {
		return charsTotal;
	}
	public void setCharsTotal(Integer charsTotal) {
		if (charsTotal <= 0)
			throw new IllegalArgumentException( concat("Incorrect charsTotal: ", charsTotal) );

		this.charsTotal = charsTotal;
	}

	public Integer getErrorsCount() {
		return errorsCount;
	}
	public void setErrorsCount(Integer errorsCount) {
		if (errorsCount < 0)
			throw new IllegalArgumentException( concat("Incorrect errorsCount: ", errorsCount) );

		this.errorsCount = errorsCount;
	}

	public Double getErrorPercentage() {
		return errorPercentage;
	}
	public void setErrorPercentage(Double errorPercentage) {
		if (errorPercentage < 0)
			throw new IllegalArgumentException( concat("Incorrect errorPercentage: ", errorPercentage) );

		this.errorPercentage = errorPercentage;
	}

	public boolean isRecord() {
		return record;
	}
	public void setRecord(boolean record) {
		this.record = record;
	}

	public Float getTime() {
		return time;
	}
	public void setTime(Float time) {
		if (time < 0)
			throw new IllegalArgumentException( concat("Incorrect time: ", time) );

		this.time = time;
	}

	public static boolean isCorrectPlace(Integer place) {
		if (place == null)
			return true;

		return (place >= FIRST_PLACE);
	}

	/**
	 * Заезд.
	 */
	private Round round;

	/**
	 * Игрок.
	 */
	private Player player;

	/**
	 * Место в заезде. Может быть 1 или больше.
	 */
	private Integer place;

	/**
	 * Время окончания заезда в Unix-time (миллисекунды).
	 */
	private long finishedTimeMilliseconds;

	/**
	 * Время окончания заезда.
	 */
	private Date finishedTime;

	/**
	 * Скорость набора, в знаках\минуту. Должна быть больше 0.
	 */
	private Integer speed;

	/**
	 * Количество символов, набранных игроком в заезде.
	 * Актуально для марафона, для остальных режимов должно быть равно длине текста заезда.
	 */
	private Integer charsTotal;

	/**
	 * Количество ошибок. Может быть 0 или больше.
	 */
	private Integer errorsCount;

	/**
	 * Процент ошибок. Может быть 0 или больше.
	 */
	private Double errorPercentage;

	/**
	 * <code>true</code> &mdash; если игрок поставил рекорд в данном заезде.
	 * <code>false</code> &mdash; в противном случае.
	 */
	private boolean record = false;

	/**
	 * Время в секундах, за которое игрок прошел заезд. Должно быть больше 0.
	 */
	private Float time;

	/**
	 * Номер первого места в заезде. Место игрока в заезде не может быть меньше, чем первое.
	 */
	public static final int FIRST_PLACE = 1;
}