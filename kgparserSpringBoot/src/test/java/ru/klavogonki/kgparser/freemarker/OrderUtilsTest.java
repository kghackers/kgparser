package ru.klavogonki.kgparser.freemarker;

import org.junit.jupiter.api.Test;
import ru.klavogonki.kgparser.jsonParser.Assertions;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderUtilsTest {

    @Test
    void testOrderByBestSpeed() {
        // imitate players top by best speed desc
        PlayerDto player1 = createPlayerDto("player 900 1", 900); // order 1-3
        PlayerDto player2 = createPlayerDto("player 900 2", 900); // order 1-3
        PlayerDto player3 = createPlayerDto("player 900 3", 900); // order 1-3
        PlayerDto player4 = createPlayerDto("player 850 1", 850); // order 4
        PlayerDto player5 = createPlayerDto("player 800 1", 800); // order 5-6
        PlayerDto player6 = createPlayerDto("player 800 2", 800); // order 5-6

        List<PlayerDto> topByBestSpeed = List.of(player1, player2, player3, player4, player5, player6);

        OrderUtils.fillOrderNumbers(topByBestSpeed, PlayerDto::getBestSpeed);

        Assertions
            .assertThat(player1)
            .hasLogin("player 900 1")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player2)
            .hasLogin("player 900 2")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player3)
            .hasLogin("player 900 3")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player4)
            .hasLogin("player 850 1")
            .hasBestSpeed(850)
            .hasOrderNumber("4");

        Assertions
            .assertThat(player5)
            .hasLogin("player 800 1")
            .hasBestSpeed(800)
            .hasOrderNumber(OrderUtils.formatRange(5, 6));

        Assertions
            .assertThat(player6)
            .hasLogin("player 800 2")
            .hasBestSpeed(800)
            .hasOrderNumber(OrderUtils.formatRange(5, 6));
    }

    @Test
    void testOrderByBestSpeedOnePlayer() {
        PlayerDto player1 = createPlayerDto("player 900 1", 900); // order 1

        List<PlayerDto> topByBestSpeed = List.of(player1);

        OrderUtils.fillOrderNumbers(topByBestSpeed, PlayerDto::getBestSpeed);

        Assertions
            .assertThat(player1)
            .hasLogin("player 900 1")
            .hasBestSpeed(900)
            .hasOrderNumber("1");
    }

    @Test
    void testOrderByBestSpeedPlayersWithTheSameBestSpeed() {
        PlayerDto player1 = createPlayerDto("player 900 1", 900); // order 1-3
        PlayerDto player2 = createPlayerDto("player 900 2", 900); // order 1-3
        PlayerDto player3 = createPlayerDto("player 900 3", 900); // order 1-3

        List<PlayerDto> topByBestSpeed = List.of(player1, player2, player3);

        OrderUtils.fillOrderNumbers(topByBestSpeed, PlayerDto::getBestSpeed);

        Assertions
            .assertThat(player1)
            .hasLogin("player 900 1")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player2)
            .hasLogin("player 900 2")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player3)
            .hasLogin("player 900 3")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));
    }

    @Test
    void testOrderByBestSpeedWithNullBestSpeedsAtEnd() {
        // imitate players top by best speed desc
        PlayerDto player1 = createPlayerDto("player 900 1", 900); // order 1-3
        PlayerDto player2 = createPlayerDto("player 900 2", 900); // order 1-3
        PlayerDto player3 = createPlayerDto("player 900 3", 900); // order 1-3
        PlayerDto player4 = createPlayerDto("player 850 1", 850); // order 4
        PlayerDto player5 = createPlayerDto("player null 1", null); // order 5-6
        PlayerDto player6 = createPlayerDto("player null 2", null); // order 5-6

        List<PlayerDto> topByBestSpeed = List.of(player1, player2, player3, player4, player5, player6);

        OrderUtils.fillOrderNumbers(topByBestSpeed, PlayerDto::getBestSpeed);

        Assertions
            .assertThat(player1)
            .hasLogin("player 900 1")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player2)
            .hasLogin("player 900 2")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player3)
            .hasLogin("player 900 3")
            .hasBestSpeed(900)
            .hasOrderNumber(OrderUtils.formatRange(1, 3));

        Assertions
            .assertThat(player4)
            .hasLogin("player 850 1")
            .hasBestSpeed(850)
            .hasOrderNumber("4");

        Assertions
            .assertThat(player5)
            .hasLogin("player null 1")
            .hasBestSpeed(null)
            .hasOrderNumber(OrderUtils.formatRange(5, 6));

        Assertions
            .assertThat(player6)
            .hasLogin("player null 2")
            .hasBestSpeed(null)
            .hasOrderNumber(OrderUtils.formatRange(5, 6));
    }

    @Test
    void testOrderByBestSpeedDoesNotFailOnEmptyList() {
        List<PlayerDto> topByBestSpeed = Collections.emptyList();

        OrderUtils.fillOrderNumbers(topByBestSpeed, PlayerDto::getBestSpeed); // nothing must fail

        assertThat(topByBestSpeed).isEmpty();
    }

    private PlayerDto createPlayerDto(final String login, final Integer bestSpeed) {
        PlayerDto player = new PlayerDto();
        player.setLogin(login);
        player.setBestSpeed(bestSpeed);

        return player;
    }
}
