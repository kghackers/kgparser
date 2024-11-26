package ru.klavogonki.common

@Suppress("Unused")
enum class DictionaryTag(

    /**
     * Название тэга для отображения. На русском.
     */
    @JvmField val displayName: String,
) {
    ENGLISH("English"),
    GERMAN("Deutsch"),
    DVORAK("Dvorak"),
    EXERCISE("Упражнение"),
    HRUST("Хруст"),
    DIGITS("Цифры"),
    CHARS("Буквы"),
    MINI_MARATHON("Мини-марафон"),
    HUNDRED("Соточка"),
    SHORT_TEXTS("Короткие тексты"),
    FREQUENT("Частотный"),
    FINGERS("Пальцы"),
    HAND("Рука"),
    LEFT_HAND("Левая"),
    RIGHT_HAND("Правая"),
}