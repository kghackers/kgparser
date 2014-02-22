<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Страница соревнования</title>
	<%@ include file="./headerInclude.jspf" %>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/kgparser.DomHelper.js"></script>
</head>
<body>
<div id="wrapper">
	<h2>Страница соревнования</h2>

	<h3>Название</h3>
	<div id="name-container"></div>

	<h3>Количество заездов</h3>
	<div id="roundsCount-container"></div>

	<h3>Словари&nbsp;<a class="toggle" id="dictionaries-toggle-link">Показать</a></h3>
	<div id="dictionaries-container"></div>

	<h3>Заезды&nbsp;<a class="toggle" id="rounds-toggle-link">Показать</a></h3>
	<div id="rounds-container"></div>

	<h3>Игроки (не включая гостей)&nbsp;<a class="toggle" id="players-toggle-link">Показать</a></h3>
	<div id="players-container"></div>

	<h3>Обработка результатов&nbsp;<a class="toggle" id="results-toggle-link">Показать</a></h3>
	<div id="results-container">
		<div><a href="./competitionSpeedChart.jsp?competitionId=<%=request.getParameter("competitionId")%>">График скоростей игроков по заездам</a></div>
		<div><a href="./competitionErrorsCountChart.jsp?competitionId=<%=request.getParameter("competitionId")%>">График количеств ошибок игроков по заездам</a></div>
		<div><span class="disabled">График процентов ошибок игроков по заездам</span></div>
		<div><a href="./competitionPlayersSummary.jsp?competitionId=<%=request.getParameter("competitionId")%>">Сводная таблица результатов игроков</a></div>
		<div><span class="disabled">Расчет результатов по БГ (средней скорости и средней ошибочности)</span></div>
		<div><span class="disabled">Расчет результатов по Биатлону</span></div>
		<div><span class="disabled">Расчет результатов по Октатлону</span></div>
		<div><a href="./supermanOrderResults.jsp?competitionId=<%=request.getParameter("competitionId")%>">Расчет результатов по Ордену Суперменов</a></div>
	</div>

	<div class="bottomLinks">
		<a href="./competitionsList.jsp">Вернуться к списку соревнований</a>
	</div>
</div>

<script type="text/javascript">
	var competitionId = '<%=request.getParameter("competitionId")%>';

	var contextPath = '<%=request.getContextPath()%>';
	var competitionBasicInfoGetUrl = contextPath + '/competitionBasicInfoGet';

	var roundPageUrl = contextPath + '/round.jsp';

	var TABLE_STYLE = 'results';
	var TD_ALIGN_RIGHT_STYLE = 'right';
	var PROFILE_IMG_URL = './img/profile2.png';
	var PROFILE_IMG_WIDTH = '11';
	var PROFILE_IMG_HEIGHT = '16';


	var NAME_CONTAINER_ID = 'name-container';

	var ROUNDS_COUNT_CONTAINER_ID = 'roundsCount-container';

	var DICTIONARIES_CONTAINER_TOGGLE_LINK_ID = 'dictionaries-toggle-link';
	var DICTIONARIES_CONTAINER_ID = 'dictionaries-container';

	var PLAYERS_CONTAINER_TOGGLE_LINK_ID = 'players-toggle-link';
	var PLAYERS_CONTAINER_ID = 'players-container';

	var ROUNDS_CONTAINER_TOGGLE_LINK_ID = 'rounds-toggle-link';
	var ROUNDS_CONTAINER_ID = 'rounds-container';

	var RESULTS_CONTAINER_TOGGLE_LINK_ID = 'results-toggle-link';
	var RESULTS_CONTAINER_ID = 'results-container';

	var dh = DomHelper;

	function getRoundLink(roundNumber) {
		return roundPageUrl + '?competitionId=' + competitionId + '&roundNumber=' + roundNumber;
	}

	function appendCompetitionName(basicInfo) {
		var container = dh.getEl(NAME_CONTAINER_ID);
		dh.removeChildrenHierarchy(container);

		container.appendChild( dh.createTN(basicInfo.name) );
	}
	function appendRoundsCount(basicInfo) {
		var container = dh.getEl(ROUNDS_COUNT_CONTAINER_ID);
		dh.removeChildrenHierarchy(container);

		container.appendChild( dh.createTN(basicInfo.roundsCount) );
	}
	function appendDictionaries(basicInfo) {
		var container = dh.getEl(DICTIONARIES_CONTAINER_ID);
		dh.removeChildrenHierarchy(container);

		var table = dh.createEl(dh.TABLE_TAG, null, TABLE_STYLE);
		var tBody = dh.createEl(dh.TBODY_TAG);

		// header
		var headerTr = dh.createEl(dh.TR_TAG);
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Название') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Количество заездов') );

		tBody.appendChild(headerTr);

		// rows
		$.each(basicInfo.dictionaries, function(index, dictionary) {
			var tr = dh.createEl(dh.TR_TAG);

			var nameTd = dh.createEl(dh.TD_TAG);
			var nameSpan = dh.createEl(dh.SPAN_TAG, null, null, dictionary.dictionaryName);
			nameSpan.setAttribute(dh.STYLE_ATTRIBUTE, ('color: ' + dictionary.dictionaryColor + ';') );
			nameTd.appendChild(nameSpan);

			if (dictionary.dictionaryLink)
			{
				var link = dh.createEl(dh.A_TAG);
				link.setAttribute(dh.HREF_ATTRIBUTE, dictionary.dictionaryLink);

				var linkImg = dh.createEl(dh.IMG_TAG);
				linkImg.setAttribute(dh.SRC_ATTRIBUTE, PROFILE_IMG_URL);
				linkImg.setAttribute(dh.WIDTH_ATTRIBUTE, PROFILE_IMG_WIDTH);
				linkImg.setAttribute(dh.HEIGHT_ATTRIBUTE, PROFILE_IMG_HEIGHT);
				link.appendChild(linkImg);

				nameTd.appendChild( dh.createTN('\u00a0') );
				nameTd.appendChild(link);
			}

			var roundsCountTd = dh.createEl(dh.TD_TAG, null, null, dictionary.roundsCount);

			tr.appendChild(nameTd);
			tr.appendChild(roundsCountTd);

			tBody.appendChild(tr);
		});


		table.appendChild(tBody);
		container.appendChild(table);
	}

	function appendRounds(basicInfo) {
		var container = dh.getEl('rounds-container');
		dh.removeChildrenHierarchy(container);

		var table = dh.createEl(dh.TABLE_TAG, null, TABLE_STYLE);
		var tBody = dh.createEl(dh.TBODY_TAG);

		// header
		var headerTr = dh.createEl(dh.TR_TAG);
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Номер') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Номер в словаре') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Словарь') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Время начала') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Количество доехавших игроков (включая гостей)') ); // todo: move 'Включая гостей' to hint

		table.appendChild(headerTr);

		// rows
		$.each(basicInfo.rounds, function(index, round) {
			var trClass = (index % 2 == 1) ? 'odd': 'even';
			var tr = dh.createEl(dh.TR_TAG, null, trClass);

			var numberTd = dh.createEl(dh.TD_TAG);
			var numberLink = dh.createEl(dh.A_TAG, null, null, round.number);
			numberLink.setAttribute(dh.HREF_ATTRIBUTE, getRoundLink(round.number));
			numberTd.appendChild(numberLink);

			var numberInDictionaryTd = dh.createEl(dh.TD_TAG, null, null, round.numberInDictionary);

			var dictionaryTd = dh.createEl(dh.TD_TAG);
			var dictionarySpan = dh.createEl(dh.SPAN_TAG, null, null, round.dictionaryName);
			dictionarySpan.setAttribute(dh.STYLE_ATTRIBUTE, ('color: ' + round.dictionaryColor + ';'));
			dictionaryTd.appendChild(dictionarySpan);

			var beginTimeTd = dh.createEl(dh.TD_TAG, null, null, round.beginTimeStr);

			var finishedPlayersCountTd = dh.createEl(dh.TD_TAG, null, TD_ALIGN_RIGHT_STYLE, round.finishedPlayersCount);

			tr.appendChild(numberTd);
			tr.appendChild(numberInDictionaryTd);
			tr.appendChild(dictionaryTd);
			tr.appendChild(beginTimeTd);
			tr.appendChild(finishedPlayersCountTd);

			tBody.appendChild(tr);
		});

		table.appendChild(tBody);
		container.appendChild(table);
	}

	function appendPlayers(basicInfo) {
		var container = dh.getEl('players-container');
		dh.removeChildrenHierarchy(container);

		var table = dh.createEl(dh.TABLE_TAG, null, TABLE_STYLE);
		var tBody = dh.createEl(dh.TBODY_TAG);

		// header
		var headerTr = dh.createEl(dh.TR_TAG);
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Имя') );

		$.each(basicInfo.dictionaries, function(index, dictionary) {
			var dictionaryTh = dh.createEl(dh.TH_TAG, null, null);
			dictionaryTh.appendChild( dh.createTN('Заездов по словарю') );
			dictionaryTh.appendChild( dh.createEl(dh.BR_TAG) );
			dictionaryTh.appendChild( dh.createTN('\u00AB' + dictionary.dictionaryName + '\u00BB') ); // todo: use dicitonary color here

			headerTr.appendChild( dictionaryTh );
		});

		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Общее количество заездов') );

		tBody.appendChild(headerTr);

		// row
		$.each(basicInfo.players, function(index, player) {
			var trClass = (index % 2 == 1) ? 'odd': 'even';
			var tr = dh.createEl(dh.TR_TAG, null, trClass);

			var nameTd = dh.createEl(dh.TD_TAG);
			var nameSpan = dh.createEl(dh.SPAN_TAG, null, null, player.name);
			nameSpan.setAttribute(dh.STYLE_ATTRIBUTE, ('color: ' + player.rankColor + ';'));

			var profileLink = dh.createEl(dh.A_TAG);
			profileLink.setAttribute(dh.HREF_ATTRIBUTE, player.profileLink);

			var profileImg = dh.createEl(dh.IMG_TAG);
			profileImg.setAttribute(dh.SRC_ATTRIBUTE, PROFILE_IMG_URL);
			profileImg.setAttribute(dh.WIDTH_ATTRIBUTE, PROFILE_IMG_WIDTH);
			profileImg.setAttribute(dh.HEIGHT_ATTRIBUTE, PROFILE_IMG_HEIGHT);
			profileLink.appendChild(profileImg);

			nameTd.appendChild(nameSpan);
			nameTd.appendChild( dh.createTN('\u00a0') );
			nameTd.appendChild(profileLink);

			tr.appendChild(nameTd);

			// dictionaries counts
			$.each(basicInfo.dictionaries, function(index, dictionary) {
				var count = player.dictionariesRoundsCount[dictionary.dictionaryCode];
				var dictionaryCountTd = dh.createEl(dh.TD_TAG, null, null, count);
				tr.appendChild(dictionaryCountTd);
			});

			var totalRoundsCountTd = dh.createEl(dh.TD_TAG, null, null, player.totalRoundsCount);
			tr.appendChild(totalRoundsCountTd);

			tBody.appendChild(tr);
		});

		table.appendChild(tBody);
		container.appendChild(table);
	}

	function appendBasicInfo(basicInfo) {
		appendCompetitionName(basicInfo);
		appendRoundsCount(basicInfo);
		appendDictionaries(basicInfo);
		appendRounds(basicInfo);
		appendPlayers(basicInfo);
	}
	function loadBasicInfo() {
		jQuery.ajax({
				url: competitionBasicInfoGetUrl
			, type: 'POST'
			, dataType: 'json'
			, data: { competitionId: competitionId }
			, success: function(response) {
				if (response.success)
				{
					appendBasicInfo(response);
				}
				else
				{
					if (response && response.msg)
						alert('Произошла ошибка при получении базовой информации о соревновании:' + response.msg);
					else
						alert('Произошла ошибка при получении базовой информации о соревновании.');
				}
			}
		});
	}

	function bindShowHideLink(linkId, divId) {
		var divSelector = '#' + divId;
		$(divSelector).hide(); // default div is hidden

		$('#' + linkId).click(function() {
			$(divSelector).toggle();

			var link = dh.getEl(linkId);
			dh.removeChildrenHierarchy(link);

			if ( $(divSelector).is(":hidden") )
			{
				link.appendChild( dh.createTN('Показать') );
			}
			else
			{
				link.appendChild( dh.createTN('Скрыть') );
			}
		});
	}
	function bindShowHideLinks() {
		bindShowHideLink(DICTIONARIES_CONTAINER_TOGGLE_LINK_ID, DICTIONARIES_CONTAINER_ID);
		bindShowHideLink(ROUNDS_CONTAINER_TOGGLE_LINK_ID, ROUNDS_CONTAINER_ID);
		bindShowHideLink(PLAYERS_CONTAINER_TOGGLE_LINK_ID, PLAYERS_CONTAINER_ID);
		bindShowHideLink(RESULTS_CONTAINER_TOGGLE_LINK_ID, RESULTS_CONTAINER_ID);
	}

	$(function() {
		if ( !competitionId )
		{
			alert('Необходимо задать параметр \"competitionId\"');
			return;
		}

		bindShowHideLinks();
		loadBasicInfo();
	});
</script>
</body>
</html>