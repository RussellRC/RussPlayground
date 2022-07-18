package org.russellrc.playground.util.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;

public class JacksonConfiguration {

    private static final ImmutableSet<Module> MODULES = ImmutableSet.of(
        // Include Optional<> and other Java8 serialization
        new Jdk8Module(),
        // Include jdk8 java.time support
        new JavaTimeModule(),
        // Include guava classes support
        new GuavaModule());

    private static final ImmutableSet<ConfigFeature> DISABLED_FEATURES = ImmutableSet.of(
        // Always use string representation for dates
        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
        // Make the API forward compatible by ignoring unknown attributes
        DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final ImmutableSet<ConfigFeature> ENABLED_FEATURES = ImmutableSet.of(
        // Indent output to make it user friendly
        SerializationFeature.INDENT_OUTPUT);

    public static ObjectMapper createObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(MODULES);
        configureFeatures(objectMapper);
//        objectMapper.setTimeZone(getTimeZone());
        return objectMapper;
    }

    private static void configureFeatures(@Nonnull final ObjectMapper objectMapper) {
        DISABLED_FEATURES.forEach(feature -> configureFeatures(objectMapper, feature, false));
        ENABLED_FEATURES.forEach(feature -> configureFeatures(objectMapper, feature, true));
    }

    private static void configureFeatures(@Nonnull final ObjectMapper objectMapper, @Nonnull final ConfigFeature feature, boolean enabled) {
        if (feature instanceof SerializationFeature) {
            objectMapper.configure((SerializationFeature) feature, enabled);
        } else if (feature instanceof DeserializationFeature) {
            objectMapper.configure((DeserializationFeature) feature, enabled);
        }
    }

}
