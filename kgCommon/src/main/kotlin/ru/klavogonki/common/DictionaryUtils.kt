package ru.klavogonki.common

import ru.klavogonki.common.StandardDictionary.Companion.getByKlavogonkiName
import ru.klavogonki.common.UrlConstructor.dictionaryPage

object DictionaryUtils {

    /**
     * Префикс, с которого начинается код нестандартного словаря.
     */
    const val NON_STANDARD_DICTIONARY_ID_PREFIX = "voc-"

    /**
     * Цвет для отображения нестандартного словаря.
     */
    const val NON_STANDARD_DICTIONARY_COLOR = "#524CA7"

    /**
     * @param code строковый код словаря (gametype в ajax-api)
     * @return `true` — если словарь с указанным кодом является [стандартным][StandardDictionary];
     *
     * `false` — если словарь с указанным кодом является пользовательским словарем.
     */
    @JvmStatic
    fun isStandard(code: String): Boolean {
        if (code.startsWith(NON_STANDARD_DICTIONARY_ID_PREFIX)) {
            return false
        }

        if (StandardDictionary.isValidStandardDictionaryCode(code)) {
            return true
        }

        throw IllegalArgumentException("Incorrect dictionary code: \"$code\".")
    }

    @JvmStatic
    fun isValid(code: String) =
        code.startsWith(NON_STANDARD_DICTIONARY_ID_PREFIX) ||
            StandardDictionary.isValidStandardDictionaryCode(code)

    @JvmStatic
    fun getValidCode(code: String): String {
        if (isValid(code)) {
            return code
        }

        val codeConstructedFromIntValue = getDictionaryCode(code.toInt())

        require( isValid(codeConstructedFromIntValue) ) {
            "Vocabulary code \"$codeConstructedFromIntValue\"" +
                " constructed from given vocabulary code \"$code\"" +
                " is still not valud."
        }

        return codeConstructedFromIntValue
    }

    /**
     * @param code строковый код словаря (gametype в ajax-api)
     * @return числовой идентификатор словаря
     */
    @JvmStatic
    fun getDictionaryId(code: String): Int {
        require(!isStandard(code)) {
            "Dictionary with code = \"$code\" is standard. Cannot get dictionary id from it."
        }

        val codeStr = code.substring(NON_STANDARD_DICTIONARY_ID_PREFIX.length)
        return codeStr.toInt()
    }

    /**
     * @param dictionaryId идентификатор словаря
     * @return строковый код словаря
     */
    @JvmStatic
    fun getDictionaryCode(dictionaryId: Int): String {
        return NON_STANDARD_DICTIONARY_ID_PREFIX + dictionaryId
    }

    @JvmStatic
    fun getTextType(code: String?): Int {
        requireNotNull(code) { "dictionaryCode cannot be null" }

        if (!isStandard(code)) {
            return getDictionaryId(code)
        }

        val standardDictionary = StandardDictionary.getByKlavogonkiName(code)
        return standardDictionary.textType
    }

    /**
     * @param dictionaryCode строковый код словаря
     * @return цвет, соответствующий словарю.
     */
    @JvmStatic
    fun getDictionaryColor(dictionaryCode: String?): String {
        requireNotNull(dictionaryCode) { "dictionaryCode cannot be null" }

        if (isStandard(dictionaryCode)) {
            return getByKlavogonkiName(dictionaryCode).color
        }

        return NON_STANDARD_DICTIONARY_COLOR
    }

    /**
     * @param dictionaryCode строковый код словаря
     * @return страница вики клавогонок — для стандартных словарей
     *
     * ссылка на страницу словаря — для нестандартных словарей
     */
    @JvmStatic
    fun getDictionaryPageUrl(dictionaryCode: String?): String {
        requireNotNull(dictionaryCode) { "dictionaryCode cannot be null" }

        if (isStandard(dictionaryCode)) {
            return getByKlavogonkiName(dictionaryCode).wikiPageUrl
        }

        return dictionaryPage(
            getDictionaryId(dictionaryCode)
        )
    }


}