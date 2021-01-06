package ru.klavogonki.kgparser.excel.data;

import ru.klavogonki.kgparser.Rank;

/**
 * Root class used as Excel export data.
 * To implement more specific getters, extend this interface.
 * // todo: maybe split to trivial interfaces, at least methods like #getProfileLink
 */
public interface ExcelExportContextData {
    Integer getPlayerId();

    String getLogin();

    String getProfileLink();

    Rank getRank();
}
