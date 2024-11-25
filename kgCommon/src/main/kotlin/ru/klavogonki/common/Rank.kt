package ru.klavogonki.common

/**
 * Ранг игрока. Различается в зависимости от рекорда в обычном режиме.
 */
@Suppress("MagicNumber")
enum class Rank(

    /**
     * Имя в API клавогонок. Все буквы в нижнем регистре.
     *
     * // todo: на самом деле, в API Клавогонок нет таких буквенных обозначений, только числовой код ранга.
     */
    @Suppress("unused")
    @JvmField val klavogonkiName: String,

    /**
     * Числовой код ранга (level).
     */
	@JvmField val level: Int,

	/**
     * Русское название ранга для отображения.
     */
	@JvmField val displayName: String,

	/**
     * Цвет на КГ, соответствующий рангу, в форме "#00FF02".
     */
	@JvmField val color: String
) {
    /**
     * Новичок. Рекорд &lt; 100.
     */
    novice("novice", 1, "Новичок", "#8D8D8D"),

    /**
     * Любитель. Рекорд от 100 до 199.
     */
    amateur("amateur", 2, "Любитель", "#4F9A97"),

    /**
     * Таксист. Рекорд от 200 до 299.
     */
    cabman("cabman", 3, "Таксист", "#187818"),

    /**
     * Профи. Рекорд от 300 до 399.
     */
    pro("pro", 4, "Профи", "#8C8100"),

    /**
     * Гонщик. Рекорд от 400 до 499.
     */
    racer("racer", 5, "Гонщик", "#BA5800"),

    /**
     * Маньяк. Рекорд от 500 до 599.
     */
    maniac("maniac", 6, "Маньяк", "#BC0143"),

    /**
     * Супермен. Рекорд от 600 до 699.
     */
    superman("superman", 7, "Супермен", "#5E0B9E"),

    /**
     * Кибергонщик. Рекорд от 700 до 799.
     */
    cyberracer("cyberracer", 8, "Кибергонщик", "#00037C"),

    /**
     * Экстракибер. Рекорд &gt;= 800.
     */
    extracyber("extracyber", 9, "Экстракибер", "#061956"),
    ;

    companion object {
        /**
         * @param normalRecord рекорд в [режиме &quot;Обычный&quot;][StandardDictionary.normal]
         * @return ранг, соответствующий указанному рекорду
         */
		@JvmStatic
        @Suppress("MagicNumber", "ReturnCount")
		fun getRankByNormalRecord(normalRecord: Int): Rank {
            if (normalRecord < 100) return novice

            if (normalRecord < 200) return amateur

            if (normalRecord < 300) return cabman

            if (normalRecord < 400) return pro

            if (normalRecord < 500) return racer

            if (normalRecord < 600) return maniac

            if (normalRecord < 700) return superman

            if (normalRecord < 800) return cyberracer

            return extracyber // рекорд в обычном >= 700 -> экстракибер
        }

        @JvmStatic
		fun getDisplayName(level: Int): String {
            return getRank(level).displayName
        }

        /**
         * @param level числовой код ранга
         * @return ранг с указанным числовым кодом
         */
        @JvmStatic
        fun getRank(level: Int): Rank {
            return entries
                .firstOrNull { it.level == level }
                ?: throw IllegalArgumentException("Incorrect rank level: $level.")
        }

        /**
         * Цвет игрока-гостя.
         */
        const val GUEST_COLOR: String = "#222222"

        /**
         * У клавомехаников свойство `title` заполнено словом "Клавомеханик", а не названием ранга.
         */
        const val KLAVO_MECHANIC_TITLE: String = "Клавомеханик"
    }
}