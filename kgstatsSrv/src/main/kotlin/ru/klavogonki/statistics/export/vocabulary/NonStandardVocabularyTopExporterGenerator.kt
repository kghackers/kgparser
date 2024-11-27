package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.DictionaryUtils
import ru.klavogonki.common.NonStandardDictionary
import ru.klavogonki.statistics.dictionaries.NonStandardDictionariesCache
import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporter

object NonStandardVocabularyTopExporterGenerator : Logging {

    fun generate(dictionaryCode: String): NonStandardVocabularyTopExporter {
        val dictionaryId = DictionaryUtils.getDictionaryId(dictionaryCode)

        return generate(dictionaryId)
    }

    fun generate(dictionaryId: Int): NonStandardVocabularyTopExporter {
        val dictionaryData = NonStandardDictionariesCache.getDictionary(dictionaryId)

        requireNotNull(dictionaryData.top) {
            "Dictionary with code = ${dictionaryData.code}, name = \"${dictionaryData.displayName}" +
                " has NO top metadata."
        }

        return object : NonStandardVocabularyTopExporter {
            override fun minRacesCount(): Int {
                return dictionaryData.top.minRacesCount
            }

            override fun logger(): Logger {
                TODO("Not yet implemented")

                // todo: change the parent signature method to return String and log like this
            }

            override fun vocabulary(): NonStandardDictionary {
                // todo: change the parent signature to return NonStandardDictionaryData instead

                return NonStandardDictionary.getByDictionaryId(dictionaryData.code)
            }

            override fun topByBestSpeedExcelSheetName() =
                dictionaryData.top.topByBestSpeedExcelSheetName
                    ?: super.topByBestSpeedExcelSheetName()

            override fun topByRacesCountExcelSheetName() =
                dictionaryData.top.topByRacesCountExcelSheetName
                    ?: super.topByRacesCountExcelSheetName()

            override fun topByHaulExcelSheetName() =
                dictionaryData.top.topByHaulExcelSheetName
                    ?: super.topByHaulExcelSheetName()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val normalInEnglishTopExporter = generate(NonStandardDictionary.NORMAL_IN_ENGLISH.code)

        logger.info(
            "normalInEnglishTopExporter:" +
                "\n  - minRacesCount = ${normalInEnglishTopExporter.minRacesCount()}" +
                "\n  - topByBestSpeedExcelSheetName = ${normalInEnglishTopExporter.topByBestSpeedExcelSheetName()}" +
                "\n  - topByRacesCountExcelSheetName = ${normalInEnglishTopExporter.topByRacesCountExcelSheetName()}" +
                "\n  - topByHaulExcelSheetName = ${normalInEnglishTopExporter.topByHaulExcelSheetName()}"
        )

        val digitsOneHundredTopExporter = generate(NonStandardDictionary.DIGITS_ONE_HUNDRED.code)

        logger.info(
            "digitsOneHundredTopExporter:" +
                "\n  - minRacesCount = ${digitsOneHundredTopExporter.minRacesCount()}" +
                "\n  - topByBestSpeedExcelSheetName = ${digitsOneHundredTopExporter.topByBestSpeedExcelSheetName()}" +
                "\n  - topByRacesCountExcelSheetName = ${digitsOneHundredTopExporter.topByRacesCountExcelSheetName()}" +
                "\n  - topByHaulExcelSheetName = ${digitsOneHundredTopExporter.topByHaulExcelSheetName()}"
        )
    }
}