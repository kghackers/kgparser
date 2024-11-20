package ru.klavogonki.kgparser;

import su.opencode.kefir.srv.json.JsonObject;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br>
 * <br>
 * Оболочка для передачи данных о ранге.
 */
public class RankDto extends JsonObject
{
	public RankDto(Rank rank) {
		if (rank == null)
			throw new IllegalArgumentException("rank cannot be null");

		this.name = rank.toString();
		this.level = rank.level;
		this.color = rank.color;
		this.displayName = rank.displayName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	private String name;

	private int level;

	private String color;

	private String displayName;
}
