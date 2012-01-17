/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:05:59
 */
package ru.klavogonki.kgparser;

/**
 * Ранг игрока. Различается в зависимости от рекорда в обычном режиме.
 */
public enum Rank
{
	/**
	 * Новичок. Рекорд < 100.
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
	 * Кибергонщик. Рекорд >= 700.
	 */
	cyberracer;

	/**
	 * @param normalRecord рекорд в режиме "Обычный"
	 * @return ранг, соответствующий указанному рекорду
	 */
	public static Rank getRank(int normalRecord) {
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

		return Rank.cyberracer; // > 700 -> кибергонщик
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

			default: throw new IllegalArgumentException("Incorrect rank: " + rank);
		}
	}
}