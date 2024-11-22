package ru.klavogonki.statistics.zip

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import org.apache.logging.log4j.kotlin.Logging

object ZipUtils : Logging {

    @JvmStatic
    fun zipFile(filePath: String, zipFilePath: String) {
        ZipFile(zipFilePath).use {
            try {
                it.addFile(filePath)

                logger.debug("Successfully zipped file \"$filePath\" to zip file \"$zipFilePath\".")
            } catch (e: ZipException) {
                logger.error("Error on zipping file \"$filePath\" to zip file \"$zipFilePath\"", e)

                throw e
            }
        }
    }
}