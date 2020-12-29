package ru.klavogonki.kgparser;

import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetSummaryResponse;

import java.time.LocalDateTime;

public class PlayerJsonData {
    public PlayerJsonData(final LocalDateTime importDate, final GetSummaryResponse summary, final GetIndexDataResponse indexData) {
        this.importDate = importDate;
        this.summary = summary;
        this.indexData = indexData;
    }

    public LocalDateTime importDate;
    public GetSummaryResponse summary;
    public GetIndexDataResponse indexData;
}
