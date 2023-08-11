package com.example.refit.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    fun isCurrentDate(format: String, inputDate: String?): Boolean {
        val formatter = DateTimeFormatter.ofPattern(format)
        val current = LocalDateTime.now().format(formatter)
        return inputDate == current
    }
}