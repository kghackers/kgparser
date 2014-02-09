package ru.klavogonki.kgparser.dataparser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.klavogonki.kgparser.*;
import ru.klavogonki.kgparser.Dictionary;
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

/*
//		String filePath = "C:\\java\\kgparser\\doc\\voidmain\\game_37411.json";
//		String filePath = "C:\\java\\kgparser\\doc\\voidmain\\kuvet_55\\kuvet_01_game_97265.json";
		String filePath =  "C:\\java\\kgparser\\doc\\voidmain\\kuvet_55\\kuvet_12_game_48526.json";
		parseRoundFromFile(filePath);
*/

		String competitionName = "Кювет №55";
		String dirPath = "C:\\java\\kgparser\\doc\\voidmain\\kuvet_55\\";
		parseCompetition(competitionName, dirPath);
	}

	private static Round parseRoundFromFile(String filePath) throws UnsupportedEncodingException {
		byte[] jsonBytes = FileUtils.readFile(filePath);
		String json = new String(jsonBytes, StringUtils.CHARSET_UTF8);
		return parseRound(json);
	}

	/**
	 * Парсит соревнование на основе всех файлов, сохраненных в указанной директории.
	 * Поддиректории не учитываются.
	 * @param competitionName название соревнования
	 * @param dirPath путь к директории, в которой хранятся json-файлы с результатами заездов соревнования
	 * @return распарсенная модель соревнования
	 */
	public static Competition parseCompetition(String competitionName, String dirPath) throws UnsupportedEncodingException {
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
			Round round = parseRoundFromFile(filePath);
			rounds.add(round);
			logger.info( concat(sb, "Round successfully parsed from file \"", filePath, "\".") );
		}

		logger.info("========================================================================");
		logger.info( concat(sb, "Total rounds parsed: ", rounds.size()) );

		Competition competition = new Competition();
		competition.setName(competitionName);
		competition.setRounds(rounds);

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
	}

	public static Round parseRound(String json) {
		StringBuilder sb = new StringBuilder();
		JSONObject jsonObject = new JSONObject(json);

		int gameId = jsonObject.getInt("id");

		long beginTimeMilliseconds = jsonObject.getLong("beginTime");
		Date beginTime = new Date(beginTimeMilliseconds); // to milliseconds

		JSONObject gameInfo = jsonObject.getJSONObject("gameInfo");
		String dictionaryCode = gameInfo.getString("type");

		String text = null;
		String bookAuthor = null;
		String bookName = null;
		Integer textLength = null;
		if ( (jsonObject.has("textinfo")) && !(jsonObject.isNull("textinfo")) ) // todo: refactor this check to separate static method in kefir
		{
			JSONObject textInfo = jsonObject.getJSONObject("textinfo");

			if ( !textInfo.isNull("text") )
				text = textInfo.getString("text");

//			textLength = textInfo.getInt("length");

			if ( !textInfo.isNull("author") )
				bookAuthor = textInfo.getString("author");

			if ( !textInfo.isNull("name") )
				bookName = textInfo.getString("name");
		}

		JSONArray placesJson = jsonObject.getJSONArray("places");
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

		JSONArray players = jsonObject.getJSONArray("players");
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

			boolean isNotGuest = playerJson.has("user") && !playerJson.isNull("user");
			if (isNotGuest)
			{
				JSONObject user = playerJson.getJSONObject("user");

				int profileId = user.getInt("id");
				String login = user.getString("login");

				int level = user.getInt("level");
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


			if ( playerJson.has("finishedTime") )
			{
				Object finishedTimeObject = playerJson.get("finishedTime");
				if ( finishedTimeObject.toString().equals(Boolean.FALSE.toString()) )
				{ // "finishedTime: false -> not finished
					finished = false;
				}
				else
				{ // finished is long -> ok
					finished = true;
					finishedTimeMilliseconds = playerJson.getLong("finishedTime");
					finishedTime = new Date(finishedTimeMilliseconds);
				}
			}
			else
			{ // no "finishedTime" -> not finished
				finished = false;
			}

			if (finished)
			{ // игрок проехал заезд
				int speed = playerJson.getInt("speed");
				int errorsCount = playerJson.getInt("errorsCount");
				double errorsPercentage = playerJson.getDouble("errorsPercent");

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

	private static final Logger logger = Logger.getLogger(VoidmainJsonParser.class);
}