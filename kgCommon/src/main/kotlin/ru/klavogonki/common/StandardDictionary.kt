package ru.klavogonki.common

/**
 * Энум с названиями стандартных словарей (режимов), как они используются в AJAX-API.
 */
@Suppress("MagicNumber", "LongParameterList")
enum class StandardDictionary(

    /**
     * Имя в API клавогонок. Все буквы в нижнем регистре.
     */
    @JvmField val klavogonkiName: String,

    /**
     * Русское название словаря для отображения.
     */
    @JvmField val displayName: String,

    /**
     * Русское название словаря для отображения, в предложном падеже.
     */
    @JvmField val displayNamePrepositional: String,

    /**
     * Цвет стандартного словаря в формате #123456
     */
    @JvmField val color: String,

    /**
     * Страница стандартного словаря в [википедии клавогонок](http://klavogonki.ru/wiki/).
     */
    @JvmField val wikiPageUrl: String,

    /**
     * Тип текста словаря.
     * Для нестандартных словарей будет равен идентификатору словаря.
     */
    @JvmField val textType: Int,

    /**
     * Режим текста словаря.
     * Возвращается в поле `mode` в `/get-stats-overview -> ... -> info`.
     */
    @JvmField val dictionaryMode: DictionaryMode
) {
    /**
     * Обычный.
     */
    normal(
        klavogonkiName = "normal",
        displayName = "Обычный",
        displayNamePrepositional = "Обычном",
        color = "#333333",
        wikiPageUrl = Wiki.getUrl("%D0%9E%D0%B1%D1%8B%D1%87%D0%BD%D1%8B%D0%B9"),
        textType = 0,
        dictionaryMode = DictionaryMode.normal
    ),

    /**
     * Безошибочный.
     */
    noerror(
        klavogonkiName = "noerror",
        displayName = "Безошибочный",
        displayNamePrepositional = "Безошибочном",
        color = "#4692AA",
        wikiPageUrl = Wiki.getUrl("%D0%91%D0%B5%D0%B7%D0%BE%D1%88%D0%B8%D0%B1%D0%BE%D1%87%D0%BD%D1%8B%D0%B9"),
        textType = 0,
        dictionaryMode = DictionaryMode.noerror
    ),

    /**
     * Буквы.
     */
    chars(
        klavogonkiName = "chars",
        displayName = "Буквы",
        displayNamePrepositional = "Буквах",
        color = "#B55900",
        wikiPageUrl = Wiki.getUrl("%D0%91%D1%83%D0%BA%D0%B2%D1%8B"),
        textType = -4,
        dictionaryMode = DictionaryMode.normal
    ),

    /**
     * Марафон.
     */
    marathon(
        klavogonkiName = "marathon",
        displayName = "Марафон",
        displayNamePrepositional = "Марафоне",
        color = "#D43E68",
        wikiPageUrl = Wiki.getUrl("%D0%9C%D0%B0%D1%80%D0%B0%D1%84%D0%BE%D0%BD"),
        textType = 0,
        dictionaryMode = DictionaryMode.marathon
    ),

    /**
     * Абракадабра.
     */
    abra(
        klavogonkiName = "abra",
        displayName = "Абракадабра",
        displayNamePrepositional = "Абракадабре",
        color = "#3D4856",
        wikiPageUrl = Wiki.getUrl("%D0%90%D0%B1%D1%80%D0%B0%D0%BA%D0%B0%D0%B4%D0%B0%D0%B1%D1%80%D0%B0"),
        textType = -1,
        dictionaryMode = DictionaryMode.normal
    ),

    /**
     * Яндекс.Рефераты.
     */
    referats(
        klavogonkiName = "referats",
        displayName = "Яндекс.Рефераты",
        displayNamePrepositional = "Яндекс.Рефератах",
        color = "#698725",
        wikiPageUrl = Wiki.getUrl("%D0%AF%D0%BD%D0%B4%D0%B5%D0%BA%D1%81.%D0%A0%D0%B5%D1%84%D0%B5%D1%80%D0%B0%D1%82%D1%8B"),
        textType = -3,
        dictionaryMode = DictionaryMode.normal
    ),

    /**
     * Цифры.
     */
    digits(
        klavogonkiName = "digits",
        displayName = "Цифры",
        displayNamePrepositional = "Цифрах",
        color = "#777777",
        wikiPageUrl = Wiki.getUrl("%D0%A6%D0%B8%D1%84%D1%80%D1%8B"),
        textType = -2,
        dictionaryMode = DictionaryMode.normal
    ),

    /**
     * Спринт.
     */
    sprint(
        klavogonkiName = "sprint",
        displayName = "Спринт",
        displayNamePrepositional = "Спринте",
        color = "#833F3A",
        wikiPageUrl = Wiki.getUrl("%D0%A1%D0%BF%D1%80%D0%B8%D0%BD%D1%82"),
        textType = 0,
        dictionaryMode = DictionaryMode.sprint
    ),
    ;

    companion object {

        @JvmStatic
        fun isValidStandardDictionaryCode(code: String) =
            entries.any { it.klavogonkiName == code }

        @JvmStatic
        fun getByKlavogonkiName(code: String): StandardDictionary {
            val values = entries.filter { it.klavogonkiName == code }

            check(values.isNotEmpty()) {
                "No standard dictionary for code = \"$code\"."
            }

            check(values.size == 1) {
                "More than one standard dictionary for code = \"$code\"."
            }

            return values[0]
        }
    }
}
