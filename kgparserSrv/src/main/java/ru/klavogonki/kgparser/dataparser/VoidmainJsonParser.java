package ru.klavogonki.kgparser.dataparser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.Round;
import su.opencode.kefir.util.DateUtils;
import su.opencode.kefir.util.FileUtils;
import su.opencode.kefir.util.JsonUtils;
import su.opencode.kefir.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class VoidmainJsonParser {

	public static void main(String[] args) {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);

//		String filePath = "C:\\java\\kgparser\\doc\\voidmain\\game_37411__v15.json";
//		String filePath = "C:\\java\\kgparser\\doc\\voidmain\\kuvet_55\\kuvet_01_game_97265.json";
//		String filePath =  "C:\\java\\kgparser\\doc\\voidmain\\kuvet_55\\kuvet_12_game_48526.json";
//		parseRoundFromFile(filePath, VERSION_1_5);

/*
		String filePath =  "C:\\java\\kgparser\\doc\\voidmain\\game_67854__v17.json";
		parseRoundFromFile(filePath, VERSION_1_7);
*/

/*
		String competitionName = "Кювет №55";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\kuvet_55\\";
		Competition competition = parseCompetition(competitionName, dirPath);

		AverageSpeedCounter.logCompetitionInfo(competition);
*/

/*
		String competitionName = "Марафоны 11.02.2014";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\marathons_2014_02_11\\";
		Competition competition = parseCompetition(competitionName, dirPath, VERSION_1_7);
*/
		String competitionName = "Street racer № 38 (13.02.2014";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\StreetRacer_83__2014_02_13";
		Competition competition = parseCompetition(competitionName, dirPath, null);

		JSONObject competitionJson = competition.toJson();
		logger.info("Competition json: ");
		logger.info(competitionJson);

/*
		String competitionName = "Серпантин №8 (11.02.2014)";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\serpantin_08__2014_02_11\\";
		Competition competition = parseCompetition(competitionName, dirPath, VERSION_1_5);
*/


/*
		AverageSpeedCounter.logCompetitionInfo(competition);

		HighChartValue highChartValue = SpeedChartFiller.fillData(competition);
		logger.info("highchartValue: ");
		logger.info(highChartValue.toJson());
*/
	}

	private static String getScriptVersionFromFile(String filePath) {
		byte[] jsonBytes = FileUtils.readFile(filePath);
		String json = new String(jsonBytes, StandardCharsets.UTF_8);

		JSONObject jsonObject = new JSONObject(json);
		String version = jsonObject.getString(EXPORT_SCRIPT_VERSION_FIELD_NAME);

		if (StringUtils.empty(version))
			return VERSION_1_5;

		return version;
	}

	private static Round parseRoundFromFile(String filePath, String scriptVersion) {
		byte[] jsonBytes = FileUtils.readFile(filePath);
		String json = new String(jsonBytes, StandardCharsets.UTF_8);

		if (StringUtils.empty(scriptVersion) || scriptVersion.equals(VERSION_1_5))
			return parseRound15(json);

		if (scriptVersion.equals(VERSION_1_7))
			return parseRound17(json);

		if (scriptVersion.equals(VERSION_1_8))
			return parseRound18(json);

		String errorMessage = String.format("Unsupported voidmain script version: %s", scriptVersion);
		throw new IllegalArgumentException(errorMessage);
	}

	/**
	 * Парсит соревнование на основе всех файлов, сохраненных в указанной директории.
	 * Поддиректории не учитываются.
	 *
	 * @param competitionName название соревнования
	 * @param dirPath         путь к директории, в которой хранятся json-файлы с результатами заездов соревнования
	 * @param scriptVersion   версия скрипта voidmain.
	 *                        Если равна <code>null</code>, то версия скрипта каждого заезда определяется на основе поля
	 *                        &laquo;{@linkplain #EXPORT_SCRIPT_VERSION_FIELD_NAME exportScriptVersion}&raquo; в JSON каждого заезда.
	 * @return распарсенная модель соревнования
	 */
	public static Competition parseCompetition(String competitionName, String dirPath, String scriptVersion) {
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
			String errorMessage = String.format("\"%s\" is not a directory.", dirPath);
			throw new IllegalArgumentException(errorMessage);
		}

		List<Round> rounds = new ArrayList<>();

		File[] files = dir.listFiles();
		if ((files == null) || (files.length == 0)) {
			String errorMessage = String.format("Directory \"%s\" does not contain any files.", dirPath);
			throw new IllegalArgumentException(errorMessage);
		}

		for (File file : files) {
			if (file.isDirectory()) // skip inner directories
				continue;

			String filePath = file.getAbsolutePath();
			logger.info("");
			logger.info("========================================================================");
			logger.info("Parsing Round from file \"{}\"...", filePath);

			String version;
			if (StringUtils.notEmpty(scriptVersion)) {
				logger.info("Using passed scriptVersion = \"{}\".", scriptVersion);
				version = scriptVersion;
			}
			else {
				logger.info("ScriptVersion is not passed. Getting scriptVersion from file \"{}\".", filePath);
				version = getScriptVersionFromFile(filePath);
				logger.info("ScriptVersion from file \"{}\" is \"{}\".", filePath, version);
			}

			Round round = parseRoundFromFile(filePath, version);
			rounds.add(round);
			logger.info("Round successfully parsed from file \"{}\".", filePath);
		}

		logger.info("========================================================================");
		logger.info("Total rounds parsed: {}", rounds.size());

		Competition competition = new Competition();
		competition.setName(competitionName);

		competition.setRounds(rounds);
		competition.orderRoundsByBeginTime();

		competition.fillDictionariesNames();
		competition.fillPlayersNormalModeData(); // fill ranks and normal records

		logFilledCompetitionInfo(competition);
		return competition;
	}

	private static void logFilledCompetitionInfo(Competition competition) {
		logger.info("===========================================");
		logger.info("Competition info:");
		logger.info("Name: {}", competition.getName());

		Set<Dictionary> dictionaries = competition.getDictionaries();
		logger.info("");
		logger.info("Total dictionaries: {}", dictionaries.size());

		for (Dictionary dictionary : dictionaries) {
			logger.info("Dictionary: {}. Code: {}.", dictionary.getName(), dictionary.getCode());
		}

		logger.info("===========================================");
		Map<String, List<Round>> roundsByDictionaries = competition.getRoundsByDictionariesMap();
		for (String dictionaryCode : roundsByDictionaries.keySet()) {
			List<Round> dictionaryRounds = roundsByDictionaries.get(dictionaryCode);
			Dictionary dictionary = dictionaryRounds.get(0).getDictionary();

			logger.info(
				"Dictionary: {}. Code: {}. Rounds by dictionary count: {}.",
				dictionary.getName(),
				dictionary.getCode(),
				dictionaryRounds.size()
			);
		}

		logger.info("===========================================");
		SortedSet<Rank> ranks = competition.getRanks();
		logger.info("Present player dictionary ranks: {}", ranks.toString());

		Set<Player> players = competition.getPlayers();
		logger.info("");
		logger.info("Total players (not including guests): {}.", players.size());

		// todo: may be sort players by rounds count desc
		for (Player player : players) {
			logger.info("===========================================");
			logger.info("Player {} (profileId = {})", player.getName(), player.getProfileId());

			int playerTotalRoundsCount = competition.getRoundsCount(player);
			logger.info("Player total rounds count: {}", playerTotalRoundsCount);

			for (Dictionary dictionary : dictionaries) { // players result for each dictionary
				int playerDictionaryRoundsCount = competition.getRoundsCount(player, dictionary);
				logger.info(
					"Dictionary: {} (code = {}). Player rounds count: {}",
					dictionary.getName(),
					dictionary.getCode(),
					playerDictionaryRoundsCount
				);
			}
		}

		List<Round> rounds = competition.getRounds();
		logger.info("");
		logger.info("Total rounds: {}", rounds.size());

		for (Round round : rounds) {
			Dictionary roundDictionary = round.getDictionary();

			logger.info(
				"Round number: {}. Dictionary: {} (code = {}). Number in dictionary: {}.",
				round.getNumber(),
				roundDictionary.getName(),
				roundDictionary.getCode(),
				round.getNumberInDictionary()
			);
		}
	}

	/**
	 * @param json JSON-данные, сохраненные версией 1.5 скрипта <a href="http://klavogonki.ru/u/#/364239/">voidmain</a>
	 * @return модель заезда на основе json-данных заезда
	 */
	public static Round parseRound15(String json) {
		JSONObject jsonObject = new JSONObject(json);

		int gameId = jsonObject.getInt(GAME_ID_FIELD_NAME);

		long beginTimeMilliseconds = jsonObject.getLong(GAME_BEGIN_TIME_FIELD_NAME);
		Date beginTime = new Date(beginTimeMilliseconds); // to milliseconds

		JSONObject gameInfo = jsonObject.getJSONObject(GAME_INFO_FIELD_NAME);
		String dictionaryCode = gameInfo.getString(GAME_INFO_TYPE_FIELD_NAME);

		String text = null;
		String bookAuthor = null;
		String bookName = null;
		Integer textLength = null;
		if (JsonUtils.hasField(jsonObject, TEXT_INFO_FIELD_NAME_1_5)) {
			JSONObject textInfo = jsonObject.getJSONObject(TEXT_INFO_FIELD_NAME_1_5);

			if (!textInfo.isNull(TEXT_INFO_TEXT_FIELD_NAME))
				text = textInfo.getString(TEXT_INFO_TEXT_FIELD_NAME);

//			if ( !textInfo.isNull(TEXT_INFO_LENGTH_FIELD_NAME) )
//				textLength = textInfo.getInt(TEXT_INFO_LENGTH_FIELD_NAME);

			if (!textInfo.isNull(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME))
				bookAuthor = textInfo.getString(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME);

			if (!textInfo.isNull(TEXT_INFO_BOOK_NAME_FIELD_NAME))
				bookName = textInfo.getString(TEXT_INFO_BOOK_NAME_FIELD_NAME);
		}

		JSONArray placesJson = jsonObject.getJSONArray(PLACES_FIELD_NAME);
		int placesCount = placesJson.length();
		logger.info("places count: {}", placesCount);
		int[] placesIndices = new int[placesCount]; // по порядку мест - индексы в массиве "players"

		for (int i = 0; i < placesCount; i++) {
			int playerIndex = placesJson.getInt(i);
			if (playerIndex < 0) {
				String errorMessage = String.format("places contains negative playerIndex: %d.", playerIndex);
				throw new IllegalStateException(errorMessage);
			}

			placesIndices[i] = playerIndex;
		}
		logger.info("players places indices: {}", Arrays.toString(placesIndices));

		JSONArray players = jsonObject.getJSONArray(PLAYERS_FIELD_NAME);
		int playersCount = players.length();
		logger.info("players count: {}", playersCount);

		List<PlayerRoundResult> results = new ArrayList<>();

		for (int playerIndex = 0; playerIndex < playersCount; playerIndex++) {
			JSONObject playerJson = players.getJSONObject(playerIndex);
			Player player = new Player();
			PlayerRoundResult result = new PlayerRoundResult();

			boolean finished;
			long finishedTimeMilliseconds = -1; // finished time, in milliseconds
			Date finishedTime = null;

			boolean isNotGuest = JsonUtils.hasField(playerJson, PLAYER_USER_FIELD_NAME);
			if (isNotGuest) {
				JSONObject userJson = playerJson.getJSONObject(PLAYER_USER_FIELD_NAME);

				int profileId = userJson.getInt(PLAYER_USER_PROFILE_ID_FIELD_NAME);
				String login = userJson.getString(PLAYER_USER_LOGIN_FIELD_NAME);

				int level = userJson.getInt(PLAYER_USER_LEVEL_FIELD_NAME);
				Rank rank = Rank.getRank(level);

				player.setProfileId(profileId);
				player.setName(login);
				player.setRank(rank);

				logger.info("Player number {} is {} (profileId = {}).", playerIndex, login, profileId);
			}
			else {
				logger.info("Player number {} is a guest.", playerIndex);
			}

			// "players[i].finishedTime"
			if (playerJson.has(PLAYER_FINISHED_TIME_FIELD_NAME)) {
				Object finishedTimeObject = playerJson.get(PLAYER_FINISHED_TIME_FIELD_NAME);
				if (finishedTimeObject.toString().equals(Boolean.FALSE.toString())) { // "finishedTime: false -> not finished
					finished = false;
				}
				else { // finished is long -> ok
					finished = true;
					finishedTimeMilliseconds = playerJson.getLong(PLAYER_FINISHED_TIME_FIELD_NAME);
					finishedTime = new Date(finishedTimeMilliseconds);
				}
			}
			else { // no "finishedTime" -> not finished
				finished = false;
			}

			// other fields from "players[i]"
			if (finished) { // игрок проехал заезд
				int speed = playerJson.getInt(PLAYER_SPEED_FIELD_NAME);
				int errorsCount = playerJson.getInt(PLAYER_ERRORS_COUNT_FIELD_NAME);
				double errorsPercentage = playerJson.getDouble(PLAYER_ERRORS_PERCENTAGE_FIELD_NAME);

				result.setFinishedTimeMilliseconds(finishedTimeMilliseconds);
				result.setFinishedTime(finishedTime);
				result.setSpeed(speed);
				result.setErrorsCount(errorsCount);
				result.setErrorPercentage(errorsPercentage);

				if (isNotGuest) { // player is not guest
					result.setPlayer(player);
				}
				else { // player is guest
					result.setPlayer(player); // save guest as player
				}

				// todo: think about adding not guests
				results.add(result);
			}
			else { // игрок не проехал заезд
				// do not add result to list // todo: think about actions in this case
				logger.info("Player number {} has not finished.", playerIndex);
			}

			// search player index in "places" array
			for (int j = 0; j < placesIndices.length; j++) {
				if (placesIndices[j] == playerIndex) { // индекс игрока найден в массиве "places"
					int place = j + PlayerRoundResult.FIRST_PLACE; // места начинаются с 1
					if (!finished) {
						String errorMessage = String.format(
							"Player number %d has not finished, but he is present in \"players\" array (players[%s] = %d)",
							playerIndex,
							j,
							placesIndices[j]
						);

						throw new IllegalStateException(errorMessage);
					}

					result.setPlace(place);
				}
			}
		}

		Dictionary dictionary = new Dictionary();
		dictionary.setCode(dictionaryCode);
		// todo: fill dictionary name using ajax api

		Round round = new Round();
		round.setGameId(gameId);
		round.setBeginTimeMilliseconds(beginTimeMilliseconds);
		round.setBeginTime(beginTime);
		round.setText(text);
		round.setBookName(bookName);
		round.setBookAuthor(bookAuthor);

		round.setDictionary(dictionary);
		round.setResults(results);

		logFilledRoundInfo(round);

		return round;
	}

	/**
	 * @param json JSON-данные, сохраненные версией 1.7 скрипта <a href="http://klavogonki.ru/u/#/364239/">voidmain</a>
	 * @return модель заезда на основе json-данных заезда
	 */
	public static Round parseRound17(String json) {
		JSONObject jsonObject = new JSONObject(json);

		int gameId = jsonObject.getInt(GAME_ID_FIELD_NAME);

		long beginTimeMilliseconds = jsonObject.getLong(GAME_BEGIN_TIME_FIELD_NAME);
		Date beginTime = new Date(beginTimeMilliseconds);

		// "gameInfo"
		JSONObject gameInfo = jsonObject.getJSONObject(GAME_INFO_FIELD_NAME);
		String dictionaryCode = gameInfo.getString(GAME_INFO_TYPE_FIELD_NAME);

		// "textInfo"
		String text = null;
		String bookAuthor = null;
		String bookName = null;
		Integer textLength = null;
		if (JsonUtils.hasField(jsonObject, TEXT_INFO_FIELD_NAME)) {
			JSONObject textInfo = jsonObject.getJSONObject(TEXT_INFO_FIELD_NAME);

			if (!textInfo.isNull(TEXT_INFO_TEXT_FIELD_NAME))
				text = textInfo.getString(TEXT_INFO_TEXT_FIELD_NAME);

			if (!textInfo.isNull(TEXT_INFO_LENGTH_FIELD_NAME))
				textLength = textInfo.getInt(TEXT_INFO_LENGTH_FIELD_NAME);

			if (!textInfo.isNull(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME))
				bookAuthor = textInfo.getString(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME);

			if (!textInfo.isNull(TEXT_INFO_BOOK_NAME_FIELD_NAME))
				bookName = textInfo.getString(TEXT_INFO_BOOK_NAME_FIELD_NAME);
		}

		// "places"
		JSONArray placesJson = jsonObject.getJSONArray(PLACES_FIELD_NAME);
		int placesCount = placesJson.length();
		logger.info("places count: {}", placesCount);
		int[] placesIndices = new int[placesCount]; // по порядку мест - индексы в массиве "players"

		for (int i = 0; i < placesCount; i++) {
			int playerIndex = placesJson.getInt(i);
			if (playerIndex < 0) {
				String errorMessage = String.format("places contains negative playerIndex: %d.", playerIndex);
				throw new IllegalStateException(errorMessage);
			}

			placesIndices[i] = playerIndex;
		}
		logger.info("players places indices: {}", Arrays.toString(placesIndices));

		// "players"
		JSONArray players = jsonObject.getJSONArray(PLAYERS_FIELD_NAME);
		int playersCount = players.length();
		logger.info("players count: {}", playersCount);

		List<PlayerRoundResult> results = new ArrayList<>();

		for (int playerIndex = 0; playerIndex < playersCount; playerIndex++) {
			JSONObject playerJson = players.getJSONObject(playerIndex);
			Player player = new Player();
			PlayerRoundResult result = new PlayerRoundResult();

			boolean finished;

			// "players[i].user"
			boolean isNotGuest = JsonUtils.hasField(playerJson, PLAYER_USER_FIELD_NAME);
			if (isNotGuest) {
				JSONObject userJson = playerJson.getJSONObject(PLAYER_USER_FIELD_NAME);

				int profileId = userJson.getInt(PLAYER_USER_PROFILE_ID_FIELD_NAME);
				String login = userJson.getString(PLAYER_USER_LOGIN_FIELD_NAME);

				int level = userJson.getInt(PLAYER_USER_LEVEL_FIELD_NAME);
				Rank rank = Rank.getRank(level);

				player.setProfileId(profileId);
				player.setName(login);
				player.setRank(rank);

				logger.info("Player number {} is {} (profileId = {}).", playerIndex, login, profileId);
			}
			else {
				logger.info("Player number {} is a guest.", playerIndex);
			}

			// "players[i].result"
			if (JsonUtils.hasField(playerJson, PLAYER_RESULT_FIELD_NAME)) { // "result" is present in "players[i]" and "result" is not null
				finished = true;

				JSONObject resultJson = playerJson.getJSONObject(PLAYER_RESULT_FIELD_NAME);
				long finishedTimeMilliseconds = resultJson.getLong(PLAYER_RESULT_FINISHED_TIME_FIELD_NAME);
				Date finishedTime = new Date(finishedTimeMilliseconds);

				int speed = resultJson.getInt(PLAYER_RESULT_SPEED_FIELD_NAME);
				int charsTotal = resultJson.getInt(PLAYER_RESULT_CHARS_TOTAL_FIELD_NAME);
				int errorsCount = resultJson.getInt(PLAYER_RESULT_ERRORS_COUNT_FIELD_NAME);
				double errorsPercentage = resultJson.getDouble(PLAYER_RESULT_ERRORS_PERCENTAGE_FIELD_NAME);

				result.setFinishedTimeMilliseconds(finishedTimeMilliseconds);
				result.setFinishedTime(finishedTime);
				result.setSpeed(speed);
				result.setCharsTotal(charsTotal);
				result.setErrorsCount(errorsCount);
				result.setErrorPercentage(errorsPercentage);

				if (isNotGuest) { // player is not guest
					result.setPlayer(player);
				}
				else { // player is guest
					result.setPlayer(player); // save guest as player
				}

				// add independently of whether player is guest or not guest
				results.add(result);
			}
			else { // no "result" or "result" is null -> not finished
				finished = false;
				logger.info("Player number {} has not finished. Do not add him to Round results.", playerIndex);
			}

			// search player index in "places" array
			for (int j = 0; j < placesIndices.length; j++) {
				if (placesIndices[j] == playerIndex) { // индекс игрока найден в массиве "places"
					int place = j + PlayerRoundResult.FIRST_PLACE; // места начинаются с 1
					if (!finished) {
						String errorMessage = String.format(
							"Player number %d has not finished, but he is present in \"players\" array (players[%d] = %d)",
							playerIndex,
							j,
							placesIndices[j]
						);

						throw new IllegalStateException(errorMessage);
					}

					result.setPlace(place);
				}
			}
		}

		Dictionary dictionary = new Dictionary();
		dictionary.setCode(dictionaryCode);
		// todo: fill dictionary name using ajax api

		Round round = new Round();
		round.setGameId(gameId);
		round.setBeginTimeMilliseconds(beginTimeMilliseconds);
		round.setBeginTime(beginTime);
		round.setText(text);
		round.setBookName(bookName);
		round.setBookAuthor(bookAuthor);

		round.setDictionary(dictionary);
		round.setResults(results);

		logFilledRoundInfo(round);

		return round;
	}

	/**
	 * @param json JSON-данные, сохраненные версией 1.8 скрипта <a href="http://klavogonki.ru/u/#/364239/">voidmain</a>
	 * @return модель заезда на основе json-данных заезда
	 */
	public static Round parseRound18(String json) {
		// todo: implement vocs data parsing
		return parseRound17(json);
	}

	private static void logFilledRoundInfo(Round round) {
		logger.info("===========================================");
		logger.info("Round info:");
		logger.info("Begin time: {}.", DateUtils.getDayMonthYearHourMinuteSecondFormat().format(round.getBeginTime()));

		Dictionary dictionary = round.getDictionary();
		logger.info("Dictionary: {}. Code: {}.", dictionary.getName(), dictionary.getCode());

		logger.info("Text length: {}", round.getTextLength());
		logger.info("Text: {}", round.getText());
		logger.info("Book author: {}", round.getBookAuthor());
		logger.info("Book name: {}", round.getBookName());
		logger.info("Min place: {}", round.getMinPlace());
		logger.info("Max place: {}", round.getMaxPlace());

		List<PlayerRoundResult> results = round.getResultsSortedByPlace();
		for (PlayerRoundResult result : results) {
			Player player = result.getPlayer();
			Integer place = result.getPlace();

			if (player.isGuest()) {
				logger.info(
					"Place {}: guest. Speed = {}. Errors count = {}. Error percentage = {}.",
					place,
					result.getSpeed(),
					result.getErrorsCount(),
					result.getErrorPercentage()
				);
			}
			else {
				logger.info("Place {}: {} (profileId = {}). Speed = {}. Errors count = {}. Error percentage = {}.",
					place,
					player.getName(),
					player.getProfileId(),
					result.getSpeed(),
					result.getErrorsCount(),
					result.getErrorPercentage()
				);
			}
		}
	}

	// "exportScriptVersion" - version of Voidmain script
	public static final String EXPORT_SCRIPT_VERSION_FIELD_NAME = "exportScriptVersion";

	// "id"
	public static final String GAME_ID_FIELD_NAME = "id";

	// "beginTime"
	public static final String GAME_BEGIN_TIME_FIELD_NAME = "beginTime";

	// "gameInfo"
	public static final String GAME_INFO_FIELD_NAME = "gameInfo";
	public static final String GAME_INFO_TYPE_FIELD_NAME = "type";

	// "places"
	public static final String PLACES_FIELD_NAME = "places";

	// "players"
	public static final String PLAYERS_FIELD_NAME = "players";

	// "players[i].user"
	public static final String PLAYER_USER_FIELD_NAME = "user";
	public static final String PLAYER_USER_PROFILE_ID_FIELD_NAME = "id";
	public static final String PLAYER_USER_LOGIN_FIELD_NAME = "login";
	public static final String PLAYER_USER_LEVEL_FIELD_NAME = "level";

	// "players[i].result" (from 1.7)
	public static final String PLAYER_RESULT_FIELD_NAME = "result"; // from 1.7
	public static final String PLAYER_RESULT_FINISHED_TIME_FIELD_NAME = "finishedTime"; // from 1.7
	public static final String PLAYER_RESULT_SPEED_FIELD_NAME = "speed"; // from 1.7
	public static final String PLAYER_RESULT_CHARS_TOTAL_FIELD_NAME = "charsTotal"; // from 1.7
	public static final String PLAYER_RESULT_ERRORS_COUNT_FIELD_NAME = "errorsCount"; // from 1.7
	public static final String PLAYER_RESULT_ERRORS_PERCENTAGE_FIELD_NAME = "errorsPercent"; // from 1.7

	// "players[i]" old fields (they were moved to "players[i].result" in 1.7)
	public static final String PLAYER_FINISHED_TIME_FIELD_NAME = "finishedTime"; // before 1.7
	public static final String PLAYER_SPEED_FIELD_NAME = "speed"; // before 1.7
	public static final String PLAYER_ERRORS_COUNT_FIELD_NAME = "errorsCount"; // before 1.7
	public static final String PLAYER_ERRORS_PERCENTAGE_FIELD_NAME = "errorsPercent"; // before 1.7


	// "textInfo" (was "textinfo" prior to 1.7)
	public static final String TEXT_INFO_FIELD_NAME_1_5 = "textinfo"; // before 1.7
	public static final String TEXT_INFO_FIELD_NAME = "textInfo"; // from 1.7
	public static final String TEXT_INFO_TEXT_FIELD_NAME = "text";
	public static final String TEXT_INFO_LENGTH_FIELD_NAME = "length";
	public static final String TEXT_INFO_BOOK_AUTHOR_FIELD_NAME = "author";
	public static final String TEXT_INFO_BOOK_NAME_FIELD_NAME = "name";

	public static final String VERSION_1_5 = "1.5";
	public static final String VERSION_1_7 = "1.7";
	public static final String VERSION_1_8 = "1.8";

	private static final Logger logger = LogManager.getLogger(VoidmainJsonParser.class);
}
