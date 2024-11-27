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

        return nonStandardDictionaries
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val nonStandardDictionaries = parse()

        logger.info("Total non-standard dictionaries read from json file: ${nonStandardDictionaries.size}.")
    }
}
