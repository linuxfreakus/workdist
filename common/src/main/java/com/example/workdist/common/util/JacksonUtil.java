package com.example.workdist.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.base.Suppliers;

import java.util.function.Supplier;

public class JacksonUtil {
    private static Supplier<ObjectMapper> MAPPER_SUPPLIER = Suppliers.memoize(
            () -> JsonMapper.builder()
                    .addModule(new GuavaModule())
                    .addModule(new JavaTimeModule())
                    .addModule(new Jdk8Module())
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .build());

    public static ObjectMapper mapper() {
        return MAPPER_SUPPLIER.get();
    }
}
