package ru.klavogonki.kgparser.export;

import lombok.Data;

import java.time.LocalDateTime;

// todo: move to kgParserSrv and use instead of PlayerSummaryDownloader.Config
@Data
public class ExportContext {

    public String webRootDir; // where to export web files

    public int minPlayerId;
    public int maxPlayerId;
    public LocalDateTime dataDownloadStartDate;
    public LocalDateTime dataDownloadEndDate;
}
