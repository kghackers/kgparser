<#setting number_format="computer"> <#-- remove annoying commas in integers-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Топ по числу используемых словарей</title>
    <#include "./styles.ftl">
</head>
<body>
<#include "./header.ftl">
<main>
    <div class="section">
        <h2>
            Топ-${players?size} по числу используемых словарей
            <a class="excel" href="./${links.topByVocabulariesCountZip}"><img src="${links.excelPng}" class="excel" alt="Скачать Excel" title="Скачать Excel"/>Скачать Excel (в архиве)</a>
        </h2>
    </div>

    <div class="section" id="table-container">

        <table class="data">
            <tr>
                <th scope="col">#</th>
                <th scope="col">Логин</th>
                <th scope="col">Словарей</th>
                <th scope="col">Рекорд в &laquo;Обычном&raquo;</th>
                <th scope="col">Общий пробег</th>
                <th scope="col">Зарегистрирован</th>
                <th scope="col">Достижений</th>
                <th scope="col">Уровень</th>
                <th scope="col">Друзей</th>
                <th scope="col">Машин</th>
            </tr>

            <#import "./player-td.ftl" as ptd>

            <#list players as player>
                <tr>
                    <td class="right">${player.orderNumber}</td>
                    <@ptd.playerTd player=player/>
                    <td class="right">${player.vocabulariesCount}</td>
                    <td class="right">${(player.bestSpeed)!"&mdash;"}</td> <#-- BestSpeed can be null -->
                    <td class="right">${player.totalRacesCount}</td>
                    <#-- Java 8 Date/Time format does not work in Freemarker -->
                    <#-- see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker -->
                    <#--                    <td>${player.registered?string["yyyy-MM-dd HH:mm:ss"]}</td>-->
                    <td>${player.registered}</td>
                    <td class="right">${player.achievementsCount}</td>
                    <td class="right">${player.ratingLevel}</td>
                    <td class="right">${player.friendsCount}</td>
                    <td class="right">${player.carsCount}</td>
                </tr>
            </#list>
        </table>

    </div>
</main>
</body>
</html>
