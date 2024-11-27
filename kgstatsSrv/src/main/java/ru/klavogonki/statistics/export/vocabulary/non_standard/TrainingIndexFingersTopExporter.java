package ru.klavogonki.statistics.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.common.NonStandardDictionary;
import ru.klavogonki.statistics.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;
import ru.klavogonki.statistics.springboot.Profiles;

@Log4j2
@Component
@Profile(Profiles.DATABASE)
public class TrainingIndexFingersTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public int dictionaryId() {
        return NonStandardDictionary.TRAINING_INDEX_FINGERS.id;
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
