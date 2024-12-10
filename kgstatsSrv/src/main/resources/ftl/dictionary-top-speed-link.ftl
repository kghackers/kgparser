<#-- expects NonStandardVocabularyGeneratorContext as dictionaries -->
<#macro dictionaryTopSpeedLink dictionaries dictionaryId>
<a href="./${dictionaries.getTopBySpeedFirstPageFilePath(dictionaryId)}">${dictionaries.getHeaderName(dictionaryId)}</a>
</#macro>