package ru.klavogonki.statistics.dictionaries

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.statistics.util.JacksonUtils

object NonStandardDictionariesCache : Logging {

    private const val NON_STANDARD_DICTIONARIES_JSON_RESOURCE = "/dictionaries/non-standard-dictionaries.json"

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

    @JvmStatic
    fun main(args: Array<String>) {
        val nonStandardDictionaries = parse()

        logger.info("[main]: Total non-standard dictionaries read from json file: ${nonStandardDictionaries.size}.")
    }
}
