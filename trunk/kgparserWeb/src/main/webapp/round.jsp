<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Страница заезда</title>
	<%@ include file="./headerInclude.jspf" %>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/kgparser.DomHelper.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
</head>
<body>
<div id="wrapper">
	<h2>Страница заезда</h2>

	<h3>Соревнование</h3>
	<div id="competitionName-container"></div>

	<h3>Номер</h3>
	<div id="number-container"></div>

	<h3>Номер в словаре</h3>
	<div id="numberInDictionary-container"></div>

	<h3>Словарь</h3>
	<div id="dictionaryInfo-container"></div>

	<h3>Время начала</h3>
	<div id="beginTime-container"></div>

	<h3>Текст</h3>
	<div id="text-container"></div>

	<div id="bookInfo-container">
		<h3>Автор книги</h3>
		<div id="bookAuthor-container"></div>

		<h3>Название книги</h3>
		<div id="bookName-container"></div>
	</div>

	<h3>Результаты игроков (включая гостей)&nbsp;<a class="toggle" id="players-toggle-link">Показать</a></h3>
	<div id="players-container"></div>

	<div class="bottomLinks">
		<div id="neighbour-rounds-container" class="bottomLinksRow">
			<span id="previousRound-link-container"></span>
			&nbsp;&nbsp;
			<span id="nextRound-link-container"></span>
		</div>
		<div>
			<a href="./competition.jsp?competitionId=<%=request.getParameter("competitionId")%>">Вернуться к странице соревнования</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var competitionId = '<%=request.getParameter("competitionId")%>';
	var roundNumber = '<%=request.getParameter("roundNumber")%>';

	CONTEXT_PATH = '<%=request.getContextPath()%>';
	var roundInfoGetUrl = CONTEXT_PATH + '/roundInfoGet';

	var COMPETITION_NAME_CONTAINER_ID = 'competitionName-container';
	var NUMBER_CONTAINER_ID = 'number-container';
	var NUMBER_IN_DICTIONARY_CONTAINER_ID = 'numberInDictionary-container';
	var DICTIONARY_INFO_CONTAINER_ID = 'dictionaryInfo-container';
	var BEGIN_TIME_CONTAINER_ID = 'beginTime-container';
	var TEXT_CONTAINER_ID = 'text-container';

	var BOOK_INFO_CONTAINER_ID = 'bookInfo-container';
	var BOOK_AUTHOR_CONTAINER_ID = 'bookAuthor-container';
	var BOOK_NAME_CONTAINER_ID = 'bookName-container';

	var PLAYERS_CONTAINER_TOGGLE_LINK_ID = 'players-toggle-link';
	var PLAYERS_CONTAINER_ID = 'players-container';

	var NEIGHBOUR_ROUNDS_CONTAINER_ID = 'neighbour-rounds-container';
	var PREVIOUS_ROUND_LINK_CONTAINER_ID = 'previousRound-link-container';
	var NEXT_ROUND_LINK_CONTAINER_ID = 'nextRound-link-container';

	var PREVIOUS_ROUND_LINK_TEXT = 'К предыдущему заезду';
	var NEXT_ROUND_LINK_TEXT = 'К следующему заезду';

	var dh = DomHelper;

	function appendDictionaryInfo(round) {
		var dictionaryInfoContainer = dh.getEl(DICTIONARY_INFO_CONTAINER_ID);
		dh.removeChildrenHierarchy(dictionaryInfoContainer);

		var dictionary = round.dictionary;
		var dictionarySpan = dh.createEl(dh.SPAN_TAG, null, null, dictionary.dictionaryName);
		dictionarySpan.setAttribute(dh.STYLE_ATTRIBUTE, ('color: ' + dictionary.dictionaryColor + ';'));

		dictionaryInfoContainer.appendChild(dictionarySpan);

		if (dictionary.dictionaryLink)
		{
			var link = dh.createEl(dh.A_TAG);
			link.setAttribute(dh.HREF_ATTRIBUTE, dictionary.dictionaryLink);

			var linkImg = dh.createEl(dh.IMG_TAG);
			linkImg.setAttribute(dh.SRC_ATTRIBUTE, PROFILE_IMG_URL);
			linkImg.setAttribute(dh.WIDTH_ATTRIBUTE, PROFILE_IMG_WIDTH);
			linkImg.setAttribute(dh.HEIGHT_ATTRIBUTE, PROFILE_IMG_HEIGHT);
			link.appendChild(linkImg);

			dictionaryInfoContainer.appendChild(dh.createTN('\u00a0')); // &nbsp;
			dictionaryInfoContainer.appendChild(link);
		}
	}
	function appendBasicInfo(round) {
		appendText(COMPETITION_NAME_CONTAINER_ID, round.competitionName);
		appendText(NUMBER_CONTAINER_ID, round.number);
		appendText(NUMBER_IN_DICTIONARY_CONTAINER_ID, round.numberInDictionary);

		appendText(BEGIN_TIME_CONTAINER_ID, round.beginTimeStr);
		appendText(TEXT_CONTAINER_ID, round.text);

		if (round.bookAuthor && round.bookName)
		{ // book info present -> fill book fields
			appendText(BOOK_AUTHOR_CONTAINER_ID, round.bookAuthor);
			appendText(BOOK_NAME_CONTAINER_ID, round.bookName);
		}
		else
		{ // book info is not present -> hide book div
			$('#' + BOOK_INFO_CONTAINER_ID).hide();
		}
	}

	function appendPlayers(round) {
		var container = dh.getEl(PLAYERS_CONTAINER_ID);
		dh.removeChildrenHierarchy(container);

		var table = dh.createEl(dh.TABLE_TAG, null, RESULTS_TABLE_CLASS);
		var tBody = dh.createEl(dh.TBODY_TAG);

		// header
		var headerTr = dh.createEl(dh.TR_TAG);
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Место') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Игрок') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Скорость') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Набрано символов') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Количество ошибок') );
		headerTr.appendChild( dh.createEl(dh.TH_TAG, null, null, 'Процент ошибок') );

		tBody.appendChild(headerTr);

		// rows
		$.each(round.playerResults, function(index, result) {
			var trClass = (index % 2 == 1) ? 'odd': 'even';
			var tr = dh.createEl(dh.TR_TAG, null, trClass);

			var placeTd = dh.createEl(dh.TD_TAG, null, null, result.place);

			var player = result.player;
			var playerTd = dh.createEl(dh.TD_TAG);
			var nameSpan = dh.createEl(dh.SPAN_TAG, null, null, player.name);
			nameSpan.setAttribute(dh.STYLE_ATTRIBUTE, ('color: ' + player.rankColor + ';'));
			playerTd.appendChild(nameSpan);

			if ( !player.guest )
			{
				var profileLink = dh.createEl(dh.A_TAG);
				profileLink.setAttribute(dh.HREF_ATTRIBUTE, player.profileLink);

				var profileImg = dh.createEl(dh.IMG_TAG);
				profileImg.setAttribute(dh.SRC_ATTRIBUTE, PROFILE_IMG_URL);
				profileImg.setAttribute(dh.WIDTH_ATTRIBUTE, PROFILE_IMG_WIDTH);
				profileImg.setAttribute(dh.HEIGHT_ATTRIBUTE, PROFILE_IMG_HEIGHT);
				profileLink.appendChild(profileImg);

				playerTd.appendChild( dh.createTN('\u00a0') );
				playerTd.appendChild(profileLink);
			}

			var speedTd = dh.createEl(dh.TD_TAG, null, null, result.speed);
			var charsTotalTd = dh.createEl(dh.TD_TAG, null, null, result.charsTotal);
			var errorsCountTd = dh.createEl(dh.TD_TAG, null, null, result.errorsCountStr);
			var errorsPercentageTd = dh.createEl(dh.TD_TAG, null, null, result.errorPercentageStr);

			tr.appendChild(placeTd);
			tr.appendChild(playerTd);
			tr.appendChild(speedTd);
			tr.appendChild(charsTotalTd);
			tr.appendChild(errorsCountTd);
			tr.appendChild(errorsPercentageTd);

			tBody.appendChild(tr);
		});

		table.appendChild(tBody);
		container.appendChild(table);
	}
	function appendRoundInfo(round) {
		appendBasicInfo(round);
		appendDictionaryInfo(round);
		appendPlayers(round);
	}

	function appendNeighbourRoundsLinks(round) {
		var previousRoundContainer = dh.getEl(PREVIOUS_ROUND_LINK_CONTAINER_ID);
		dh.removeChildrenHierarchy(previousRoundContainer);

		if (round.hasPreviousRound)
		{
			var previousRoundLink = dh.createEl(dh.A_TAG, null, null, PREVIOUS_ROUND_LINK_TEXT);
			previousRoundLink.setAttribute( dh.HREF_ATTRIBUTE, getRoundPageLink(competitionId, round.previousRoundNumber) );
			previousRoundContainer.appendChild(previousRoundLink);
		}
		else
		{
			var previousRoundDisabledSpan = dh.createEl(dh.SPAN_TAG, null, DISABLED_CLASS, PREVIOUS_ROUND_LINK_TEXT);
			previousRoundContainer.appendChild(previousRoundDisabledSpan);
		}

		var nextRoundContainer = dh.getEl(NEXT_ROUND_LINK_CONTAINER_ID);
		dh.removeChildrenHierarchy(nextRoundContainer);

		if (round.hasNextRound)
		{
			var nextRoundLink = dh.createEl(dh.A_TAG, null, null, NEXT_ROUND_LINK_TEXT);
			nextRoundLink.setAttribute( dh.HREF_ATTRIBUTE, getRoundPageLink(competitionId, round.nextRoundNumber) );
			nextRoundContainer.appendChild(nextRoundLink);
		}
		else
		{
			var nextRoundDisabledSpan = dh.createEl(dh.SPAN_TAG, null, DISABLED_CLASS, NEXT_ROUND_LINK_TEXT);
			nextRoundContainer.appendChild(nextRoundDisabledSpan);
		}
	}

	function loadRoundInfo() {
		jQuery.ajax({
			  url: roundInfoGetUrl
			, type: 'POST'
			, dataType: 'json'
			, data: {
				  competitionId: competitionId
				, roundNumber: roundNumber
			}
			, success: function(response) {
				if (response.success)
				{
					appendRoundInfo(response);
					appendNeighbourRoundsLinks(response);
				}
				else
				{
					if (response && response.msg)
						alert('Произошла ошибка при получении информации о заезде:' + response.msg);
					else
						alert('Произошла ошибка при получении информации о заезде.');
				}
			}
		});
	}

	function bindShowHideLinks() {
		bindShowHideLink(PLAYERS_CONTAINER_TOGGLE_LINK_ID, PLAYERS_CONTAINER_ID);
	}

	$(function() {
		if ( !competitionId )
		{
			alert('Необходимо задать параметр \"competitionId\"');
			return;
		}
		if ( !roundNumber )
		{
			alert('Необходимо задать параметр \"roundNumber\"');
			return;
		}

		bindShowHideLinks();
		loadRoundInfo();
	});
</script>
</body>
</html>