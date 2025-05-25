package ru.otus.basicarchitecture.helpers

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toText(pattern: String = "dd.MM.yyyy"): Result<String> {
    return runCatching {
        this.format(DateTimeFormatter.ofPattern(pattern))
    }
}