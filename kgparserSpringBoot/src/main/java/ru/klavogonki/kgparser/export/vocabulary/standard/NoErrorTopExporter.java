package ru.klavogonki.kgparser.export.vocabulary.standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.StandardDictionary;

@Log4j2
@Component
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
