package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.StandardDictionary
import ru.klavogonki.statistics.dictionaries.StandardDictionariesCache
import ru.klavogonki.statistics.export.vocabulary.standard.StandardVocabularyTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.StandardVocabularyTopExporterDefaultImpl

object StandardVocabularyTopExporterGenerator : Logging {

    fun generate(dictionary: StandardDictionary): StandardVocabularyTopExporter =
        generate(dictionary.klavogonkiName)

    fun generate(dictionaryKlavogonkiName: String): StandardVocabularyTopExporter {
        val dictionaryData = StandardDictionariesCache.getDictionary(dictionaryKlavogonkiName)

        requireNotNull(dictionaryData.top) {
            "Dictionary with code = ${dictionaryData.klavogonkiName}, name = \"${dictionaryData.displayName}" +
                " has NO top metadata."
        }

        return object : StandardVocabularyTopExporterDefaultImpl() {
            override fun vocabularyCode() = dictionaryData.klavogonkiName

            override fun minRacesCount() = dictionaryData.top.minRacesCount

            override fun loggerName() = dictionaryData.top.loggerName

            override fun vocabulary() = dictionaryData.getStandardDictionary()

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

    fun generateContext(dictionaryCodes: List<String>) : StandardVocabularyGeneratorContext {
        val distinctDictionaryCodes = dictionaryCodes.distinct()

        val dictionaryCodeToExporter = mutableMapOf<String, StandardVocabularyTopExporter>()

        distinctDictionaryCodes.forEach {
            val exporter = generate(it)

            dictionaryCodeToExporter[it] = exporter
        }

        return StandardVocabularyGeneratorContext(dictionaryCodeToExporter)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val normalTopExporter = generate(StandardDictionary.normal)

        logger.info(
            "normalTopExporter:" +
                "\n  - loggerName = ${normalTopExporter.loggerName()}" +
                "\n  - headerName = ${normalTopExporter.headerName()}" +
                "\n  - minRacesCount = ${normalTopExporter.minRacesCount()}" +
                "\n  - topByBestSpeedExcelSheetName = ${normalTopExporter.topByBestSpeedExcelSheetName()}" +
                "\n  - topByRacesCountExcelSheetName = ${normalTopExporter.topByRacesCountExcelSheetName()}" +
                "\n  - topByHaulExcelSheetName = ${normalTopExporter.topByHaulExcelSheetName()}"
        )

        val digitsTopExporter = generate(StandardDictionary.digits)

        logger.info(
            "digitsTopExporter:" +
                "\n  - loggerName = ${digitsTopExporter.loggerName()}" +
                "\n  - headerName = ${digitsTopExporter.headerName()}" +
                "\n  - minRacesCount = ${digitsTopExporter.minRacesCount()}" +
                "\n  - topByBestSpeedExcelSheetName = ${digitsTopExporter.topByBestSpeedExcelSheetName()}" +
                "\n  - topByRacesCountExcelSheetName = ${digitsTopExporter.topByRacesCountExcelSheetName()}" +
                "\n  - topByHaulExcelSheetName = ${digitsTopExporter.topByHaulExcelSheetName()}"
        )
    }
}