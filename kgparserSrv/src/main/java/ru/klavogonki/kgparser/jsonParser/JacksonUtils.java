package ru.klavogonki.kgparser.jsonParser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class JacksonUtils {
    private static final Logger logger = LogManager.getLogger(JacksonUtils.class);

    public static <T> T parse(File file, Class<T> clazz) {
        try {
            ObjectMapper mapper = createObjectMapper();
            return mapper.readValue(file, clazz);
        }
        catch (IOException e) {
            String errorMessage = String.format("Error on parsing file %s to class %s", file.getPath(), clazz.getName());
            throw handleError(e, errorMessage);
        }
    }

    public static void serialize(File file, Object object) {
        try {
            ObjectMapper mapper = createObjectMapper();
            mapper.writeValue(file, object);
        }
        catch (IOException e) {
            String errorMessage = String.format("Error on writing object of class %s to file %s", object.getClass().getName(), file.getPath());
            throw handleError(e, errorMessage);
        }
    }


    public static String serializeToString(Object object) {
        try {
            ObjectMapper mapper = createObjectMapper();
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            String errorMessage = String.format("Error on writing object of class %s to String", object.getClass().getName());
            throw handleError(e, errorMessage);
        }
    }

    private static RuntimeException handleError(final IOException e, final String errorMessage) {
        logger.error(errorMessage, e);
        return new RuntimeException(errorMessage, e);
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // because of _id / id clash in /get-summary response, see https://github.com/OpenAPITools/openapi-generator/issues/8291
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true); // to not fail on "type": 0 in "voc-107263" in get-stats-overview-80523.json. See https://stackoverflow.com/a/51407361/8534088
    }
}
