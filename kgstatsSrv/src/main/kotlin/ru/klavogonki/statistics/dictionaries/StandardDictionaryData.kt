package ru.klavogonki.statistics.dictionaries

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import ru.klavogonki.common.DictionaryTag
import ru.klavogonki.common.DictionaryUtils
import ru.klavogonki.common.StandardDictionary
import ru.klavogonki.common.UrlConstructor

/**
 * Стандартный словарь Клавогонок.
 *
 * Конфигурируется на основе JSON-файла, а НЕ захардкоженного энума.
 *
 * @see [NonStandardDictionaryData]
 */
@JsonPropertyOrder(
    "klavogonkiName",
    "displayName",
    "displayNamePrepositional",
    "tags",
    "top"
)
data class StandardDictionaryData(

    /**
     * Строковый код словаря. Как используется на клавогонках.
     * @see [ru.klavogonki.common.StandardDictionary.klavogonkiName]
     */
    @JsonProperty("klavogonkiName")
    @JvmField val klavogonkiName: String,

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
    fun getStandardDictionary() = StandardDictionary.getByKlavogonkiName(klavogonkiName)

    // todo: think whether this should be also configured by the json file
    fun getLink() = getStandardDictionary().wikiPageUrl
}