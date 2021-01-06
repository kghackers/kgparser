package ru.klavogonki.kgparser.freemarker;

import java.util.Map;

public abstract class VocabularyTopLoginToPageTemplate extends FreemarkerTemplate {  // now is same as LoginToPageTemplate // todo: maybe join them into one class

    private static final String LOGIN_TO_PAGE_KEY = "loginToPage";
    private static final String LOGIN_TO_PAGE_STRING_KEY = "loginToPageString";

    public VocabularyTopLoginToPageTemplate loginToPage(Map<String, Integer> loginToPage) {
        templateData.put(LOGIN_TO_PAGE_KEY, loginToPage);
        return this;
    }

    public Map<String, Integer> getLoginToPage() {
        return (Map<String, Integer>) templateData.get(LOGIN_TO_PAGE_KEY);
    }

    public VocabularyTopLoginToPageTemplate loginToPageString(String loginToPageString) {
        templateData.put(LOGIN_TO_PAGE_STRING_KEY, loginToPageString);
        return this;
    }

    public String getLoginToPageString() {
        return (String) templateData.get(LOGIN_TO_PAGE_STRING_KEY);
    }

}
