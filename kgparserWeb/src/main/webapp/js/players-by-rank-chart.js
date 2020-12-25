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
