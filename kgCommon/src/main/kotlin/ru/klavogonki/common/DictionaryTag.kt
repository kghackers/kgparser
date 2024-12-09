package ru.klavogonki.common

@Suppress("Unused")
enum class DictionaryTag(

    /**
     * Название тэга для отображения. На русском.
     */
    @JvmField val displayName: String,
) {
    STANDARD("Стандартный"),
    NORMAL("Обычный"),
    ABRA("Абракадабра"),
    REFERATS("Рефераты"),
    NO_ERROR("Безошибочный"),
    MARATHON("Марафон"),
    LETTERS("Буквы"),
    DIGITS("Цифры"),
    SPRINT("Спринт"),

    ENGLISH("English"),
    GERMAN("Deutsch"),
    DVORAK("Dvorak"),
    EXERCISE("Упражнение"),
    HRUST("Хруст"),
    MINI_MARATHON("Мини-марафон"),
    HUNDRED("Соточка"),
    SHORT_TEXTS("Короткие тексты"),
    FREQUENT("Частотный"),
    CYBERTEXT("Кибертекст"),
    FINGERS("Пальцы"),
    HAND("Рука"),
    LEFT_HAND("Левая"),
    RIGHT_HAND("Правая"),
}