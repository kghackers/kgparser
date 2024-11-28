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
    val exportNormalTop: Boolean = false,
    val exportAbraTop: Boolean = false,
    val exportReferatsTop: Boolean = false,
    val exportNoErrorTop: Boolean = false,
    val exportMarathonTop: Boolean = false,
    val exportCharsTop: Boolean = false,
    val exportDigitsTop: Boolean = false,
    val exportSprintTop: Boolean = false,

    // non-standard vocabularies
    val nonStandardDictionariesCodes: List<Int> = emptyList()
)