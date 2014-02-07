package ru.klavogonki.kgparser.dataparser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.klavogonki.kgparser.*;
import su.opencode.kefir.util.FileUtils;
import su.opencode.kefir.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

		String filePath = "C:\\java\\kgparser\\doc\\voidmain\\game_37411.json";
		byte[] jsonBytes = FileUtils.readFile(filePath);
		String json = new String(jsonBytes, StringUtils.CHARSET_UTF8);
		parseRound(json);
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
		if ( jsonObject.has("text") )
			text = jsonObject.getString("text");

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

		for (int i = 0; i < playersCount; i++)
		{
			JSONObject playerJson = players.getJSONObject(i);
			Player player = new Player();

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

				logger.info( concat("Player number ", i, " is ", login, " (profileId = ", profileId, ")."));
			}
			else
			{
				logger.info( concat("Player number ", i, " is a guest."));
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
			{
				int speed = playerJson.getInt("speed");
				int errorsCount = playerJson.getInt("errorsCount");
				double errorsPercentage = playerJson.getDouble("errorsPercent");

				PlayerRoundResult result = new PlayerRoundResult();
				result.setFinishedTimeMilliseconds(finishedTimeMilliseconds);
				result.setFinishedTime(finishedTime);
				result.setSpeed(speed);
				result.setErrorsCount(errorsCount);
				result.setErrorPercentage(errorsPercentage);

				if (isNotGuest)
				{ /// todo: think about saving guests as players
					result.setPlayer(player);
				}

				// todo: think about adding not guests
				results.add(result);
			}
			else
			{
				// do not add result to list // todo: think about actions in this case
			}

		}

		Dictionary dictionary = new Dictionary();
		dictionary.setCode(dictionaryCode);

		Round round = new Round();
		round.setGameId(gameId);
		round.setBeginTimeMilliseconds(beginTimeMilliseconds);
		round.setBeginTime(beginTime);
		round.setText(text);

		round.setDictionary(dictionary);

		return round;
	}

	private static final Logger logger = Logger.getLogger(VoidmainJsonParser.class);
}