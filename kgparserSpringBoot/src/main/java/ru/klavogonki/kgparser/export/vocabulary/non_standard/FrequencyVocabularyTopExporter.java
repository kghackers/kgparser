package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;

@Log4j2
@Component
public class FrequencyVocabularyTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.FREQUENCY_VOCABULARY;
    }

    @Override
    public int minRacesCount() {
        return 200; // about 2664 users
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по рекорду в «Частотном словаре»"; // 36 chars
        return "Топ по рекорду в «Частотном»"; // 28 chars
    }

    @Override
    public String topByRacesCountExcelSheetName() {
//        return "Топ по пробегу в «Частотном словаре»"; // 36 chars
        return "Топ по пробегу в «Частотном»"; // 28 chars
    }

    @Override
    public String topByHaulExcelSheetName() {
//        return "Топ по времени в «Частотном словаре»"; // 36 chars
        return "Топ по времени в «Частотном»"; // 28 chars
    }
}
