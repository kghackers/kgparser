package ru.klavogonki.kgparser;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br/>
 * <br/>
 * Сущность соревнования, сохраняемая в базе.
 */
@Entity
public class CompetitionEntity
{
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public byte[] getZipFile() {
		return zipFile;
	}
	public void setZipFile(byte[] zipFile) {
		this.zipFile = zipFile;
	}
	public String getZipFileName() {
		return zipFileName;
	}
	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}
	public Long getZipFileSize() {
		return zipFileSize;
	}
	public void setZipFileSize(Long zipFileSize) {
		this.zipFileSize = zipFileSize;
	}
	public String getCompetitionJson() {
		return competitionJson;
	}
	public void setCompetitionJson(String competitionJson) {
		this.competitionJson = competitionJson;
	}

	/**
	 * Идентификатор.
	 */
	private Long id;

	/**
	 * Название соревнования.
	 */
	private String name;

	/**
	 * Ссылка на страницу соревнования на форуме Клавогонок.
	 */
	private String link;

	/**
	 * Комментарий к соревнованию.
	 */
	private String comment;

	/**
	 * Исходный zip-файл, в котором загружались данные заездов.
	 */
	private byte[] zipFile;

	/**
	 * Имя исходного zip-файла.
	 */
	private String zipFileName;

	/**
	 * Размер исходного zip-файла в байтах.
	 */
	private Long zipFileSize;

	/**
	 * Модель соревнования, сериализованная в json.
	 */
	private String competitionJson;
}