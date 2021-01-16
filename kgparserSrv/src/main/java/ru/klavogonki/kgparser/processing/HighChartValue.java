package ru.klavogonki.kgparser.processing;

import ru.klavogonki.kgparser.RankDto;
import su.opencode.kefir.srv.json.JsonObject;

import java.util.List;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class HighChartValue extends JsonObject
{
	public String getCompetitionName() {
		return competitionName;
	}
	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<HighChartSeries> getSeries() {
		return series;
	}
	public void setSeries(List<HighChartSeries> series) {
		this.series = series;
	}
	public List<RankDto> getRanks() {
		return ranks;
	}
	public void setRanks(List<RankDto> ranks) {
		this.ranks = ranks;
	}

	private String competitionName;

	private List<String> categories;

	private List<HighChartSeries> series;

	private List<RankDto> ranks;
}
