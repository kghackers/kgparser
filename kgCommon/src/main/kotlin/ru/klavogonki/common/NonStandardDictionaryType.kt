package ru.klavogonki.common

/**
 * Тип нестандартного словаря.
 */
enum class NonStandardDictionaryType(
    /**
     * Имя в API клавогонок. Все буквы в нижнем регистре.
     */
    @JvmField val klavogonkiName: String
) {
    /**
     * Слова.
     */
    words("words"),

    /**
     * Фразы.
     */
    phrases("phrases"),

    /**
     * Тексты.
     */
    texts("texts"),

    /**
     * URL.
     */
    url("url"),

    /**
     * Книга.
     */
    book("book"),

    /**
     * Генератор.
     */
    generator("generator"),
}