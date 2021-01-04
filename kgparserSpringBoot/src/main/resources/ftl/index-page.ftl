<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Статистика по клавогонщикам</title>
    <#include "./styles.ftl">
</head>
<body>
<#include "./header.ftl">
<main>
    <div class="section">
        Экспорт шёл с ${dataDownloadStartDate} по ${dataDownloadEndDate}, время на сервере UTC.
    </div>

    <div class="section">
        Выполняемые запросы (на примере пользователя <a href="${examplePlayerProfileLink}">${examplePlayerId}</a>):
        <ul>
            <li><a href="${examplePlayerGetSummaryUrl}">${examplePlayerGetSummaryUrl}</a></li>
            <li><a href="${examplePlayerGetIndexDataUrl}">${examplePlayerGetIndexDataUrl}</a></li>
            <li><a href="${examplePlayerGetStatsOverviewUrl}">${examplePlayerGetStatsOverviewUrl}</a></li>
        </ul>
    </div>

    <div class="section">
        Выполнялись запросы по userId от ${minPlayerId} до ${maxPlayerId}.
    </div>

    <div class="section">
        <table class="data data-left data-no-header">
            <tr>
                <td>Первый валидный id</td>
                <td><a href="${minExistingPlayerProfileLink}">${minExistingPlayerId}</a></td>
            </tr>
            <tr>
                <td>Последний валидный id</td>
                <td><a href="${maxExistingPlayerProfileLink}">${maxExistingPlayerId}</a></td>
            </tr>
            <tr>
                <td>Несуществующие пользователи (<code>"err": "invalid user id")</code></td>
                <td>${nonExistingPlayersCount}</td>
            </tr>
            <tr>
                <td>Заблокированные пользователи (<code>blocked > 0</code>)</td>
                <td>${blockedPlayersCount}</td>
            </tr>
            <tr>
                <td>Действующие пользователи, у которых падает <code>/get-index-data</code></td>
                <td>${playersWithGetIndexDataError?size}</td>
            </tr>
            <tr>
                <td>Действующие пользователи с 0 пробега по всем режимам (<code>total_races_count = 0</code>)</td>
                <td>${actualPlayersWithoutRacesCount}</td>
            </tr>
            <tr>
                <td>Действующие пользователи с хотя бы одним текстом пробега</td>
                <td><strong>${actualPlayersWithAtLeast1RaceCount}</strong></td>
            </tr>
            <tr>
                <td>Всего пользователей в базе</td>
                <td>${totalUsersInDatabase}</td>
            </tr>
        </table>
    </div>

    <div class="section">
        <h2>Действующие пользователи, у которых падает <code>/get-index-data</code></h2>
        <table class="data data-left data-no-header">
            <#list playersWithIndexDataErrorGroupedByError as getIndexDataError, playersWithError>
                <tr>
                    <td>
                        <#switch getIndexDataError>
                            <#case "hidden profile">
                                Пользователи со скрытым профилем
                                <#break>
                            <#case "invalid user id">
                                Ошибка в базе? <code>/get-summary</code> такой ошибки не выдаёт
                                <#break>
                            <#case "mongo refs joining failed: invalid key users.achieves.achieve_id=599bd392df4e4d963a8b4570">
                                Явно ошибка в базе, что-то с достижениями
                                <#break>
                            <#default>
                                Неизвестная ошибка
                        </#switch>
                    </td>
                    <td><code>"err": "${getIndexDataError}"</code></td>
                    <td>
                        <#list playersWithError as playerWithError>
                            <a href="${playerWithError.profileLink}">${playerWithError.login}</a><#sep>, </#sep>
                        </#list>
                    </td>
                </tr>
            </#list>
        </table>
    </div>

    <div class="section" id="section-players_by_rank">
        <h2>Действующие игроки по рангам</h2>

        <div id="playersByRank-switcher-wrapper" class="paging left">
            <span id="playersByRank-switcher-container"></span>&nbsp;&nbsp;&nbsp;<a href="${links.playersByRank}">Страница с выбором диапазона</a>
        </div>

        <div id="playersByRank-single-chart-wrapper" class="flexWrap">
            <canvas id="playersByRank-single-barChart-canvas" class="chart-left"></canvas>
            <canvas id="playersByRank-single-doughnutChart-canvas" class="chart-right"></canvas>

            <div id="playersByRank-single-table-container"></div>
        </div>

        <div id="playersByRank-combined-charts-wrapper">
            <div id="playersByRank-1-race-wrapper" class="flexWrap">
                <canvas id="playersByRank-1-race-barChart-canvas" class="chart-left"></canvas>
                <canvas id="playersByRank-1-race-doughnutChart-canvas" class="chart-right"></canvas>

                <div id="playersByRank-1-race-table-container"></div>
            </div>

            <div id="playersByRank-10-races-wrapper" class="flexWrap">
                <canvas id="playersByRank-10-races-barChart-canvas" class="chart-left"></canvas>
                <canvas id="playersByRank-10-races-doughnutChart-canvas" class="chart-right"></canvas>

                <div id="playersByRank-10-races-table-container"></div>
            </div>

            <div id="playersByRank-100-races-wrapper" class="flexWrap">
                <canvas id="playersByRank-100-races-barChart-canvas" class="chart-left"></canvas>
                <canvas id="playersByRank-100-races-doughnutChart-canvas" class="chart-right"></canvas>

                <div id="playersByRank-100-races-table-container"></div>
            </div>

            <div id="playersByRank-1000-races-wrapper" class="flexWrap">
                <canvas id="playersByRank-1000-races-barChart-canvas" class="chart-left"></canvas>
                <canvas id="playersByRank-1000-races-doughnutChart-canvas" class="chart-right"></canvas>

                <div id="playersByRank-1000-races-table-container"></div>
            </div>

            <div id="playersByRank-10000-races-wrapper" class="flexWrap">
                <canvas id="playersByRank-10000-races-barChart-canvas" class="chart-left"></canvas>
                <canvas id="playersByRank-10000-races-doughnutChart-canvas" class="chart-right"></canvas>

                <div id="playersByRank-10000-races-table-container"></div>
            </div>
        </div>
    </div>

    <div class="section">
        <h2>Агрегированные данные по всем игрокам</h2>
        <ul>
            <li>Всего текстов набрано всеми игроками: ${totalRacesCountByAllPlayers?string[",000;; groupingSeparator=\" \""]}</li>
            <li>Всего машин у всех игроков: ${totalCarsCountByAllPlayers?string[",000;; groupingSeparator=\" \""]}</li>
        </ul>
    </div>

    <#import "./player-td.ftl" as ptd>

    <div class="section">
        <h2>Рекорды (только топ-1)</h2>
        <table class="data data-left data-no-header">
            <tr>
                <td>Наибольший рекорд в &laquo;Обычном&raquo;</td>
                <@ptd.playerTd player=top1PlayerByBestSpeed/>
                <td class="right">${top1PlayerByBestSpeed.bestSpeed}</td>
                <td><a href="./${links.topByBestSpeedPage1}">Топ</a></td>
            </tr>

            <tr>
                <td>Наибольший общий пробег</td>
                <@ptd.playerTd player=top1PlayerByTotalRacesCount/>
                <td class="right">${top1PlayerByTotalRacesCount.totalRacesCount}</td>
                <td><a href="./${links.topByTotalRaces}">Топ</a></td>
            </tr>

            <tr>
                <td>Наибольший рейтинговый уровень</td>
                <@ptd.playerTd player=top1PlayerByRatingLevel/>
                <td class="right">${top1PlayerByRatingLevel.ratingLevel}</td>
                <td><a href="./${links.topByRatingLevel}">Топ</a></td>
            </tr>

            <tr>
                <td>Больше всего достижений</td>
                <@ptd.playerTd player=top1PlayerByAchievementsCount/>
                <td class="right">${top1PlayerByAchievementsCount.achievementsCount}</td>
                <td><a href="./${links.topByAchievementsCount}">Топ</a></td>
            </tr>

            <tr>
                <td>Больше всего машин в гараже</td>
                <@ptd.playerTd player=top1PlayerByCarsCount/>
                <td class="right">${top1PlayerByCarsCount.carsCount}</td>
                <td><a href="./${links.topByCarsCount}">Топ</a></td>
            </tr>

            <tr>
                <td>Больше всего друзей</td>
                <@ptd.playerTd player=top1PlayerByFriendsCount/>
                <td class="right">${top1PlayerByFriendsCount.friendsCount}</td>
                <td><a href="./${links.topByFriendsCount}">Топ</a></td>
            </tr>

            <tr>
                <td>Больше всего используемых словарей</td>
                <@ptd.playerTd player=top1PlayerByVocabulariesCount/>
                <td class="right">${top1PlayerByVocabulariesCount.vocabulariesCount}</td>
                <td><a href="./${links.topByVocabulariesCount}">Топ</a></td>
            </tr>

            <#-- hardcoded record -->
            <#-- see https://klavogonki.ru/forum/software/59/page6/#post116 -->
            <tr>
                <td>Наибольшее число рейтингового опыта за месяц</td>
                <@ptd.playerTd player=top1PlayerByRatingExperienceInOneMonth/>
                <td class="right">334117</td>
                <td><a href="https://klavogonki.ru/top/rating/archive-202012">Ссылка</a></td>
            </tr>
        </table>
    </div>
</main>

<!-- see https://www.chartjs.org/docs/latest/getting-started/installation.html -->
<script src="${links.chartJs}" integrity="${links.chartJsIntegrity}" crossorigin="anonymous"></script>
<script src="${links.playersByRankChartJs}"></script>
<script>
    window.addEventListener('load', function () {
        const playersByRankWithAtLeast1Race = ${playersByRankWithAtLeast1Race};
        const playersByRankWithAtLeast10Races = ${playersByRankWithAtLeast10Races};
        const playersByRankWithAtLeast100Races = ${playersByRankWithAtLeast100Races};
        const playersByRankWithAtLeast1000Races = ${playersByRankWithAtLeast1000Races};
        const playersByRankWithAtLeast10000Races = ${playersByRankWithAtLeast10000Races};

        const singleChart = new PlayersByRankChart({
            data: playersByRankWithAtLeast1Race, // by default, show players with at least 1 race
            label: 'Действующие игроки по рангам (общий пробег 1+)'
        });
        singleChart.append('playersByRank-single-barChart-canvas', 'playersByRank-single-doughnutChart-canvas', 'playersByRank-single-table-container');

        // charts to display them all together
        const playersByRankWithAtLeast1RaceChart = new PlayersByRankChart({
            data: playersByRankWithAtLeast1Race,
            label: 'Действующие игроки по рангам (общий пробег 1+)'
        });
        playersByRankWithAtLeast1RaceChart.append('playersByRank-1-race-barChart-canvas', 'playersByRank-1-race-doughnutChart-canvas', 'playersByRank-1-race-table-container');

        const playersByRankWithAtLeast10RacesChart = new PlayersByRankChart({
            data: playersByRankWithAtLeast10Races,
            label: 'Действующие игроки по рангам (общий пробег 10+)'
        });
        playersByRankWithAtLeast10RacesChart.append('playersByRank-10-races-barChart-canvas', 'playersByRank-10-races-doughnutChart-canvas', 'playersByRank-10-races-table-container');

        const playersByRankWithAtLeast100RacesChart = new PlayersByRankChart({
            data: playersByRankWithAtLeast100Races,
            label: 'Действующие игроки по рангам (общий пробег 100+)'
        });
        playersByRankWithAtLeast100RacesChart.append('playersByRank-100-races-barChart-canvas', 'playersByRank-100-races-doughnutChart-canvas', 'playersByRank-100-races-table-container');

        const playersByRankWithAtLeast1000RacesChart = new PlayersByRankChart({
            data: playersByRankWithAtLeast1000Races,
            label: 'Действующие игроки по рангам (общий пробег 1000+)'
        });
        playersByRankWithAtLeast1000RacesChart.append('playersByRank-1000-races-barChart-canvas', 'playersByRank-1000-races-doughnutChart-canvas', 'playersByRank-1000-races-table-container');

        const playersByRankWithAtLeast10000RacesChart = new PlayersByRankChart({
            data: playersByRankWithAtLeast10000Races,
            label: 'Действующие игроки по рангам (общий пробег 10000+)'
        });
        playersByRankWithAtLeast10000RacesChart.append('playersByRank-10000-races-barChart-canvas', 'playersByRank-10000-races-doughnutChart-canvas', 'playersByRank-10000-races-table-container');

        const chartSwitcher = new PlayersByRankChartSwitcher({
            currentChart: 1,

            singleChart: singleChart,
            singleChartContainerId: `playersByRank-single-chart-wrapper`,

            combinedCharts: [
                playersByRankWithAtLeast1RaceChart,
                playersByRankWithAtLeast10RacesChart,
                playersByRankWithAtLeast100RacesChart,
                playersByRankWithAtLeast1000RacesChart,
                playersByRankWithAtLeast10000RacesChart
            ],
            combinedChartsContainerId: `playersByRank-combined-charts-wrapper`,

            containers: [
                {
                    minTotalRacesCount: 1,
                    data: playersByRankWithAtLeast1Race,
                    label: 'Действующие игроки по рангам (общий пробег 1+)'
                },
                {
                    minTotalRacesCount: 10,
                    data: playersByRankWithAtLeast10Races,
                    label: 'Действующие игроки по рангам (общий пробег 10+)'
                },
                {
                    minTotalRacesCount: 100,
                    data: playersByRankWithAtLeast100Races,
                    label: 'Действующие игроки по рангам (общий пробег 100+)'
                },
                {
                    minTotalRacesCount: 1000,
                    data: playersByRankWithAtLeast1000Races,
                    label: 'Действующие игроки по рангам (общий пробег 1000+)'
                },
                {
                    minTotalRacesCount: 10000,
                    data: playersByRankWithAtLeast10000Races,
                    label: 'Действующие игроки по рангам (общий пробег 10000+)'
                }
            ]
        });

        chartSwitcher.append('playersByRank-switcher-container');

        // we must hide it after appending the charts, else it becomes ugly
        // todo: to avoid the height jump, we can append the layout of the combined wrapper dynamically (only when showing it for the first time). Not worth it for now.
        chartSwitcher.hideContainer('playersByRank-combined-charts-wrapper');
    });
</script>

</body>
</html>
