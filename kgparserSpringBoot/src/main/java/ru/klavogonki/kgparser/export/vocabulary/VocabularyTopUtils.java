package ru.klavogonki.kgparser.export.vocabulary;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.StandardDictionary;
import ru.klavogonki.kgparser.excel.ExcelExporter;

@Log4j2
public final class VocabularyTopUtils {

    private VocabularyTopUtils() {
    }

    public static String takenPlayersWithMinimalRacesCount(StandardDictionary vocabulary, int racesCount) {
        return String.format("Учтены игроки с минимальным пробегом %d в «%s»", racesCount, StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }

    public static String topByBestSpeedIn(StandardDictionary vocabulary) {
        return String.format("Топ по лучшей скорости в «%s»", StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }

    public static String topByRecordIn(StandardDictionary vocabulary) {
        return String.format("Топ по рекорду в «%s»", StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
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
            logger.debug("\"{}\" (short name) is not a valid topByBestSpeedExcelSheetName for dictionary {}. This is an epic fail.", shortName, vocabulary);
        }

        throw new IllegalStateException(String.format(
            "Cannot get topByBestSpeedExcelSheetName for vocabulary %s. Both long name \"%s\" and short name \"%s\" are non-valid.",
            vocabulary,
            longName,
            shortName
        ));
    }


    public static String topByRacesCountIn(StandardDictionary vocabulary) {
        return String.format("Топ по пробегу в «%s»", StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }

    public static String topByHaulIn(StandardDictionary vocabulary) {
        return String.format("Топ по времени в «%s»", StandardDictionary.getDisplayNameInPrepositionalCase(vocabulary));
    }


    // todo: also implement for non-standard libraries which do not have a hard-code name
    // (although we still can hard-code the constants)
}
