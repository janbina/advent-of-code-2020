package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(5)
    Day05(input).also {
        printAndTest(it.part1(), 850)
        printAndTest(it.part2(), 599)
    }
}

class Day05(input: List<String>) {

    private val passes = input.map {
        BoardingPass.fromString(it)
    }

    fun part1(): Int {
        return passes.maxOf { it.id }
    }

    fun part2(): Int {
        return passes.map { it.id }.sorted().zipWithNext()
            .first { it.first + 1 != it.second }
            .first + 1
    }

    data class BoardingPass(
        val row: Int,
        val seat: Int,
    ) {
        val id: Int = row * 8 + seat

        companion object {
            fun fromString(pass: String): BoardingPass {
                val p = pass
                    .replace("[FL]".toRegex(), "0")
                    .replace("[BR]".toRegex(), "1")
                return BoardingPass(
                    p.take(7).toInt(2),
                    p.drop(7).toInt(2),
                )
            }
        }
    }
}
