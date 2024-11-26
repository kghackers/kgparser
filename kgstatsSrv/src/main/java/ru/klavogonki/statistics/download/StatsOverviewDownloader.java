package ru.klavogonki.statistics.download;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.common.UrlConstructor;
import ru.klavogonki.statistics.Config;

public class StatsOverviewDownloader implements DataDownloader {
    private static final Logger logger = LogManager.getLogger(StatsOverviewDownloader.class);

    @Override
    public void logDownloadStarting(final int playerId) {
        logger.debug("Loading player stats overview for player {}...", playerId);
    }

    @Override
    public String getUrl(final int playerId) {
        return UrlConstructor.getStatsOverview(playerId);
    }

    @Override
    public String getJsonFilePath(final Config config, final int playerId) {
        return config.getStatsOverviewFilePath(playerId);
    }

    @Override
    public void logDataWrittenToFile(final int playerId, final String jsonFilePath) {
        logger.debug("Stats overview for player {} successfully written to file {}.", playerId, jsonFilePath);
    }
}
