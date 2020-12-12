var playerByRankTableContainer = document.getElementById('table-container-section-players_by_rank');

// todo: better solutions than innerHTML
let playersByRankTable = '<table class="data">';
STATS_DATA.playersByRank.forEach(rankByData => {
    playersByRankTable += `<tr><td class=${rankByData.rank_name}>${rankByData.rank_display_name}</td><td>${rankByData.players_count}</td></tr>`;
});

playersByRankTable += '</table>'
playerByRankTableContainer.innerHTML = playersByRankTable;

const canvas1 = document.getElementById('chart-players_by_rank_1');
const canvas2 = document.getElementById('chart-players_by_rank_2');

const playersByRankChartLabels = STATS_DATA.playersByRank.map(rankByData => rankByData.rank_display_name); // horizontal axes
const playersByRankPlayersCount = STATS_DATA.playersByRank.map(rankByData => rankByData.players_count); // data
const playersByRankBackgroundColors = STATS_DATA.playersByRank.map(rankByData => RANK_COLORS[rankByData.rank_name]); // background colors according to ranks

const horizontalBarChartConfig = {
    // type: 'bar',
    type: 'horizontalBar',
    // type: 'pie',
    // type: 'doughnut',
    // type: 'line',
    // type: 'polarArea',
    // type: 'bar',
    data: {
        label: 'Действующие игроки по рангам',
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
            text: 'Действующие игроки по рангам'
        },
        legend: { // do not display the legend of the dataset
            display: false
        }
    }
};

// same config, other chart type
const doughnutChartConfig = Object.assign({}, horizontalBarChartConfig, { type: 'doughnut'});

const playersByRankHorizontalBarChart = new Chart(canvas1, horizontalBarChartConfig);
const playersByRankDoughnutChart = new Chart(canvas2, doughnutChartConfig);

