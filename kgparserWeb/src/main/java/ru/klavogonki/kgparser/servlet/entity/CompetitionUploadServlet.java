package ru.klavogonki.kgparser.servlet.entity;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.entity.CompetitionEntity;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import su.opencode.kefir.util.StringUtils;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

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
				CompetitionEntity entity;

				if (ServletFileUpload.isMultipartContent(request))
				{
					entity = processMultipartContent();
				}
				else
				{
					// todo: return error normally
					response.getOutputStream().write( "Необходимо загрузить zip-файл".getBytes(StringUtils.CHARSET_UTF8) );
					return;
				}

				// todo: return errors normally
				if ( StringUtils.empty(entity.getName()) )
					throw new IllegalArgumentException("Name of the competition must not be empty.");

				if ( (entity.getZipFileData() == null) || (entity.getZipFileData().length <= 0) )
					throw new IllegalArgumentException("Zip file must not be empty.");

				if ( StringUtils.empty(entity.getZipFileName()) )
					throw new IllegalArgumentException("Zip file name must not be empty.");

				// transform competition to json
				Competition competition;
				try
				{
					competition = ZipFileParser.parseZipFile(entity, entity.getExportScriptVersion());
				}
				catch (Exception e)
				{
					logger.error( concat("Error while parsing zip file \"", entity.getZipFileName(), "\" into Competition model:"), e );
					throw new RuntimeException( concat("Error while parsing zip file \"", entity.getZipFileName(), "\" into Competition model:"), e);
				}

				try
				{
					JSONObject jsonObject = competition.toJson();
					entity.setCompetitionJson( jsonObject.toString() );
				}
				catch (Exception e)
				{
					logger.error(concat("Error while transforming Competition model to JSON:"), e);
					throw new RuntimeException( concat("Error while transforming Competition model to JSON:"), e);
				}

				CompetitionEntityService service = getService(CompetitionEntityService.class);
				Long competitionId = service.createCompetitionEntity(entity);

				response.sendRedirect( concat(sb, "./competitionUploadSuccess.jsp?competitionId=", competitionId) );
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
							case EXPORT_SCRIPT_VERSION_FIELD_NAME: entity.setExportScriptVersion(itemStr); break;
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

	public static final String EXPORT_SCRIPT_VERSION_FIELD_NAME = "exportScriptVersion";
	public static final String NAME_FILE_FIELD_NAME = "name";
	public static final String LINK_FILE_FIELD_NAME = "link";
	public static final String COMMENT_FILE_FIELD_NAME = "comment";
	public static final String ZIP_FILE_FIELD_NAME = "zipFile";
}