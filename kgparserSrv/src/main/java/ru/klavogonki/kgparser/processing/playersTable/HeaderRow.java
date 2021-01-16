package ru.klavogonki.kgparser.processing.playersTable;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */

import su.opencode.kefir.srv.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Оболочка для заголовка таблицы.
 * Таблица может иметь несколько заголовочных рядов.
 */
public class HeaderRow extends JsonObject
{
	public HeaderRow() {
		cells = new ArrayList<>();
	}

	public List<HeaderCell> getCells() {
		return cells;
	}
	public void setCells(List<HeaderCell> cells) {
		this.cells = cells;
	}

	public void addCell(HeaderCell cell) {
		this.cells.add(cell);
	}
	public void addCell(String text) {
		this.cells.add( new HeaderCell(text) );
	}
	public void addCell(String text, Integer colSpan) {
		this.cells.add( new HeaderCell(text, colSpan) );
	}
	public void addEmptyCell() {
		this.cells.add( new HeaderCell(null) );
	}
	public void addEmptyCell(Integer colSpan) {
		this.cells.add( new HeaderCell(null, colSpan) );
	}

	private List<HeaderCell> cells;
}
