package ru.klavogonki.kgparser.jsonParser.dto;

import lombok.Data;

/**
 * Minimal amount of data for dynamically filtered "rank to players count" page.
 */
@Data
public class PlayerRankLevelAndTotalRacesCount {

    public PlayerRankLevelAndTotalRacesCount(final Integer rankLevel, final Integer totalRacesCount) {
        this.rankLevel = rankLevel;
        this.totalRacesCount = totalRacesCount;
    }

    public static int[] toArray(PlayerRankLevelAndTotalRacesCount count) {
        return new int[] { count.rankLevel, count.totalRacesCount };
    }

    private Integer rankLevel;
    private Integer totalRacesCount;
}
