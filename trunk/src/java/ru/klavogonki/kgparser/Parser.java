/**
 * User: 1
 * Date: 13.01.2012
 * Time: 23:36:17
 */
package ru.klavogonki.kgparser;

public interface Parser
{
	/**
	 * @param fileName имя html-файла с сохраненным заездом
	 * @return заезд, заполненный согласно содержимому страницы.
	 */
	Round getRound(String fileName);
}