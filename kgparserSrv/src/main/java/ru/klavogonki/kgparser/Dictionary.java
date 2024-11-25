package ru.klavogonki.kgparser;

import ru.klavogonki.common.DictionaryUtils;
import ru.klavogonki.common.StandardDictionary;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.util.StringUtils;

/**
 * Словарь.
 */
public class Dictionary // extends JsonObject // this leads to javadoc generation failure if we use this class in kgstatsSrv module
{
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Json(exclude = true)
	public Integer getId() {
		if ( this.isStandard() )
			return null;

		return DictionaryUtils.getDictionaryId( this.getCode() );
//		return id;
	}
//	public void setId(int id) {
//		this.id = id;
//	}

	public boolean hasCode(String dictionaryCode) {
		if ( StringUtils.empty(dictionaryCode) )
			throw new IllegalArgumentException("dictionaryCode cannot be null or empty");

		if ( StringUtils.empty(this.code) )
			throw new IllegalStateException("This Dictionary has no code");

		return this.code.equals(dictionaryCode);
	}

	public boolean isSame(Dictionary dictionary) {
		if (dictionary == null)
			throw new IllegalArgumentException("dictionary cannot be null");

		return this.hasCode( dictionary.getCode() );
	}

	@Override
	public boolean equals(Object obj) { // Dictionaries are equal if their codes are equal
		if ( !(obj instanceof Dictionary) )
			return false;

		return this.code.equals(((Dictionary) obj).getCode());
	}

	@Override
	public int hashCode() { // necessary for HashMap
		return this.code.hashCode();
	}

	/**
	 * @return <code>true</code> &mdash; если словарь является {@linkplain StandardDictionary стандартным};
	 * <br>
	 * <code>false</code> &mdash; если словарь является пользовательским словарем.
	 */
	@Json(exclude = true)
	public boolean isStandard() {
		return DictionaryUtils.isStandard(this.code);
	}

	@Json(exclude = true)
	public String getColor() {
		return DictionaryUtils.getDictionaryColor(this.code);
	}

	@Json(exclude = true)
	public String getDictionaryPageUrl() {
		return DictionaryUtils.getDictionaryPageUrl(this.code);
	}

	/**
	 * Строковый код словаря.
	 * Для нестандартных словарей начинается с {@linkplain ru.klavogonki.common.DictionaryUtils#NON_STANDARD_DICTIONARY_ID_PREFIX соответствующего префикса}.
	 */
	private String code;

//	/**
//	 * Код словаря для нестандартных словарей.
//	 */
//	private int id;

	/**
	 * Название словаря.
	 */
	private String name;
}
