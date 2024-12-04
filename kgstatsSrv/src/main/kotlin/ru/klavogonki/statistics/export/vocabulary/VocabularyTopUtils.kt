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

    // todo: topBy...SheetName methods are the hell of dupilcate code
    @JvmStatic
    fun topByBestSpeedExcelSheetName(vocabulary: StandardDictionary): String {
        val dictionaryName = vocabulary.displayName
        val excelSheetType = "topByBestSpeedExcelSheetName"

        // for topByBestSpeedExcelSheetName, try long and short name options
        val longName = topByBestSpeedIn(vocabulary)
        if (ExcelExporter.isValidSheetName(longName)) {
            logger.debug(
                "\"$longName\" (long name)" +
                    " is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return longName
        } else {
            logger.debug(
                "\"$longName\" (long name)" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Trying a short name..."
            )
        }

        val shortName = topByRecordIn(vocabulary)
        if (ExcelExporter.isValidSheetName(shortName)) {
            logger.debug(
                "\"$shortName\" (short name) is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return shortName
        } else {
            logger.warn(
                "\"$shortName\" (short name)" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get $excelSheetType for vocabulary \"$dictionaryName\"." +
                " Both long name \"$longName\" and short name \"$shortName\" are non-valid."
        )
    }

    @JvmStatic
    fun topByBestSpeedExcelSheetName(vocabulary: NonStandardDictionaryData): String {
        val dictionaryName = vocabulary.displayName
        val excelSheetType = "topByBestSpeedExcelSheetName"

        // for topByBestSpeedExcelSheetName, try long and short name options
        val longName = topByBestSpeedIn(vocabulary)
        if (ExcelExporter.isValidSheetName(longName)) {
            logger.debug(
                "\"$longName\" (long name) is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return longName
        } else {
            logger.debug(
                "\"$longName\" (long name)" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Trying a short name..."
            )
        }

        val shortName = topByRecordIn(vocabulary)
        if (ExcelExporter.isValidSheetName(shortName)) {
            logger.debug(
                "\"$shortName\" (short name) is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return shortName
        } else {
            logger.warn(
                "\"$shortName\" (short name)" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get $excelSheetType for vocabulary \"$dictionaryName\"." +
                " Both long name \"$longName\" and short name \"$shortName\" are non-valid."
        )
    }

    @JvmStatic
    fun topByRacesCountExcelSheetName(vocabulary: NonStandardDictionaryData): String {
        val dictionaryName = vocabulary.displayName
        val excelSheetType = "topByRacesCountExcelSheetName"

        // for topByRacesCount, we don't have long/short name distinction
        val name = topByRacesCountIn(vocabulary)
        if (ExcelExporter.isValidSheetName(name)) {
            logger.debug(
                "\"$name\" is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return name
        } else {
            logger.warn(
                "\"$name\"" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get $excelSheetType for vocabulary \"$dictionaryName\"." +
                " Name \"$name\" is non-valid."
        )
    }

    @JvmStatic
    fun topByRacesCountExcelSheetName(vocabulary: StandardDictionary): String {
        val dictionaryName = vocabulary.displayName
        val excelSheetType = "topByRacesCountExcelSheetName"

        // for topByRacesCount, we don't have long/short name distinction
        val name = topByRacesCountIn(vocabulary)
        if (ExcelExporter.isValidSheetName(name)) {
            logger.debug(
                "\"$name\" is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return name
        } else {
            logger.warn(
                "\"$name\"" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get $excelSheetType for vocabulary \"$dictionaryName\"." +
                " Name \"$name\" is non-valid."
        )
    }

    @JvmStatic
    fun topByHaulExcelSheetName(vocabulary: StandardDictionary): String {
        val dictionaryName = vocabulary.displayName
        val excelSheetType = "topByHaulExcelSheetName"

        // for topByHaul, we don't have long/short name distinction
        val name = topByHaulIn(vocabulary)
        if (ExcelExporter.isValidSheetName(name)) {
            logger.debug(
                "\"$name\" is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return name
        } else {
            logger.warn(
                "\"$name\"" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get $excelSheetType for vocabulary \"$dictionaryName\"." +
                " Name \"$name\" is non-valid."
        )
    }

    @JvmStatic
    fun topByHaulExcelSheetName(vocabulary: NonStandardDictionaryData): String {
        val dictionaryName = vocabulary.displayName
        val excelSheetType = "topByHaulExcelSheetName"

        // for topByHaul, we don't have long/short name distinction
        val name = topByHaulIn(vocabulary)
        if (ExcelExporter.isValidSheetName(name)) {
            logger.debug(
                "\"$name\" is a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " Use it."
            )

            return name
        } else {
            logger.warn(
                "\"$name\"" +
                    " is not a valid $excelSheetType for dictionary \"$dictionaryName\"." +
                    " This is an epic fail."
            )
        }

        error(
            "Cannot get $excelSheetType for vocabulary \"$dictionaryName\"." +
                " Name \"$name\" is non-valid."
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

    @JvmStatic
    fun validateTopExporters(exporters: List<VocabularyTopExporter>) {
        val errors = mutableListOf<String>()

        exporters.forEach {
            validateExcelSheetName(
                errors,
                "topByBestSpeedExcelSheetName",
                it,
                VocabularyTopExporter::topByBestSpeedExcelSheetName
            )

            validateExcelSheetName(
                errors,
                "topByRacesCountExcelSheetName",
                it,
                VocabularyTopExporter::topByRacesCountExcelSheetName
            )

            validateExcelSheetName(
                errors,
                "topByHaulExcelSheetName",
                it,
                VocabularyTopExporter::topByHaulExcelSheetName
            )
        }

        if (errors.isEmpty()) {
            logger.info("No Excel sheet name errors. Validation passed.")
            return
        }

        // errors are not empty -> fail the validation
        val errorMessage = "There are ${errors.size} Excel sheet name errors. Errors: \n${errors.joinToString("\n")}"
        logger.error(errorMessage)
        error(errorMessage)
    }

    private fun validateExcelSheetName(
        errors: MutableList<String>,
        excelSheetType: String,
        exporter: VocabularyTopExporter,
        getSheetNameFunction: (topExporter: VocabularyTopExporter) -> String
    ) {
        try {
            val sheetName = getSheetNameFunction(exporter)

            // super-safe - the sheet getter should already have failed on an incorrect name
            ExcelExporter.validateSheetName(sheetName)
        } catch (e: Exception) {
            // error message already contains a dot at the end
            val error = "[${exporter.loggerName()}]: $excelSheetType is not valid. Error: ${e.message}"

            errors.add(error)
        }
    }
}