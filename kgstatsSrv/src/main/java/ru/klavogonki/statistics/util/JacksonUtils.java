package ru.klavogonki.statistics.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.dictionaries.NonStandardDictionaryData;
import ru.klavogonki.statistics.export.StatisticsGeneratorConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class JacksonUtils {
    private static final Logger logger = LogManager.getLogger(JacksonUtils.class);

    private JacksonUtils() {
    }

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

    public static Config parseConfig(String filePath) {
        File file = new File(filePath);
        Config config = parse(file, Config.class);
        logger.debug("Successfully parsed config from file {}", filePath);
        logger.debug("Config: {}", config);
        return config;
    }

    public static StatisticsGeneratorConfig parseStatisticsGeneratorConfig(String filePath) {
        File file = new File(filePath);
        StatisticsGeneratorConfig config = parse(file, StatisticsGeneratorConfig.class);
        logger.debug("Successfully parsed statistics generator config from file {}", filePath);
        logger.debug("Config: {}", config);
        return config;
    }

    public static List<NonStandardDictionaryData> parseNonStandardDictionaryData(InputStream inputStream) {
        TypeReference<List<NonStandardDictionaryData>> typeReference = new TypeReference<>() {
        };

        try {
            ObjectMapper mapper = createObjectMapper();

            return mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            String errorMessage = String.format("Error on parsing inputStream \"%s\" to class %s", inputStream, typeReference.getClass().getName());

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
        return serializeToString(object, false);
    }

    public static String serializeToString(Object object, boolean prettyPrint) {
        try {
            ObjectMapper mapper = createObjectMapper(prettyPrint);
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            String errorMessage = String.format("Error on writing object of class %s to String", object.getClass().getName());
            throw handleError(e, errorMessage);
        }
    }

    public static void serializeToFile(String filePath, Object object, boolean prettyPrint) {
        try {
            String serializedObject = serializeToString(object, prettyPrint);

            File file = new File(filePath);
            FileUtils.writeStringToFile(file, serializedObject, StandardCharsets.UTF_8);
            logger.debug("Successfully serialized object of class {} to file {}.", object.getClass().getName(), filePath);
            logger.debug("Serialized object: {}", serializedObject);
        }
        catch (IOException e) {
            String errorMessage = String.format("Error on writing object of class %s to file %s", object.getClass().getName(), filePath);
            throw handleError(e, errorMessage);
        }
    }

    private static RuntimeException handleError(final IOException e, final String errorMessage) {
        logger.error(errorMessage, e);
        return new RuntimeException(errorMessage, e); // todo: special dedicated RuntimeException
    }

    private static ObjectMapper createObjectMapper() {
        return createObjectMapper(false);
    }

    private static ObjectMapper createObjectMapper(boolean prettyPrint) {
        ObjectMapper mapper = new ObjectMapper()
            // serialize LocalDateTime not as object, but as date string
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false) // do not lose timezone when de-serializing OffsetDateTime

            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // because of _id / id clash in /get-summary response, see https://github.com/OpenAPITools/openapi-generator/issues/8291
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true); // to not fail on "type": 0 in "voc-107263" in get-stats-overview-80523.json. See https://stackoverflow.com/a/51407361/8534088

        if (prettyPrint) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        return mapper;
    }
}
