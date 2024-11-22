package ru.klavogonki.kgparser;

public class Wiki {
    public static final String WIKI_BASE_URL = "https://klavogonki.ru/wiki/";

    private Wiki() {
    }

    public static String getUrl(String pageName) {
        return WIKI_BASE_URL + pageName.trim();
    }
}
