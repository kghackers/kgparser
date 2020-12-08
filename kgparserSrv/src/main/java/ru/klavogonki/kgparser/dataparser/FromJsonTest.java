package ru.klavogonki.kgparser.dataparser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.json.JSONObject;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.processing.AverageSpeedCounter;
import ru.klavogonki.kgparser.processing.HighChartValue;
import ru.klavogonki.kgparser.processing.SpeedChartFiller;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.FileUtils;

import java.nio.charset.StandardCharsets;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class FromJsonTest
{
	private static final Logger logger = LogManager.getLogger(FromJsonTest.class);

	public static void main(String[] args) {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);

		String competitionJsonFilePath = "C:\\java\\kgparser\\doc\\voidmain\\marathons__2014_02_11_model.json";

		byte[] bytes = FileUtils.readFile(competitionJsonFilePath);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		logger.info("Source json:");
		logger.info(jsonString);

		JSONObject jsonObject = new JSONObject(jsonString);

		Competition competition = JsonObject.fromJson(jsonObject, Competition.class);
		JSONObject parsedCompetitionToJson = competition.toJson();

		logger.info("Parsed competition toJson:");
		logger.info(parsedCompetitionToJson);

		AverageSpeedCounter.logCompetitionInfo(competition);

		HighChartValue highChartValue = SpeedChartFiller.fillData(competition);
		logger.info("highchartValue: ");
		logger.info(highChartValue.toJson());

/*
		if ( parsedCompetitionToJson.toString().equals(jsonString) )
			logger.info("ok");
		else
			logger.error("fail");
*/
	}
}
