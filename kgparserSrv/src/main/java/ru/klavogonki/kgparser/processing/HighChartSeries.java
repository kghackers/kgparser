package ru.klavogonki.kgparser.processing;

import su.opencode.kefir.srv.json.JsonObject;

import java.util.List;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class HighChartSeries extends JsonObject
{
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
	public String getRankDisplayName() {
		return rankDisplayName;
	}
	public void setRankDisplayName(String rankDisplayName) {
		this.rankDisplayName = rankDisplayName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}

	private String name;

	private String rank;

	private String rankDisplayName;

	private String color;

	private List<Integer> data;
}