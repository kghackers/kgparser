package ru.klavogonki.kgparser;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br/>
 * <br/>
 * Энум с названиями стандартных словарей (режимов), как они используются в ajax-api.
 */
public enum StandardDictionary
{
	/**
	 * Обычный.
	 */
	  normal

	/**
	 * Безошибочный.
	 */
	, noerror

	/**
	 * Буквы.
	 */
	, chars

	/**
	 * Марафон.
	 */
	, marathon

	/**
	 * Абракадабра.
	 */
	, abra

	/**
	 * Яндекс.Рефераты.
	 */
	, referats

	/**
	 * Цифры.
	 */
	, digits

	/**
	 * Спринт.
	 */
	, sprint

	;

	/**
	 * @param dictionary стандартный словарь
	 * @return цвет стандартного словаря в формате &laquo;#123456&raquo;
	 */
	public static String getColor(StandardDictionary dictionary) {
		switch (dictionary)
		{
			case normal: return "#333333";
			case noerror: return "#4692AA";
			case chars: return "#B55900";
			case marathon: return "#D43E68";
			case abra: return "#3D4856";
			case referats: return "#698725";
			case digits: return "#777777";
			case sprint: return "#833F3A";

			default:
				throw new IllegalArgumentException("Unknown standard dictionary: " + dictionary); // todo: use concat
		}
	}

	/**
	 * @param dictionary стандартный словарь
	 * @return русское название словаря для отображения
	 */
	public static String getDisplayName(StandardDictionary dictionary) {
		switch (dictionary)
		{
			case normal: return "Обычный";
			case noerror: return "Безошибочный";
			case chars: return "Буквы";
			case marathon: return "Марафон";
			case abra: return "Абракадабра";
			case referats: return "Яндекс.Рефераты";
			case digits: return "Цифры";
			case sprint: return "Спринт";

			default:
				throw new IllegalArgumentException("Unknown standard dictionary: " + dictionary); // todo: use concat
		}
	}
}