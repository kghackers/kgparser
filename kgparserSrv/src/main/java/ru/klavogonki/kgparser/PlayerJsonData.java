package ru.klavogonki.kgparser;

import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;

public class PlayerJsonData {
    public PlayerJsonData(final PlayerSummary summary, final PlayerIndexData indexData) {
        this.summary = summary;
        this.indexData = indexData;
    }

    // todo: add importDate and fill it
    public PlayerSummary summary;
    public PlayerIndexData indexData;
}
