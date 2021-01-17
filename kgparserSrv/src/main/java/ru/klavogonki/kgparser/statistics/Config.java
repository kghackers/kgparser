package ru.klavogonki.kgparser.statistics;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Конфиг, настраивающий процесс загрузки данных игроков для статистики,
 * генерацию базы данных из загруженных файлов
 * и генерацию статистики на основе базы данных.
 */
@Data
public class Config {

    // start data for PlayerDataDownloader
    private String jsonFilesRootDir;
    private int threadsCount;
    private int minPlayerId;
    private int maxPlayerId;

    // data filled by PlayerDataDownloader
    private LocalDateTime dataDownloadStartDate;
    private LocalDateTime dataDownloadEndDate;

    // data used for statistics pages generation
    // @see ExportContext
    private String statisticsPagesRootDir;
}
