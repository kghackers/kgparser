package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.NonStandardDictionary
import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporter

data class NonStandardVocabularyGeneratorContext(
    @JvmField val dictionaryIdToExporter: Map<Int, NonStandardVocabularyTopExporter>
) : Logging {
    fun getExporter(dictionary: NonStandardDictionary): NonStandardVocabularyTopExporter =
        getExporter(dictionary.id)

    fun getExporter(dictionaryId: Int): NonStandardVocabularyTopExporter =
        getExporterOrNull(dictionaryId)
            ?: error(
                "No non-standard dictionary generator for dictionaryId = $dictionaryId." +
                    " Please check you generator config."
            )

    fun getExporterOrNull(dictionary: NonStandardDictionary): NonStandardVocabularyTopExporter? =
        getExporterOrNull(dictionary.id)

    fun getExporterOrNull(dictionaryId: Int): NonStandardVocabularyTopExporter? =
        dictionaryIdToExporter[dictionaryId]

    fun getTopBySpeedFirstPageFilePath(nonStandardDictionary: NonStandardDictionary) =
        getTopBySpeedFirstPageFilePath(nonStandardDictionary.id)

    fun getTopBySpeedFirstPageFilePath(dictionaryId: Int): String {
        val exporter = getExporterOrNull(dictionaryId)

        if (exporter == null) {
            logger.warn(
                "Non-standard dictionary with id = $dictionaryId is not configured." +
                    " Retuning empty file path to the top speed first page.",
            )

            return ""
        }

        return exporter.topByBestSpeedFirstPageFilePath()
    }

    fun getHeaderName(nonStandardDictionary: NonStandardDictionary) =
        getHeaderName(nonStandardDictionary.id)

    fun getHeaderName(dictionaryId: Int): String {
        val exporter = getExporterOrNull(dictionaryId)

        if (exporter == null) {
            logger.warn(
                "Non-standard dictionary with id = $dictionaryId is not configured. Retuning empty header name.",
            )

            return "—" // todo: think about returning an empty string
        }

        return exporter.headerName()
    }
}