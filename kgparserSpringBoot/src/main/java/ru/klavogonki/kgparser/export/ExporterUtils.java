package ru.klavogonki.kgparser.export;

import java.util.List;

public final class ExporterUtils {

    public static final int FIRST_PAGE_NUMBER = 1;

    private ExporterUtils() {
    }

    public static int getPagesCount(int totalRecords, int pageSize) {
        if ((totalRecords % pageSize) == 0)
            return totalRecords / pageSize;

        return (totalRecords / pageSize) + 1;
    }

    public static <T> List<T> subList(List<T> list, int pageSize, int pageNumberStartingWith1) {
        int fromIndex = (pageNumberStartingWith1 - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());

        return list.subList(fromIndex, toIndex);
    }
}
