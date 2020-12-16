<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Топ по рекорду в &laquo;Обычном&raquo;</title>
    <link rel="stylesheet" type="text/css" href="./css/stats.css">
</head>
<body>
<header>
    <!-- todo: make header some html include for all pages -->
    <a href="./stats.html">На главную</a>
    | <a href="https://github.com/dmitry-weirdo/kgparser/">Kgparser on GitHub</a>
    | By <a href="https://klavogonki.ru/u/#/242585/">nosferatum</a>
    <br/>
    <a href="./stat-top-by-total-races.html">Топ по общему пробегу</a>
    | <a href="./stat-top-by-best-speed-page-1.html">Топ по рекорду в &laquo;Обычном&raquo;</a>
    | <a href="./stat-top-by-best-speed.html">Топ-500 в &laquo;Обычном&raquo;</a> <#-- todo: think whether we need it -->
    | <a href="./stat-top-by-rating-level.html">Топ по уровню</a>
    | <a href="./stat-top-by-friends-count.html">Топ по числу друзей</a>
    | <a href="./stat-top-by-achievements-count.html">Топ по числу достижений</a>
    | <a href="./stat-top-by-vocabularies-count.html">Топ по числу используемых словарей</a>
    | <a href="./stat-top-by-cars-count.html">Топ по числу машин в гараже</a>
</header>
<main>
    <div class="section">
        <h2>Топ по лучшей скорости в &laquo;Обычном&raquo;</h2>
        Учтены игроки с минимальным общим пробегом: ${totalRacesCountMin}
    </div>

    <div class="paging" id="paging-top"></div>

    <div class="section" id="table-container">

        <table class="data">
            <#-- todo: headers-->
            <tr>
                <th>#</th>
                <th>Логин</th>
                <th>Рекорд в &laquo;Обычном&raquo;</th>
                <th>Общий пробег</th>
                <th>Зарегистрирован</th>
                <th>Достижений</th>
                <th>Уровень</th>
                <th>Друзей</th>
                <th>Словарей</th>
                <th>Машин</th>
            </tr>

            <#list players as player>
                <tr>
                    <#-- todo: number, from Mapped instance-->
                    <td class="right">${player.orderNumber}</td>
                    <td class="${player.rank}">
                        ${player.login}&nbsp;
                        <a href="https://klavogonki.ru/u/#/${player.playerId}/" target="_blank" rel="noopener noreferrer">
                            <img src="img/info.png" alt="Профиль" title="Профиль" width="10" height="10"/>
                        </a>
                    </td>
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
                    <td class="right">${player.carsCount}</td
                </tr>
            </#list>
        </table>

    </div>

    <div class="paging" id="paging-bottom"></div>
</main>

<#-- todo: script with login -> page mapping -->
<script src="./js/stats-top-table.js"></script>

<script>
    window.addEventListener('load', function() {
        appendPaging();
    });

    function appendPaging() {
        const paging = new Paging({
            totalPages: ${totalPages},
            currentPage: ${pageNumber},
            bindLinks: true,

            getPagingLink: function(linkId, pageNumber) {
                <#noparse>
                return `<a href="./stat-top-by-best-speed-page-${pageNumber}.html" id="${linkId}">${pageNumber}</a>${Paging.SPACE_SEPARATOR}`;
                </#noparse>
            }
        });

        paging.append('paging-top');
        paging.append('paging-bottom');
    }
</script>

