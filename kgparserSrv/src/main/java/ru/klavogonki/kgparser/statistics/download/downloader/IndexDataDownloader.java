package ru.klavogonki.kgparser.statistics.download.downloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.kgparser.statistics.Config;

public class IndexDataDownloader implements DataDownloader {
    private static final Logger logger = LogManager.getLogger(IndexDataDownloader.class);

    @Override
    public void logDownloadStarting(final int playerId) {
        logger.debug("Loading player index data for player {}...", playerId);
    }

    @Override
    public String getUrl(final int playerId) {
        return UrlConstructor.getIndexData(playerId);
    }

    @Override
    public String getJsonFilePath(final Config config, final int playerId) {
        return config.getPlayerIndexDataFilePath(playerId);
    }

    @Override
    public void logDataWrittenToFile(final int playerId, final String jsonFilePath) {
        logger.debug("Index data for player {} successfully written to file {}.", playerId, jsonFilePath);
    }
}
