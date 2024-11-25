package ru.klavogonki.common

object Wiki {
    private const val WIKI_BASE_URL = "https://klavogonki.ru/wiki/"

    fun getUrl(pageName: String) =
        WIKI_BASE_URL + pageName.trim()
}
