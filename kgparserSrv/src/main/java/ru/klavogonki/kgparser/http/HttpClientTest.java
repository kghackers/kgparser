package ru.klavogonki.kgparser.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
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

		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);

		int playerId = 204574; // eksdak
		getUserNormalRecord(playerId);
		if (true)
			return;

		String url;
		String body;

		logger.info("");
		url = UrlConstructor.getSummary(playerId);
		logger.info("url: {}", url);
		body = getResponseBody(url);
		logger.info("player summary body:\n{}", body);
		parsePlayerSummaryBody(body);


		logger.info("");
//		String dictionaryCode = StandardDictionary.normal.toString(); // обычный
		String dictionaryCode = StandardDictionary.chars.toString(); // буквы
//		String dictionaryCode = Dictionary.getDictionaryCode(192); // частотный
		url = UrlConstructor.getStatsDetail(playerId, dictionaryCode);
		logger.info("url: {}",  url);
		body = getResponseBody(url);
		logger.info("player stats body:\n{}", body);
	}

	public static Rank getUserRank(int profileId) {
		try
		{
			String url = UrlConstructor.getSummary(profileId);
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
			String url = UrlConstructor.getIndexData(profileId);
//			String body = getResponseBody(url);
			String body = getResponseBodyEnsureJson(url);

			JSONObject jsonObject = new JSONObject(body);
			if ( !JsonUtils.hasField(jsonObject, OK_FIELD_NAME) )
			{
				String errorMessage = String.format(
					"Cannot get normal record for profileId = %d. Request to url \"%s\" failed (\"%s\" is not present in response or is null).",
					profileId,
					url,
					OK_FIELD_NAME
				);

				throw new IllegalStateException(errorMessage);
			}

			int okValue = jsonObject.getInt(OK_FIELD_NAME);
			if (okValue != OK_FIELD_CORRECT_VALUE)
			{
				String errorMessage = String.format(
					"Cannot get normal record for profileId = %d. Request to url \"%s\" failed (\"%s\" field = %s).",
					profileId,
					url,
					OK_FIELD_NAME,
					okValue
				);

				throw new IllegalStateException(errorMessage);
			}

			if ( !JsonUtils.hasField(jsonObject, "stats") )
			{
				logger.warn("Cannot get normal record for profileId = {}. \"stats\" field is not present in response.", profileId);
				return null;
			}

			JSONObject statsJson = jsonObject.getJSONObject("stats");
			if ( !JsonUtils.hasField(statsJson, "best_speed") )
			{
				logger.warn("Cannot get normal record for profileId = {}. \"stats.best_speed\" field is not present in response.", profileId);
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

		try
		{
			String url = UrlConstructor.getStatsOverview(NOSFERATUM_PROFILE_ID);
			String body = getResponseBody(url);

			JSONObject jsonObject = new JSONObject(body);
			if ( // todo: refactor these checks to separate kefir method
				   ( !jsonObject.has(OK_FIELD_NAME) )
				|| ( jsonObject.isNull(OK_FIELD_NAME) )
				|| ( jsonObject.getInt(OK_FIELD_NAME) != 1)
			)
			{ // check "ok" field
				String errorMessage = String.format("Request to url \"%s\" failed (is not ok).", url);
				throw new RuntimeException(errorMessage);
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
					String errorMessage = String.format("Cannot get dictionary info for dictionary code = \"%s\".", dictionaryCode);
					throw new IllegalStateException(errorMessage);
				}

				JSONObject dictionaryInfo = gameTypes.getJSONObject(dictionaryCode);
				String dictionaryName = dictionaryInfo.getString("name");
				logger.info("Dictionary code: \"{}\". Dictionary name = \"{}\".", dictionaryCode, dictionaryName);
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
		boolean failed;
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
				logger.warn("Failed to access url \"{}\", trying once more.", url);
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
					String errorMessage = String.format("Access to url \"%s\" granted, but response is not a valid JSON string. Trying once more.", url);
					logger.error(errorMessage, e);

					failed = true;
				}
			}
			catch (UrlAccessFailedException e)
			{
				String errorMessage = String.format("Failed to access url \"%s\", trying once more.", url);
				logger.error(errorMessage, e);

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
		logger.debug("json key set: {}", jsonObject.keySet());

		JSONObject user = jsonObject.getJSONObject("user");
		int profileId = user.getInt("id");
		String login = user.getString("login");

		int level = jsonObject.getInt("level");
		Rank rank = Rank.getRank(level);
		String rankTitle = jsonObject.getString("title");

		logger.debug("id: {}", profileId);
		logger.debug("login: {}", login);
		logger.debug("level: {}", level);
		logger.debug("rank: {}", rank);
		logger.debug("rankTitle (from json): {}", rankTitle);

		JSONObject car = jsonObject.getJSONObject("car");
		int carId = car.getInt("car");

		logger.debug("carId: {}", carId);

		// todo: form player object from parsed data
	}

	public static final int NOSFERATUM_PROFILE_ID = 242585;

	public static final String OK_FIELD_NAME = "ok";
	public static final int OK_FIELD_CORRECT_VALUE = 1;

	private static final Logger logger = LogManager.getLogger(HttpClientTest.class);
}
