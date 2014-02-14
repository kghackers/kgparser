package ru.klavogonki.kgparser.dataparser;

import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.processing.AverageSpeedCounter;
import ru.klavogonki.kgparser.processing.HighChartValue;
import ru.klavogonki.kgparser.processing.SpeedChartFiller;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.FileUtils;
import su.opencode.kefir.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class FromJsonTest
{
	public static void main(String[] args) throws UnsupportedEncodingException {
		BasicConfigurator.configure();

		String competitionJsonFilePath = "C:\\java\\kgparser\\doc\\voidmain\\marathons__2014_02_11_model.json";

		byte[] bytes = FileUtils.readFile(competitionJsonFilePath);
		String jsonString = new String(bytes, StringUtils.CHARSET_UTF8);
		System.out.println("Source json:");
		System.out.println( jsonString.toString() );

		JSONObject jsonObject = new JSONObject(jsonString);

		Competition competition = JsonObject.fromJson(jsonObject, Competition.class);
		JSONObject parsedCompetitionToJson = competition.toJson();

		System.out.println("Parsed competition toJson:");
		System.out.println( parsedCompetitionToJson.toString() );

		AverageSpeedCounter.logCompetitionInfo(competition);

		HighChartValue highChartValue = SpeedChartFiller.fillData(competition);
		System.out.println("highchartValue: ");
		System.out.println(highChartValue.toJson());


/*
		if ( parsedCompetitionToJson.toString().equals(jsonString) )
			System.out.println("ok");
		else
			System.out.println("fail");
*/
	}
}