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
public class NormalInEnglishTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public int dictionaryId() {
        return NonStandardDictionary.NORMAL_IN_ENGLISH.id;
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
