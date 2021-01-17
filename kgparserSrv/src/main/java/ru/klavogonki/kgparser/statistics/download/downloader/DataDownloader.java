package ru.klavogonki.kgparser.statistics.download.downloader;

import ru.klavogonki.kgparser.statistics.Config;

import javax.ws.rs.HttpMethod;

public interface DataDownloader {

    void logDownloadStarting(int playerId);

    String getUrl(int playerId);

    String getJsonFilePath(Config config, int playerId);

    void logDataWrittenToFile(int playerId, String jsonFilePath);

    default String getHttpMethod() { // all now used methods are GET
        return HttpMethod.GET;
    }
}
