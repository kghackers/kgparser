package ru.klavogonki.statistics.dto;

import lombok.Data;
import ru.klavogonki.common.Rank;

@Data
public class PlayersByRankCount {

    public PlayersByRankCount(final Integer rankLevel, final Long playersCount) {
        this.rankLevel = rankLevel;
        this.playersCount = playersCount;

        Rank rank = Rank.getRank(rankLevel);

        this.rankName = rank.name();
        this.rankDisplayName = rank.displayName;
    }

    private Integer rankLevel;

    // todo: parse rankLevel to instead, refactor Rank to have fields
    private String rankName;
    private String rankDisplayName;

    private Long playersCount; // count from JPA query is long, setting to int fails on non-found constructor
}
