/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:05:59
 */
package ru.klavogonki.kgparser;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Ранг игрока. Различается в зависимости от рекорда в обычном режиме.
 */
public enum Rank
{
	/**
	 * Новичок. Рекорд &lt; 100.
	 */
	novice,

	/**
	 * Любитель. Рекорд от 100 до 199.
	 */
	amateur,

	/**
	 * Таксист. Рекорд от 200 до 299.
	 */
	cabman,

	/**
	 * Профи. Рекорд от 300 до 399.
	 */
	pro,

	/**
	 * Гонщик. Рекорд от 400 до 499.
	 */
	racer,

	/**
	 * Маньяк. Рекорд от 500 до 599.
	 */
	maniac,

	/**
	 * Супермен. Рекорд от 600 до 699.
	 */
	superman,

	/**
	 * Кибергонщик. Рекорд от 700 до 799.
	 */
	cyberracer,

	/**
	 * Экстракибер. Рекорд >= 800.
	 */
	extracyber,
	;

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

	/**
	 * @param rank ранг
	 * @return цвет на КГ, соответствующий рангу, в форме "#00FF02".
	 */
	public static String getColor(Rank rank) {
		switch (rank)
		{
			case novice: return "#8D8D8D";
			case amateur: return "#4F9A97";
			case cabman: return "#187818";
			case pro: return "#8C8100";
			case racer: return "#BA5800";
			case maniac: return "#BC0143";
			case superman: return "#5E0B9E";
			case cyberracer: return "#00037C";
			case extracyber: return "#061956";

			default: throw new IllegalArgumentException( concat("Incorrect rank: ", rank) );
		}
	}

	/**
	 * @param rank ранг
	 * @return русское название ранга для отображения
	 */
	public static String getDisplayName(Rank rank) {
		switch (rank)
		{
			case novice: return "Новичок";
			case amateur: return "Любитель";
			case cabman: return "Таксист";
			case pro: return "Профи";
			case racer: return "Гонщик";
			case maniac: return "Маньяк";
			case superman: return "Супермен";
			case cyberracer: return "Кибергонщик";
			case extracyber: return "Экстракибер";

			default: throw new IllegalArgumentException( concat("Incorrect rank: ", rank) );
		}
	}

	/**
	 * @param rank ранг
	 * @return числовой код ранга (level)
	 */
	public static Byte getLevel(Rank rank) { // todo: change to int, stop this perversion!
		switch (rank)
		{
			case novice: return 1;
			case amateur: return 2;
			case cabman: return 3;
			case pro: return 4;
			case racer: return 5;
			case maniac: return 6;
			case superman: return 7;
			case cyberracer: return 8;
			case extracyber: return 9;

			default: throw new IllegalArgumentException( concat("Incorrect rank: ", rank) );
		}
	}
	/**
	 * @param level числовой код ранга
	 * @return ранг с указанным числовым кодом
	 */
	public static Rank getRank(int level) {
		switch (level)
		{
			case 1: return novice;
			case 2: return amateur;
			case 3: return cabman;
			case 4: return pro;
			case 5: return racer;
			case 6: return maniac;
			case 7: return superman;
			case 8: return cyberracer;
			case 9: return extracyber;

			default: throw new IllegalArgumentException( concat("Incorrect rank level: " + level) );
		}
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
