package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.StandardDictionary
import ru.klavogonki.statistics.export.vocabulary.standard.StandardVocabularyTopExporter

data class StandardVocabularyGeneratorContext(
    @JvmField val dictionaryCodeToExporter: Map<String, StandardVocabularyTopExporter>
) : Logging {
    fun getAllExporters() = dictionaryCodeToExporter.values.toList()

    fun getExporter(dictionary: StandardDictionary): StandardVocabularyTopExporter =
        getExporter(dictionary.klavogonkiName)

    fun getExporter(dictionaryCode: String): StandardVocabularyTopExporter =
        getExporterOrNull(dictionaryCode)
            ?: error(
                "No standard dictionary generator for dictionaryCode = \"$dictionaryCode\"." +
                    " Please check you generator config."
            )

    fun getExporterOrNull(dictionary: StandardDictionary): StandardVocabularyTopExporter? =
        getExporterOrNull(dictionary.klavogonkiName)

    fun getExporterOrNull(dictionaryCode: String): StandardVocabularyTopExporter? =
        dictionaryCodeToExporter[dictionaryCode]

    fun getTopBySpeedFirstPageFilePath(standardDictionary: StandardDictionary) =
        getTopBySpeedFirstPageFilePath(standardDictionary.klavogonkiName)

    fun getTopBySpeedFirstPageFilePath(dictionaryCode: String): String {
        val exporter = getExporterOrNull(dictionaryCode)

        if (exporter == null) {
            logger.warn(
                "Standard dictionary with code = \"$dictionaryCode\" is not configured." +
                    " Retuning empty file path to the top speed first page.",
            )

            return ""
        }

        return exporter.topByBestSpeedFirstPageFilePath()
    }

    fun getHeaderName(standardDictionary: StandardDictionary) =
        getHeaderName(standardDictionary.klavogonkiName)

    fun getHeaderName(dictionaryCode: String): String {
        val exporter = getExporterOrNull(dictionaryCode)

        if (exporter == null) {
            logger.warn(
                "Standard dictionary with code = \"$dictionaryCode\" is not configured. Retuning empty header name.",
            )

            return "—" // todo: think about returning an empty string
        }

        return exporter.headerName()
    }
}