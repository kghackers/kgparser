package ru.klavogonki.kgparser.dataparser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.klavogonki.kgparser.*;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.processing.AverageSpeedCounter;
import ru.klavogonki.kgparser.processing.HighChartValue;
import ru.klavogonki.kgparser.processing.SpeedChartFiller;
import su.opencode.kefir.util.DateUtils;
import su.opencode.kefir.util.FileUtils;
import su.opencode.kefir.util.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class VoidmainJsonParser
{
	public static void main(String[] args) throws UnsupportedEncodingException {
		BasicConfigurator.configure();

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

		String competitionName = "Марафоны 11.02.2014";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\marathons_2014_02_11\\";
		Competition competition = parseCompetition(competitionName, dirPath, VERSION_1_7);

/*
		String competitionName = "Серпантин №8 (11.02.2014)";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\serpantin_08__2014_02_11\\";
		Competition competition = parseCompetition(competitionName, dirPath, VERSION_1_5);
*/


		AverageSpeedCounter.logCompetitionInfo(competition);

		HighChartValue highChartValue = SpeedChartFiller.fillData(competition);
		System.out.println("highchartValue: ");
		System.out.println(highChartValue.toJson());
	}

	private static Round parseRoundFromFile(String filePath, String scriptVersion) throws UnsupportedEncodingException {
		byte[] jsonBytes = FileUtils.readFile(filePath);
		String json = new String(jsonBytes, StringUtils.CHARSET_UTF8);

		if ( StringUtils.empty(scriptVersion) || scriptVersion.equals(VERSION_1_5) )
			return parseRound15(json);

		if ( scriptVersion.equals(VERSION_1_7) )
			return parseRound17(json);

		throw new IllegalArgumentException( concat("Unsupported voidmain script version: ", scriptVersion) );
	}

	/**
	 * Парсит соревнование на основе всех файлов, сохраненных в указанной директории.
	 * Поддиректории не учитываются.
	 * @param competitionName название соревнования
	 * @param dirPath путь к директории, в которой хранятся json-файлы с результатами заездов соревнования
	 * @param scriptVersion версия скрипта voidmain
	 * @return распарсенная модель соревнования
	 */
	public static Competition parseCompetition(String competitionName, String dirPath, String scriptVersion) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();

		File dir = new File(dirPath);
		if ( !dir.isDirectory() )
			throw new IllegalArgumentException( concat(sb, "\"", dirPath, "\" is not a directory.") );

		List<Round> rounds = new ArrayList<>();

		File[] files = dir.listFiles();
		if ( (files == null) || (files.length == 0) )
			throw new IllegalArgumentException( concat(sb, "Directory \"", dirPath, "\" does not contain any files.") );

		for (File file : files)
		{
			if ( file.isDirectory() ) // skip inner directories
				continue;

			String filePath = file.getAbsolutePath();
			logger.info("");
			logger.info("========================================================================");
			logger.info( concat(sb, "Parsing Round from file \"", filePath, "\"") );
			Round round = parseRoundFromFile(filePath, scriptVersion);
			rounds.add(round);
			logger.info( concat(sb, "Round successfully parsed from file \"", filePath, "\".") );
		}

		logger.info("========================================================================");
		logger.info( concat(sb, "Total rounds parsed: ", rounds.size()) );

		Competition competition = new Competition();
		competition.setName(competitionName);

		competition.setRounds(rounds);
		competition.orderRoundsByBeginTime();

		competition.fillDictionariesNames();

		logFilledCompetitionInfo(competition);
		return competition;
	}
	private static void logFilledCompetitionInfo(Competition competition) {
		StringBuilder sb = new StringBuilder();
		logger.info("===========================================");
		logger.info("Competition info:");
		logger.info( concat(sb, "Name: ", competition.getName()) );

		Set<Dictionary> dictionaries = competition.getDictionaries();
		logger.info("");
		logger.info( concat(sb, "Total dictionaries: ", dictionaries.size()) );

		for (Dictionary dictionary : dictionaries)
		{
			logger.info( concat(sb
				, "Dictionary: "
				, dictionary.getName(), "."
				, " Code: ", dictionary.getCode(), "."
			) );
		}

		logger.info("===========================================");
		Map<String, List<Round>> roundsByDictionaries = competition.getRoundsByDictionariesMap();
		for (String dictionaryCode : roundsByDictionaries.keySet())
		{
			List<Round> dictionaryRounds = roundsByDictionaries.get(dictionaryCode);
			Dictionary dictionary = dictionaryRounds.get(0).getDictionary();

			logger.info( concat(sb
				, "Dictionary: "
				, dictionary.getName(), "."
				, " Code: ", dictionary.getCode(), "."
				, " Rounds by dictionary count: ", dictionaryRounds.size(), "."
			) );
		}

		logger.info("===========================================");
		SortedSet<Rank> ranks = competition.getRanks();
		logger.info( concat(sb, "Present player dictionary ranks: ", ranks.toString()) );

		Set<Player> players = competition.getPlayers();
		logger.info("");
		logger.info( concat(sb, "Total players (not including guests): ", players.size()) );

		// todo: may be sort players by rounds count desc
		for (Player player : players)
		{
			logger.info("===========================================");
			logger.info( concat(sb, "Player ", player.getName(), " (profileId = ", player.getProfileId(), ")") );

			int playerTotalRoundsCount = competition.getRoundsCount(player);
			logger.info( concat(sb, "Player total rounds count: ", playerTotalRoundsCount) );

			for (Dictionary dictionary : dictionaries)
			{ // players result for each dictionary
				int playerDictionaryRoundsCount = competition.getRoundsCount(player, dictionary);
				logger.info( concat(sb
					, "Dictionary: ", dictionary.getName(), " ( code = ", dictionary.getCode(), " )."
					, " Player rounds count: ", playerDictionaryRoundsCount
				) );
			}
		}

		List<Round> rounds = competition.getRounds();
		logger.info("");
		logger.info( concat(sb, "Total rounds: ", rounds.size()) );

		for (Round round : rounds)
		{
			Dictionary roundDictionary = round.getDictionary();

			logger.info( concat(sb
				, "Round number: ", round.getNumber(), "."
				, " Dictionary: ", roundDictionary.getName(), " (code = ", roundDictionary.getCode(), ")."
				, " Number in dictionary: ", round.getNumberInDictionary(), "."
			) );
		}
	}

	/**
	 * @param json JSON-данные, сохраненные версией 1.5 скрипта <a href="http://klavogonki.ru/u/#/364239/">voidmain</a>
	 * @return модель заезда на основе json-данных заезда
	 */
	public static Round parseRound15(String json) {
		StringBuilder sb = new StringBuilder();
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
		if ( JSONObjectUtils.hasField(jsonObject, TEXT_INFO_FIELD_NAME_1_5) )
		{
			JSONObject textInfo = jsonObject.getJSONObject(TEXT_INFO_FIELD_NAME_1_5);

			if ( !textInfo.isNull(TEXT_INFO_TEXT_FIELD_NAME) )
				text = textInfo.getString(TEXT_INFO_TEXT_FIELD_NAME);

//			if ( !textInfo.isNull(TEXT_INFO_LENGTH_FIELD_NAME) )
//				textLength = textInfo.getInt(TEXT_INFO_LENGTH_FIELD_NAME);

			if ( !textInfo.isNull(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME) )
				bookAuthor = textInfo.getString(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME);

			if ( !textInfo.isNull(TEXT_INFO_BOOK_NAME_FIELD_NAME) )
				bookName = textInfo.getString(TEXT_INFO_BOOK_NAME_FIELD_NAME);
		}

		JSONArray placesJson = jsonObject.getJSONArray(PLACES_FIELD_NAME);
		int placesCount = placesJson.length();
		logger.info( concat(sb, "places count: ", placesCount) );
		int[] placesIndices = new int[placesCount]; // по порядку мест - индексы в массиве "players"

		for (int i = 0; i < placesCount; i++)
		{
			int playerIndex = placesJson.getInt(i);
			if (playerIndex < 0)
				throw new IllegalStateException( concat("places contains negative playerIndex: ", playerIndex) );

			placesIndices[i] = playerIndex;
		}
		logger.info( concat(sb, "players places indices: ", Arrays.toString(placesIndices)) );

		JSONArray players = jsonObject.getJSONArray(PLAYERS_FIELD_NAME);
		int playersCount = players.length();
		logger.info( concat(sb, "players count: ", playersCount) );

		List<PlayerRoundResult> results = new ArrayList<>();

		for (int playerIndex = 0; playerIndex < playersCount; playerIndex++)
		{
			JSONObject playerJson = players.getJSONObject(playerIndex);
			Player player = new Player();
			PlayerRoundResult result = new PlayerRoundResult();

			boolean finished;
			long finishedTimeMilliseconds = -1; // finished time, in milliseconds
			Date finishedTime = null;

			boolean isNotGuest = JSONObjectUtils.hasField(playerJson, PLAYER_USER_FIELD_NAME);
			if (isNotGuest)
			{
				JSONObject userJson = playerJson.getJSONObject(PLAYER_USER_FIELD_NAME);

				int profileId = userJson.getInt(PLAYER_USER_PROFILE_ID_FIELD_NAME);
				String login = userJson.getString(PLAYER_USER_LOGIN_FIELD_NAME);

				int level = userJson.getInt(PLAYER_USER_LEVEL_FIELD_NAME);
				Rank rank = Rank.getRank(level);

				player.setProfileId(profileId);
				player.setName(login);
				player.setRank(rank);

				logger.info( concat(sb, "Player number ", playerIndex, " is ", login, " (profileId = ", profileId, ")."));
			}
			else
			{
				logger.info( concat(sb, "Player number ", playerIndex, " is a guest."));
			}

			// "players[i].finishedTime"
			if ( playerJson.has(PLAYER_FINISHED_TIME_FIELD_NAME) )
			{
				Object finishedTimeObject = playerJson.get(PLAYER_FINISHED_TIME_FIELD_NAME);
				if ( finishedTimeObject.toString().equals(Boolean.FALSE.toString()) )
				{ // "finishedTime: false -> not finished
					finished = false;
				}
				else
				{ // finished is long -> ok
					finished = true;
					finishedTimeMilliseconds = playerJson.getLong(PLAYER_FINISHED_TIME_FIELD_NAME);
					finishedTime = new Date(finishedTimeMilliseconds);
				}
			}
			else
			{ // no "finishedTime" -> not finished
				finished = false;
			}

			// other fields from "players[i]"
			if (finished)
			{ // игрок проехал заезд
				int speed = playerJson.getInt(PLAYER_SPEED_FIELD_NAME);
				int errorsCount = playerJson.getInt(PLAYER_ERRORS_COUNT_FIELD_NAME);
				double errorsPercentage = playerJson.getDouble(PLAYER_ERRORS_PERCENTAGE_FIELD_NAME);

				result.setFinishedTimeMilliseconds(finishedTimeMilliseconds);
				result.setFinishedTime(finishedTime);
				result.setSpeed(speed);
				result.setErrorsCount(errorsCount);
				result.setErrorPercentage(errorsPercentage);

				if (isNotGuest)
				{ // player is not guest
					result.setPlayer(player);
				}
				else
				{ // player is guest
					result.setPlayer(player); // save guest as player
				}

				// todo: think about adding not guests
				results.add(result);
			}
			else
			{ // игрок не проехал заезд
				// do not add result to list // todo: think about actions in this case
				logger.info( concat(sb, "Player number ", playerIndex, " has not finished.") );
			}

			// search player index in "places" array
			for (int j = 0; j < placesIndices.length; j++)
			{
				if (placesIndices[j] == playerIndex)
				{ // индекс игрока найден в массиве "places"
					int place = j + PlayerRoundResult.FIRST_PLACE; // места начинаются с 1
					if ( !finished )
						throw new IllegalStateException( concat(sb, "Player number ", playerIndex, " has not finished, but he is present in \"players\" array (players[", j, "] = ", placesIndices[j], ")") );

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
		StringBuilder sb = new StringBuilder();
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
		if ( JSONObjectUtils.hasField(jsonObject, TEXT_INFO_FIELD_NAME) )
		{
			JSONObject textInfo = jsonObject.getJSONObject(TEXT_INFO_FIELD_NAME);

			if ( !textInfo.isNull(TEXT_INFO_TEXT_FIELD_NAME) )
				text = textInfo.getString(TEXT_INFO_TEXT_FIELD_NAME);

			if ( !textInfo.isNull(TEXT_INFO_LENGTH_FIELD_NAME) )
				textLength = textInfo.getInt(TEXT_INFO_LENGTH_FIELD_NAME);

			if ( !textInfo.isNull(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME) )
				bookAuthor = textInfo.getString(TEXT_INFO_BOOK_AUTHOR_FIELD_NAME);

			if ( !textInfo.isNull(TEXT_INFO_BOOK_NAME_FIELD_NAME) )
				bookName = textInfo.getString(TEXT_INFO_BOOK_NAME_FIELD_NAME);
		}

		// "places"
		JSONArray placesJson = jsonObject.getJSONArray(PLACES_FIELD_NAME);
		int placesCount = placesJson.length();
		logger.info( concat(sb, "places count: ", placesCount) );
		int[] placesIndices = new int[placesCount]; // по порядку мест - индексы в массиве "players"

		for (int i = 0; i < placesCount; i++)
		{
			int playerIndex = placesJson.getInt(i);
			if (playerIndex < 0)
				throw new IllegalStateException( concat("places contains negative playerIndex: ", playerIndex) );

			placesIndices[i] = playerIndex;
		}
		logger.info( concat(sb, "players places indices: ", Arrays.toString(placesIndices)) );

		// "players"
		JSONArray players = jsonObject.getJSONArray(PLAYERS_FIELD_NAME);
		int playersCount = players.length();
		logger.info( concat(sb, "players count: ", playersCount) );

		List<PlayerRoundResult> results = new ArrayList<>();

		for (int playerIndex = 0; playerIndex < playersCount; playerIndex++)
		{
			JSONObject playerJson = players.getJSONObject(playerIndex);
			Player player = new Player();
			PlayerRoundResult result = new PlayerRoundResult();

			boolean finished;

			// "players[i].user"
			boolean isNotGuest = JSONObjectUtils.hasField(playerJson, PLAYER_USER_FIELD_NAME);
			if (isNotGuest)
			{
				JSONObject userJson = playerJson.getJSONObject(PLAYER_USER_FIELD_NAME);

				int profileId = userJson.getInt(PLAYER_USER_PROFILE_ID_FIELD_NAME);
				String login = userJson.getString(PLAYER_USER_LOGIN_FIELD_NAME);

				int level = userJson.getInt(PLAYER_USER_LEVEL_FIELD_NAME);
				Rank rank = Rank.getRank(level);

				player.setProfileId(profileId);
				player.setName(login);
				player.setRank(rank);

				logger.info( concat(sb, "Player number ", playerIndex, " is ", login, " (profileId = ", profileId, ")."));
			}
			else
			{
				logger.info( concat(sb, "Player number ", playerIndex, " is a guest."));
			}

			// "players[i].result"
			if ( JSONObjectUtils.hasField(playerJson, PLAYER_RESULT_FIELD_NAME) )
			{ // "result" is present in "players[i]" and "result" is not null
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

				if (isNotGuest)
				{ // player is not guest
					result.setPlayer(player);
				}
				else
				{ // player is guest
					result.setPlayer(player); // save guest as player
				}

				// add independently of whether player is guest or not guest
				results.add(result);
			}
			else
			{ // no "result" or "result" is null -> not finished
				finished = false;
				logger.info( concat(sb, "Player number ", playerIndex, " has not finished. Do not add him to Round results.") );
			}

			// search player index in "places" array
			for (int j = 0; j < placesIndices.length; j++)
			{
				if (placesIndices[j] == playerIndex)
				{ // индекс игрока найден в массиве "places"
					int place = j + PlayerRoundResult.FIRST_PLACE; // места начинаются с 1
					if ( !finished )
						throw new IllegalStateException( concat(sb, "Player number ", playerIndex, " has not finished, but he is present in \"players\" array (players[", j, "] = ", placesIndices[j], ")") );

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

	private static void logFilledRoundInfo(Round round) {
		StringBuilder sb = new StringBuilder();

		logger.info("===========================================");
		logger.info("Round info:");
		logger.info( concat(sb, "Begin time: ", DateUtils.getDayMonthYearHourMinuteSecondFormat().format(round.getBeginTime()) ) );

		Dictionary dictionary = round.getDictionary();
		logger.info( concat(sb
			, "Dictionary: "
			, dictionary.getName(), "."
			, " Code: ", dictionary.getCode(), "."
		) );

//		logger.info( concat(sb, "Text length: ", round.getTextLength()) );
		logger.info( concat(sb, "Text: ", round.getText()) );
		logger.info( concat(sb, "Book author: ", round.getBookAuthor()) );
		logger.info( concat(sb, "Book name: ", round.getBookName()) );
		logger.info( concat(sb, "Min place: ", round.getMinPlace()) );
		logger.info( concat(sb, "Max place: ", round.getMaxPlace()) );

		List<PlayerRoundResult> results = round.getResultsSortedByPlace();
		for (PlayerRoundResult result : results)
		{
			Player player = result.getPlayer();
			Integer place = result.getPlace();

			if (player.isGuest())
			{
				logger.info( concat(sb
					, "Place ", place, ": guest."
					, " Speed = ", result.getSpeed(), "."
					, " Errors count = ", result.getErrorsCount(), "."
					, " Error percentage = ", result.getErrorPercentage(), "."
				) );
			}
			else
			{
				logger.info( concat(sb
					, "Place ", place, ": ", player.getName() , " (profileId = ", player.getProfileId(), ")."
					, " Speed = ", result.getSpeed(), "."
					, " Errors count = ", result.getErrorsCount(), "."
					, " Error percentage = ", result.getErrorPercentage(), "."
				) );
			}
		}
	}

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

	private static final Logger logger = Logger.getLogger(VoidmainJsonParser.class);
}