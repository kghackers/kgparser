package ru.klavogonki.kgparser.processing.players_table;

import su.opencode.kefir.srv.json.JsonObject;

/**
 * Объект-оболочка для ячейки в теле таблицы.
 * Может иметь объединение столбцов.
 */
public class PlayerCell extends JsonObject
{
	public PlayerCell(String text) {
		this.text = text;
	}

	public PlayerCell(String text, Integer colSpan) {
		this.text = text;
		this.colSpan = colSpan;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getColSpan() {
		return colSpan;
	}
	public void setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
	}

	public boolean hasColSpan() {
		return (colSpan != null) && (colSpan > 1);
	}

	/**
	 * Текст в ячейке.
	 */
	private String text;

	/**
	 * Количество объединенных в ряде ячеек.
	 * Если равно 1 или <code>null</code>, то ячейка не объединена.
	 */
	private Integer colSpan;
}
