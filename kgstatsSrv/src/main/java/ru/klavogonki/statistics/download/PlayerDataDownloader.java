package ru.klavogonki.statistics.download;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.util.JacksonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerDataDownloader {
    private static final Logger logger = LogManager.getLogger(PlayerDataDownloader.class);

    public static final int REQUIRED_ARGUMENTS_COUNT = 2; // input config file path, output config file path

    public static void main(String[] args) {
        if (args.length != REQUIRED_ARGUMENTS_COUNT) {
            // todo: use logger instead of System.out??
            System.out.printf("Usage: %s <inputConfigFilePath> <outputConfigFilePath> %n", PlayerDataDownloader.class.getSimpleName());
            return;
        }

        String inputConfigFilePath = args[0];
        String outputConfigFilePath = args[1];
        // todo: validate that file paths are non-empty

        Config config = JacksonUtils.parseConfig(inputConfigFilePath);

        OffsetDateTime startDate = OffsetDateTime.now();
        logger.info("Download start date: {}", startDate);

        config.setDataDownloadStartDate(startDate);

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
        OffsetDateTime endDate = OffsetDateTime.now();

        logger.info("Threads used: {}", threadsCount);
        logger.info("Download start date: {}", startDate);
        logger.info("Download end date: {}", endDate);

        logger.info("Downloading data for players [{}; {}] (total {} players) took:", config.getMinPlayerId(), config.getMaxPlayerId(), config.getTotalPlayers());
        logDateTimeDiff(startDate, endDate);

        // save config with filled download dates to file
        config.setDataDownloadEndDate(endDate);
        JacksonUtils.serializeToFile(outputConfigFilePath, config, true); // todo: maybe set prettyPrint to false
    }

    public static String callableTask(final Config config, final Integer minPlayerId, final Integer maxPlayerId) throws IOException {
        String threadName = String.format("players-from-%d-to-%d", minPlayerId, maxPlayerId);
        Thread.currentThread().setName(threadName);

        OffsetDateTime chunkStartDate = OffsetDateTime.now();
        logger.info("Chunk download start date for players [{}; {}]: {}", minPlayerId, maxPlayerId, chunkStartDate);

        // for great thread-safety, create the downloader objects in each of the threads
        SummaryDownloader summaryDownloader = new SummaryDownloader();
        IndexDataDownloader indexDataDownloader = new IndexDataDownloader();
        StatsOverviewDownloader statsOverviewDownloader = new StatsOverviewDownloader();

        for (int playerId = minPlayerId; playerId <= maxPlayerId; playerId++) {
            savePlayerDataToJsonFile(summaryDownloader, config, playerId);
            savePlayerDataToJsonFile(indexDataDownloader, config, playerId);
            savePlayerDataToJsonFile(statsOverviewDownloader, config, playerId);
        }

        OffsetDateTime chunkEndDate = OffsetDateTime.now();
        logger.info("Chunk download start date for players [{}; {}]: {}", minPlayerId, maxPlayerId, chunkStartDate);
        logger.info("Chunk download end date for players [{}; {}]: {}", minPlayerId, maxPlayerId, chunkEndDate);

        logger.info("Downloading data for {} players took:", maxPlayerId - minPlayerId + 1);
        logDateTimeDiff(chunkStartDate, chunkEndDate);

        return String.format("Successfully downloaded player info for players from %d to %d", minPlayerId, maxPlayerId);
    }

    public static void logDateTimeDiff(final OffsetDateTime startDate, final OffsetDateTime endDate) {
        logger.info("Hours: {}", ChronoUnit.HOURS.between(startDate, endDate));
        logger.info("Minutes: {}", ChronoUnit.MINUTES.between(startDate, endDate));
        logger.info("Seconds: {}", ChronoUnit.SECONDS.between(startDate, endDate));
    }

    private static void savePlayerDataToJsonFile(final DataDownloader downloader, final Config config, final int playerId) throws IOException {
        downloader.logDownloadStarting(playerId);

        String urlString = downloader.getUrl(playerId);

        String out = loadUrlToString(urlString);
        // todo: make fake mode turned on/off with config
//        String out = String.format("player-%d-fake-result (url: %s)", playerId, urlString);

        String jsonFilePath = downloader.getJsonFilePath(config, playerId);
        FileUtils.writeStringToFile(new File(jsonFilePath), out, StandardCharsets.UTF_8);

        downloader.logDataWrittenToFile(playerId, jsonFilePath);
    }

    private static String loadUrlToString(final String urlString) throws IOException {
        logger.debug("Url to load: {}", urlString);

        // todo: use some nice library instead
        URL url = new URL(urlString);
        String out = new Scanner(url.openStream(), StandardCharsets.UTF_8) // this line will require java 10
            .useDelimiter("\\A")
            .next();

        logger.debug("Response for url {}:", urlString);
        logger.debug(StringUtils.abbreviate(out, 100));  // do not spam the whole response to log!
        return out;
    }

    static List<ImmutablePair<Integer, Integer>> split(Config config) {
        return split(
            config.getThreadsCount(),
            config.getMinPlayerId(),
            config.getMaxPlayerId()
        );
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
