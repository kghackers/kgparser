package ru.klavogonki.kgparser;

/**
 * Популярные нестандартные словари.
 */
public enum NonStandardDictionary {

    // most popular non-standard dictionaries
    NORMAL_IN_ENGLISH(5539, "Обычный in English", "Обычном in English"),
    ONE_HUNDRED_RUSSIAN(25856, "Соточка", "Соточке"),
    FREQUENCY_VOCABULARY(192, "Частотный словарь", "Частотном словаре"),
    SHORT_TEXTS(1789, "Короткие тексты", "Коротких текстах"),
    DIGITS_ONE_HUNDRED(62238, "Цифросоточка", "Цифросоточке"),
    FIVE_THOUSAND_MOST_POPULAR_WORDS(203, "5000 самых частых слов", "5000 самых частых словах"),
    ONE_HUNDRED(62586, "One Hundred", "One Hundred"),
    SHORT_TEXTS_IN_ENGLISH(14878, "Short Texts in English", "Short Texts in English"),
    MINI_MARATHON(6018, "Мини-марафон, 800 знаков", "Мини-марафоне, 800 знаков"),
    CYBERTEXT(6562, "Кибертекст", "Кибертексте"),
    CYBERTEXT_2(115606, "Кибертекст 2.0", "Кибертексте 2.0"),
    PINKIES_PLUS(3714, "Мизинцы+", "Мизинцах+"),
    TRAINING_INDEX_FINGERS(226, "Тренируем указательные", "Тренируем указательные"),
    RING_FINGERS(8223, "Безымянные", "Безымянных"),

    // Упражнения Хруста
    // номера словарей от 1 до 24 по порядку: 13571 13572 13573 13574 13583 13584 13585 13654 13656 13659
    // 13661 13663 13664 16346 16759 16762 17495 17497 17498 17499
    // 32013 32014 32015 32016
    HRUST_EXERCISE_1(13571, "Упражнение №1", "Упражнении №1"),
    HRUST_EXERCISE_2(13572, "Упражнение №2", "Упражнении №2"),
    HRUST_EXERCISE_3(13573, "Упражнение №3", "Упражнении №3"),
    HRUST_EXERCISE_4(13574, "Упражнение №4", "Упражнении №4"),
    HRUST_EXERCISE_5(13583, "Упражнение №5", "Упражнении №5"),
    HRUST_EXERCISE_6(13584, "Упражнение №6", "Упражнении №6"),
    HRUST_EXERCISE_7(13585, "Упражнение №7", "Упражнении №7"),
    HRUST_EXERCISE_8(13654, "Упражнение №8", "Упражнении №8"),
    HRUST_EXERCISE_9(13656, "Упражнение №9", "Упражнении №9"),
    HRUST_EXERCISE_10(13659, "Упражнение №10", "Упражнении №10"),
    HRUST_EXERCISE_11(13661, "Упражнение №11", "Упражнении №11"),
    HRUST_EXERCISE_12(13663, "Упражнение №12", "Упражнении №12"),
    HRUST_EXERCISE_13(13664, "Упражнение №13", "Упражнении №13"),
    HRUST_EXERCISE_14(16346, "Упражнение №14", "Упражнении №14"),
    HRUST_EXERCISE_15(16759, "Упражнение №15", "Упражнении №15"),
    HRUST_EXERCISE_16(16762, "Упражнение №16", "Упражнении №16"),
    HRUST_EXERCISE_17(17495, "Упражнение №17", "Упражнении №17"),
    HRUST_EXERCISE_18(17497, "Упражнение №18", "Упражнении №18"),
    HRUST_EXERCISE_19(17498, "Упражнение №19", "Упражнении №19"),
    HRUST_EXERCISE_20(17499, "Упражнение №20", "Упражнении №20"),
    HRUST_EXERCISE_21(32013, "Упражнение №21", "Упражнении №21"),
    HRUST_EXERCISE_22(32014, "Упражнение №22", "Упражнении №22"),
    HRUST_EXERCISE_23(32015, "Упражнение №23", "Упражнении №23"),
//    HRUST_EXERCISE_24(32016, "Упражнение №24", "Упражнении №24"),
    HRUST_EXERCISE_24(32016, "Упражнение заключительное", "Упражнении заключительном"),

    // Мультилингва
    // https://klavogonki.ru/forum/software/59/page6/#post108
    // http://klavogonki.ru/forum/events/18143/

    // номера словарей по возрастанию: normal 5539 8950 25130 29236 29464 29468 29510 29513 29521 29537 29616 29667 30395 30641 35239 40559 106181 106487 106554 106688 114083 114825 115209 115378 115424 115943 116073 116553 116554 116745 117862 122698 122702 122761 122888 122891 123163 130272 136302 136354 136403 136795 136908 137357 138500 141412 141415 141423 141609 141610 141960
    ;


    NonStandardDictionary(final int code, final String name, final String namePrepositional) {
        this(Dictionary.NON_STANDARD_DICTIONARY_ID_PREFIX + code, name, namePrepositional);
    }

    NonStandardDictionary(final String code, final String name, final String namePrepositional) {
        this.code = code;
        this.name = name;
        this.namePrepositional = namePrepositional;
    }

    public final String code;
    public final String name;
    public final String namePrepositional;
}
