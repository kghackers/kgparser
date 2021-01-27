package ru.klavogonki.kgparser.servlet.model.basic_info;

import ru.klavogonki.kgparser.Dictionary;
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
	public CompetitionDictionary() {
	}
	public CompetitionDictionary(Dictionary dictionary) {
		this.dictionaryCode = dictionary.getCode();
		this.dictionaryIsStandard = dictionary.isStandard();
		this.dictionaryName = dictionary.getName();
		this.dictionaryLink = dictionary.getDictionaryPageUrl();
		this.dictionaryColor = dictionary.getColor();
//		vo.setRoundsCount( competition.getRoundsCount(dictionary) );
	}

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
