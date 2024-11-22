package ru.klavogonki.statistics.export

import ru.klavogonki.statistics.Config
import ru.klavogonki.statistics.util.DateUtils
import java.time.LocalDateTime

// todo: probably we don't require this class, all fields are already present in Config
// todo: remove @JvmField annotations after moving all the using classes to Kotlin
data class ExportContext(
    @JvmField val webRootDir: String,
    @JvmField val minPlayerId: Int,
    @JvmField val maxPlayerId: Int,
    @JvmField val dataDownloadStartDate: LocalDateTime,
    @JvmField val dataDownloadEndDate: LocalDateTime
) {
    constructor(config: Config): this(
        // we can use property-access from a Java class when there is an explicit geter
        // but the lombok-generated getters do NOT work

        webRootDir = config.statisticsPagesRootDir,
        minPlayerId = config.minPlayerId,
        maxPlayerId = config.maxPlayerId,

        // todo: think about UTC timeZone
        // todo: also change to OffsetDateTime
        dataDownloadStartDate = DateUtils.convertToUtcLocalDateTime(config.dataDownloadStartDate),

        // todo: think about UTC timeZone
        // todo: also change to OffsetDateTime
        dataDownloadEndDate = DateUtils.convertToUtcLocalDateTime(config.dataDownloadEndDate)
    )
}