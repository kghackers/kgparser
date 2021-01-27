package ru.klavogonki.kgparser.processing.players_table;

import su.opencode.kefir.srv.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Оболочка для заголовка таблицы.
 * Таблица может иметь несколько заголовочных рядов.
 */
public class PlayerRow extends JsonObject
{
	public PlayerRow() {
		cells = new ArrayList<>();
	}

	public List<PlayerCell> getCells() {
		return cells;
	}
	public void setCells(List<PlayerCell> cells) {
		this.cells = cells;
	}

	public void addCell(PlayerCell cell) {
		this.cells.add(cell);
	}
	public void addCell(String text) {
		this.cells.add( new PlayerCell(text) );
	}
	public void addCell(String text, Integer colSpan) {
		this.cells.add( new PlayerCell(text, colSpan) );
	}
	public void addEmptyCell() {
		this.cells.add( new PlayerCell(null) );
	}
	public void addEmptyCell(Integer colSpan) {
		this.cells.add( new PlayerCell(null, colSpan) );
	}

	private List<PlayerCell> cells;
}
