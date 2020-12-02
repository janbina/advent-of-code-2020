package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(2)
    Day02(input).also {
        printAndTest(it.part1(), 636)
        printAndTest(it.part2(), 588)
    }
}

class Day02(inputLines: List<String>) {

    private val passRegex = Regex("(\\d+)-(\\d+) ([a-z]): ([a-z]+)")

    private val input = inputLines.map {
        lineToEntry(it)
    }

    fun part1(): Int {
        return input.count {
            it.pass.count { c -> c == it.policy.char } in it.policy.min..it.policy.max
        }
    }

    fun part2(): Int {
        return input.count {
            val a = it.pass[it.policy.min - 1] == it.policy.char
            val b = it.pass[it.policy.max - 1] == it.policy.char
            a xor b
        }
    }

    private fun lineToEntry(line: String): PasswordEntry {
        val matches = passRegex.find(line) ?: throw IllegalArgumentException("Invalid input line: \"$line\"")
        val (min, max, char, pass) = matches.destructured

        return PasswordEntry(
            pass = pass,
            policy = PasswordPolicy(
                char = char.first(),
                min = min.toInt(),
                max = max.toInt(),
            )
        )
    }

    data class PasswordEntry(
        val pass: String,
        val policy: PasswordPolicy,
    )

    data class PasswordPolicy(
        val char: Char,
        val min: Int,
        val max: Int,
    )
}
