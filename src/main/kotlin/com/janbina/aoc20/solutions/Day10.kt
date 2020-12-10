package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.max
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(10)
    Day10(input).also {
        printAndTest(it.part1(), 2346)
        printAndTest(it.part2(), 6044831973376)
    }
}

class Day10(lines: List<String>) {

    private val input = lines.map { it.toInt() }.toMutableList().apply {
        add(0)
        add(max() + 3)
    }.sorted()

    fun part1(): Int {
        var df1 = 0
        var df3 = 0
        input.zipWithNext { a, b ->
            if (a + 1 == b) df1++
            if (a + 3 == b) df3++
        }
        return df1 * df3
    }

    fun part2(): Long {
        val sums = LongArray(input.size)
        sums[0] = 1
        for (i in 1..input.lastIndex) {
            val from = (i-3).coerceAtLeast(0)
            (from until i).forEach { j ->
                if (input[i] <= input[j] + 3) {
                    sums[i] += sums[j]
                }
            }
        }
        return sums[input.lastIndex]
    }
}
