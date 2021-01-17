package ru.klavogonki.kgparser.statistics.download;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerDataDownloaderTest {

    @Test
    void testSplitPlayerIdsWithoutMod() {
        List<ImmutablePair<Integer, Integer>> split = PlayerDataDownloader.split(9, 100, 999);

        assertThat(split)
            .hasSize(9)
            .containsExactly(
                new ImmutablePair<>(100, 199),
                new ImmutablePair<>(200, 299),
                new ImmutablePair<>(300, 399),
                new ImmutablePair<>(400, 499),
                new ImmutablePair<>(500, 599),
                new ImmutablePair<>(600, 699),
                new ImmutablePair<>(700, 799),
                new ImmutablePair<>(800, 899),
                new ImmutablePair<>(900, 999)
            );
    }

    @Test
    void testSplitPlayerIdsWithMod() {
        List<ImmutablePair<Integer, Integer>> split = PlayerDataDownloader.split(9, 100, 1000);

        assertThat(split)
            .hasSize(10)
            .containsExactly(
                new ImmutablePair<>(100, 199),
                new ImmutablePair<>(200, 299),
                new ImmutablePair<>(300, 399),
                new ImmutablePair<>(400, 499),
                new ImmutablePair<>(500, 599),
                new ImmutablePair<>(600, 699),
                new ImmutablePair<>(700, 799),
                new ImmutablePair<>(800, 899),
                new ImmutablePair<>(900, 999),
                new ImmutablePair<>(1000, 1000)
            );
    }

    @Test
    void testThreadsMoreThanPlayers() {
        List<ImmutablePair<Integer, Integer>> split = PlayerDataDownloader.split(9, 1, 1);

        assertThat(split)
            .hasSize(1)
            .containsExactly(
                new ImmutablePair<>(1, 1)
            );
    }

    @Test
    void testOnePlayerPerThread() {
        List<ImmutablePair<Integer, Integer>> split = PlayerDataDownloader.split(9, 100, 108);

        assertThat(split)
            .hasSize(9)
            .containsExactly(
                new ImmutablePair<>(100, 100),
                new ImmutablePair<>(101, 101),
                new ImmutablePair<>(102, 102),
                new ImmutablePair<>(103, 103),
                new ImmutablePair<>(104, 104),
                new ImmutablePair<>(105, 105),
                new ImmutablePair<>(106, 106),
                new ImmutablePair<>(107, 107),
                new ImmutablePair<>(108, 108)
            );
    }

    @Test
    void testOneThread() {
        List<ImmutablePair<Integer, Integer>> split = PlayerDataDownloader.split(1, 100, 109);

        assertThat(split)
            .hasSize(1)
            .containsExactly(
                new ImmutablePair<>(100, 109)
            );
    }
}
