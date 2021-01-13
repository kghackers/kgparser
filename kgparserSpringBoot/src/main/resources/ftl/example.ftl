<#-- a template just to test the freemarker -->
<#assign d = testDouble>
<#--double format: ${d?string[",00;; decimalSeparator=\",\""]}-->
double format: ${d?string(",##0.00;; decimalSeparator=\",\"")}

Test integer: ${testInteger?string[",000;; groupingSeparator=\" \""]}

<#assign x = testInteger>
X first assign: ${x}

<#assign x = 666>
X second assign: ${x}

<#-- import the macro from the other file -->
<#-- see https://stackoverflow.com/a/46788690/8534088 -->
<#import "./player-td.ftl" as ptd>
<@ptd.playerTd player=testPlayer/>
