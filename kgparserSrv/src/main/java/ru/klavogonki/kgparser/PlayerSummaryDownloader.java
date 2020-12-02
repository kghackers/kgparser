package ru.klavogonki.kgparser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PlayerSummaryDownloader { // todo: use logger instead of System.out

    private static final int MIN_PLAYER_ID = 30000; // todo: confirm
    private static final int MAX_PLAYER_ID = 623112; // todo: at 02.12.2020 00:20 (nice date)

    private static final String ROOT_JSON_DATA_DIR = "C:\\java\\kg\\players-summary\\";

    public static void main(String[] args) throws IOException {
//        int playerId = 242585; // nosferatum

        for (int playerId = MIN_PLAYER_ID; playerId <= MAX_PLAYER_ID; playerId++) {
            savePlayerSummaryToJsonFile(playerId);
        }
    }

    private static void savePlayerSummaryToJsonFile(final int playerId) throws IOException {
        System.out.printf("Loading player summary for player %d...%n", playerId);

        String urlString = String.format("https://klavogonki.ru/api/profile/get-summary?id=%d", playerId);
        System.out.println("Url to load summary:" + urlString);

        // todo: use some nice library instead
        URL url = new URL(urlString);
        String out = new Scanner(url.openStream(), StandardCharsets.UTF_8)
            .useDelimiter("\\A")
            .next();

        System.out.printf("Response for player %d:%n", playerId);
        System.out.println(out);

        String jsonFilePath = ROOT_JSON_DATA_DIR + playerId + ".json";
        FileUtils.writeStringToFile(new File(jsonFilePath), out, StandardCharsets.UTF_8);
        System.out.printf("Summary for user %d successfully written to file %s%n.", playerId, jsonFilePath);
    }
}
