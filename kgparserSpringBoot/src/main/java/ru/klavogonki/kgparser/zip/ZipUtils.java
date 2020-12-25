package ru.klavogonki.kgparser.zip;

import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Log4j2
public final class ZipUtils {

    private ZipUtils() {
    }

    public static void zipFile(String filePath, String zipFilePath) {
        try {
            new ZipFile(zipFilePath).addFile(filePath);
            logger.debug("Successfully zipped file {} to zip file {}", filePath, zipFilePath);
        }
        catch (ZipException e) {
            logger.error(String.format("Error on zipping file %s to zip file %s", filePath, zipFilePath), e );

            throw new RuntimeException(e);
        }
    }
}
