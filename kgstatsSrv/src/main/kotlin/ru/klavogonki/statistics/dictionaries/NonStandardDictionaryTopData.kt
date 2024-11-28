package ru.klavogonki.statistics.dictionaries

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(
    "loggerName",
    "minRacesCount",
    "topByBestSpeedExcelSheetName",
    "topByRacesCountExcelSheetName",
    "topByHaulExcelSheetName"
)
data class NonStandardDictionaryTopData(

    /**
     * Английское имя для использования в логгировании при генерации топов словаря.
     */
    @JsonProperty("loggerName")
    @JvmField val loggerName: String,

    /**
     * Название словаря для отображения в заголовке статистики.
     * Обычно содержит неразрывные пробелы, если в названии словаря есть пробелы
     * (то есть если название состоит из нескольких слов.
     */
    @JsonProperty("headerName")
    @JvmField val headerName: String,

    /**
     * Минимальное число заездов (пробег) по словарю для того, чтобы игрок попадал в топ по данному словарю.
     */
    @JsonProperty("minRacesCount")
    @JvmField val minRacesCount: Int,

    /**
     * Название листа в Excel для топа по скорости по словарю.
     *
     * Если не указано, будет использоваться стандартное название листа.
     *
     * Используется обычно в случае, если стандартное название листа имеет длину больше 31 символа,
     * что ломает Excel для Windows.
     */
    @JsonProperty("topByBestSpeedExcelSheetName")
    @JvmField val topByBestSpeedExcelSheetName: String?,

    /**
     * Название листа в Excel для топа по пробегу по словарю.
     *
     * Если не указано, будет использоваться стандартное название листа.
     *
     * Используется обычно в случае, если стандартное название листа имеет длину больше 31 символа,
     * что ломает Excel для Windows.
     */
    @JsonProperty("topByRacesCountExcelSheetName")
    @JvmField val topByRacesCountExcelSheetName: String?,

    /**
     * Название листа в Excel для топа по времени по словарю.
     *
     * Если не указано, будет использоваться стандартное название листа.
     *
     * Используется обычно в случае, если стандартное название листа имеет длину больше 31 символа,
     * что ломает Excel для Windows.
     */
    @JsonProperty("topByHaulExcelSheetName")
    @JvmField val topByHaulExcelSheetName: String?
)