package ru.klavogonki.statistics.export.vocabulary.non_standard;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.common.NonStandardDictionary;
import ru.klavogonki.statistics.export.vocabulary.standard.NonStandardVocabularyTopExporterDefaultImpl;
import ru.klavogonki.statistics.springboot.Profiles;

@Log4j2
@Component
@Profile(Profiles.DATABASE)
public class PinkiesPlusTopExporter extends NonStandardVocabularyTopExporterDefaultImpl {

    @Override
    public NonStandardDictionary vocabulary() {
        return NonStandardDictionary.PINKIES_PLUS;
    }

    @Override
    public int minRacesCount() {
        return 100; // about 571 users
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
