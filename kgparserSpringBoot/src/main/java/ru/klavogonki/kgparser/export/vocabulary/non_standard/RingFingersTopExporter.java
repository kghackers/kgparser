package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.NonStandardDictionary;
import ru.klavogonki.kgparser.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;

@Log4j2
@Component
public class RingFingersTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.RING_FINGERS;
    }

    @Override
    public int minRacesCount() {
        return 50; // about 279 users
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
