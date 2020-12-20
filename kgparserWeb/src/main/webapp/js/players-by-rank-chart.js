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

    fillChartConfigs() {
        const playersByRankChartLabels = this.config.data.map(playersByRankCount => playersByRankCount.rankDisplayName); // horizontal axes
        const playersByRankPlayersCount = this.config.data.map(playersByRankCount => playersByRankCount.playersCount); // data
        const playersByRankBackgroundColors = this.config.data.map(playersByRankCount => PlayersByRankChart.RANK_COLORS[playersByRankCount.rankName]); // background colors according to ranks

        this.barChartConfig = {
            type: 'horizontalBar',
            data: {
                label: this.config.label,
                labels: playersByRankChartLabels,
                datasets: [{
                    data: playersByRankPlayersCount,
                    backgroundColor: playersByRankBackgroundColors
                }],
            },
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
                }
            }
        };

        // same config, different diagram type
        this.doughnutChartConfig = Object.assign({}, this.barChartConfig, { type: 'doughnut'});
    }

    appendBarChart(canvasId) {
        this.barChart = new Chart(canvasId, this.barChartConfig);
    }

    appendDoughnutChart(canvasId) {
        this.doughnutChart = new Chart(canvasId, this.doughnutChartConfig);
    }

    appendTable(containerId) {
        const totalPlayers = this.config.data
            .map(playersByRankCount => playersByRankCount.playersCount)
            .reduce((prev, next) => prev + next);

        let playersByRankTable = '<table class="data">';

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
