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
	 * Комментарий к соревнованию.
	 */
	private String comment;

	/**
	 * Исходный zip-файл, в котором загружались данные заездов.
	 */
	private byte[] zipFile;

	/**
	 * Модель соревнования, сериализованная в json.
	 */
	private String competitionJson;
}