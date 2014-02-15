package ru.klavogonki.kgparser.servlet.model.basicInfo;

import su.opencode.kefir.srv.json.JsonObject;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionDictionary extends JsonObject
{
	public String getDictionaryCode() {
		return dictionaryCode;
	}
	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
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
	public Integer getRoundsCount() {
		return roundsCount;
	}
	public void setRoundsCount(Integer roundsCount) {
		this.roundsCount = roundsCount;
	}

	private String dictionaryCode;

	private String dictionaryName;

	private Boolean dictionaryIsStandard;

	private String dictionaryLink;

	private String dictionaryColor;

	private Integer roundsCount;
}