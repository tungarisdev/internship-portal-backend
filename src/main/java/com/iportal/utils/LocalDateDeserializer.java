package com.iportal.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText().trim();
        if (date.isEmpty()) {
            return null; // Nếu nhận "", chuyển thành null
        }
        try {
            return LocalDate.parse(date, FORMATTER_1); // Try "dd-MM-yyyy" first
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(date, FORMATTER_2); // Fallback to "yyyy-MM-dd"
            } catch (DateTimeParseException e2) {
                throw new IOException("Invalid date format: " + date + ". Expected 'dd-MM-yyyy' or 'yyyy-MM-dd'");
            }
        }
    }
}