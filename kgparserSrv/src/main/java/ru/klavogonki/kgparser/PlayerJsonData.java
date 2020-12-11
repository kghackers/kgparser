package ru.klavogonki.kgparser;

import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;

import java.time.LocalDateTime;

public class PlayerJsonData {
    public PlayerJsonData(final LocalDateTime importDate, final PlayerSummary summary, final PlayerIndexData indexData) {
        this.importDate = importDate;
        this.summary = summary;
        this.indexData = indexData;
    }

    public LocalDateTime importDate;
    public PlayerSummary summary;
    public PlayerIndexData indexData;
}
