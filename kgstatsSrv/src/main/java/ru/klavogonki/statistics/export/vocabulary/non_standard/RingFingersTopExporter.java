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
    public String loggerName() {
        return this.getClass().getSimpleName();
    }
}
