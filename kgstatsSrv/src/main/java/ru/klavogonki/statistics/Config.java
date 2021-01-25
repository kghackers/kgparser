package ru.klavogonki.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.klavogonki.statistics.util.DateUtils;

import java.io.File;
import java.time.OffsetDateTime;

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
    private OffsetDateTime dataDownloadStartDate;
    private OffsetDateTime dataDownloadEndDate;

    // data used for statistics pages generation
    // @see ExportContext
    private String statisticsPagesRootDir;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // do not de-serialize, there is no setter and no field
    public int getTotalPlayers() {
        return maxPlayerId - minPlayerId + 1;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // do not de-serialize, there is no setter and no field
    public String getDataDownloadStartDateString() {
        if (dataDownloadStartDate == null) {
            throw new IllegalStateException("dataDownloadStartDate not yet set. Cannot format it to String.");
        }

        return DateUtils.formatDateTime(dataDownloadStartDate);
    }

    public String getPlayerSummaryFilePath(final int playerId) {
        return getDataDirectory(playerId, "summary");
    }

    public String getPlayerIndexDataFilePath(final int playerId) {
        return getDataDirectory(playerId, "index-data");
    }

    public String getStatsOverviewFilePath(final int playerId) {
        return getDataDirectory(playerId, "stats-overview");
    }

    public String getDataDirectory(final int playerId, final String subdir) {
        return jsonFilesRootDir // D:/kg/json
            + File.separator + getDataDownloadStartDateString() // /2021-01-17 05-27-39
            + File.separator + subdir // /summary
            + File.separator + playerId + ".json"; // /242585.json
    }
}
