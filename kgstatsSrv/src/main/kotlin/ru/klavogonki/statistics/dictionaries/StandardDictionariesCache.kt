package ru.klavogonki.statistics.dictionaries

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.DictionaryTag
import ru.klavogonki.common.StandardDictionary
import ru.klavogonki.statistics.util.JacksonUtils

object StandardDictionariesCache : Logging {

    private const val STANDARD_DICTIONARIES_JSON_RESOURCE = "/dictionaries/standard-dictionaries.json"

    private val standardDictionaries = parse()

    private val dictionaryByCode: Map<String, StandardDictionaryData> = standardDictionaries
        .groupBy { it.klavogonkiName }
        .mapValues { it.value.first() } // we're sure the code (klavogonkiName) is unique

    private fun parse(): List<StandardDictionaryData> {
        val fileName = this.javaClass.getResourceAsStream(STANDARD_DICTIONARIES_JSON_RESOURCE)

        val standardDictionaries = JacksonUtils.parseStandardDictionaryData(fileName)

        logger.info(
            "Total standard dictionaries read from json file \"$STANDARD_DICTIONARIES_JSON_RESOURCE\": " +
                "${standardDictionaries.size}."
        )

        // validate no duplicate ids
        validate(standardDictionaries)

        return standardDictionaries
    }

    private fun validate(dictionaries: List<StandardDictionaryData>) {
        val repeatCodeToCount: Map<String, Int> = dictionaries
            .groupingBy { it.klavogonkiName }
            .eachCount()
            .filter { it.value > 1 }

        if (repeatCodeToCount.isNotEmpty()) {
            repeatCodeToCount.forEach {
                logger.error("Duplicate dictionary code (klavogonkiName) ${it.key}: used ${it.value} times.")
            }
        }

        check(repeatCodeToCount.isEmpty()) {
            "Duplicate dictionary codes: $repeatCodeToCount"
        }

        logger.info("No dictionary code duplicates.")
    }

    fun getDictionary(klavogonkiName: String): StandardDictionaryData {
        return dictionaryByCode[klavogonkiName]
            ?: error("No dictionary found by code = $klavogonkiName.")
    }

    fun getDictionary(dictionary: StandardDictionary): StandardDictionaryData {
        return getDictionary(dictionary.klavogonkiName)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val normal = getDictionary(StandardDictionary.normal)
        logger.info("Normal dictionary: \n$normal")

        val bad = getDictionary("bad_code")
    }
}