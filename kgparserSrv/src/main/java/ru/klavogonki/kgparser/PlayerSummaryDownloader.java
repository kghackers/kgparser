package ru.klavogonki.kgparser;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.kgparser.util.DateUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;

public class PlayerSummaryDownloader {
    private static final Logger logger = LogManager.getLogger(PlayerSummaryDownloader.class);

    static class Config {
        public static final int REQUIRED_ARGUMENTS_COUNT = 3;

        String rootDir;
        int minPlayerId;
        int maxPlayerId;
        private String startDateString;
        LocalDateTime startDate;

        public void setStartDate(String startDate) {
            this.startDateString = startDate;
            this.startDate = DateUtils.parseLocalDateTime(startDateString);
        }

        public String getPlayerSummaryFilePath(final int playerId) {
            return rootDir + File.separator + startDateString + File.separator + "summary" + File.separator + playerId + ".json";
        }

        public String getPlayerIndexDataFilePath(final int playerId) {
            return rootDir + File.separator + startDateString + File.separator + "index-data" + File.separator + playerId + ".json";
        }

        public void log() {
            logger.debug("Config:");
            logger.debug("  rootDir: {}", rootDir);
            logger.debug("  minPlayerId: {}", minPlayerId);
            logger.debug("  maxPlayerId: {}", maxPlayerId);
            logger.debug("  startDateString: {}", startDateString);
            logger.debug("  startDate: {}", startDate);
        }

        public static Config parseFromArguments(final String[] args) {
            int index = 0;

            Config config = new Config();
            config.rootDir = args[index++];
            config.minPlayerId = Integer.parseInt(args[index++]);
            config.maxPlayerId = Integer.parseInt(args[index++]);
            return config;
        }
    }

    public static void main(String[] args) throws IOException {
        // todo: pass a path to a json file with config instead

        if (args.length != Config.REQUIRED_ARGUMENTS_COUNT) {
            // todo: use logger instead of System.out??
            System.out.printf("Usage: %s <rootJsonDir> <minPlayerId> <maxPlayerId> %n", PlayerSummaryDownloader.class.getSimpleName());
            return;
        }

        Config config = Config.parseFromArguments(args);

        config.setStartDate(DateUtils.formatDateTime(LocalDateTime.now()));
        config.log();

        for (int playerId = config.minPlayerId; playerId <= config.maxPlayerId; playerId++) {
            savePlayerSummaryToJsonFile(config, playerId);
            savePlayerIndexDataToJsonFile(config, playerId);
        }
    }

    private static void savePlayerSummaryToJsonFile(final Config config, final int playerId) throws IOException {
        logger.debug("Loading player summary for player {}...", playerId);

        String urlString = UrlConstructor.getSummary(playerId);

        String out = loadUrlToString(urlString);

        String jsonFilePath = config.getPlayerSummaryFilePath(playerId);
        FileUtils.writeStringToFile(new File(jsonFilePath), out, StandardCharsets.UTF_8);
        logger.debug("Summary for player {} successfully written to file {}.", playerId, jsonFilePath);
    }

    private static void savePlayerIndexDataToJsonFile(final Config config, final int playerId) throws IOException {
        logger.debug("Loading player index data for player {}...", playerId);

        String urlString = UrlConstructor.getIndexData(playerId);

        String out = loadUrlToString(urlString);

        String jsonFilePath = config.getPlayerIndexDataFilePath(playerId);
        FileUtils.writeStringToFile(new File(jsonFilePath), out, StandardCharsets.UTF_8);
        logger.debug("Index data for player {} successfully written to file {}.", playerId, jsonFilePath);
    }

    private static String loadUrlToString(final String urlString) throws IOException {
        logger.debug("Url to load: {}", urlString);

        // todo: use some nice library instead
        URL url = new URL(urlString);
        String out = new Scanner(url.openStream(), StandardCharsets.UTF_8) // this line will require java 10
            .useDelimiter("\\A")
            .next();

        logger.debug("Response for url {}:", urlString);
        logger.debug(out);
        return out;
    }
}
