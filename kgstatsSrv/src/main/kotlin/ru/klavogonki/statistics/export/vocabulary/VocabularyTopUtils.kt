package ru.klavogonki.statistics.export.vocabulary

import org.apache.logging.log4j.kotlin.Logging
import ru.klavogonki.kgparser.NonStandardDictionary
import ru.klavogonki.kgparser.StandardDictionary
import ru.klavogonki.statistics.excel.ExcelExporter

@Suppress("TooManyFunctions")
object VocabularyTopUtils : Logging {
    @JvmStatic
    fun takenPlayersWithMinimalRacesCount(vocabulary: StandardDictionary, racesCount: Int) =
        takenPlayersWithMinimalRacesCount(
            StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary),
            racesCount
        )

    @JvmStatic
    fun takenPlayersWithMinimalRacesCount(vocabulary: NonStandardDictionary, racesCount: Int) =
        takenPlayersWithMinimalRacesCount(vocabulary.namePrepositional, racesCount)

    @JvmStatic
    fun takenPlayersWithMinimalRacesCount(vocabularyNamePrepositional: String, racesCount: Int) =
        "Учтены игроки с минимальным пробегом $racesCount в «$vocabularyNamePrepositional»"

    @JvmStatic
    fun topByBestSpeedIn(vocabulary: StandardDictionary) =
        topByBestSpeedIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary))

    @JvmStatic
    fun topByBestSpeedIn(vocabulary: NonStandardDictionary) =
        topByBestSpeedIn(vocabulary.namePrepositional)

    @JvmStatic
    fun topByBestSpeedIn(vocabularyNamePrepositional: String) =
        "Топ по лучшей скорости в «$vocabularyNamePrepositional»"

    @JvmStatic
    fun topByRecordIn(vocabulary: StandardDictionary) =
        topByRecordIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary))

    @JvmStatic
    fun topByRecordIn(vocabulary: NonStandardDictionary) =
        topByRecordIn(vocabulary.namePrepositional)

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
    fun topByBestSpeedExcelSheetName(vocabulary: NonStandardDictionary): String {
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
        topByRacesCountIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary))

    @JvmStatic
    fun topByRacesCountIn(vocabulary: NonStandardDictionary) =
        topByRacesCountIn(vocabulary.namePrepositional)

    @JvmStatic
    fun topByRacesCountIn(vocabularyNamePrepositional: String) =
        "Топ по пробегу в «$vocabularyNamePrepositional»"

    @JvmStatic
    fun topByHaulIn(vocabulary: StandardDictionary) =
        topByHaulIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary))

    @JvmStatic
    fun topByHaulIn(vocabulary: NonStandardDictionary) =
        topByHaulIn(vocabulary.namePrepositional)

    @JvmStatic
    fun topByHaulIn(vocabularyNamePrepositional: String) =
        "Топ по времени в «$vocabularyNamePrepositional»"
}
