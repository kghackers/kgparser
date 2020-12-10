package ru.klavogonki.kgparser;

import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;
import ru.klavogonki.kgparser.jsonParser.PlayerSummary;

public class PlayerJsonData {
    public PlayerJsonData(final PlayerSummary summary, final PlayerIndexData indexData) {
        this.summary = summary;
        this.indexData = indexData;
    }

    // todo: write a MapStruct converter from this object to a plain entity object
    public PlayerSummary summary;
    public PlayerIndexData indexData;
}
