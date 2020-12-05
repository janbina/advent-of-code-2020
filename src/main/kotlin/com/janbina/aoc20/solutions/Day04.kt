package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputText(4)
    Day04(input).also {
        printAndTest(it.part1(), 260)
        printAndTest(it.part2(), 153)
    }
}

class Day04(input: String) {

    private val passports = input.splitToSequence("\n\n").map { pass ->
        pass.split("\\s".toRegex()).associate {
            val (k, v) = it.split(":")
            k to v
        }
    }

    fun part1(): Int {
        return passports.count { validatePassport1(it) }
    }

    fun part2(): Int {
        return passports.count { validatePassport2(it) }
    }

    private fun validatePassport1(pass: Map<String, String>): Boolean {
        return listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid").all { it in pass }
    }

    private fun validatePassport2(pass: Map<String, String>): Boolean {
        return pass["byr"]?.toIntOrNull() in 1920..2002 &&
                pass["iyr"]?.toIntOrNull() in 2010..2020 &&
                pass["eyr"]?.toIntOrNull() in 2020..2030 &&
                pass["ecl"] in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") &&
                pass["pid"]?.matches("\\d{9}".toRegex()) == true &&
                pass["hcl"]?.matches("#[0-9a-f]{6}".toRegex()) == true &&
                when (pass["hgt"]?.takeLast(2)) {
                    "cm" -> pass["hgt"]?.dropLast(2)?.toIntOrNull() in 150..193
                    "in" -> pass["hgt"]?.dropLast(2)?.toIntOrNull() in 59..76
                    else -> false
                }
    }
}
