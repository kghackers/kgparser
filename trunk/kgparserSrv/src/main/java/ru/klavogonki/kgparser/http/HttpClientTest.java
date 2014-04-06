package ru.klavogonki.kgparser.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.StandardDictionary;
import su.opencode.kefir.util.JsonUtils;
import su.opencode.kefir.util.ObjectUtils;
import su.opencode.kefir.util.StringUtils;

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
//		int playerId = NOSFERATUM_PROFILE_ID; // nosferatum (superman)
//		int playerId = 79473; // _Jack_ (cyberracer>
//		int playerId = 1210; // ToNick (extracyber>


//		int playerId = 231371; // Phemmer
//		int playerId = 123036; // kryto

		BasicConfigurator.configure();

		int playerId = 204574; // eksdak
		getUserNormalRecord(playerId);
		if (true)
			return;

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

	public static Integer getUserNormalRecord(int profileId) {
		try
		{
			String url = UrlConstructor.getGetPlayerIndexData(profileId);
//			String body = getResponseBody(url);
			String body = getResponseBodyEnsureJson(url);

			JSONObject jsonObject = new JSONObject(body);
			if ( !JsonUtils.hasField(jsonObject, OK_FIELD_NAME) )
			{
				throw new IllegalStateException( concat("Cannot get normal record for profileId = ", profileId, ". Request to url \"", url, "\" failed (\"", OK_FIELD_NAME, "\" is not present in response or is null).") );
			}

			int okValue = jsonObject.getInt(OK_FIELD_NAME);
			if (okValue != OK_FIELD_CORRECT_VALUE)
			{
				throw new IllegalStateException( concat("Cannot get normal record for profileId = ", profileId, ". Request to url \"", url, "\" failed (\"", OK_FIELD_NAME, "\" field = ", okValue, ").") );
			}

			if ( !JsonUtils.hasField(jsonObject, "stats") )
			{
				logger.info( concat("Cannot get normal record for profileId = ", profileId, ". \"stats\" field is not present in response.") );
				return null;
			}

			JSONObject statsJson = jsonObject.getJSONObject("stats");
			if ( !JsonUtils.hasField(statsJson, "best_speed") )
			{
				logger.info( concat("Cannot get normal record for profileId = ", profileId, ". \"stats.best_speed\" field is not present in response.") );
				return null;
			}

			return statsJson.getInt("best_speed");
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
		boolean failed = false;
		String response = null;
		String cookie = new ConfigurationLoader().readConfigurationFile(ConfigurationLoader.COOKIE_CONF_FILE_NAME);

		do
		{
			try
			{
				response = tryToGetResponseBody(url, cookie);
				failed = false;
			}
			catch (UrlAccessFailedException e)
			{
				logger.info( concat("Failed to access url \"", url, "\", trying once more.") );
				failed = true;
			}
		}
		while (failed);

		return response;
	}
	private static String getResponseBodyEnsureJson(String url) throws IOException {
		boolean failed;
		String response = null;
		String cookie = new ConfigurationLoader().readConfigurationFile(ConfigurationLoader.COOKIE_CONF_FILE_NAME);

		do
		{
			try
			{
				response = tryToGetResponseBody(url, cookie);

				try
				{
					new JSONObject(response);
					failed = false;
				}
				catch (Exception e)
				{
					logger.info( concat("Access url granted \"", url, "\", but response is not a valid JSON string. Trying once more.") );
					failed = true;
				}
			}
			catch (UrlAccessFailedException e)
			{
				logger.info( concat("Failed to access url \"", url, "\", trying once more.") );
				failed = true;
			}
		}
		while (failed);

		return response;
	}

	private static String tryToGetResponseBody(String url, String cookie) throws IOException, UrlAccessFailedException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);

//		httpGet.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		if (StringUtils.notEmpty(cookie))
			httpGet.setHeader("Cookie", cookie);

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

	public static final String OK_FIELD_NAME = "ok";
	public static final int OK_FIELD_CORRECT_VALUE = 1;

	private static final Logger logger = Logger.getLogger(HttpClientTest.class);
}