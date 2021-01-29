<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <#include "./styles.ftl">
</head>
<body>
<#include "./header.ftl">
<main>
<#--
    <div class="section">
        <a href="${topByBestSpeedUrl}">Топ по лучшей скорости</a>&nbsp;&nbsp;&nbsp;
        <a href="${topByRacesCountUrl}">Топ по пробегу</a>&nbsp;&nbsp;&nbsp;
        <a href="${topByHaulUrl}">Топ по времени</a>&nbsp;&nbsp;&nbsp;
    </div>
-->
    <div class="section">
        <h2>
            ${header}
<#--            <a class="excel" href="${excelUrl}"><img src="${links.excelPng}" class="excel" alt="Скачать Excel" title="Скачать Excel"/>Скачать Excel (все на одной странице)</a>-->
        </h2>
        ${additionalHeader}
    </div>

<#--
    <div>
        <label for="player-search-input">Найти игрока по логину (полный логин):</label>&nbsp;
        <input id="player-search-input" autofocus/>
        <button id="search-button" class="search-button">Искать</button>
    </div>
    <div class="paging" id="paging-top"></div>
-->

    <div class="section" id="table-container">

        <table class="data" aria-label="${header}">
            <tr>
                <th scope="col" rowspan="2">#</th>
                <th scope="col" rowspan="2">Логин</th>
                <th scope="col" colspan="6">Всего</th>

                <#list vocabularies as vocabulary>

                <#-- todo: link to vocabulary, get right from Dto, use ftl template -->
                <th scope="col" colspan="7">${vocabulary.name}</th>

                </#list>
            </tr>
            <tr>
                <#-- Сумма -->
                <th scope="col">Словарей</th>
                <th scope="col">Сумма рекордов</th>
                <th scope="col">Пробег</th>
                <th scope="col">Средняя</th>
                <th scope="col">Ошибки, %</th>
                <th scope="col">Общее время</th>

                <#-- Словари -->
                <#list vocabularies as vocabulary>
                <th scope="col">Рекорд</th>
                <th scope="col">Пробег</th>
                <th scope="col">Средняя</th>
                <th scope="col">Ошибки, %</th>
                <th scope="col">Квала</th>
                <th scope="col">Общее время</th>
                <th scope="col">Обновлено</th>
                </#list>

            </tr>

            <#import "./player-td.ftl" as ptd>  <#-- link to player profile -->
            <#import "./player-td.ftl" as ptd> <#-- link to player stats in the vocabulary -->
<#--            <#import "./player-td-vocabulary-stats-link.ftl" as ptd> &lt;#&ndash; link to player stats in the vocabulary &ndash;&gt;-->

            <#list players as player>
                <tr>
                    <td class="right">${player.orderNumber}</td>
                    <@ptd.playerTd player=player/>

                    <#-- Сумма -->
                    <td class="right">${player.totalVocabularies}</td>
                    <td class="right">${player.bestSpeedsSum}</td>
                    <td class="right">${player.totalRacesCount}</td>
                    <td class="right">${player.averageSpeed?string(",##0.00;; decimalSeparator=\",\"")}</td> <#-- 2 decimal, with rounding, comma as decimal separator -->
                    <td class="right">${player.averageError?string(",##0.00;; decimalSeparator=\",\"")}</td> <#-- 2 decimal, with rounding, comma as decimal separator -->
                    <td class="right nowrap">${player.totalHaul}</td>

                    <#-- Словари -->
                    <#list vocabularies as vocabulary>

                        <#if player.vocabulariesMap[vocabulary.code]??>
                            <#assign playerVocabulary = player.vocabulariesMap[vocabulary.code]>

                            <td class="right">${(playerVocabulary.bestSpeed)!"&mdash;"}</td> <#-- BestSpeed can be null -->
                            <td class="right">${playerVocabulary.racesCount}</td>
                            <td class="right">${playerVocabulary.averageSpeed?string(",##0.00;; decimalSeparator=\",\"")}</td> <#-- 2 decimal, with rounding, comma as decimal separator -->
                            <td class="right">${playerVocabulary.averageError?string(",##0.00;; decimalSeparator=\",\"")}</td> <#-- 2 decimal, with rounding, comma as decimal separator -->
                            <td class="right">${playerVocabulary.qual}</td>
                            <td class="right nowrap">${playerVocabulary.haul}</td>
                            <td class="right nowrap">${playerVocabulary.updated}</td>
                        <#else>
                            <td class="right">&mdash;</td>
                            <td class="right">&mdash;</td>
                            <td class="right">&mdash;</td>
                            <td class="right">&mdash;</td>
                            <td class="right">&mdash;</td>
                            <td class="right">&mdash;</td>
                            <td class="right">&mdash;</td>
                        </#if>

                    </#list>


<#--
                    <td class="right">${(player.bestSpeed)!"&mdash;"}</td> &lt;#&ndash; BestSpeed can be null &ndash;&gt;
                    <td class="right">${player.racesCount}</td>

                    &lt;#&ndash; Java 8 Date/Time format does not work in Freemarker &ndash;&gt;
                    &lt;#&ndash; see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker &ndash;&gt;
                    &lt;#&ndash;                    <td>${player.registered?string["yyyy-MM-dd HH:mm:ss"]}</td>&ndash;&gt;
&lt;#&ndash;                    <td>${player.registered}</td>&ndash;&gt;

                    <td class="right">${player.averageSpeed?string(",##0.00;; decimalSeparator=\",\"")}</td> &lt;#&ndash; 2 decimal, with rounding, comma as decimal separator &ndash;&gt;
                    <td class="right">${player.averageError?string(",##0.00;; decimalSeparator=\",\"")}</td> &lt;#&ndash; 2 decimal, with rounding, comma as decimal separator &ndash;&gt;
                    <td class="right">${player.qual}</td>
                    <td class="right">${player.haul}</td>
                    <td class="right">${player.updated}</td>-->
            </#list>
                </tr>
        </table>

    </div>

    <div class="paging" id="paging-bottom"></div>
</main>
<#--
<script src="${links.topTableJs}"></script>
<script src="${loginToPageJsPath}"></script>-->

<#--
<script>
    window.addEventListener('load', function() {
        const login = TopTable.getLoginFromQueryParameter();

        appendPaging();
        bindSearch(login);

        TopTable.highlightTableRow(login);
    });

    function getPageUrl(pageNumber) {
        return `${pageUrlTemplate}`;
    }

    function appendPaging() {
        const paging = new Paging({
            totalPages: ${totalPages},
            currentPage: ${pageNumber},
            bindLinks: true,

<#noparse>
            getPagingLink: function(linkId, pageNumber) {
                const pageUrl = getPageUrl(pageNumber);

                return `<a href="${pageUrl}" id="${linkId}">${pageNumber}</a>${Paging.SPACE_SEPARATOR}`;
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
                const pageUrl = getPageUrl(pageNumber);

                const redirectUrl = `${pageUrl}?${TopTable.LOGIN_PARAMETER}=${login}`;
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
-->
</body>
</html>
