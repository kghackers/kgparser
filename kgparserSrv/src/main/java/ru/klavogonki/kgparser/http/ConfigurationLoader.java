package ru.klavogonki.kgparser.http;

import su.opencode.kefir.util.FileUtils;
import su.opencode.kefir.util.StringUtils;

import java.io.UnsupportedEncodingException;

import static su.opencode.kefir.util.StringUtils.concat;

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
		String path = concat( System.getProperty("jboss.server.home.dir"), "/conf/", filePath);
		byte[] bytes = FileUtils.readFile(path);
		try
		{
			return new String(bytes, StringUtils.CHARSET_UTF8);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static final String COOKIE_CONF_FILE_NAME = "kgparser-cookie.txt";
}