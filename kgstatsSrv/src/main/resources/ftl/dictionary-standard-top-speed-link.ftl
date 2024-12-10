<#-- expects StandardVocabularyGeneratorContext as dictionaries -->
<#macro dictionaryStandardTopSpeedLink dictionaries dictionaryCode>
<a href="./${dictionaries.getTopBySpeedFirstPageFilePath(dictionaryCode)}" class="${dictionaries.getHeaderCssClass(dictionaryCode)}">${dictionaries.getHeaderName(dictionaryCode)}</a>
</#macro>