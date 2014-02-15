package ru.klavogonki.kgparser.servlet.model.basicInfo;

import su.opencode.kefir.srv.json.JsonObject;

import java.util.Date;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class RoundBasicInfo extends JsonObject
{
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
	public String getDictionaryName() {
		return dictionaryName;
	}
	public void setDictionaryName(String dictionaryName) {
		this.dictionaryName = dictionaryName;
	}
	public Boolean getDictionaryIsStandard() {
		return dictionaryIsStandard;
	}
	public void setDictionaryIsStandard(Boolean dictionaryIsStandard) {
		this.dictionaryIsStandard = dictionaryIsStandard;
	}
	public String getDictionaryLink() {
		return dictionaryLink;
	}
	public void setDictionaryLink(String dictionaryLink) {
		this.dictionaryLink = dictionaryLink;
	}
	public String getDictionaryColor() {
		return dictionaryColor;
	}
	public void setDictionaryColor(String dictionaryColor) {
		this.dictionaryColor = dictionaryColor;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public String getBeginTimeStr() {
		return beginTimeStr;
	}
	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}
	public Integer getFinishedPlayersCount() {
		return finishedPlayersCount;
	}
	public void setFinishedPlayersCount(Integer finishedPlayersCount) {
		this.finishedPlayersCount = finishedPlayersCount;
	}

	private Integer number;

	private Integer numberInDictionary;

	private String dictionaryName;

	private Boolean dictionaryIsStandard;

	private String dictionaryLink;

	private String dictionaryColor;

	private Date beginTime;

	private String beginTimeStr;

	private Integer finishedPlayersCount;
}