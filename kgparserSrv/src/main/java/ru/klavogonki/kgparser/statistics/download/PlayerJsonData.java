package ru.klavogonki.kgparser.statistics.download;

import ru.klavogonki.openapi.model.GetIndexDataResponse;
import ru.klavogonki.openapi.model.GetStatsOverviewResponse;
import ru.klavogonki.openapi.model.GetSummaryResponse;

import java.time.OffsetDateTime;

public class PlayerJsonData {
    public PlayerJsonData(final OffsetDateTime importDate, final GetSummaryResponse summary, final GetIndexDataResponse indexData, final GetStatsOverviewResponse statsOverview) {
        this.importDate = importDate;
        this.summary = summary;
        this.indexData = indexData;
        this.statsOverview = statsOverview;
    }

    public OffsetDateTime importDate;
    public GetSummaryResponse summary;
    public GetIndexDataResponse indexData;
    public GetStatsOverviewResponse statsOverview;
}
