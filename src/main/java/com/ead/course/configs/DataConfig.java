package com.ead.course.configs;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

//@Configuration
public class DataConfig {
	
	public static final String DATETIME_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final LocalDateTimeSerializer LOACAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(
			DateTimeFormatter.ofPattern(DATETIME_FORMAT_ISO8601));

	
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LOACAL_DATETIME_SERIALIZER);
		return new ObjectMapper().registerModule(javaTimeModule);
	}
	
}
