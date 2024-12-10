package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.DictionaryUtils
import ru.klavogonki.common.NonStandardDictionary
import ru.klavogonki.statistics.dictionaries.NonStandardDictionariesCache
import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporterDefaultImpl

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

        return object : NonStandardVocabularyTopExporterDefaultImpl() {
            override fun dictionaryId() = dictionaryData.code

            override fun minRacesCount() = dictionaryData.top.minRacesCount

            override fun loggerName() = dictionaryData.top.loggerName

            // todo: think whether this override from NonStandardVocabularyTopExporter is required
            override fun vocabulary() = dictionaryData

            override fun headerName() = dictionaryData.top.headerName

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

    fun generateContext(dictionaryIds: List<Int>) : NonStandardVocabularyGeneratorContext {
        val distinctDictionaryIds = dictionaryIds.distinct()

        val dictionaryIdToExporter = mutableMapOf<Int, NonStandardVocabularyTopExporter>()

        distinctDictionaryIds.forEach {
            val exporter = generate(it)

            dictionaryIdToExporter[it] = exporter
        }

        return NonStandardVocabularyGeneratorContext(dictionaryIdToExporter)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val normalInEnglishTopExporter = generate(NonStandardDictionary.NORMAL_IN_ENGLISH.code)

        logger.info(
            "normalInEnglishTopExporter:" +
                "\n  - loggerName = ${normalInEnglishTopExporter.loggerName()}" +
                "\n  - headerName = ${normalInEnglishTopExporter.headerName()}" +
                "\n  - minRacesCount = ${normalInEnglishTopExporter.minRacesCount()}" +
                "\n  - topByBestSpeedExcelSheetName = ${normalInEnglishTopExporter.topByBestSpeedExcelSheetName()}" +
                "\n  - topByRacesCountExcelSheetName = ${normalInEnglishTopExporter.topByRacesCountExcelSheetName()}" +
                "\n  - topByHaulExcelSheetName = ${normalInEnglishTopExporter.topByHaulExcelSheetName()}"
        )

        val digitsOneHundredTopExporter = generate(NonStandardDictionary.DIGITS_ONE_HUNDRED.code)

        logger.info(
            "digitsOneHundredTopExporter:" +
                "\n  - loggerName = ${digitsOneHundredTopExporter.loggerName()}" +
                "\n  - headerName = ${digitsOneHundredTopExporter.headerName()}" +
                "\n  - minRacesCount = ${digitsOneHundredTopExporter.minRacesCount()}" +
                "\n  - topByBestSpeedExcelSheetName = ${digitsOneHundredTopExporter.topByBestSpeedExcelSheetName()}" +
                "\n  - topByRacesCountExcelSheetName = ${digitsOneHundredTopExporter.topByRacesCountExcelSheetName()}" +
                "\n  - topByHaulExcelSheetName = ${digitsOneHundredTopExporter.topByHaulExcelSheetName()}"
        )
    }
}