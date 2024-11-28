package ru.klavogonki.statistics.export.vocabulary

import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporter

data class NonStandardVocabularyGeneratorContext(
    @JvmField val dictionaryIdToExporter: Map<Int, NonStandardVocabularyTopExporter>
) {
    fun getExporter(dictionaryId: Int): NonStandardVocabularyTopExporter =
        dictionaryIdToExporter[dictionaryId]
            ?: error(
                "No non-standard dictionary generator for dictionaryId = $dictionaryId." +
                    " Please check you generator config."
            )
}