package ru.klavogonki.kgparser.util;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

    public static File readResourceFile(final String resourceName) {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        URL resource = classLoader.getResource(resourceName);
        assertThat(resource).isNotNull();

        return new File(resource.getFile());
    }
}
