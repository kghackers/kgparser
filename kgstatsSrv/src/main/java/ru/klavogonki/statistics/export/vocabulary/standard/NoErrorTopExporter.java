package ru.klavogonki.statistics.export.vocabulary.standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.common.StandardDictionary;
import ru.klavogonki.statistics.springboot.Profiles;

@Log4j2
@Component
@Profile(Profiles.DATABASE)
public class NoErrorTopExporter extends StandardVocabularyTopExporterDefaultImpl {

    @Override
    public StandardDictionary vocabulary() {
        return StandardDictionary.noerror;
    }

    @Override
    public int minRacesCount() {
        return 100; // todo: maybe increase, 3027 players >= 100, 1727 players >= 200
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
