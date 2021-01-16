<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Топ по рекорду в &laquo;Обычном&raquo;</title>
    <#include "./styles.ftl">
</head>
<body>
<#include "./header.ftl">
<main>
    <div class="section">
        <h2>
            Топ по лучшей скорости в &laquo;Обычном&raquo;
            <a class="excel" href="./${links.topByBestSpeedAllPagesZip}"><img src="${links.excelPng}" class="excel" alt="Скачать Excel" title="Скачать Excel"/>Скачать Excel (все на одной странице)</a>
        </h2>
        Учтены игроки с минимальным общим пробегом: ${totalRacesCountMin}
    </div>

    <div>
        <label for="player-search-input">Найти игрока по логину (полный логин):</label>&nbsp;
        <input id="player-search-input" autofocus/>
        <button id="search-button" class="search-button">Искать</button>
    </div>
    <div class="paging" id="paging-top"></div>

    <div class="section" id="table-container">

        <table class="data">
            <tr>
                <th scope="col">#</th>
                <th scope="col">Логин</th>
                <th scope="col">Рекорд в &laquo;Обычном&raquo;</th>
                <th scope="col">Общий пробег</th>
                <th scope="col">Зарегистрирован</th>
                <th scope="col">Достижений</th>
                <th scope="col">Уровень</th>
                <th scope="col">Друзей</th>
                <th scope="col">Словарей</th>
                <th scope="col">Машин</th>
            </tr>

            <#import "./player-td.ftl" as ptd>

            <#list players as player>
                <tr>
                    <td class="right">${player.orderNumber}</td>
                    <@ptd.playerTd player=player/>
                    <td class="right">${(player.bestSpeed)!"&mdash;"}</td> <#-- BestSpeed can be null -->
                    <td class="right">${player.totalRacesCount}</td>
                    <#-- Java 8 Date/Time format does not work in Freemarker -->
                    <#-- see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker -->
                    <#--                    <td>${player.registered?string["yyyy-MM-dd HH:mm:ss"]}</td>-->
                    <td>${player.registered}</td>
                    <td class="right">${player.achievementsCount}</td>
                    <td class="right">${player.ratingLevel}</td>
                    <td class="right">${player.friendsCount}</td>
                    <td class="right">${player.vocabulariesCount}</td>
                    <td class="right">${player.carsCount}</td>
                </tr>
            </#list>
        </table>

    </div>

    <div class="paging" id="paging-bottom"></div>
</main>

<script src="${links.topTableJs}"></script>
<script src="${links.topByBestSpeedLoginToPageJs}"></script>

<script>
    window.addEventListener('load', function() {
        const login = TopTable.getLoginFromQueryParameter();

        appendPaging();
        bindSearch(login);

        TopTable.highlightTableRow(login);
    });

    function appendPaging() {
        const paging = new Paging({
            totalPages: ${totalPages},
            currentPage: ${pageNumber},
            bindLinks: true,

<#noparse>
            getPagingLink: function(linkId, pageNumber) {
                return `<a href="./stat-top-by-best-speed-page-${pageNumber}.html" id="${linkId}">${pageNumber}</a>${Paging.SPACE_SEPARATOR}`;
            }
        });

        paging.append('paging-top');
        paging.append('paging-bottom');
    }

    function bindSearch(login) {
        const search = new PageSearch({
            searchInputId: 'player-search-input',
            searchButtonId: 'search-button',
            searchMap: STATS_DATA.topBySpeedLoginToPage,

            handleSearch: function(login, pageNumber) {
                const redirectUrl = `./stat-top-by-best-speed-page-${pageNumber}.html?${TopTable.LOGIN_PARAMETER}=${login}`;
                // console.log(`redirectUrl: ${redirectUrl}`);
                window.location.href = redirectUrl;
            }
        });

        search.bind();

        // fill input field with given login parameter
        search.fillInput(login)
    }
</script>
</#noparse>
</body>
</html>
