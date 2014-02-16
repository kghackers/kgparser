<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Список соревнований</title>
	<%@ include file="./headerInclude.jspf" %>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/kgparser.DomHelper.js"></script>
</head>
<body>
<div id="wrapper">
  <h2>Список загруженных соревнований</h2>
	<div id="competitions-list-container"></div>
	<div class="bottomLinks">
		<a href="./competitionUpload.jsp">Загрузить новое соревнование</a>
	</div>
</div>

<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	var zipFileDownloadUrl = contextPath + '/competitionZipFileDownload';
	var zipFileDownloadCompetitionIdParamName = 'competitionId';

	var competitionPageUrl = contextPath + '/competition.jsp';
	var competitionPageCompetitionIdParamName = 'competitionId';

	var LIST_CONTAINER_ID = 'competitions-list-container';
	var dh = DomHelper;

	function getZipFileDownloadUrl(competitionId) {
		return zipFileDownloadUrl + '?' + zipFileDownloadCompetitionIdParamName + '=' + competitionId;
	}
	function getCompetitionPageUrl(competitionId) {
		return competitionPageUrl + '?' + competitionPageCompetitionIdParamName + '=' + competitionId;
	}

	function appendCompetitionsList(competitions, total) {
		var container = dh.getEl(LIST_CONTAINER_ID);

		dh.removeChildrenHierarchy(container);

		if (!total)
		{
			container.appendChild(dh.createTN('Ни одного соревнования не найдено'));
			return;
		}

		var table = dh.createEl(dh.TABLE_TAG, null, 'competitionsList');
		var tBody = dh.createEl(dh.TBODY_TAG);
//			{"id":1,"zipFileName":"zipFile.zip","link":"http://klavogonki.ru","name":"Название соревнования","zipFileSize":666,"comment":"Коммент к соревнованию","zipFileContentType":"application/zip"}

		// header of the table
		var headerTr = dh.createEl(dh.TR_TAG);
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Название') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Страница соревнования') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Комментарий') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Исходный zip-файл') );

		tBody.appendChild(headerTr);

		$.each(competitions, function(index, competition) {
			var trClass = (index % 2 == 1) ? 'odd': 'even';

			var tr = dh.createEl(dh.TR_TAG, null, trClass);

			var nameTd = dh.createEl(dh.TD_TAG);
			var competitionLink = dh.createEl(dh.A_TAG, null, null, competition.name);
			competitionLink.setAttribute(dh.HREF_ATTRIBUTE, getCompetitionPageUrl(competition.id)); // todo: idStr

			nameTd.appendChild(competitionLink);

			var linkTd = dh.createEl(dh.TD_TAG);
			if (competition.link)
			{
				var link = dh.createEl(dh.A_TAG, null, null, 'Ссылка');
				link.setAttribute(dh.HREF_ATTRIBUTE, competition.link);

				linkTd.appendChild(link);
			}

			var commentTd = dh.createEl(dh.TD_TAG);
			if (competition.comment)
			{
				commentTd.appendChild( dh.createTN(competition.comment) );
			}

			var zipFileTd = dh.createEl(dh.TD_TAG);
			if (competition.zipFileName)
			{
				var zipFileLink = dh.createEl(dh.A_TAG, null, null, 'Скачать');
				zipFileLink.setAttribute(dh.HREF_ATTRIBUTE, getZipFileDownloadUrl(competition.id)); // todo: idStr

				zipFileTd.appendChild(zipFileLink);
			}

			// todo: competitionJson download link

			tr.appendChild(nameTd);
			tr.appendChild(linkTd);
			tr.appendChild(commentTd);
			tr.appendChild(zipFileTd);

			tBody.appendChild(tr);
		});

		table.appendChild(tBody);

		container.appendChild(table);
	}

	$(function() {
		jQuery.ajax({
				url: contextPath + '/competitionsList'
			, type: 'POST'
			, dataType: 'json'
			, success: function(response) {
				if (response.success)
				{
					appendCompetitionsList(response.results, response.total)
				}
				else
				{
					if (response && response.msg)
						alert('Произошла ошибка при получении списка соревнований:' + response.msg);
					else
						alert('Произошла ошибка при получении списка соревнований.');
				}
			}
		});
	});
</script>
</body>
</html>