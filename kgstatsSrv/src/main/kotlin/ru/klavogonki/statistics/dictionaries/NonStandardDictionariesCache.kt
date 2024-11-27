package ru.klavogonki.statistics.dictionaries

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.DictionaryUtils
import ru.klavogonki.common.NonStandardDictionary
import ru.klavogonki.statistics.util.JacksonUtils

object NonStandardDictionariesCache : Logging {

    private const val NON_STANDARD_DICTIONARIES_JSON_RESOURCE = "/dictionaries/non-standard-dictionaries.json"

    private val nonStandardDictionaries = parse()

/*
    val nonStandardDictionaries: List<NonStandardDictionaryData> = ObjectMapper()
        .readValue(
            this.javaClass.getResource(NON_STANDARD_DICTIONARIES_JSON_RESOURCE),
            object : TypeReference<List<NonStandardDictionaryData>>() {}
        )
*/

    private fun parse(): List<NonStandardDictionaryData> {
        val fileName = this.javaClass.getResource(NON_STANDARD_DICTIONARIES_JSON_RESOURCE).file

        val nonStandardDictionaries = JacksonUtils.parseNonStandardDictionaryData(fileName)

        logger.info(
            "Total non-standard dictionaries read from json file \"$NON_STANDARD_DICTIONARIES_JSON_RESOURCE\": " +
                "${nonStandardDictionaries.size}."
        )

        // validate no duplicate ids
        validate(nonStandardDictionaries)

        return nonStandardDictionaries
    }

    private fun validate(dictionaries: List<NonStandardDictionaryData>) {
        val repeatCodeToCount: Map<Int, Int> = dictionaries
            .groupingBy { it.code }
            .eachCount()
            .filter { it.value > 1 }

        if (repeatCodeToCount.isNotEmpty()) {
            repeatCodeToCount.forEach {
                logger.error("Duplicate dictionary code ${it.key}: used ${it.value} times.")
            }
        }

        check(repeatCodeToCount.isEmpty()) {
            "Duplicate dictionary codes: $repeatCodeToCount"
        }

        logger.info("No dictionary code duplicates.")
    }

    fun getDictionary(code: Int): NonStandardDictionaryData {
        // todo: get it from a static map
        return nonStandardDictionaries
            .firstOrNull { it.code == code }
            ?: error("No dictionary found by code = $code.")
    }

    fun getDictionary(code: String): NonStandardDictionaryData {
        val intCode = DictionaryUtils.getDictionaryId(code)

        return getDictionary(intCode)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val shortTexts = getDictionary(NonStandardDictionary.SHORT_TEXTS.code)
        logger.info("Short texts dictionary: \n$shortTexts")

        val bad = getDictionary(666999)
    }
}
