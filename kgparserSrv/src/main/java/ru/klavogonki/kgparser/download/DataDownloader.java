package ru.klavogonki.kgparser.download;

import ru.klavogonki.kgparser.PlayerDataDownloader;

import javax.ws.rs.HttpMethod;

public interface DataDownloader {

    void logDownloadStarting(int playerId);

    String getUrl(int playerId);

    String getJsonFilePath(PlayerDataDownloader.Config config, int playerId);

    void logDataWrittenToFile(int playerId, String jsonFilePath);

    default String getHttpMethod() { // all now used methods are GET
        return HttpMethod.GET;
    }
}
