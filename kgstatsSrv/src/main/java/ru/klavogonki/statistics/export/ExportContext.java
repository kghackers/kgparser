package ru.klavogonki.statistics.export;

import lombok.Data;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.util.DateUtils;

import java.time.LocalDateTime;

@Data
public class ExportContext {

    public ExportContext(Config config) {
        // todo: probably we don't require this class, all fields are already present in Config
        this.webRootDir = config.getStatisticsPagesRootDir();
        this.minPlayerId = config.getMinPlayerId();
        this.maxPlayerId = config.getMaxPlayerId();
        this.dataDownloadStartDate = DateUtils.convertToUtcLocalDateTime(config.getDataDownloadStartDate()); // todo: think about UTC timeZone
        this.dataDownloadEndDate = DateUtils.convertToUtcLocalDateTime(config.getDataDownloadEndDate()); // todo: think about UTC timeZone
    }

    public String webRootDir; // where to export web files

    public int minPlayerId;
    public int maxPlayerId;
    public LocalDateTime dataDownloadStartDate; // todo: also change to OffsetDateTime
    public LocalDateTime dataDownloadEndDate; // todo: also change to OffsetDateTime
}
