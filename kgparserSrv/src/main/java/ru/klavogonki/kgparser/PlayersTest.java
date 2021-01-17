package ru.klavogonki.kgparser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayersTest
{
	private static final Logger logger = LogManager.getLogger(PlayersTest.class);

	public static void main(String[] args) {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);

		testPlayersSet();
		testCompetitionPlayers();
	}

	private static void testPlayersSet() {
		Player player1 = new Player();
		player1.setProfileId(1);
		player1.setName("name 1");

		Player player2 = new Player();
		player2.setProfileId(2);
		player2.setName("name 2");

		Player player1copy = new Player();
		player1copy.setProfileId(1);
		player1copy.setName("name 1 copy");

		Set<Player> players = new HashSet<>();
		players.add(player1);
		players.add(player2);
		players.add(player1copy);

		for (Player player : players) {
			logger.info("player: {}, {}.",player.getProfileId(), player.getName());
		}
	}

	private static void testCompetitionPlayers() {
		Player player1 = new Player();
		player1.setProfileId(1);
		player1.setName("name 1");

		Player player2 = new Player();
		player2.setProfileId(2);
		player2.setName("name 2");

		Player player3 = new Player();
		player3.setProfileId(3);
		player3.setName("name 3");

		Player player4 = new Player();
		player4.setProfileId(4);
		player4.setName("name 4");

		Round round1 = new Round();
		PlayerRoundResult result11 = new PlayerRoundResult(round1, player1);
		round1.addResult(result11);
		PlayerRoundResult result12 = new PlayerRoundResult(round1, player2);
		round1.addResult(result12);
		PlayerRoundResult result13 = new PlayerRoundResult(round1, player3);
		round1.addResult(result13);

		Round round2 = new Round();
		PlayerRoundResult result21 = new PlayerRoundResult(round2, player1);
		round2.addResult(result21);
		PlayerRoundResult result24 = new PlayerRoundResult(round2, player4);
		round2.addResult(result24);

		Round round3 = new Round();
		PlayerRoundResult result32 = new PlayerRoundResult(round3, player2);
		round3.addResult(result32);

		List<Round> rounds = new ArrayList<>();
		rounds.add(round1);
		rounds.add(round2);
		rounds.add(round3);

		Competition competition = new Competition(rounds);

		for (Player player : competition.getPlayers()) {
			logger.info("competition player: {}, {}", player.getProfileId(), player.getName());
		}
	}
}
