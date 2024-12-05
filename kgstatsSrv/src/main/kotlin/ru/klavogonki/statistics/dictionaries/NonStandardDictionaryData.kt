package ru.klavogonki.statistics.dictionaries

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import ru.klavogonki.common.DictionaryTag
import ru.klavogonki.common.DictionaryUtils
import ru.klavogonki.common.UrlConstructor

/**
 * Нестандартный словарь.
 *
 * Конфигурируется на основе JSON-файла, а НЕ захардкоженного энума.
 */
@JsonPropertyOrder(
    "code",
    "displayName",
    "displayNamePrepositional",
    "tags",
    "top"
)
data class NonStandardDictionaryData(

    /**
     * Числовой код словаря. Без префикса `voc-`.
     */
    @JsonProperty("code")
    @JvmField val code: Int,

    /**
     * Русское название словаря для отображения.
     */
    @JsonProperty("displayName")
    @JvmField val displayName: String,

    /**
     * Русское название словаря для отображения, в предложном падеже.
     */
    @JsonProperty("displayNamePrepositional")
    @JvmField val displayNamePrepositional: String,

    /**
     * Тэги словаря. Может быть пустым множеством.
     */
    @JsonProperty("tags")
    @JsonSetter(nulls = Nulls.AS_EMPTY) // if null in json -> parse to empty set
    @JvmField val tags: Set<DictionaryTag>,

    /**
     * Метаданные для генерации топа по нестандартному словарю.
     *
     * Может быть не указано, если топ по словарю не генерируется.
     */
    @JsonProperty("top")
    @JvmField val top: NonStandardDictionaryTopData?
) {
    fun getFullCode() = DictionaryUtils.getDictionaryCode(code)

    fun getLink() = UrlConstructor.dictionaryPage(code)
}