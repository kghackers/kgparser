package ru.klavogonki.kgparser;

public enum DictionaryMode {

    normal,

    noerror,

    sprint,

    marathon,

    ;

    public static DictionaryMode getDictionaryMode(String dictionaryCode) {
        if (!Dictionary.isStandard(dictionaryCode)) {
            return normal;
        }

        StandardDictionary standardDictionary = StandardDictionary.valueOf(dictionaryCode);

        switch (standardDictionary) {
            case normal:
            case chars:
            case abra:
            case referats:
            case digits:
                return normal;

            case sprint:
                return sprint;

            case noerror:
                return noerror;

            case marathon:
                return marathon;

            default:
                throw new IllegalArgumentException(String.format("Unknown standard dictionary code: %s", dictionaryCode));
        }
    }
}
