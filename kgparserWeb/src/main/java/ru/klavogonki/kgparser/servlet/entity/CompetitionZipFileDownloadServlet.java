package ru.klavogonki.kgparser.servlet.entity;

import ru.klavogonki.kgparser.entity.CompetitionEntity;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionZipFileDownloadServlet extends JsonServlet
{
	@Override
	protected Action getAction() {
		return new InitiableAction()
		{
			@Override
			public void doAction() throws Exception {
				Long competitionEntityId = getLongParam(COMPETITION_ENTITY_ID_PARAM_NAME);
				if (competitionEntityId == null) {
					String errorMessage = String.format("\"%s\" parameter is not set", COMPETITION_ENTITY_ID_PARAM_NAME);
					throw new ClientException(errorMessage);
				}

				CompetitionEntityService service = getService(CompetitionEntityService.class);
				CompetitionEntity entity = service.getCompetitionEntity(competitionEntityId);

				if (entity == null) {
					String errorMessage = String.format("CompetitionEntity not found for id = %d", competitionEntityId);
					throw new ClientException(errorMessage);
				}

				String contentType = entity.getZipFileContentType();
				if (contentType == null) {
					contentType = "application/octet-stream";
				}

				response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
				response.setContentType(contentType);
				response.setContentLength( entity.getZipFileSize().intValue() );
				response.setHeader("Cache-Control", "public, max-age=0");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + getEncodedFilename(request, entity.getZipFileName()) + "\"");

				byte[] zipFileData = entity.getZipFileData();

				try
					(ServletOutputStream output = response.getOutputStream(); InputStream input = new ByteArrayInputStream(zipFileData, 0, zipFileData.length))
				{
					byte[] buffer = new byte[BUFFER_SIZE];
					response.setBufferSize(BUFFER_SIZE);

					int length;
					while ((length = input.read(buffer)) >= 0)
						output.write(buffer, 0, length);

					output.flush();
				}
			}

			private String getEncodedFilename(HttpServletRequest request, String filename) {
				StringBuilder sb = new StringBuilder();
				if (request.getHeader("User-Agent").contains("Opera") || !request.getHeader("User-Agent").contains("MSIE"))
				{ // mozilla
					sb.append(new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
				}
				else
				{ // MSIE
					// todo: если возможно, решить проблему с именами длиннее 30 символов в IE
					byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
					for (byte filenameByte : filenameBytes)
					{
						if ((filenameByte | 0x7F) == 0xFFFFFFFF)
						{ // starts with 1
							sb.append('%');
							sb.append(HEXDIGITS[(filenameByte & (15 * 16)) / 16]); // first 4 digits
							sb.append(HEXDIGITS[filenameByte & 15]); // last 4 digits
						}
						else
						{ // starts with 0
							sb.append((char) filenameByte);
						}
					}
				}

				return sb.toString();
			}
		};
	}

	public static final String COMPETITION_ENTITY_ID_PARAM_NAME = "competitionId";

	private static final int BUFFER_SIZE = 2048;
	private static final char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
