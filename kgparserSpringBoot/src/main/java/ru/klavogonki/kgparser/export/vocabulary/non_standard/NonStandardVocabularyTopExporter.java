package ru.klavogonki.kgparser.export.vocabulary.non_standard;

import ru.klavogonki.kgparser.export.vocabulary.VocabularyTopExporter;

@SuppressWarnings("unused") // todo: use it for non-standard dictionaries, implement like StandardVocabularyTopExporter
public interface NonStandardVocabularyTopExporter extends VocabularyTopExporter {

    @Override
    default boolean isStandard() {
        return false;
    }
}
