package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;

@Log4j2
@Component
public class NormalInEnglishTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.NORMAL_IN_ENGLISH;
    }

    @Override
    public int minRacesCount() {
        return 200; // about 3248 users
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по рекорду в «Обычном in English»"; // 37 chars
        return "Рекорды в «Обычном in English»"; // 30 chars
    }

    @Override
    public String topByRacesCountExcelSheetName() {
//        return "Топ по пробегу в «Обычном in English»"; // 37 chars
        return "Пробег в «Обычном in English»"; // 29 chars
    }

    @Override
    public String topByHaulExcelSheetName() {
//        return "Топ по времени в «Обычном in English»"; // 37 chars
        return "Время в «Обычном in English»"; // 28 chars
    }
}
