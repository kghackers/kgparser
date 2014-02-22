<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Сводная таблица результатов игроков</title>
	<%@ include file="./headerInclude.jspf" %>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/kgparser.DomHelper.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
</head>
<body>
<div id="wrapper">
	<h2>Общая таблица по игрокам</h2>

	<h3>Соревнование</h3>
	<div id="competitionName-container"></div>

	<h3>Таблица результатов игроков</h3>
	<div id="playersResultsTable-container"></div>

	<div class="bottomLinks">
		<div>
			<a href="./playerResultsTableXlsGet?competitionId=<%=request.getParameter("competitionId")%>">Выгрузить в Excel</a>
		</div>
		<div>
			<a href="./competition.jsp?competitionId=<%=request.getParameter("competitionId")%>">Вернуться к странице соревнования</a>
		</div>
	</div>

</div>
<script type="text/javascript">
	var competitionId = '<%=request.getParameter("competitionId")%>';

	CONTEXT_PATH = '<%=request.getContextPath()%>';
	var playersResultsTableDataGetUrl = CONTEXT_PATH + '/playersResultsTableDataGet';

	var COMPETITION_NAME_CONTAINER_ID = 'competitionName-container';
	var PLAYERS_RESULTS_TABLE_CONTAINER_ID = 'playersResultsTable-container';
	// todo: toggle link for table

	var dh = DomHelper;

	function appendCompetitionInfo(result) {
		appendText(COMPETITION_NAME_CONTAINER_ID, result.competitionName);
	}

	function appendPlayersResultsTable(result) {
		var container = dh.getEl(PLAYERS_RESULTS_TABLE_CONTAINER_ID);
		dh.removeChildrenHierarchy(container);

		var table = dh.createEl(dh.TABLE_TAG, null, RESULTS_TABLE_CLASS);
		var tBody = dh.createEl(dh.TBODY_TAG);

		// header rows
		$.each(result.headerRows, function(index, headerRow) {
			var tr = dh.createEl(dh.TR_TAG);

			$.each(headerRow.cells, function(index, cell) {
				var th = dh.createEl(dh.TH_TAG, null, null, cell.text);

				if ( (cell.colSpan) && (cell.colSpan > 1) )
					th.setAttribute(dh.COL_SPAN_ATTRIBUTE, cell.colSpan);

				tr.appendChild(th);
			});

			tBody.appendChild(tr);
		});

		// players rows
		$.each(result.playersRows, function(index, playerRow) {
			var trClass = (index % 2 == 1) ? 'odd': 'even';
			var tr = dh.createEl(dh.TR_TAG, null, trClass);

			$.each(playerRow.cells, function(index, cell) {
				var td = dh.createEl(dh.TD_TAG, null, null, cell.text);

				if ( (cell.colSpan) && (cell.colSpan > 1) )
					td.setAttribute(dh.COL_SPAN_ATTRIBUTE, cell.colSpan);

				tr.appendChild(td);
			});

			tBody.appendChild(tr);
		});

		table.appendChild(tBody);
		container.appendChild(table);
	}

	function loadPlayersResults() {
		jQuery.ajax({
			  url: playersResultsTableDataGetUrl
			, type: 'POST'
			, dataType: 'json'
			, data: {
				competitionId: competitionId
			}
			, success: function(response) {
				if (response.success)
				{
					appendCompetitionInfo(response);
					appendPlayersResultsTable(response);
				}
				else
				{
					if (response && response.msg)
						alert('Произошла ошибка при получении данных игроков:' + response.msg);
					else
						alert('Произошла ошибка при получении данных игроков.');
				}
			}
		});
	}

	$(function() {
		if ( !competitionId )
		{
			alert('Необходимо задать параметр \"competitionId\"');
			return;
		}

//		bindShowHideLinks();
		loadPlayersResults();
	});
</script>
</body>
</html>