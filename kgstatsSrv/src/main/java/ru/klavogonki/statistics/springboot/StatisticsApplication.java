package ru.klavogonki.statistics.springboot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.download.PlayerDataDownloader;
import ru.klavogonki.statistics.export.StatisticsGenerator;
import ru.klavogonki.statistics.export.StatisticsGeneratorConfig;
import ru.klavogonki.statistics.import_db.DatabaseImporter;
import ru.klavogonki.statistics.util.DateDiffUtils;
import ru.klavogonki.statistics.util.DateUtils;
import ru.klavogonki.statistics.util.JacksonUtils;

import java.time.OffsetDateTime;

@EntityScan(basePackages= {"ru.klavogonki.statistics.entity"})
@ComponentScan({"ru.klavogonki.statistics"})
@Import(StatisticsApplicationConfiguration.class) // see https://stackoverflow.com/a/55591954/8534088
@Log4j2
public class StatisticsApplication implements CommandLineRunner {

	public enum Mode {
		DOWNLOAD_PLAYER_DATA(3),
		IMPORT_JSON_TO_DATABASE(2),
		GENERATE_STATISTICS_FROM_DATABASE(3),
		;

		Mode(final int requiredArgumentsCount) {
			this.requiredArgumentsCount = requiredArgumentsCount;
		}

		public final int requiredArgumentsCount; // including mode
	}

	@Autowired(required = false)
	private StatisticsGenerator statisticsGenerator;

	@Autowired(required = false)
	private DatabaseImporter databaseImporter;

	public static void main(String[] args) {
		SpringApplication.run(StatisticsApplication.class, args);
	}

	@Override
	public void run(final String... args) {
		if (args.length == 0) {
			System.out.printf("Usage must be one of the following: %n");
			System.out.printf("%s %s <inputConfigFilePath> <outputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.DOWNLOAD_PLAYER_DATA.name());
			System.out.printf("%s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.IMPORT_JSON_TO_DATABASE.name());
			System.out.printf("%s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.GENERATE_STATISTICS_FROM_DATABASE.name());

			return;
		}

		String modeString = args[0];
		Mode mode = Mode.valueOf(modeString);
		// todo: nice handling of incorrect mode value

		switch (mode) {
			case DOWNLOAD_PLAYER_DATA:
				handleDownloadPlayerData(args);
				break;

			case IMPORT_JSON_TO_DATABASE:
				handleImportJsonToDatabase(args);
				break;

			case GENERATE_STATISTICS_FROM_DATABASE:
				handleGenerateStatistics(args);
				break;

			default:
				throw new IllegalArgumentException(String.format("Unknown mode value: %s", mode));
		}
	}

	private void handleDownloadPlayerData(final String[] args) {
		if (args.length != Mode.DOWNLOAD_PLAYER_DATA.requiredArgumentsCount) {
			System.out.printf("Usage: %s %s <inputConfigFilePath> <outputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.DOWNLOAD_PLAYER_DATA.name());
			return;
		}

		String inputConfigFilePath = args[1];
		String outputConfigFilePath = args[2];
		String[] playerDataDownloaderArgs = {inputConfigFilePath, outputConfigFilePath};

		logOperationDuration(
			Mode.DOWNLOAD_PLAYER_DATA,
			() -> PlayerDataDownloader.main(playerDataDownloaderArgs)
		);
	}

	private void handleImportJsonToDatabase(final String[] args) {
		if (args.length != Mode.IMPORT_JSON_TO_DATABASE.requiredArgumentsCount) {
			System.out.printf("Usage: %s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.IMPORT_JSON_TO_DATABASE.name());
			return;
		}

		Config config = JacksonUtils.parseConfig(args[1]);

		logOperationDuration(
			Mode.IMPORT_JSON_TO_DATABASE,
			() -> databaseImporter.importJsonToDatabase(config)
		);
	}

	private void handleGenerateStatistics(final String[] args) {
		if (args.length != Mode.GENERATE_STATISTICS_FROM_DATABASE.requiredArgumentsCount) {
			System.out.printf("Usage: %s %s <inputConfigFilePath> <statisticsGeneratorConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.GENERATE_STATISTICS_FROM_DATABASE.name());
			return;
		}

		Config config = JacksonUtils.parseConfig(args[1]);
		StatisticsGeneratorConfig statisticsGeneratorConfig = JacksonUtils.parseStatisticsGeneratorConfig(args[2]);

		logOperationDuration(
			Mode.GENERATE_STATISTICS_FROM_DATABASE,
			() -> statisticsGenerator.generateStatistics(config, statisticsGeneratorConfig)
		);
	}

	private void logOperationDuration(Mode operation, Runnable operationCaller) {
		String operationName = operation.name();

		OffsetDateTime startDate = OffsetDateTime.now();

		try {
			operationCaller.run();
		} catch (Exception e) {
			logger.error("Operation {} failed:", operationName, e);

			throw new RuntimeException(e); // todo: custom runtime exception
		} finally {
			OffsetDateTime endDate = OffsetDateTime.now();

			String formattedDuration = DateDiffUtils.formatDiff(startDate, endDate);

			logger.info("Operation {} start: {}.", operationName, DateUtils.formatDateTimeForLog(startDate));
			logger.info("Operation {} end: {}.", operationName, DateUtils.formatDateTimeForLog(endDate));
			logger.info("Operation {} duration: {}.", operationName, formattedDuration);
		}
	}
}
