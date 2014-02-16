<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Количество ошибок игроков по заездам</title>
	<%@ include file="./headerInclude.jspf" %>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/kgparser.DomHelper.js"></script>

	<script src="./js/Highcharts-3.0.9/js/highcharts.js"></script>
	<script src="./js/Highcharts-3.0.9/js/modules/exporting.js"></script>
</head>
<body>
<div id="wrapper">
	<h2>График количеств ошибок игроков по заездам</h2>

	<div id="chart-container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	<div id="ranks-container" style="border: 1px solid gray; padding: 10px; margin-bottom: 10px;">
		<div>Фильтр по рангам</div>
		<div id="ranks-checkboxes-container" style="margin-bottom: 5px;"></div>
		<div>
			<input type="button" value="Отфильтровать ранги" onclick="filterRanks();"/>
		</div>
	</div>
	<input type="button" value="Скрыть всех" onclick="hideAllSeries();"/>
	&nbsp;&nbsp;
	<input type="button" value="Показать всех" onclick="showAllSeries();"/>

	<div class="bottomLinks">
		<a href="./competition.jsp?competitionId=<%=request.getParameter("competitionId")%>">Вернуться к странице соревнования</a>
	</div>
</div>

<script type="text/javascript">
	var competitionId = '<%=request.getParameter("competitionId")%>';

	var contextPath = '<%=request.getContextPath()%>';
	var errorsCountChartValuesGetUrl = contextPath + '/errorsCountChartValuesGet';

	var dh = DomHelper;
	var rankCheckboxesId;

	var CHART_CONTAINER_ID = 'chart-container';
	var RANKS_CHECKBOXES_CONTAINER_ID = 'ranks-checkboxes-container';

	var namesToRanks;

	function fillNamesToRanks(data) {
		namesToRanks = {};

		$.each(data.series, function(index, series) {
			namesToRanks[ series.name ] = series.rank;
		});
	}

	function getRankCheckboxId(rank) {
		return rank.name + '-rank-checkbox';
	}
	function getRankByCheckboxId(checkboxId) {
		return checkboxId.substring(0, (checkboxId.length - '-rank-checkbox'.length));
	}
	function fillRanks(data) {
		var containerDom = dh.getEl(RANKS_CHECKBOXES_CONTAINER_ID);

		rankCheckboxesId = [];

		$.each(data.ranks, function(index, rank) {
			var checkboxId = getRankCheckboxId(rank);
			var checkbox = dh.createEl(dh.INPUT_TAG, checkboxId);
			checkbox.setAttribute(dh.TYPE_ATTRIBUTE, dh.CHECKBOX_TYPE_ATTRIBUTE_VALUE);

			containerDom.appendChild(checkbox);

			var label = dh.createEl(dh.LABEL_TAG, null, null, rank.displayName);
			label.setAttribute(dh.STYLE_ATTRIBUTE, ('color: ' + rank.color + ';'));
			label.setAttribute(dh.FOR_ATTRIBUTE, checkboxId);
			containerDom.appendChild(label);

			containerDom.appendChild( dh.createTN('\u00a0\u00a0\u00a0')); // space between checkboxes

			rankCheckboxesId.push(checkboxId);
		});
	}

	function initChart(data) {
		$('#' + CHART_CONTAINER_ID).highcharts({
			title: {
					text: data.competitionName
				, x: -20 // center
			}
			, subtitle: {
					text: 'Количество ошибок игроков по заездам'
				, x: -20
			}
			, xAxis: {
					categories: data.categories
			}
			, yAxis: {
				title: {
						text: 'Количество ошибок (шт.)'
				},
				plotLines: [{
						value: 0
					, width: 1
					, color: '#808080'
				}]
			}
			, tooltip: {
					valueSuffix: ' ош.' // todo: set tooltip value depending of errors count
			}
			, legend: {
					layout: 'vertical'
				, align: 'right'
				, verticalAlign: 'middle'
				, borderWidth: 0
			},

			series: data.series
		});
	}
	function getChart() {
		return Highcharts.charts[0];
	}

	function hideAllSeries() {
		var chart = getChart();
		$.each(chart.series, function(index, series) {
			series.setVisible(false, false); // see http://stackoverflow.com/questions/16625423/how-can-i-hide-all-the-series-in-highcharts-at-a-time
		});

		chart.redraw();
	}
	function showAllSeries() {
		var chart = getChart();
		$.each(chart.series, function(index, series) {
			series.setVisible(true, false); // 2nd false -> do not redraw
		});

		chart.redraw();
	}

	function filterRanks() {
		var ranks = [];
		$.each(rankCheckboxesId, function(index, checkboxId) {
			var checkbox = dh.getEl(checkboxId);
			if (checkbox.checked)
			{
				ranks.push( getRankByCheckboxId(checkboxId) )
			}
		});

		hideAllSeries(); // force redrawing selected ranks from clean chart

		var chart = getChart();
		chart.xAxis[0].update({}, true); // force axis redraw
		chart.yAxis[0].update({}, true); // force axis redraw

		$.each(chart.series, function(index, series) {
			var playerName = series.name;

			var playerRank = namesToRanks[playerName];
			if (playerRank && (ranks.indexOf(playerRank) >= 0) )
			{ // player rank is in selected ranks -> show series
				series.setVisible(true, false);
			}
			else
			{ // player rank is in selected ranks -> hide series
				series.setVisible(false, false);
			}
		});

		chart.redraw();
	}

	function loadChartValues() {
		jQuery.ajax({
				url: errorsCountChartValuesGetUrl
			, type: 'POST'
			, dataType: 'json'
			, data: { competitionId: competitionId }
			, success: function(response) {
				if (response.success)
				{
					initChart(response);
					fillNamesToRanks(response);
					fillRanks(response);
				}
				else
				{
					if (response && response.msg)
						alert('Произошла ошибка при получении данных для графика количеств ошибок игроков:' + response.msg);
					else
						alert('Произошла ошибка при получении данных для графика количеств ошибок  игроков.');
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

		loadChartValues();
	});
</script>
</body>
</html>