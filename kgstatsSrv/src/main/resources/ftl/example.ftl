<#setting number_format="computer"> <#-- remove annoying commas in integers-->

<#-- a template just to test the freemarker -->
<#assign d = testDouble>
<#--double format: ${d?string[",00;; decimalSeparator=\",\""]}-->
double format: ${d?string(",##0.00;; decimalSeparator=\",\"")}

Test integer: ${testInteger?string[",000;; groupingSeparator=\" \""]}


<p>
Test string with non-breaking spaces: ${testString}
</p>

<#assign x = testInteger>
X first assign: ${x}

<#assign x = 666>
X second assign: ${x}

<#-- import the macro from the other file -->
<#-- see https://stackoverflow.com/a/46788690/8534088 -->
<#import "./player-td.ftl" as ptd>
<@ptd.playerTd player=testPlayer/>

<#list testMap as key, value>
    ==========================================
    <#if key?is_first>
        I am first!
    <#else>
        I am NOT first!
    </#if>

    Key: ${key}
    Value: ${value}
    ==========================================
</#list>

<p>
One key from map: ${testMap["key 1"]}
<br/>

<#-- just getting non-existing value without default will fail the generation -->
One non-existing key from map: ${testMap["bad key"]!""}
</p>

<h1>Just Player object</h1>
<p>
    Player object: ${testPlayer}
</p>
<p>
    Player login: ${testPlayer.login}
    <br/>
    Player id: ${testPlayer.playerId?int}
</p>


<#assign playerId = 242585?int>

<h1>Getting from map with int keys</h1>
<p>

<#-- !!! need to use ?.int, else it will be BigDecimal and fail on ClassCast -->
Player from map by id: ${idToPlayerMap?api.get(playerId).login}

</p>

<p>
    <#assign nonStandardDictionaries = links.nonStandardDictionaries>

    <#import "./dictionary-top-speed-link.ftl" as dtsl>

    Non standard dictionaries context (from links): ${links.nonStandardDictionaries}
    <br/>
    <br/>
    Non standard dictionaries context (from variables): ${nonStandardDictionaries}
    <br/>
    <br/>
    Page name: ${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(5539)}
    <br/>
    <br/>
    Page header: ${nonStandardDictionaries.getHeaderName(5539)}
    <br/>
    <br/>
    Link test:
    <@dtsl.dictionaryTopSpeedLink dictionaries=nonStandardDictionaries dictionaryId=5539/>

    <#-- Мини-марафон, 800 знаков -->
    | <@dtsl.dictionaryTopSpeedLink dictionaries=nonStandardDictionaries dictionaryId=6018/>
</p>