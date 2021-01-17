package ru.klavogonki.kgparser;

public interface Parser
{
	/**
	 * @param fileName имя html-файла с сохраненным заездом
	 * @return заезд, заполненный согласно содержимому страницы.
	 */
	Round getRound(String fileName);
}
