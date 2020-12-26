class PlayersByRankChartSwitcher {
    static SPACE_SEPARATOR = '&nbsp;&nbsp;&nbsp;';
    static COMBINED_CHARTS = 'COMBINED_CHARTS';

    constructor(config) {
        this.config = config;
    }

    append(containerId) {
        let switcherHtml = '';

        switcherHtml += `Общий пробег${PlayersByRankChartSwitcher.SPACE_SEPARATOR}`

        this.config.containers.forEach(container => {
            switcherHtml += this.getPageNumberHtml(containerId, container.minTotalRacesCount);
        });

        // append "Show all combined" link
        switcherHtml += this.getCombinedHtml(containerId);

        document.getElementById(containerId).innerHTML = switcherHtml;
        this.bindLinks(containerId);
        this.bindShowAllLink(containerId);
    }

    getPageNumberHtml(containerId, minTotalRacesCount) {
        if (minTotalRacesCount === this.config.currentChart) {
            return `${minTotalRacesCount}+${PlayersByRankChartSwitcher.SPACE_SEPARATOR}`;
        }

        const linkId = this.getLinkId(containerId, minTotalRacesCount);

        return this.getSwitchLink(linkId, minTotalRacesCount);
    }

    getCombinedHtml(containerId) {
        if (this.config.currentChart === PlayersByRankChartSwitcher.COMBINED_CHARTS) {
            return `Все сразу`;
        }

        const linkId = this.getLinkId(containerId, PlayersByRankChartSwitcher.COMBINED_CHARTS);

        return `<a href="javascript:void(0);" id="${linkId}">Все сразу</a>`; // no separator on the last link
    }

    getSwitchLink(linkId, minTotalRacesCount) {
        return `<a href="javascript:void(0);" id="${linkId}">${minTotalRacesCount}+</a>${PlayersByRankChartSwitcher.SPACE_SEPARATOR}`;
    }

    getLinkId(containerId, minTotalRacesCount) {
        return `${containerId}-playersByRankChartSwitcher-${minTotalRacesCount}-link`;
    }

    bindLinks(containerId) {
        this.config.containers.forEach(container => {
            if (container.minTotalRacesCount === this.config.currentChart) { // current position is not a link
                return;
            }

            const linkId = this.getLinkId(containerId, container.minTotalRacesCount);
            const link = document.getElementById(linkId);

            const switcher = this;

            link.onclick = function () {
                switcher.config.currentChart = container.minTotalRacesCount;
                switcher.append(containerId);
                switcher.switchToCurrentContainer();
            }
        });
    }

    bindShowAllLink(containerId) {
        // Bind "Show all" link
        if (PlayersByRankChartSwitcher.COMBINED_CHARTS === this.config.currentChart) { // current position is not a link
            return;
        }

        const linkId = this.getLinkId(containerId, PlayersByRankChartSwitcher.COMBINED_CHARTS);
        const link = document.getElementById(linkId);

        const switcher = this;

        link.onclick = function () {
            switcher.config.currentChart = PlayersByRankChartSwitcher.COMBINED_CHARTS;
            switcher.append(containerId);
            switcher.switchToCurrentContainer();
        }
    }

    switchToCurrentContainer() {
        if (this.config.currentChart === PlayersByRankChartSwitcher.COMBINED_CHARTS) {
            // hide single container, show combined container
            this.hideContainer(this.config.singleChartContainerId);
            this.showContainer(this.config.combinedChartsContainerId);
        }
        else {
            // hide combined container, show single container
            this.hideContainer(this.config.combinedChartsContainerId);
            this.showContainer(this.config.singleChartContainerId);
        }

        // single chart is updated with the different data
        this.config.containers.forEach(container => {
            if (container.minTotalRacesCount === this.config.currentChart) {
                this.config.singleChart.update({
                    data: container.data,
                    label: container.label
                });
            }
        });
    }

    hideContainer(containerId) {
        document.getElementById(containerId).classList.add('hidden');
    }

    showContainer(containerId) {
        document.getElementById(containerId).classList.remove('hidden');
    }
}

class PlayerByRankFilter {
    // index of field within 2D array of player data
    static RANK_INDEX = 0;
    static TOTAL_RACES_COUNT_INDEX = 1;

    constructor(config) {
        this.config = config;
        // config.minRacesInputId
        // config.maxRacesInputId
        // config.searchButtonId
        // config.chart
        // config.data
        // config.minTotalRacesCount
    }

    bindSearch() {
        const minInput = document.getElementById(this.config.minRacesInputId);
        const maxInput = document.getElementById(this.config.maxRacesInputId);

        const thisScope = this;

        // todo: extract "bind on Enter" to some common JS, use in both Filter and Search
        const inputSearch = function(event) {
            if (event.key === 'Enter') {
                // console.log('Enter pressed in input field!');

                // Cancel the default action, if needed
                event.preventDefault();

                thisScope.doSearch();
            }
        };

        minInput.addEventListener("keyup", inputSearch)
        maxInput.addEventListener("keyup", inputSearch)

        const button = document.getElementById(this.config.searchButtonId);
        button.onclick = function() {
            thisScope.doSearch();
        };
    }

    doSearch() {
        const minInput = document.getElementById(this.config.minRacesInputId);
        const minValue = (minInput.value || '').trim();
        if ((minValue !== '') && !this.isPositiveInteger(minValue)) {
            alert('Минимальный пробег должен быть целым числом > 0.'); // todo: use a nice error display
            minInput.focus();
            return;
        }

        const maxInput = document.getElementById(this.config.maxRacesInputId);
        const maxValue = (maxInput.value || '').trim();
        if ((maxValue !== '') && !this.isPositiveInteger(maxValue)) {
            alert('Максимальный пробег должен быть целым числом > 0.'); // todo: use a nice error display
            maxInput.focus();
            return;
        }

        const minValueInt = parseInt(minValue);
        const maxValueInt = parseInt(maxValue);

        if (minValueInt && maxValueInt && (minValueInt > maxValueInt)) {
            alert('Минимальный пробег должен быть <= максимальному пробегу.');
            maxInput.focus();
            return;
        }

        // console.log(`Performing search with ${minValueInt} <= totalRacesCount <= ${maxValueInt}`);
        this.updateChartData(this.config.data, minValueInt, maxValueInt);
    }

    isPositiveInteger(s) {
        const result = parseInt(s);
        return !isNaN(result) && (result > 0);
    }

    updateChartData(players, minTotalRacesCount, maxTotalRacesCount) {
        const playersWithGivenTotalRacesCount = this.filter(this.config.data, minTotalRacesCount, maxTotalRacesCount);
        // console.log(`Players filtered by minRacesCount = ${minTotalRacesCount}, maxRacesCount = ${maxTotalRacesCount}:`);
        // console.log(playersWithGivenTotalRacesCount);

        const countsByRank = PlayerByRankFilter.groupByRank(playersWithGivenTotalRacesCount);
        const chartData = PlayerByRankFilter.convertToChartData(countsByRank);
        // console.log('converted to chart data format:')
        // console.log(chartData);

        this.config.chart.update({
            data: chartData,
            label: PlayerByRankFilter.getChartLabel(this.config.minTotalRacesCount, minTotalRacesCount, maxTotalRacesCount)
        });
    }

    static getChartLabel(firstTotalRacesCount, minTotalRacesCount, maxTotalRacesCount) {
        if (!minTotalRacesCount && !maxTotalRacesCount) {
            return `Действующие игроки по рангам (общий пробег ${firstTotalRacesCount}+)`;
        }

        if (minTotalRacesCount && !maxTotalRacesCount) {
            return `Действующие игроки по рангам (общий пробег ${minTotalRacesCount}+)`;
        }

        if (!minTotalRacesCount && maxTotalRacesCount) {
            return `Действующие игроки по рангам (общий пробег ${firstTotalRacesCount}–${maxTotalRacesCount}`;
        }

        if (minTotalRacesCount && maxTotalRacesCount) {
            return `Действующие игроки по рангам (общий пробег ${minTotalRacesCount}–${maxTotalRacesCount})`;
        }
    }

    filter(players, minTotalRacesCount, maxTotalRacesCount) {
        if (minTotalRacesCount && maxTotalRacesCount && (minTotalRacesCount > maxTotalRacesCount)) {
            alert('Минимальный пробег должен быть <= максимальному пробегу.');
            return;
        }

        let filterFunction = this.getFilterFunction(minTotalRacesCount, maxTotalRacesCount);

        return players.filter(player => {
            const totalRacesCount = player[PlayerByRankFilter.TOTAL_RACES_COUNT_INDEX];

            return filterFunction(totalRacesCount);
        });
    }

    getFilterFunction(minTotalRacesCount, maxTotalRacesCount) {
        if (!minTotalRacesCount && !maxTotalRacesCount) {
            return totalRacesCount => true;
        }

        if (minTotalRacesCount && !maxTotalRacesCount) {
            return totalRacesCount => (minTotalRacesCount <= totalRacesCount);
        }

        if (!minTotalRacesCount && maxTotalRacesCount) {
            return totalRacesCount => (totalRacesCount <= maxTotalRacesCount);
        }

        if (minTotalRacesCount && maxTotalRacesCount) {
            return totalRacesCount => (minTotalRacesCount <= totalRacesCount) && (totalRacesCount <= maxTotalRacesCount);
        }

        throw `Incorrect filter combination: minTotalRacesCount = ${minTotalRacesCount}, maxTotalRacesCount = ${maxTotalRacesCount}`;
    }

    static groupByRank(players) {
        const countByRank = players.reduce(function(result, player) {
            const rank = player[PlayerByRankFilter.RANK_INDEX];

            const currentCount = result[rank] || 0;
            result[rank] = currentCount + 1;
            return result;
        }, {});

        // console.log('countByRank:');
        // console.log(countByRank);

        return countByRank;
    }

    static convertToChartData(countsByRank) {
        const chartData = [];

        for (let [rankLevel, playersCount] of Object.entries(countsByRank)) {
            chartData.push({
                rankLevel: parseInt(rankLevel),
                rankName: PlayersByRankChart.RANK_NAMES[rankLevel],
                rankDisplayName: PlayersByRankChart.RANK_DISPLAY_NAMES[rankLevel],
                playersCount: playersCount
            })
        }

        return chartData;
    }
}

class PlayersByRankChart {
    static RANK_COLORS = {
        novice: '#8D8D8D',
        amateur: '#4F9A97',
        cabman: '#187818',
        pro: '#8C8100',
        racer: '#BA5800',
        maniac: '#BC0143',
        superman: '#5E0B9E',
        cyberracer: '#00037C',
        extracyber: '#061956'
    };

    static RANK_NAMES = {
        1: 'novice',
        2: 'amateur',
        3: 'cabman',
        4: 'pro',
        5: 'racer',
        6: 'maniac',
        7: 'superman',
        8: 'cyberracer',
        9: 'extracyber'
    };

    static RANK_DISPLAY_NAMES = {
        // by rank level
        1: 'Новичок',
        2: 'Любитель',
        3: 'Таксист',
        4: 'Профи',
        5: 'Гонщик',
        6: 'Маньяк',
        7: 'Супермен',
        8: 'Кибергонщик',
        9: 'Экстракибер',

        // by Rank enum name
        novice: 'Новичок',
        amateur: 'Любитель',
        cabman: 'Таксист',
        pro: 'Профи',
        racer: 'Гонщик',
        maniac: 'Маньяк',
        superman: 'Супермен',
        cyberracer: 'Кибергонщик',
        extracyber: 'Экстракибер'
    };

    constructor(config) {
        this.config = config;
        // config.data - array of PlayersByRankCount
        // config.label
    }

    append(barChartCanvasId, doughnutChartCanvasId, tableContainerId) {
        this.fillChartConfigs();
        this.appendBarChart(barChartCanvasId);
        this.appendDoughnutChart(doughnutChartCanvasId);

        this.appendTable(tableContainerId);
    }

    update(config) {
        // console.log('update started. Config:' + JSON.stringify(config));

        this.config = config;
        this.fillChartConfigs();

        // update title
        this.barChart.options.title.text = this.config.label;
        this.doughnutChart.options.title.text = this.config.label;

        // update data
        this.barChart.config.data = this.getChartData(this.config);
        this.doughnutChart.config.data = this.getChartData(this.config);

        // reload same chart with new parameters
        this.barChart.update();
        this.doughnutChart.update();

        this.appendTable(this.tableContainerId);

        // console.log('update executed. I am: ' + this.config.label);
    }

    fillChartConfigs() {
        const data = this.getChartData(this.config);

        this.barChartConfig = {
            type: 'horizontalBar',
            data: data,
            options: {
                responsive: false,
                // responsive: true, // responsive: true is necessary when 2 diagrams horizontally, especially for big screens
                // maintainAspectRatio: true,
                title: {
                    display: true,
                    text: this.config.label
                },
                legend: { // do not display the legend of the dataset
                    display: false
                },
                scales: {
                    xAxes: [{
                        display: true,

                        ticks: {
                            // beginAtZero: 0,
                            // min: 0,
                            suggestedMin: 0
                        }
                    }],
                    yAxes: [{
                        display: true
                    }]
                }
            }
        };

        // same config, different diagram type, no scales
        // this.doughnutChartConfig = Object.assign({}, this.barChartConfig, { type: 'doughnut'}); // no, Object.assign does not make a copy, references stay the same!
        this.doughnutChartConfig = JSON.parse(JSON.stringify(this.barChartConfig));
        this.doughnutChartConfig.type = 'doughnut';
        this.doughnutChartConfig.options.scales = undefined; // no axes on doughnutChart!
    }

    getChartData(config) {
        const playersByRankChartLabels = config.data.map(playersByRankCount => playersByRankCount.rankDisplayName); // horizontal axes
        const playersByRankPlayersCount = config.data.map(playersByRankCount => playersByRankCount.playersCount); // data
        const playersByRankBackgroundColors = config.data.map(playersByRankCount => PlayersByRankChart.RANK_COLORS[playersByRankCount.rankName]); // background colors according to ranks

        return {
            label: config.label,
            labels: playersByRankChartLabels,
            datasets: [{
                data: playersByRankPlayersCount,
                backgroundColor: playersByRankBackgroundColors
            }],
        };
    }

    appendBarChart(canvasId) {
        this.barChartContainerId = canvasId;
        this.barChart = new Chart(canvasId, this.barChartConfig);
    }

    appendDoughnutChart(canvasId) {
        this.doughnutChartContainerId = canvasId;
        this.doughnutChart = new Chart(canvasId, this.doughnutChartConfig);
    }

    appendTable(containerId) {
        this.tableContainerId = containerId;

        const totalPlayers = this.config.data
            .map(playersByRankCount => playersByRankCount.playersCount)
            .reduce((prev, next) => prev + next, 0);

        let playersByRankTable = '<table class="data data-no-header">';

        // rank rows
        this.config.data.forEach(playersByRankCount => {
            const percentageOfTotalPlayers = (playersByRankCount.playersCount / totalPlayers * 100)
            const percentageOfTotalPlayersString = parseFloat(
                percentageOfTotalPlayers.toFixed(2)
            )
                .toLocaleString('ru-RU'); // use comma as decimal separator

            playersByRankTable += `<tr>`;
            playersByRankTable += `<td class=${playersByRankCount.rankName}>${playersByRankCount.rankDisplayName}</td>`;
            playersByRankTable += `<td>${playersByRankCount.playersCount}</td>`
            playersByRankTable += `<td>${percentageOfTotalPlayersString}%</td>`
            playersByRankTable += `</tr>`;
        });

        // total players row
        playersByRankTable += `<tr class="bold"><td>Всего</td><td>${totalPlayers}</td><td>100%</td></tr>`;

        playersByRankTable += '</table>'
        document.getElementById(containerId).innerHTML = playersByRankTable;
    }
}
