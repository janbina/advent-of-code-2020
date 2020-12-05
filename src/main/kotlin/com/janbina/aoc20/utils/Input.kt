package com.janbina.aoc20.utils

import java.io.File

object Input {
    fun getDayInputText(day: Int): String {
        return getDayInputFile(day).readText()
    }

    fun getDayInputLines(day: Int): List<String> {
        return getDayInputFile(day).readLines()
    }

    private fun getDayInputFile(day: Int): File {
        return File(javaClass.classLoader.getResource(getDayInputFileName(day)).toURI())
    }

    private fun getDayInputFileName(day: Int): String {
        return buildString {
            append("day")
            append(day.toString().padStart(2, '0'))
            append(".txt")
        }
    }
}
