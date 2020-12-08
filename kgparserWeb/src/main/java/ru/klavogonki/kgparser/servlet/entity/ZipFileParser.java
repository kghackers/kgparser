package ru.klavogonki.kgparser.servlet.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.dataparser.VoidmainJsonParser;
import ru.klavogonki.kgparser.entity.CompetitionEntity;
import su.opencode.kefir.util.FileUtils;
import su.opencode.kefir.util.ZipUtils;

import java.util.UUID;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class ZipFileParser
{
	public static Competition parseZipFile(CompetitionEntity entity, String exportScriptVersion) {
		StringBuilder sb = new StringBuilder();
		String dirPath = getDirPath(sb);
		logger.info("Directory path to extract zip file: \"{}\"", dirPath);
		FileUtils.createDirs(dirPath);
		logger.info("Directory \"{}\" created successfully.", dirPath);

		String filePath = concat(sb, dirPath, FileUtils.FILE_SEPARATOR, entity.getZipFileName());
		logger.info("Zip file path: {}", filePath);

		FileUtils.writeToFile(filePath, entity.getZipFileData());
		logger.info("Zip file successfully written to path \"{}\".", filePath);

		String extractDirPath = concat(sb, dirPath, FileUtils.FILE_SEPARATOR, "extracted");
		ZipUtils.unzip(filePath, extractDirPath);
		logger.info("Zip file \"{}\" successfully extracted to path \"{}\".", filePath, extractDirPath);

		return VoidmainJsonParser.parseCompetition( entity.getName(), extractDirPath, exportScriptVersion );
	}
	private static String getDirPath(StringBuilder sb) {
		return concat(sb, System.getProperty("jboss.server.home.dir"), FileUtils.FILE_SEPARATOR, ZIP_FILES_EXTRACT_DIR, FileUtils.FILE_SEPARATOR, "zip-", UUID.randomUUID() );
	}

	public static final String ZIP_FILES_EXTRACT_DIR = "competitionZipFiles";

	private static final Logger logger = LogManager.getLogger(ZipFileParser.class);
}
