package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;

@Log4j2
@Component
public class TrainingIndexFingersTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.TRAINING_INDEX_FINGERS;
    }

    @Override
    public int minRacesCount() {
        return 100; // about 562 users
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по рекорду в «Тренируем указательные»"; // 41 chars
        return "Рекорды в «Тренируем указ-е»"; // 28 chars
    }

    @Override
    public String topByRacesCountExcelSheetName() {
//        return "Топ по пробегу в «Тренируем указательные»"; // 41 chars
        return "Пробег в «Тренируем указ-е»"; // 27 chars
    }

    @Override
    public String topByHaulExcelSheetName() {
//        return "Топ по времени в «Тренируем указательные»"; // 41 chars
        return "Время в «Тренируем указ-е»"; // 26 chars
    }
}
