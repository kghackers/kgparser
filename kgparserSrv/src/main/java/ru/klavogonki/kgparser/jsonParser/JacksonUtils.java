package ru.klavogonki.kgparser.jsonParser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class JacksonUtils {
    private static final Logger logger = LogManager.getLogger(JacksonUtils.class);

    public static <T> T parse(File file, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
            return mapper.readValue(file, clazz);
        }
        catch (IOException e) {
            String errorMessage = String.format("Error on parsing file %s to class %s", file.getPath(), clazz.getName());
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
