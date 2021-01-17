package ru.klavogonki.kgparser.export;

import lombok.Data;
import ru.klavogonki.kgparser.statistics.Config;

import java.time.LocalDateTime;

@Data
public class ExportContext {

    public ExportContext(Config config) {
        // todo: probably we don't require this class, all fields are already present in Config
        this.webRootDir = config.getStatisticsPagesRootDir();
        this.minPlayerId = config.getMinPlayerId();
        this.maxPlayerId = config.getMaxPlayerId();
        this.dataDownloadStartDate = config.getDataDownloadStartDate();
        this.dataDownloadEndDate = config.getDataDownloadEndDate();
    }

    public String webRootDir; // where to export web files

    public int minPlayerId;
    public int maxPlayerId;
    public LocalDateTime dataDownloadStartDate;
    public LocalDateTime dataDownloadEndDate;
}
