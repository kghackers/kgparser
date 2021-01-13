package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;

@Log4j2
@Component
public class MiniMarathonTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.MINI_MARATHON;
    }

    @Override
    public int minRacesCount() {
        return 100; // about 916 users
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по рекорду в «Мини-марафоне, 800 знаков»"; // 44 chars
        return "Рекорды в «Мини-марафоне»"; // 25 chars
    }

    @Override
    public String topByRacesCountExcelSheetName() {
//        return "Топ по пробегу в «Мини-марафоне, 800 знаков»"; // 44 chars
        return "Пробег в «Мини-марафоне»"; // 24 chars
    }

    @Override
    public String topByHaulExcelSheetName() {
//        return "Топ по времени в «Мини-марафоне, 800 знаков»"; // 44 chars
        return "Время в «Мини-марафоне»"; // 23 chars
    }
}
