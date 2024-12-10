package ru.klavogonki.statistics.export

/**
 * Конфиг, настраивающий генерацию словарей.
 *
 * todo: по факту, в конфиг можно перенести настройки, которые сейчас реализуют классы,
 * реализующие [ru.klavogonki.statistics.export.vocabulary.VocabularyTopExporter].
 */
data class StatisticsGeneratorConfig(
    // aggregate tops
    val exportIndexPage: Boolean = false,

    val exportTopBySpeed: Boolean = false, // old exporter, not by normal vocabulary
    val exportTop500Pages: Boolean = false,
    val exportPlayersByRank: Boolean = false,

    // standard vocabularies
    val standardDictionariesCodes: List<String> = emptyList(),

    // non-standard vocabularies
    val nonStandardDictionariesCodes: List<Int> = emptyList()
)