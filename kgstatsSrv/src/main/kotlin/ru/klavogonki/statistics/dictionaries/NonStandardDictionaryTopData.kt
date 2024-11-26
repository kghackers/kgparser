package ru.klavogonki.statistics.dictionaries

data class NonStandardDictionaryTopData(

    /**
     * Английское имя для использования в логгировании при генерации топов словаря.
     */
    @JvmField val loggerName: String,

    /**
     * Минимальное число заездов (пробег) по словарю для того, чтобы игрок попадал в топ по данному словарю.
     */
    @JvmField val minRacesCount: Int,

    /**
     * Название листа в Excel для топа по скорости по словарю.
     *
     * Если не указано, будет использоваться стандартное название листа.
     *
     * Используется обычно в случае, если стандартное название листа имеет длину больше 31 символа,
     * что ломает Excel для Windows.
     */
    @JvmField val topByBestSpeedExcelSheetName: String?,

    /**
     * Название листа в Excel для топа по пробегу по словарю.
     *
     * Если не указано, будет использоваться стандартное название листа.
     *
     * Используется обычно в случае, если стандартное название листа имеет длину больше 31 символа,
     * что ломает Excel для Windows.
     */
    @JvmField val topByRacesCountExcelSheetName: String?,

    /**
     * Название листа в Excel для топа по времени по словарю.
     *
     * Если не указано, будет использоваться стандартное название листа.
     *
     * Используется обычно в случае, если стандартное название листа имеет длину больше 31 символа,
     * что ломает Excel для Windows.
     */
    @JvmField val topByHaulExcelSheetName: String?
)