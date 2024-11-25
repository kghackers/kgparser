package ru.klavogonki.common

import ru.klavogonki.common.DictionaryUtils.isStandard

enum class DictionaryMode(

    /**
     * Имя в API клавогонок. Все буквы в нижнем регистре.
     */
    @JvmField val klavogonkiName: String
) {
    normal("normal"),

    noerror("noerror"),

    sprint("sprint"),

    marathon("marathon"),
    ;

    companion object {

        @JvmStatic
        fun getDictionaryMode(dictionaryCode: String): DictionaryMode {
            if (!isStandard(dictionaryCode)) {
                return normal
            }

            val standardDictionary = StandardDictionary.getByKlavogonkiName(dictionaryCode)
            return standardDictionary.dictionaryMode
        }
    }
}
