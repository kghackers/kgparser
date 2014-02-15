package ru.klavogonki.kgparser.servlet.model.basicInfo;

import su.opencode.kefir.srv.json.JsonObject;

import java.util.Map;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class PlayerBasicInfo extends JsonObject
{
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public String getProfileLink() {
		return profileLink;
	}
	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getRankColor() {
		return rankColor;
	}
	public void setRankColor(String rankColor) {
		this.rankColor = rankColor;
	}
	public Integer getTotalRoundsCount() {
		return totalRoundsCount;
	}
	public void setTotalRoundsCount(Integer totalRoundsCount) {
		this.totalRoundsCount = totalRoundsCount;
	}
	public Map<String, Integer> getDictionariesRoundsCount() {
		return dictionariesRoundsCount;
	}
	public void setDictionariesRoundsCount(Map<String, Integer> dictionariesRoundsCount) {
		this.dictionariesRoundsCount = dictionariesRoundsCount;
	}

	private Integer profileId;

	private String profileLink;

	private String name;

	private String rank;

	private String rankColor;

	private Integer totalRoundsCount;

	private Map<String, Integer> dictionariesRoundsCount;
}