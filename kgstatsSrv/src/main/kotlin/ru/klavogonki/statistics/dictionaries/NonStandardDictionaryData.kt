package ru.klavogonki.statistics.dictionaries

import ru.klavogonki.common.DictionaryTag
import ru.klavogonki.common.DictionaryUtils
import ru.klavogonki.common.UrlConstructor

/**
 * Нестандартный словарь.
 *
 * Конфигурируется на основе JSON-файла, а НЕ захардкоженного энума.
 */
data class NonStandardDictionaryData(

    /**
     * Числовой код словаря. Без префикса `voc-`.
     */
    @JvmField val code: Int,

    /**
     * Русское название словаря для отображения.
     */
    @JvmField val displayName: String,

    /**
     * Русское название словаря для отображения, в предложном падеже.
     */
    @JvmField val displayNamePrepositional: String,

    /**
     * Тэги словаря. Может быть пустым множеством.
     */
    @JvmField val tags: Set<DictionaryTag>,

    /**
     * Метаданные для генерации топа по нестандартному словарю.
     *
     * Может быть не указано, если топ по словарю не генерируется.
     */
    @JvmField val top: NonStandardDictionaryTopData?
) {
    fun getFullCode() = DictionaryUtils.getDictionaryCode(code)

    fun getLink() = UrlConstructor.dictionaryPage(code)
}