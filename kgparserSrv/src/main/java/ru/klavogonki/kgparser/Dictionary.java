/**
 * User: 1
 * Date: 17.01.2012
 * Time: 22:24:23
 */
package ru.klavogonki.kgparser;

import ru.klavogonki.kgparser.http.UrlConstructor;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.util.StringUtils;

/**
 * Словарь.
 */
public class Dictionary // extends JsonObject // this leads to javadoc generation failure if we use this class in kgparserSpringBoot module
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

		return getDictionaryId( this.getCode() );
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
		return isStandard(this.code);
	}

	@Json(exclude = true)
	public String getColor() {
		return getDictionaryColor(this.code);
	}

	@Json(exclude = true)
	public String getDictionaryPageUrl() {
		return getDictionaryPageUrl(this.code);
	}

	@Json(exclude = true)
	public static boolean isValid(String code) {
		return code.startsWith(NON_STANDARD_DICTIONARY_ID_PREFIX) || StandardDictionary.isValidStandardDictionaryCode(code);
	}

	/**
	 * @param code строковый код словаря (gametype в ajax-api)
	 * @return <code>true</code> &mdash; если словарь с указанным кодом является {@linkplain StandardDictionary стандартным};
	 * <br>
	 * <code>false</code> &mdash; если словарь с указанным кодом является пользовательским словарем.
	 */
	@Json(exclude = true)
	public static boolean isStandard(String code) {
		if (code.startsWith(NON_STANDARD_DICTIONARY_ID_PREFIX)) {
			return false;
		}

		if (StandardDictionary.isValidStandardDictionaryCode(code)) {
			return true;
		}

		throw new IllegalArgumentException(String.format("Incorrect dictionary code: \"%s\".", code));
	}

	/**
	 * @param code строковый код словаря (gametype в ajax-api)
	 * @return числовой идентификатор словаря
	 */
	@Json(exclude = true)
	public static int getDictionaryId(String code) {
		if ( isStandard(code) ) {
			throw new IllegalArgumentException("Dictionary with code = \"" + code + "\" is standard. Cannot get dictionary id from it."); // todo: use concat
		}

		String codeStr = code.substring( NON_STANDARD_DICTIONARY_ID_PREFIX.length() );
		return Integer.parseInt(codeStr);
	}

	/**
	 * @param dictionaryId идентификатор словаря
	 * @return строковый код словаря
	 */
	@Json(exclude = true)
	public static String getDictionaryCode(int dictionaryId) {
		return StringUtils.concat( NON_STANDARD_DICTIONARY_ID_PREFIX, Integer.toString(dictionaryId) );
	}

	@Json(exclude = true)
	public static int getTextType(String code) {
		if (!isStandard(code)) {
			return getDictionaryId(code);
		}

		StandardDictionary standardDictionary = StandardDictionary.valueOf(code);
		return StandardDictionary.getTextType(standardDictionary);
	}

	/**
	 * @param dictionaryCode строковый код словаря
	 * @return цвет, соответствующий словарю.
	 */
	@Json(exclude = true)
	public static String getDictionaryColor(String dictionaryCode) {
		if ( isStandard(dictionaryCode) )
		{
			return StandardDictionary.getColor( StandardDictionary.valueOf(dictionaryCode) );
		}

		return NON_STANDARD_DICTIONARY_COLOR;
	}

	/**
	 * @param dictionaryCode строковый код словаря
	 * @return <code>null</code> &mdash; для стандартных словарей
	 * <br>
	 * ссылка на страницу словаря &mdash; для нестандартных словарей
	 */
	public static String getDictionaryPageUrl(String dictionaryCode) {
		if ( isStandard(dictionaryCode) )
			return StandardDictionary.getDictionaryPageUrl( StandardDictionary.valueOf(dictionaryCode) );

		return UrlConstructor.dictionaryPage( getDictionaryId(dictionaryCode) );
	}

	/**
	 * Строковый код словаря.
	 * Для нестандартных словарей начинается с {@linkplain #NON_STANDARD_DICTIONARY_ID_PREFIX соответствующего префикса}.
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

	/**
	 * Префикс, с которого начинается код нестандартного словаря.
	 */
	public static final String NON_STANDARD_DICTIONARY_ID_PREFIX = "voc-";

	/**
	 * Цвет для отображения нестандартного словаря.
	 */
	public static String NON_STANDARD_DICTIONARY_COLOR = "#524CA7";
}
