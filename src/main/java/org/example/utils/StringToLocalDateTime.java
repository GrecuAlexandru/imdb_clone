package org.example.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StringToLocalDateTime {
    public static LocalDateTime convert(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.atStartOfDay();
    }
}
