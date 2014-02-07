/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:24:23
 */
package ru.klavogonki.kgparser;

import su.opencode.kefir.util.StringUtils;

/**
 * Словарь.
 */
public class Dictionary
{
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean hasCode(String dictionaryCode) {
		if ( StringUtils.empty(dictionaryCode) )
			throw new IllegalArgumentException("dictionaryCode cannot be null or empty");

		if ( StringUtils.empty(this.code) )
			throw new IllegalStateException("This Dictionary has no code");

		return this.code.equals(dictionaryCode);
	}

	@Override
	public boolean equals(Object obj) { // Dictionaries are equal if their codes are equal
		if ( !(obj instanceof Dictionary) )
			return false;

		return this.code.equals(((Dictionary) obj).getCode());
	}

	/**
	 * @param code строковый код словаря (gametype в ajax-api)
	 * @return <code>true</code> &mdash; если словарь с указанным кодом является {@linkplain StandardDictionary стандартным};
	 * <br/>
	 * <code>false</code> &mdash; если словарь с указанным кодом является пользовательским словарем.
	 */
	public static boolean isStandard(String code) {
		return !code.startsWith(NON_STANDARD_DICTIONARY_ID_PREFIX);
	}

	/**
	 * @param code строковый код словаря (gametype в ajax-api)
	 * @return числовой идентификатор словаря
	 */
	public static int getDictionaryId(String code) {
		if ( isStandard(code) )
			throw new IllegalArgumentException("Dictionary with code = \"" + code + "\" is standard. Cannot get dictionary id from it."); // todo: use concat

		String codeStr = code.substring( NON_STANDARD_DICTIONARY_ID_PREFIX.length() );
		return Integer.parseInt(codeStr);
	}

	/**
	 * @param dictionaryId идентификатор словаря
	 * @return строковый код словаря
	 */
	public static String getDictionaryCode(int dictionaryId) {
		return StringUtils.concat( NON_STANDARD_DICTIONARY_ID_PREFIX, Integer.toString(dictionaryId) );
	}

	/**
	 * Строковый код словаря.
	 * Для нестандартных словарей начинается с {@linkplain #NON_STANDARD_DICTIONARY_ID_PREFIX соответствующего префикса}.
	 */
	private String code;

	/**
	 * Код словаря для нестандартных словарей.
	 */
	private int id;

	/**
	 * Название словаря.
	 */
	private String name;

	/**
	 * Префикс, с которого начинается код нестандартного словаря.
	 */
	public static final String NON_STANDARD_DICTIONARY_ID_PREFIX = "voc-";
}