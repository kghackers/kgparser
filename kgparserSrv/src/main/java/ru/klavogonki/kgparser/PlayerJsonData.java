package ru.klavogonki.kgparser;

import ru.klavogonki.kgparser.jsonParser.PlayerSummary;
import ru.klavogonki.openapi.model.GetIndexDataResponse;

import java.time.LocalDateTime;

public class PlayerJsonData {
    public PlayerJsonData(final LocalDateTime importDate, final PlayerSummary summary, final GetIndexDataResponse indexData) {
        this.importDate = importDate;
        this.summary = summary;
        this.indexData = indexData;
    }

    public LocalDateTime importDate;
    public PlayerSummary summary;
    public GetIndexDataResponse indexData;
}
