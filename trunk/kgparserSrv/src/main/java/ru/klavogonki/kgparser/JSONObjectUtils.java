package ru.klavogonki.kgparser;

import org.json.JSONObject;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br/>
 * <br/>
 * // todo: move this class to kefir
 */
public class JSONObjectUtils
{
	public static boolean hasField(JSONObject jsonObject, String fieldName) {
		return jsonObject.has(fieldName) && ( !jsonObject.isNull(fieldName) );
	}
}