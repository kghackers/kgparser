package ru.klavogonki.statistics.export.vocabulary

import ru.klavogonki.common.NonStandardDictionary
import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporter

data class NonStandardVocabularyGeneratorContext(
    @JvmField val dictionaryIdToExporter: Map<Int, NonStandardVocabularyTopExporter>
) {
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
}