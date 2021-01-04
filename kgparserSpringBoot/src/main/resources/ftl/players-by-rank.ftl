<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Действующие игроки по рангам</title>
    <#include "./styles.ftl">
</head>
<body>
<#include "./header.ftl">
<main>
    <div class="section">
        <h2>Действующие игроки по рангам</h2>
        Учтены существующие незаблокированные игроки с минимум ${minTotalRacesCount} текстом пробега (всего игроков: ${totalPlayersCount})
        <br/>
        Mаксимальный пробег: ${maxTotalRacesCount}
    </div>

    <div>
        Выбрать игроков с общим пробегом
        <label for="min-totalRacesCount-input">от</label>&nbsp;
        <input id="min-totalRacesCount-input" autofocus value="${minTotalRacesCount}"/>
        <label for="max-totalRacesCount-input">до</label>&nbsp;
        <input id="max-totalRacesCount-input" value="${maxTotalRacesCount}"/>
        <button id="search-button" class="search-button">Выбрать</button>
    </div>

    <div class="section" id="section-players_by_rank">
        <div id="playersByRank-single-chart-wrapper" class="flexWrap">
            <canvas id="playersByRank-single-barChart-canvas" class="chart-left"></canvas>
            <canvas id="playersByRank-single-doughnutChart-canvas" class="chart-right"></canvas>

            <div id="playersByRank-single-table-container"></div>
        </div>
    </div>
</main>

<!-- see https://www.chartjs.org/docs/latest/getting-started/installation.html -->
<script src="${links.chartJs}" integrity="${links.chartJsIntegrity}" crossorigin="anonymous"></script>
<script src="${links.playersByRankChartJs}"></script>
<script src="${links.playersByRankDataJs}"></script>
<script>
    const MIN_TOTAL_RACES_COUNT = ${minTotalRacesCount};
    const MAX_TOTAL_RACES_COUNT = ${maxTotalRacesCount};

    let singleChart;

    window.addEventListener('load', function() {
        const countsByRank = PlayerByRankFilter.groupByRank(STATS_DATA.rankToTotalRacesCount);
        const chartData = PlayerByRankFilter.convertToChartData(countsByRank);

        singleChart = new PlayersByRankChart({
            data: chartData, // by default, show players with at least 1 race
            label: PlayerByRankFilter.getChartLabel(MIN_TOTAL_RACES_COUNT, MIN_TOTAL_RACES_COUNT, MAX_TOTAL_RACES_COUNT)
        });
        singleChart.append('playersByRank-single-barChart-canvas', 'playersByRank-single-doughnutChart-canvas', 'playersByRank-single-table-container');

        const filter = new PlayerByRankFilter({
            minRacesInputId: 'min-totalRacesCount-input',
            maxRacesInputId: 'max-totalRacesCount-input',
            searchButtonId: 'search-button',
            minTotalRacesCount: MIN_TOTAL_RACES_COUNT,
            chart: singleChart,
            data: STATS_DATA.rankToTotalRacesCount
        });

        filter.bindSearch();
    });
</script>
</body>
</html>
