package ru.klavogonki.kgparser;

import ru.klavogonki.common.StandardDictionary;

import java.util.Arrays;

/**
 * Ранг игрока. Различается в зависимости от рекорда в обычном режиме.
 */
public enum Rank
{
	/**
	 * Новичок. Рекорд &lt; 100.
	 */
	novice(1, "Новичок", "#8D8D8D"),

	/**
	 * Любитель. Рекорд от 100 до 199.
	 */
	amateur(2, "Любитель", "#4F9A97"),

	/**
	 * Таксист. Рекорд от 200 до 299.
	 */
	cabman(3, "Таксист", "#187818"),

	/**
	 * Профи. Рекорд от 300 до 399.
	 */
	pro(4, "Профи", "#8C8100"),

	/**
	 * Гонщик. Рекорд от 400 до 499.
	 */
	racer(5, "Гонщик", "#BA5800"),

	/**
	 * Маньяк. Рекорд от 500 до 599.
	 */
	maniac(6, "Маньяк", "#BC0143"),

	/**
	 * Супермен. Рекорд от 600 до 699.
	 */
	superman(7, "Супермен", "#5E0B9E"),

	/**
	 * Кибергонщик. Рекорд от 700 до 799.
	 */
	cyberracer(8, "Кибергонщик", "#00037C"),

	/**
	 * Экстракибер. Рекорд &gt;= 800.
	 */
	extracyber(9, "Экстракибер", "#061956"),
	;

	Rank(int level, String displayName, String color) {
		this.level = level;
		this.displayName = displayName;
		this.color = color;
	}

	/**
	 * Числовой код ранга (level).
	 */
	public final int level;

	/**
	 * Русское название ранга для отображения.
	 */
	public final String displayName;

	/**
	 * Цвет на КГ, соответствующий рангу, в форме "#00FF02".
	 */
	public final String color;

	/**
	 * @param normalRecord рекорд в {@linkplain StandardDictionary#normal режиме "Обычный"}
	 * @return ранг, соответствующий указанному рекорду
	 */
	public static Rank getRankByNormalRecord(int normalRecord) {
		if (normalRecord < 100)
			return Rank.novice;

		if (normalRecord < 200)
			return Rank.amateur;

		if (normalRecord < 300)
			return Rank.cabman;

		if (normalRecord < 400)
			return Rank.pro;

		if (normalRecord < 500)
			return Rank.racer;

		if (normalRecord < 600)
			return Rank.maniac;

		if (normalRecord < 700)
			return Rank.superman;

		if (normalRecord < 800)
			return Rank.cyberracer;

		return Rank.extracyber; // рекорд в обычном >= 700 -> экстракибер
	}

	public static String getDisplayName(int level) {
		return Rank.getRank(level).displayName;
	}

	/**
	 * @param level числовой код ранга
	 * @return ранг с указанным числовым кодом
	 */
	public static Rank getRank(int level) {
		return Arrays.stream(Rank.values())
			.filter( rank -> rank.level == level )
			.findFirst()
			.orElseThrow(() ->
				new IllegalArgumentException( String.format("Incorrect rank level: %d.", level) )
			);
	}

	/**
	 * Цвет игрока-гостя.
	 */
	public static final String GUEST_COLOR = "#222222";

	/**
	 * У клавомехаников свойство {@code title} заполнено словом "Клавомеханик", а не названием ранга.
	 */
	public static final String KLAVO_MECHANIC_TITLE = "Клавомеханик";
}
