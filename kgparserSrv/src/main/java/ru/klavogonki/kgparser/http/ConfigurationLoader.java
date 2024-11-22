package ru.klavogonki.kgparser.http;

import su.opencode.kefir.util.FileUtils;

import java.nio.charset.StandardCharsets;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class ConfigurationLoader
{
	public String readConfigurationFile(String filePath) {
		String path = System.getProperty("jboss.server.home.dir") + "/conf/" + filePath;
		byte[] bytes = FileUtils.readFile(path);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static final String COOKIE_CONF_FILE_NAME = "kgparser-cookie.txt";
}
