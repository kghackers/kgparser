package ru.klavogonki.kgparser.export.vocabulary;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.StandardDictionary;
import ru.klavogonki.kgparser.excel.ExcelExporter;

@Log4j2
public final class VocabularyTopUtils {
    // todo: cast StandardDictionary and NonStandardDictionary to common interface and use methods for interface instead of 2 methods for each operation

    private VocabularyTopUtils() {
    }

    public static String takenPlayersWithMinimalRacesCount(StandardDictionary vocabulary, int racesCount) {
        return takenPlayersWithMinimalRacesCount(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary), racesCount);
    }
    public static String takenPlayersWithMinimalRacesCount(NonStandardDictionary vocabulary, int racesCount) {
        return takenPlayersWithMinimalRacesCount(vocabulary.namePrepositional, racesCount);
    }
    public static String takenPlayersWithMinimalRacesCount(String vocabularyNamePrepositional, int racesCount) {
        return String.format("Учтены игроки с минимальным пробегом %d в «%s»", racesCount, vocabularyNamePrepositional);
    }

    public static String topByBestSpeedIn(StandardDictionary vocabulary) {
        return topByBestSpeedIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }
    public static String topByBestSpeedIn(NonStandardDictionary vocabulary) {
        return topByBestSpeedIn(vocabulary.namePrepositional);
    }
    public static String topByBestSpeedIn(String vocabularyNamePrepositional) {
        return String.format("Топ по лучшей скорости в «%s»", vocabularyNamePrepositional);
    }

    public static String topByRecordIn(StandardDictionary vocabulary) {
        return topByRecordIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }
    public static String topByRecordIn(NonStandardDictionary vocabulary) {
        return topByRecordIn(vocabulary.namePrepositional);
    }
    public static String topByRecordIn(String vocabularyNamePrepositional) {
        return String.format("Топ по рекорду в «%s»", vocabularyNamePrepositional);
    }

    public static String topByBestSpeedExcelSheetName(StandardDictionary vocabulary) {
        String longName = topByBestSpeedIn(vocabulary);
        if (ExcelExporter.isValidSheetName(longName)) {
            logger.debug("\"{}\" (long name) is a valid topByBestSpeedExcelSheetName for dictionary {}. Use it.", longName, vocabulary);
            return longName;
        }
        else {
            logger.debug("\"{}\" (long name) is not a valid topByBestSpeedExcelSheetName for dictionary {}. Trying a short name...", longName, vocabulary);
        }

        String shortName = topByRecordIn(vocabulary);
        if (ExcelExporter.isValidSheetName(shortName)) {
            logger.debug("\"{}\" (short name) is a valid topByBestSpeedExcelSheetName for dictionary {}. Use it.", shortName, vocabulary);
            return shortName;
        }
        else {
            logger.warn("\"{}\" (short name) is not a valid topByBestSpeedExcelSheetName for dictionary {}. This is an epic fail.", shortName, vocabulary);
        }

        throw new IllegalStateException(String.format(
            "Cannot get topByBestSpeedExcelSheetName for vocabulary %s. Both long name \"%s\" and short name \"%s\" are non-valid.",
            vocabulary,
            longName,
            shortName
        ));
    }
    public static String topByBestSpeedExcelSheetName(NonStandardDictionary vocabulary) {
        String longName = topByBestSpeedIn(vocabulary);
        if (ExcelExporter.isValidSheetName(longName)) {
            logger.debug("\"{}\" (long name) is a valid topByBestSpeedExcelSheetName for dictionary {}. Use it.", longName, vocabulary);
            return longName;
        }
        else {
            logger.debug("\"{}\" (long name) is not a valid topByBestSpeedExcelSheetName for dictionary {}. Trying a short name...", longName, vocabulary);
        }

        String shortName = topByRecordIn(vocabulary);
        if (ExcelExporter.isValidSheetName(shortName)) {
            logger.debug("\"{}\" (short name) is a valid topByBestSpeedExcelSheetName for dictionary {}. Use it.", shortName, vocabulary);
            return shortName;
        }
        else {
            logger.warn("\"{}\" (short name) is not a valid topByBestSpeedExcelSheetName for dictionary {}. This is an epic fail.", shortName, vocabulary);
        }

        throw new IllegalStateException(String.format(
            "Cannot get topByBestSpeedExcelSheetName for vocabulary %s. Both long name \"%s\" and short name \"%s\" are non-valid.",
            vocabulary,
            longName,
            shortName
        ));
    }


    public static String topByRacesCountIn(StandardDictionary vocabulary) {
        return topByRacesCountIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }
    public static String topByRacesCountIn(NonStandardDictionary vocabulary) {
        return topByRacesCountIn(vocabulary.namePrepositional);
    }
    public static String topByRacesCountIn(String vocabularyNamePrepositional) {
        return String.format("Топ по пробегу в «%s»", vocabularyNamePrepositional);
    }

    public static String topByHaulIn(StandardDictionary vocabulary) {
        return topByHaulIn(StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }
    public static String topByHaulIn(NonStandardDictionary vocabulary) {
        return topByHaulIn(vocabulary.namePrepositional);
    }
    public static String topByHaulIn(String vocabularyNamePrepositional) {
        return String.format("Топ по времени в «%s»", vocabularyNamePrepositional);
    }
}
