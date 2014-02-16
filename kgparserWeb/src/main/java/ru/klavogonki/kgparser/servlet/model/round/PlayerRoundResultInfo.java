package ru.klavogonki.kgparser.servlet.model.round;

import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.servlet.model.basicInfo.PlayerBasicInfo;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.StringUtils;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class PlayerRoundResultInfo extends JsonObject
{
	public PlayerRoundResultInfo() {
	}
	public PlayerRoundResultInfo(PlayerRoundResult result) {
		this.setPlayer( new PlayerBasicInfo(result.getPlayer()) );
		this.setPlace(result.getPlace());
		this.setSpeed(result.getSpeed());
		this.setCharsTotal(result.getCharsTotal());
		this.setErrorsCount(result.getErrorsCount());
		this.setErrorPercentage(result.getErrorPercentage());
		this.setErrorPercentageStr( StringUtils.formatDouble( result.getErrorPercentage()) );
	}

	public PlayerBasicInfo getPlayer() {
		return player;
	}
	public void setPlayer(PlayerBasicInfo player) {
		this.player = player;
	}
	public Integer getPlace() {
		return place;
	}
	public void setPlace(Integer place) {
		this.place = place;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getCharsTotal() {
		return charsTotal;
	}
	public void setCharsTotal(Integer charsTotal) {
		this.charsTotal = charsTotal;
	}
	public Integer getErrorsCount() {
		return errorsCount;
	}
	public void setErrorsCount(Integer errorsCount) {
		this.errorsCount = errorsCount;
	}
	public Double getErrorPercentage() {
		return errorPercentage;
	}
	public void setErrorPercentage(Double errorPercentage) {
		this.errorPercentage = errorPercentage;
	}
	public String getErrorPercentageStr() {
		return errorPercentageStr;
	}
	public void setErrorPercentageStr(String errorPercentageStr) {
		this.errorPercentageStr = errorPercentageStr;
	}

	public String getErrorsCountStr() {
		if (errorsCount == null)
			return "0";

		return errorsCount.toString();
	}

	private PlayerBasicInfo player;

	private Integer place;

	private Integer speed;

	private Integer charsTotal;

	private Integer errorsCount;

	private Double errorPercentage;

	private String errorPercentageStr;
}