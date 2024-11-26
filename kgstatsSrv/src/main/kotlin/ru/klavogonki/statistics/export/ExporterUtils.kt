package ru.klavogonki.statistics.export

import kotlin.math.min

object ExporterUtils {
    const val FIRST_PAGE_NUMBER: Int = 1

    @JvmStatic
    fun getPagesCount(totalRecords: Int, pageSize: Int): Int {
        if ((totalRecords % pageSize) == 0) {
            return totalRecords / pageSize
        }

        return (totalRecords / pageSize) + 1
    }

    @JvmStatic
    fun <T> subList(
        list: List<T>, pageSize: Int,
        pageNumberStartingWith1: Int
    ): List<T> {
        val fromIndex = (pageNumberStartingWith1 - 1) * pageSize
        val toIndex = min(fromIndex + pageSize, list.size)

        return list.subList(fromIndex, toIndex)
    }
}
