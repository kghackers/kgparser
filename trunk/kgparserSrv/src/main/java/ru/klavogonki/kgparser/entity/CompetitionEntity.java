package ru.klavogonki.kgparser.entity;

import javax.persistence.*;

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
@Table(name = "Competition")
public class CompetitionEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "competitionGenerator")
	@SequenceGenerator(name = "competitionGenerator", sequenceName = "competition_gen")
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

	@Lob
	@Column(name = "zip_file_data")
	public byte[] getZipFileData() {
		return zipFileData;
	}
	public void setZipFileData(byte[] zipFileData) {
		this.zipFileData = zipFileData;
	}

	@Column(name = "zip_file_name")
	public String getZipFileName() {
		return zipFileName;
	}
	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

	@Column(name = "zip_file_size")
	public Long getZipFileSize() {
		return zipFileSize;
	}
	public void setZipFileSize(Long zipFileSize) {
		this.zipFileSize = zipFileSize;
	}

	@Column(name = "zip_file_content_type")
	public Long getZipFileContentType() {
		return zipFileContentType;
	}
	public void setZipFileContentType(Long zipFileContentType) {
		this.zipFileContentType = zipFileContentType;
	}

	@Lob
	@Column(name = "competition_json")
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
	private byte[] zipFileData;

	/**
	 * Имя исходного zip-файла.
	 */
	private String zipFileName;

	/**
	 * Размер исходного zip-файла в байтах.
	 */
	private Long zipFileSize;

	/**
	 * Content-type исходного zip-файла.
	 */
	private Long zipFileContentType;

	/**
	 * Модель соревнования, сериализованная в json.
	 */
	private String competitionJson;
}