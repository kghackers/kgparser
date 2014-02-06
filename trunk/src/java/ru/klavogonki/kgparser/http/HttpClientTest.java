package ru.klavogonki.kgparser.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import ru.klavogonki.kgparser.StandardDictionary;

import java.io.IOException;

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
		int playerId = 242585; // nosferatum (superman)
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
		String rankTitle = jsonObject.getString("title");

		System.out.println("id: " + profileId);
		System.out.println("login: " + login);
		System.out.println("level: " + level);
		System.out.println("rankTitle: " + rankTitle);

		JSONObject car = jsonObject.getJSONObject("car");
		int carId = car.getInt("car");

		System.out.println("carId: " + carId);

		// todo: form player object from parsed data
	}
}