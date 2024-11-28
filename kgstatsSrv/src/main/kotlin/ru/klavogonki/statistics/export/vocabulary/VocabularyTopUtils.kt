package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.common.StandardDictionary
import ru.klavogonki.statistics.dictionaries.NonStandardDictionaryData
import ru.klavogonki.statistics.excel.ExcelExporter

@Suppress("TooManyFunctions")
object VocabularyTopUtils : Logging {
    @JvmStatic
    fun takenPlayersWithMinimalRacesCount(vocabulary: StandardDictionary, racesCount: Int) =
        takenPlayersWithMinimalRacesCount(
            vocabulary.displayNamePrepositional,
            racesCount
        )

    @JvmStatic
    fun takenPlayersWithMinimalRacesCount(vocabulary: NonStandardDictionaryData, racesCount: Int) =
        takenPlayersWithMinimalRacesCount(vocabulary.displayNamePrepositional, racesCount)

    @JvmStatic
    fun takenPlayersWithMinimalRacesCount(vocabularyNamePrepositional: String, racesCount: Int) =
        "Учтены игроки с минимальным пробегом $racesCount в «$vocabularyNamePrepositional»"

    @JvmStatic
    fun topByBestSpeedIn(vocabulary: StandardDictionary) =
        topByBestSpeedIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByBestSpeedIn(vocabulary: NonStandardDictionaryData) =
        topByBestSpeedIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByBestSpeedIn(vocabularyNamePrepositional: String) =
        "Топ по лучшей скорости в «$vocabularyNamePrepositional»"

    @JvmStatic
    fun topByRecordIn(vocabulary: StandardDictionary) =
        topByRecordIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByRecordIn(vocabulary: NonStandardDictionaryData) =
        topByRecordIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByRecordIn(vocabularyNamePrepositional: String) =
        "Топ по рекорду в «${vocabularyNamePrepositional}»"

    @JvmStatic
    fun topByBestSpeedExcelSheetName(vocabulary: StandardDictionary): String {
        val longName = topByBestSpeedIn(vocabulary)
        if (ExcelExporter.isValidSheetName(longName)) {
            logger.debug(
                "\"$longName\" (long name)" +
                    " is a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " Use it."
            )

            return longName
        } else {
            logger.debug(
                "\"$longName\" (long name)" +
                    " is not a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " Trying a short name..."
            )
        }

        val shortName = topByRecordIn(vocabulary)
        if (ExcelExporter.isValidSheetName(shortName)) {
            logger.debug(
                "\"$shortName\" (short name) is a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " Use it."
            )

            return shortName
        } else {
            logger.warn(
                "\"$shortName\" (short name)" +
                    " is not a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get topByBestSpeedExcelSheetName for vocabulary \"$vocabulary\"." +
                " Both long name \"$longName\" and short name \"$shortName\" are non-valid."
        )
    }

    @JvmStatic
    fun topByBestSpeedExcelSheetName(vocabulary: NonStandardDictionaryData): String {
        val longName = topByBestSpeedIn(vocabulary)
        if (ExcelExporter.isValidSheetName(longName)) {
            logger.debug(
                "\"$longName\" (long name) is a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " Use it."
            )

            return longName
        } else {
            logger.debug(
                "\"$longName\" (long name)" +
                    " is not a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " Trying a short name..."
            )
        }

        val shortName = topByRecordIn(vocabulary)
        if (ExcelExporter.isValidSheetName(shortName)) {
            logger.debug(
                "\"$shortName\" (short name) is a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " Use it."
            )

            return shortName
        } else {
            logger.warn(
                "\"$shortName\" (short name)" +
                    " is not a valid topByBestSpeedExcelSheetName for dictionary \"$vocabulary\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get topByBestSpeedExcelSheetName for vocabulary \"$vocabulary\"." +
                " Both long name \"$longName\" and short name \"$shortName\" are non-valid."
        )
    }

    @JvmStatic
    fun topByRacesCountIn(vocabulary: StandardDictionary) =
        topByRacesCountIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByRacesCountIn(vocabulary: NonStandardDictionaryData) =
        topByRacesCountIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByRacesCountIn(vocabularyNamePrepositional: String) =
        "Топ по пробегу в «$vocabularyNamePrepositional»"

    @JvmStatic
    fun topByHaulIn(vocabulary: StandardDictionary) =
        topByHaulIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByHaulIn(vocabulary: NonStandardDictionaryData) =
        topByHaulIn(vocabulary.displayNamePrepositional)

    @JvmStatic
    fun topByHaulIn(vocabularyNamePrepositional: String) =
        "Топ по времени в «$vocabularyNamePrepositional»"
}
