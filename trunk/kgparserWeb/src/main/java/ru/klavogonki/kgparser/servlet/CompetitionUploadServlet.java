package ru.klavogonki.kgparser.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.klavogonki.kgparser.entity.CompetitionEntity;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import su.opencode.kefir.util.StringUtils;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionUploadServlet extends JsonServlet
{
	@Override
	protected Action getAction() {
		return new InitiableAction()
		{
			@Override
			public void doAction() throws Exception {
				CompetitionEntity entity = new CompetitionEntity();

				if (ServletFileUpload.isMultipartContent(request))
				{
					entity = processMultipartContent();
				}
				else
				{
					// todo: return error normally
					response.getOutputStream().write( "Необходимо загрузить файл".getBytes(StringUtils.CHARSET_UTF8) );
					return;
				}

				// todo: validate entity fields

				// todo: parse zip file and fill competitionJson

				CompetitionEntityService service = getService(CompetitionEntityService.class);

/*
				CompetitionEntity entity = new CompetitionEntity();
				entity.setName("Название соревнования");
				entity.setLink("http://klavogonki.ru");
				entity.setComment("Коммент к соревнованию");

				entity.setZipFileData( "some zip file data".getBytes() );
				entity.setZipFileName("zipFile.zip");
				entity.setZipFileSize(666L);
				entity.setZipFileContentType("application/zip");

				entity.setCompetitionJson("{\"someField\": \"some value\"}");
*/

				service.createCompetitionEntity(entity);
				writeSuccess();
			}

			private CompetitionEntity processMultipartContent() throws FileUploadException, UnsupportedEncodingException {
				CompetitionEntity entity = new CompetitionEntity();

				FileItemFactory factory = new DiskFileItemFactory();
//				factory.setSizeThreshold(BUF_SIZE);
//				factory.setRepository(new File(TEMP_DIRECTORY));

				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(request);

				for (FileItem item : items)
				{
					String fieldName = item.getFieldName();

					if (item.isFormField())
					{ // simple field
						String itemStr = item.getString(StringUtils.CHARSET_UTF8);

						switch (fieldName)
						{
							case NAME_FILE_FIELD_NAME: entity.setName(itemStr); break;
							case LINK_FILE_FIELD_NAME: entity.setLink(itemStr); break;
							case COMMENT_FILE_FIELD_NAME: entity.setComment(itemStr); break;
						}
					}
					else
					{ // file field
						if (fieldName.equals(ZIP_FILE_FIELD_NAME))
						{
							if (item.getSize() > 0)
							{ // prevent parsing empty file fields
								entity.setZipFileData( item.get() );
								entity.setZipFileName(item.getName());
								entity.setZipFileSize(item.getSize());
								entity.setZipFileContentType(item.getContentType());
							}
						}
					}
				}

				return entity;
			}
		};
	}

	public static final String NAME_FILE_FIELD_NAME = "name";
	public static final String LINK_FILE_FIELD_NAME = "link";
	public static final String COMMENT_FILE_FIELD_NAME = "comment";
	public static final String ZIP_FILE_FIELD_NAME = "zipFile";
}