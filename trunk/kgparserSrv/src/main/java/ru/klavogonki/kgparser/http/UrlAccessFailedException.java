package ru.klavogonki.kgparser.http;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class UrlAccessFailedException extends Exception
{
	public UrlAccessFailedException() {
	}
	public UrlAccessFailedException(String message) {
		super(message);
	}
	public UrlAccessFailedException(String message, Throwable cause) {
		super(message, cause);
	}
	public UrlAccessFailedException(Throwable cause) {
		super(cause);
	}
	public UrlAccessFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}