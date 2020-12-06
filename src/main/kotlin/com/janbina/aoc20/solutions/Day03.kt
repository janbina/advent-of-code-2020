package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(3)
    Day03(input).also {
        printAndTest(it.part1(), 218)
        printAndTest(it.part2(), 3847183340)
    }
}

class Day03(
    private val input: List<String>
) {

    private val width = input.first().length
    private val height = input.size

    fun part1(): Long {
        return checkSlope(3, 1)
    }

    fun part2(): Long {
        return listOf(
            1 to 1,
            3 to 1,
            5 to 1,
            7 to 1,
            1 to 2,
        ).map {
            checkSlope(it.first, it.second)
        }.reduce { a, b -> a * b }
    }

    private fun checkSlope(dx: Int, dy: Int): Long {
        var x = 0
        var y = 0
        var trees = 0L

        while (y < height) {
            if (input[y][x] == '#') {
                trees++
            }
            x = (x + dx) % width
            y += dy
        }

        return trees
    }
}
