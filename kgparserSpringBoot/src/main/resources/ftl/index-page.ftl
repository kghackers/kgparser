<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Статистика по клавогонщикам</title>
    <link rel="stylesheet" type="text/css" href="./css/stats.css">
    <link rel="shortcut icon" href="img/favicon/favicon.ico"/>
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
        </ul>
    </div>

    <div class="section">
        Выполнялись запросы по userId от ${minPlayerId} до ${maxPlayerId}.
    </div>

    <div class="section">
        <table class="data data-left">
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
        <table class="data data-left">
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
        <#-- todo: fill with different links-->
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
        <table class="data data-left">
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
        </table>
    </div>

    <#-- todo: js that fills the chart data and initializes the switcher -->
</main>
</body>
