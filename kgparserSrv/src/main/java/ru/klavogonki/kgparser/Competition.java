package ru.klavogonki.kgparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.http.HttpClientTest;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.ObjectUtils;
import su.opencode.kefir.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Соревнование. Содержит в себе набор заездов в рамках соревнования.
 * По заездам соревнования формируются результаты.
 */
public class Competition extends JsonObject
{
	public Competition() {
		this.rounds = new ArrayList<>();
	}
	public Competition(List<Round> rounds) {
		this.rounds = rounds;
	}
	public Competition(String name, List<Round> rounds) {
		this.name = name;
		this.rounds = rounds;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Round> getRounds() {
		return rounds;
	}
	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	public void addRound(Round round) {
		// todo: add and check preventAdd flag
		this.rounds.add(round);
	}

	/**
	 * Упорядочивает заезды соревнования по дате начала
	 * и устанавливает номера заездов (как по всему соревнованию, так и внутри словарей).
	 */
	public void orderRoundsByBeginTime() {
		rounds.sort(new RoundBeginTimeComparator());

		for (int i = 0, roundsSize = rounds.size(); i < roundsSize; i++)
		{
			Round round = rounds.get(i);
			round.setNumber(i + Round.FIRST_ROUND_NUMBER); // номер заезда
		}

		Map<String, List<Round>> map = getRoundsByDictionariesMap();
		for (List<Round> dictionaryRounds : map.values())
		{
			for (int i = 0, roundsSize = dictionaryRounds.size(); i < roundsSize; i++)
			{
				Round round = dictionaryRounds.get(i);
				round.setNumberInDictionary(i + Round.FIRST_ROUND_NUMBER);
			}
		}
	}

	/**
	 * @return множество рангов всех игроков, представленных в соревновании.
	 * Ранги упорядочены по возрастанию.
	 */
	@Json(exclude = true)
	public SortedSet<Rank> getRanks() {
		SortedSet<Rank> ranks = new TreeSet<>( new RankComparator() );

		Set<Player> players = getPlayers();
		for (Player player : players)
		{
			if ( !player.isGuest() )
				ranks.add( player.getRank() );
		}

		return ranks;
	}

	// todo: копия такого метода с учетом минимального числа заездов
	/**
	 * @return список всех игроков, принимавших участие хотя бы в одном заезде соревнования.
	 * <strong>Гости не учитываются.</strong>
	 */
	@Json(exclude = true)
	public Set<Player> getPlayers() {
		Set<Player> players = new HashSet<>();

		for (Round round : rounds)
		{
			List<PlayerRoundResult> results = round.getResults();
			if ( ObjectUtils.empty(results) )
				continue; // no results in round

			for (PlayerRoundResult result : results)
			{
				Player player = result.getPlayer();
				if ( !player.isGuest() )
				{
					players.add(player);
				}
			}
		}

		return players;
	}

	/**
	 * Заполняет ранги игроков согласно обычному рангу.
	 * (В скрипте voidmain сохраняются ранги по текущему словарю).
	 */
	@Json(exclude = true)
	public void fillPlayersRanks() {
		Set<Player> players = getPlayers();

		Map<Integer, Rank> profileIdsToRanks = new HashMap<>();
		for (Player player : players)
		{
			if (player.isGuest())
				continue;

			Rank normalRank = HttpClientTest.getUserRank(player.getProfileId());
			profileIdsToRanks.put(player.getProfileId(), normalRank);
		}

		for (Round round : rounds)
		{
			List<PlayerRoundResult> results = round.getResults();
			for (PlayerRoundResult result : results)
			{
				Player player = result.getPlayer();
				if ( player.isGuest() )
					continue;

				Rank normalRank = profileIdsToRanks.get( player.getProfileId() );
				if (normalRank == null)
					throw new IllegalStateException( concat("Cannot get normal rank for player with profileId = \"", player.getProfileId(), "\"") );

				player.setRank(normalRank);
			}
		}
	}

	/**
	 * Заполняет для всех игроков соревнования
	 * их ранги и их рекорды в обычном на текущий момент.
	 */
	@Json(exclude = true)
	public void fillPlayersNormalModeData() {
		Set<Player> players = getPlayers();

		Map<Integer, Integer> profileIdsToNormalRecords = new HashMap<>();
		Map<Integer, Rank> profileIdsToRanks = new HashMap<>();
		for (Player player : players)
		{
			if (player.isGuest())
				continue;

			Integer profileId = player.getProfileId();

			Integer normalRecord = HttpClientTest.getUserNormalRecord(profileId);
			if (normalRecord == null)
			{
				logger.warn("Cannot get normal record for player \"{}\" (profileId = {}). Getting his rank from userSummary query", player.getName(), profileId);
				Rank normalRank = HttpClientTest.getUserRank(profileId);
				profileIdsToRanks.put(profileId, normalRank);
			}
			else
			{
				profileIdsToNormalRecords.put(profileId, normalRecord);
				profileIdsToRanks.put(profileId, Rank.getRankByNormalRecord(normalRecord));
			}
		}

		for (Round round : rounds)
		{
			List<PlayerRoundResult> results = round.getResults();
			for (PlayerRoundResult result : results)
			{
				Player player = result.getPlayer();
				if ( player.isGuest() )
					continue;

				Integer profileId = player.getProfileId();
				Rank normalRank = profileIdsToRanks.get(profileId);
				if (normalRank == null)
					throw new IllegalStateException( concat("Cannot get normal rank for player with profileId = \"", profileId, "\"") );

				Integer normalRecord = profileIdsToNormalRecords.get(profileId);
				// do not check on null because normal record may be null

				player.setRank(normalRank);
				player.setNormalRecord(normalRecord);
			}
		}
	}

	/**
	 * @return множество всех словарей, заезды по которым есть в соревновании.
	 * Словари упорядочены по порядку появления первого заезда словаря в соревновании.
	 */
	@Json(exclude = true)
	public Set<Dictionary> getDictionaries() {
		Set<Dictionary> dictionaries = new LinkedHashSet<>(); // saves insertion order

		for (Round round : rounds)
		{
			Dictionary dictionary = round.getDictionary();
			if (dictionary == null)
				throw new IllegalStateException( StringUtils.concat("Round ", round.getNumber(), " has no set dictionary") );

			dictionaries.add(dictionary);
		}

		return dictionaries;
	}

	/**
	 * Заполняет имена словарей всех заездов.
	 */
	public void fillDictionariesNames() {
		Set<Dictionary> dictionaries = this.getDictionaries();

		List<String> nonStandardDictionariesCodes = new ArrayList<>();
		Map<String, String> codesToNames = new HashMap<>();
		for (Dictionary dictionary : dictionaries)
		{
			String dictionaryCode = dictionary.getCode();

			if (dictionary.isStandard())
			{
				StandardDictionary standardDictionary = StandardDictionary.valueOf(dictionaryCode);
				codesToNames.put(dictionaryCode, standardDictionary.displayName);
			}
			else
			{
				nonStandardDictionariesCodes.add(dictionaryCode);
			}
		}

		Map<String, String> nonStandardDictionariesCodesToNames = HttpClientTest.getDictionariesNames(nonStandardDictionariesCodes);

		for (Map.Entry<String, String> entry : nonStandardDictionariesCodesToNames.entrySet()) {
			String nonStandardDictionaryCode = entry.getKey();
			String names = entry.getValue();

			codesToNames.put(nonStandardDictionaryCode, names);
		}

		// set dictionary name for each round's dictionary
		for (Round round : rounds)
		{
			round.getDictionary().setName( codesToNames.get(round.getDictionary().getCode()) );
		}
	}

	/**
	 * @param dictionaryCode строковый код словаря
	 * @return <code>true</code> &mdash; если соревнование содержит хотя бы один заезд по словарю с указанным кодом
	 * <br>
	 * <code>false</code> &mdash; если соревнование не содержит ни одного заезда по словарю с указанным кодом
	 */
	public boolean containsDictionary(String dictionaryCode) {
		if ( StringUtils.empty(dictionaryCode) )
			throw new IllegalArgumentException("dictionaryCode must not be null or empty");

		// todo: think about iterating by rounds and use their dictionary instead of using this.getDictionaries
		for (Dictionary dictionary : this.getDictionaries()) {
			if (dictionary.hasCode(dictionaryCode)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param dictionary словарь
	 * @return <code>true</code> &mdash; если соревнование содержит хотя бы один заезд по указанному словарю
	 * <br>
	 * <code>false</code> &mdash; если соревнование не содержит ни одного заезда по указанному словарю
	 */
	public boolean containsDictionary(Dictionary dictionary) {
		if (dictionary == null)
			throw new IllegalArgumentException("dictionary cannot be null");

		return containsDictionary(dictionary.getCode());
	}


	/**
	 * @param dictionaryCode строковый код словаря
	 * @return количество заездов по словарю в соревновании. Больше либо равно 0.
	 */
	@Json(exclude = true)
	public int getRoundsCount(String dictionaryCode) {
		if ( StringUtils.empty(dictionaryCode) )
			throw new IllegalArgumentException("dictionaryCode must not be null or empty");

		int count = 0;

		for (Round round : rounds)
		{
			Dictionary dictionary = round.getDictionary();
			if (dictionary == null)
				throw new IllegalStateException( StringUtils.concat("Round ", round.getNumber(), " has no set dictionary") );

			if ( dictionary.hasCode(dictionaryCode) )
				count++;
		}

		return count;
	}
	/**
	 * @param dictionary словарь
	 * @return количество заездов по словарю в соревновании. Больше либо равно 0.
	 */
	@Json(exclude = true)
	public int getRoundsCount(Dictionary dictionary) {
		if (dictionary == null)
			throw new IllegalArgumentException("dictionary cannot be null");

		return getRoundsCount( dictionary.getCode() );
	}

	/**
	 * @param player игрок
	 * @return общее количество заездов, которое проехал игрок по всем заездам по всем словарям.
	 */
	@Json(exclude = true)
	public int getRoundsCount(Player player) {
		if (player == null)
			throw new IllegalArgumentException("player cannot be null");

		int count = 0;

		for (Round round : rounds)
		{
			if ( round.hasPlayerResult(player) )
				count++;
		}

		return count;
	}
	/**
	 * @param player игрок
	 * @param dictionary словарь
	 * @return общее количество заездов, которое проехал игрок по словарю
	 */
	@Json(exclude = true)
	public int getRoundsCount(Player player, Dictionary dictionary) {
		if (player == null)
			throw new IllegalArgumentException("player cannot be null");

		if (dictionary == null)
			throw new IllegalArgumentException("dictionary cannot be null");

		int count = 0;

		for (Round round : rounds)
		{
			if ( round.hasDictionary(dictionary) && round.hasPlayerResult(player) )
				count++;
		}

		return count;
	}

	/**
	 * @return мэп сгруппированных по словарям заездов соревнования.
	 * Ключом является {@linkplain ru.klavogonki.kgparser.Dictionary#code строковый код} словаря.
	 */
	@Json(exclude = true)
	public Map<String, List<Round>> getRoundsByDictionariesMap() {
		Map<String, List<Round>> map = new HashMap<>();

		for (Round round : rounds)
		{
			Dictionary dictionary = round.getDictionary();
			if (dictionary == null)
				throw new IllegalStateException( StringUtils.concat("Round ", round.getNumber(), " has no set dictionary") );

			String dictionaryCode = dictionary.getCode();

			if ( map.containsKey(dictionaryCode) )
			{ // map already contains round's dictionary -> add round to the existing list
				List<Round> dictionaryRounds = map.get(dictionaryCode);
				dictionaryRounds.add(round);
			}
			else
			{ // map does not contain round's dictionary -> create new list, put round to it and put it to the map with the dictionary code key
				List<Round> dictionaryRounds = new ArrayList<>();
				dictionaryRounds.add(round);

				map.put(dictionaryCode, dictionaryRounds);
			}
		}

		// todo: sort all lists by number

		return map;
	}

	/**
	 * @param number номер заезда
	 * @return заезд с таким номером или <code>null</code>, если заезда с таким номером в соревновании нет.
	 */
	@Json(exclude = true)
	public Round getRound(int number) {
		for (Round round : rounds)
		{
			Integer roundNumber = round.getNumber();
			if ( roundNumber.equals(number) )
				return round;
		}

		logger.info("Round with number {} is not present in competition \"{}\"", number, name);
		return null;
	}

	/**
	 * @param player игрок
	 * @param roundNumber сквозной номер заезда в соревновании
	 * @return результат, или <code>null</code>, если игрок не финишировал в заезде с указанным номером
	 */
	@Json(exclude = true)
	public PlayerRoundResult getPlayerRoundResult(Player player, Integer roundNumber) {
		Round round = getRound(roundNumber);
		if (round == null) {
			throw new IllegalArgumentException(String.format("Round with number %d is not present in competition \"%s\".", roundNumber, name));
		}

		return round.getPlayerResult(player);
	}

	/**
	 * @param player игрок
	 * @return список результатов игрока во всех заездах, где они присутствуют.
	 */
	@Json(exclude = true)
	public List<PlayerRoundResult> getPlayerRoundResults(Player player) {
		List<PlayerRoundResult> playerResults = new ArrayList<>();

		for (Round round : rounds)
		{
			PlayerRoundResult playerResult = round.getPlayerResult(player);
			if (playerResult != null)
			{
				playerResults.add(playerResult);
			}
		}

		return playerResults;
	}

	/**
	 * @param player игрок
	 * @param dictionary словарь
	 * @return список результатов игрока во всех заездах по указанному словарю, где результат игрока присутствует.
	 */
	@Json(exclude = true)
	public List<PlayerRoundResult> getPlayerRoundResults(Player player, Dictionary dictionary) {
		List<PlayerRoundResult> playerResults = new ArrayList<>();

		for (Round round : rounds)
		{
			if ( round.getDictionary().isSame(dictionary) )
			{
				PlayerRoundResult playerResult = round.getPlayerResult(player);
				if (playerResult != null)
				{
					playerResults.add(playerResult);
				}
			}
		}

		return playerResults;
	}

	/**
	 * Название соревнования.
	 */
	private String name;

	/**
	 * Заезды в рамках соревнования.
	 */
	private List<Round> rounds;

	private static final Logger logger = LogManager.getLogger(Competition.class);
}
