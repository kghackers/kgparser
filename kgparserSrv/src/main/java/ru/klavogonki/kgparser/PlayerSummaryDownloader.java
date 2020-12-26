package ru.klavogonki.kgparser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.kgparser.util.DateUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerSummaryDownloader {
    private static final Logger logger = LogManager.getLogger(PlayerSummaryDownloader.class);

    public static class Config {
        public static final int REQUIRED_ARGUMENTS_COUNT = 4;

        int threadsCount;
        String rootDir;
        int minPlayerId;
        int maxPlayerId;
        private String startDateString;
        LocalDateTime startDate;

        public void setStartDate(String startDate) {
            this.startDateString = startDate;
            this.startDate = DateUtils.parseLocalDateTime(startDateString);
        }

        public int getTotalPlayers() {
            return maxPlayerId - minPlayerId + 1;
        }

        public String getPlayerSummaryFilePath(final int playerId) {
            return rootDir + File.separator + startDateString + File.separator + "summary" + File.separator + playerId + ".json";
        }

        public String getPlayerIndexDataFilePath(final int playerId) {
            return rootDir + File.separator + startDateString + File.separator + "index-data" + File.separator + playerId + ".json";
        }

        public void log() {
            logger.debug("============================================");
            logger.debug("Config:");
            logger.debug("  threadsCount: {}", threadsCount);
            logger.debug("  rootDir: {}", rootDir);
            logger.debug("  minPlayerId: {}", minPlayerId);
            logger.debug("  maxPlayerId: {}", maxPlayerId);
            logger.debug("  startDateString: {}", startDateString);
            logger.debug("  startDate: {}", startDate);
            logger.debug("============================================");
        }

        public static Config parseFromArguments(final String[] args) {
            int index = 0;

            Config config = new Config();
            config.rootDir = args[index++];
            config.minPlayerId = Integer.parseInt(args[index++]);
            config.maxPlayerId = Integer.parseInt(args[index++]);
            config.threadsCount = Integer.parseInt(args[index++]);
            return config;
        }
    }

    public static void main(String[] args) {
        // todo: pass a path to a json file with config instead

        if (args.length != Config.REQUIRED_ARGUMENTS_COUNT) {
            // todo: use logger instead of System.out??
            System.out.printf("Usage: %s <rootJsonDir> <minPlayerId> <maxPlayerId> <threadsCount> %n", PlayerSummaryDownloader.class.getSimpleName());
            return;
        }

        Config config = Config.parseFromArguments(args);

        LocalDateTime startDate = LocalDateTime.now();
        logger.info("Download start date: {}", startDate);

        config.setStartDate(DateUtils.formatDateTime(startDate));
        config.log();

        // parallel loading
        List<ImmutablePair<Integer, Integer>> threadChunks = split(config);
        int threadsCount = threadChunks.size(); // might be config.threadsCount + 1 in case of threadsCount % totalPlayersCount != 0

        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        // fill callables for each chunk, each chunk performs all operations for players range for chunk
        List<Callable<String>> callableTasks = new ArrayList<>(threadsCount);

        for (ImmutablePair<Integer, Integer> chunk : threadChunks) {
            Integer minPlayerId = chunk.getLeft();
            Integer maxPlayerId = chunk.getRight();

            Callable<String> chunkCallable = () -> {
                try {
                    return callableTask(config, minPlayerId, maxPlayerId);
                }
                catch (Exception e) {
                    logger.error(String.format("Exception on handling players [%d; %d]", minPlayerId, maxPlayerId), e);
                    throw e;
                }
            };

            callableTasks.add(chunkCallable);
        }

        try {
            executorService.invokeAll(callableTasks); // we don't use the returned futures
        }
        catch (InterruptedException e) {
            logger.debug("============================================");
            logger.error("executorService.invokeAll was interrupted", e);
            throw new RuntimeException(e);
        }

        executorService.shutdown();

        // wait until all threads finish their execution
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS); // wait more or less infinitely
            logger.debug("executorService.awaitTermination executed.");
        }
        catch (InterruptedException e) {
            logger.error("executorService.awaitTermination was terminated. Shutting down NOW.", e);
            executorService.shutdownNow();
            throw new RuntimeException(e);
        }

        // log the results summary
        LocalDateTime endDate = LocalDateTime.now();

        logger.info("Threads used: {}", threadsCount);
        logger.info("Download start date: {}", startDate);
        logger.info("Download end date: {}", endDate);

        logger.info("Downloading data for players [{}; {}] (total {} players) took:", config.minPlayerId, config.maxPlayerId, config.getTotalPlayers());
        logDateTimeDiff(startDate, endDate);
    }

    public static String callableTask(final Config config, final Integer minPlayerId, final Integer maxPlayerId) throws IOException {
        String threadName = String.format("players-from-%d-to-%d", minPlayerId, maxPlayerId);
        Thread.currentThread().setName(threadName);

        LocalDateTime chunkStartDate = LocalDateTime.now();
        logger.info("Chunk download start date for players [{}; {}]: {}", minPlayerId, maxPlayerId, chunkStartDate);

        for (int playerId = minPlayerId; playerId <= maxPlayerId; playerId++) {
            savePlayerSummaryToJsonFile(config, playerId);
            savePlayerIndexDataToJsonFile(config, playerId);
        }

        LocalDateTime chunkEndDate = LocalDateTime.now();
        logger.info("Chunk download start date for players [{}; {}]: {}", minPlayerId, maxPlayerId, chunkStartDate);
        logger.info("Chunk download end date for players [{}; {}]: {}", minPlayerId, maxPlayerId, chunkEndDate);

        logger.info("Downloading data for {} players took:", maxPlayerId - minPlayerId + 1);
        logDateTimeDiff(chunkStartDate, chunkEndDate);

        return String.format("Successfully downloaded player info for players from %d to %d", minPlayerId, maxPlayerId);
    }

    public static void logDateTimeDiff(final LocalDateTime startDate, final LocalDateTime endDate) {
        logger.info("Hours: {}", ChronoUnit.HOURS.between(startDate, endDate));
        logger.info("Minutes: {}", ChronoUnit.MINUTES.between(startDate, endDate));
        logger.info("Seconds: {}", ChronoUnit.SECONDS.between(startDate, endDate));
    }

    private static void savePlayerSummaryToJsonFile(final Config config, final int playerId) throws IOException {
        logger.debug("Loading player summary for player {}...", playerId);

        String urlString = UrlConstructor.getSummary(playerId);

        String out = loadUrlToString(urlString);
        // todo: make fake mode turned on/off with config
//        String out = String.format("player-%d-fake-summary (url: %s)", playerId, urlString);

        String jsonFilePath = config.getPlayerSummaryFilePath(playerId);
        FileUtils.writeStringToFile(new File(jsonFilePath), out, StandardCharsets.UTF_8);
        logger.debug("Summary for player {} successfully written to file {}.", playerId, jsonFilePath);
    }

    private static void savePlayerIndexDataToJsonFile(final Config config, final int playerId) throws IOException {
        logger.debug("Loading player index data for player {}...", playerId);

        String urlString = UrlConstructor.getIndexData(playerId);

        String out = loadUrlToString(urlString);
        // todo: make fake mode turned on/off with config
//        String out = String.format("player-%d-fake-index-data (url: %s)", playerId, urlString);

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

    static List<ImmutablePair<Integer, Integer>> split(Config config) {
        return split(config.threadsCount, config.minPlayerId, config.maxPlayerId);
    }

    // returns list of minPlayerId; maxPlayerId
    static List<ImmutablePair<Integer, Integer>> split(int threadsCount, int minPlayerId, int maxPlayerId) {
        if (threadsCount < 1) {
            throw new IllegalArgumentException(String.format("Incorrect threadsCount: %d, threadsCount must be >= 1", threadsCount));
        }

        if (minPlayerId > maxPlayerId) {
            throw new IllegalArgumentException(String.format("minPlayerId = %d > maxPlayerId = %d", minPlayerId, maxPlayerId));
        }

        int totalPlayers = maxPlayerId - minPlayerId + 1;
        int chunkSize = totalPlayers / threadsCount;

        logger.debug("threadsCount: {}", threadsCount);
        logger.debug("minPlayerId: {}", minPlayerId);
        logger.debug("maxPlayerId: {}", maxPlayerId);
        logger.debug("totalPlayers: {}", totalPlayers);
        logger.debug("chunkSize: {}", chunkSize);

        List<ImmutablePair<Integer, Integer>> result = new ArrayList<>();

        if (chunkSize > 0) {
            for (int i = 0; i < threadsCount; i++) {
                int chunkMinPlayerId = minPlayerId + (chunkSize * i);
                int chunkMaxPlayerId = chunkMinPlayerId + chunkSize - 1;

                result.add(new ImmutablePair<>(chunkMinPlayerId, chunkMaxPlayerId));
            }
        }
        else {
            logger.warn("chunkSize = {}, probably totalPlayers = {} < threadsCount = {}. Only one chunk with all tasks will be returned.", chunkSize, totalPlayers, threadsCount);
        }

        if (totalPlayers % threadsCount != 0) { // add a "tail part" if required
            int chunkMinPlayerId = minPlayerId + (chunkSize * threadsCount);
            int chunkMaxPlayerId = maxPlayerId;

            result.add(new ImmutablePair<>(chunkMinPlayerId, chunkMaxPlayerId));
        }

        logger.debug("Total player chunks: {}", result.size());
        return result;
    }
}
