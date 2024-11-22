package ru.klavogonki.kgparser;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br>
 * <br>
 * Энум с названиями стандартных словарей (режимов), как они используются в AJAX-API.
 */
public enum StandardDictionary
{
	/**
	 * Обычный.
	 */
	normal(
		"Обычный",
		"Обычном",
		"#333333",
		Wiki.getUrl("%D0%9E%D0%B1%D1%8B%D1%87%D0%BD%D1%8B%D0%B9"),
		0
	),

	/**
	 * Безошибочный.
	 */
	noerror(
		"Безошибочный",
		"Безошибочном",
		"#4692AA",
		Wiki.getUrl("%D0%91%D0%B5%D0%B7%D0%BE%D1%88%D0%B8%D0%B1%D0%BE%D1%87%D0%BD%D1%8B%D0%B9"),
		0
	),

	/**
	 * Буквы.
	 */
	chars(
		"Буквы",
		"Буквах",
		"#B55900",
		Wiki.getUrl("%D0%91%D1%83%D0%BA%D0%B2%D1%8B"),
		-4
	),

	/**
	 * Марафон.
	 */
	marathon(
		"Марафон",
		"Марафоне",
		"#D43E68",
		Wiki.getUrl("%D0%9C%D0%B0%D1%80%D0%B0%D1%84%D0%BE%D0%BD"),
		0
	),

	/**
	 * Абракадабра.
	 */
	abra(
		"Абракадабра",
		"Абракадабре",
		"#3D4856",
		Wiki.getUrl("%D0%90%D0%B1%D1%80%D0%B0%D0%BA%D0%B0%D0%B4%D0%B0%D0%B1%D1%80%D0%B0"),
		-1
	),

	/**
	 * Яндекс.Рефераты.
	 */
	referats(
		"Яндекс.Рефераты",
		"Яндекс.Рефератах",
		"#698725",
		Wiki.getUrl("%D0%AF%D0%BD%D0%B4%D0%B5%D0%BA%D1%81.%D0%A0%D0%B5%D1%84%D0%B5%D1%80%D0%B0%D1%82%D1%8B"),
		-3
	),

	/**
	 * Цифры.
	 */
	digits(
		"Цифры",
		"Цифрах",
		"#777777",
		Wiki.getUrl("%D0%A6%D0%B8%D1%84%D1%80%D1%8B"),
		-2
	),

	/**
	 * Спринт.
	 */
	sprint(
		"Спринт",
		"Спринте",
		"#833F3A",
		Wiki.getUrl("%D0%A1%D0%BF%D1%80%D0%B8%D0%BD%D1%82"),
		0
	),
	;

	StandardDictionary(
		String displayName,
		String displayNamePrepositional,
		String color,
		String wikiPageUrl,
		int textType
	) {
		this.displayName = displayName;
		this.displayNamePrepositional = displayNamePrepositional;
		this.color = color;
		this.wikiPageUrl = wikiPageUrl;
		this.textType = textType;
	}

	/**
	 * Русское название словаря для отображения.
	 */
	public final String displayName;

	/**
	 * Русское название словаря для отображения, в предложном падеже.
	 */
	public final String displayNamePrepositional;

	/**
	 * Цвет стандартного словаря в формате &laquo;#123456&raquo;
	 */
	public final String color;

	/**
	 * Страница стандартного словаря в <a href="http://klavogonki.ru/wiki/">википедии клавогонок</a>.
	 */
	public final String wikiPageUrl;

	/**
	 * Тип текста словаря.
	 * Для нестандартных словарей будет равен идентификатору словаря.
	 */
	public final int textType;

	public static boolean isValidStandardDictionaryCode(String code) {
		try {
			StandardDictionary.valueOf(code);
			return true;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}
}
