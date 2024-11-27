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
public class FrequencyVocabularyTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public int dictionaryId() {
        return NonStandardDictionary.FREQUENCY_VOCABULARY.id;
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
