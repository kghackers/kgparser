package ru.klavogonki.statistics.download;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.statistics.Config;

public class SummaryDownloader implements DataDownloader {
    private static final Logger logger = LogManager.getLogger(SummaryDownloader.class);

    @Override
    public void logDownloadStarting(final int playerId) {
        logger.debug("Loading player summary for player {}...", playerId);
    }

    @Override
    public String getUrl(final int playerId) {
        return UrlConstructor.getSummary(playerId);
    }

    @Override
    public String getJsonFilePath(final Config config, final int playerId) {
        return config.getPlayerSummaryFilePath(playerId);
    }

    @Override
    public void logDataWrittenToFile(final int playerId, final String jsonFilePath) {
        logger.debug("Summary for player {} successfully written to file {}.", playerId, jsonFilePath);
    }
}
