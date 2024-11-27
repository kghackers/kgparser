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
public class ShortTextsTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.SHORT_TEXTS;
    }

    @Override
    public int minRacesCount() {
        return 200; // about 2440 users
    }

    @Override
    public String loggerName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по рекорду в «Коротких текстах»"; // 35 chars
        return "Рекорды в «Коротких текстах»"; // 28 chars
    }

    @Override
    public String topByRacesCountExcelSheetName() {
//        return "Топ по пробегу в «Коротких текстах»"; // 35 chars
        return "Пробег в «Коротких текстах»"; // 27 chars
    }

    @Override
    public String topByHaulExcelSheetName() {
//        return "Топ по времени в «Коротких текстах»"; // 35 chars
        return "Время в «Коротких текстах»"; // 26 chars
    }
}
