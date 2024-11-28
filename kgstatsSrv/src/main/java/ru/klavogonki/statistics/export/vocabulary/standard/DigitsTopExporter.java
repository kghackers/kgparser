package ru.klavogonki.statistics.export.vocabulary.standard;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.common.StandardDictionary;
import ru.klavogonki.statistics.springboot.Profiles;

@Log4j2
@Component
@Profile(Profiles.DATABASE)
public class DigitsTopExporter extends StandardVocabularyTopExporterDefaultImpl {

    @Override
    public StandardDictionary vocabulary() {
        return StandardDictionary.digits;
    }

    @Override
    public String headerName() {
        return "Цифры";
    }

    @Override
    public int minRacesCount() {
        return 100;
    }

    @Override
    public String loggerName() {
        return this.getClass().getSimpleName();
    }
}
