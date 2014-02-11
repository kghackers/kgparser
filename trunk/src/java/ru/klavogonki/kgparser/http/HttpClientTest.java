package ru.klavogonki.kgparser.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.StandardDictionary;
import su.opencode.kefir.util.ObjectUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class HttpClientTest
{
	public static void main(String[] args) throws IOException {
//		int playerId = 368518; // франтера (novice)
//		int playerId = 141566; // любитель (amateur)
//		int playerId = 372072; // vanja (cabman)
//		int playerId = 226863; // Октатлон (pro)
//		int playerId = 233067; // E_l_e_n_a (racer)
//		int playerId = 139052; // alanen (maniac)
		int playerId = NOSFERATUM_PROFILE_ID; // nosferatum (superman)
//		int playerId = 79473; // _Jack_ (cyberracer>
//		int playerId = 1210; // ToNick (extracyber>

		String url;
		String body;

		System.out.println();
		url = UrlConstructor.getGetPlayerSummaryUrl(playerId);
		System.out.println("url: " + url);
		body = getResponseBody(url);
		System.out.println("player summary body:\n" + body);
		parsePlayerSummaryBody(body);


		System.out.println();
//		String dictionaryCode = StandardDictionary.normal.toString(); // обычный
		String dictionaryCode = StandardDictionary.chars.toString(); // буквы
//		String dictionaryCode = Dictionary.getDictionaryCode(192); // частотный
		url = UrlConstructor.getGetPlayerStatsDetailsUrl(playerId, dictionaryCode);
		System.out.println("url: " + url);
		body = getResponseBody(url);
		System.out.println("player stats body:\n" + body);
	}

	public static Rank getUserRank(int profileId) {
		try
		{
			String url = UrlConstructor.getGetPlayerSummaryUrl(profileId);
			String body = getResponseBody(url);

			JSONObject jsonObject = new JSONObject(body);
			int level = jsonObject.getInt("level");
			return Rank.getRank(level);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Map<String, String> getDictionariesNames(List<String> dictionariesCodes) {
		if ( ObjectUtils.empty(dictionariesCodes) )
			return Collections.emptyMap();

		StringBuilder sb = new StringBuilder();

		try
		{
			String url = UrlConstructor.getGetPlayerStatsOverviewUrl(NOSFERATUM_PROFILE_ID);
			String body = getResponseBody(url);

			JSONObject jsonObject = new JSONObject(body);
			if ( // todo: refactor these checks to separate kefir method
				   ( !jsonObject.has("ok") )
				|| ( jsonObject.isNull("ok") )
				|| ( jsonObject.getInt("ok") != 1)
			)
			{ // check "ok" field
				throw new RuntimeException( concat(sb, "Request to url \"", url, "\" failed (is not ok).") );
			}

			JSONObject gameTypes = jsonObject.getJSONObject("gametypes");

			Map<String, String> result = new HashMap<>();
			for (String dictionaryCode : dictionariesCodes)
			{
				if ( // todo: refactor these checks to separate kefir method
					   !gameTypes.has(dictionaryCode)
					|| gameTypes.isNull(dictionaryCode)
				)
				{
					throw new IllegalStateException( concat(sb, "Cannot get dictionary info for dictionary code = \"", dictionaryCode, "\",") );
				}

				JSONObject dictionaryInfo = gameTypes.getJSONObject(dictionaryCode);
				String dictionaryName = dictionaryInfo.getString("name");
				logger.info( concat(sb, "Dictionary code: \"", dictionaryCode, "\". Dictionary name = \"", dictionaryName, "\".") );
				// todo: log other fields from dictionaryInfo too

				result.put(dictionaryCode, dictionaryName);
			}

			return result;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static String getResponseBody(String url) throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		ResponseHandler<String> handler = new BasicResponseHandler();

		HttpResponse response = client.execute(httpGet);
		return handler.handleResponse(response);
	}

	private static void parsePlayerSummaryBody(String body) {
		JSONObject jsonObject = new JSONObject(body);
		System.out.println("json key set: " + jsonObject.keySet());

		JSONObject user = jsonObject.getJSONObject("user");
		int profileId = user.getInt("id");
		String login = user.getString("login");

		int level = jsonObject.getInt("level");
		Rank rank = Rank.getRank(level);
		String rankTitle = jsonObject.getString("title");

		System.out.println("id: " + profileId);
		System.out.println("login: " + login);
		System.out.println("level: " + level);
		System.out.println("rank: " + rank);
		System.out.println("rankTitle (from json): " + rankTitle);

		JSONObject car = jsonObject.getJSONObject("car");
		int carId = car.getInt("car");

		System.out.println("carId: " + carId);

		// todo: form player object from parsed data
	}

	public static final int NOSFERATUM_PROFILE_ID = 242585;

	private static final Logger logger = Logger.getLogger(HttpClientTest.class);
}