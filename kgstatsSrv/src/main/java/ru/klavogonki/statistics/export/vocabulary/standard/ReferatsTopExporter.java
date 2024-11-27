package ru.klavogonki.statistics.export.vocabulary.standard;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.common.StandardDictionary;
import ru.klavogonki.statistics.springboot.Profiles;

@Log4j2
@Component
@Profile(Profiles.DATABASE)
public class ReferatsTopExporter extends StandardVocabularyTopExporterDefaultImpl {

    @Override
    public StandardDictionary vocabulary() {
        return StandardDictionary.referats;
    }

    @Override
    public int minRacesCount() {
        return 100;
    }

    @Override
    public String loggerName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по рекорду в «Яндекс.Рефератах»"; // 35 chars :(
        return "Топ по рекорду в «Я.Рефератах»"; // 30 chars
    }

    @Override
    public String topByRacesCountExcelSheetName() {
//        return "Топ по пробегу в «Яндекс.Рефератах»"; // 35 chars :(
        return "Топ по пробегу в «Я.Рефератах»"; // 30 chars
    }

    @Override
    public String topByHaulExcelSheetName() {
//        return "Топ по времени в «Яндекс.Рефератах»"; // 35 chars :(
        return "Топ по времени в «Я.Рефератах»"; // 30 chars
    }
}
