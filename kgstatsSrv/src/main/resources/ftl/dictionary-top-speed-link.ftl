<#-- expects PlayerEntity as player, or something with same used properties -->
<#macro dictionaryTopSpeedLink dictionaries dictionaryId>
<a href="./${dictionaries.getTopBySpeedFirstPageFilePath(dictionaryId)}">${dictionaries.getHeaderName(dictionaryId)}</a>
</#macro>