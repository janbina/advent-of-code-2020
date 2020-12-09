package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.*

fun main() {
    val input = Input.getDayInputLines(9)
    Day09(input).also {
        printAndTest(it.part1(), 552655238)
        printAndTest(it.part2(552655238), 70672245)
        printAndTest(it.fancyPart2(552655238), 70672245)
    }
}

class Day09(lines: List<String>) {

    private val input = lines.map { it.toLong() }

    fun part1(): Long {
        return input.windowed(size = 26).first {
            it.dropLast(1).uniquePairs().all { p ->
                p.first + p.second != it.last()
            }
        }.last()
    }

    fun part2(requiredSum: Long): Long {
        var sum = 0L
        var i = 0
        var j = 0

        while (i < input.size) {
            sum += input[j]
            j++
            if (sum == requiredSum) {
                return input.subList(i, j).minMax().sum()
            } else if (sum > requiredSum || j >= input.size) {
                i++
                j = i
                sum = 0
            }
        }
        return 0
    }

    // fancy variant of part 2, just for fun
    // its not that readable and does some useless work
    fun fancyPart2(requiredSum: Long): Long {
        input.windowed(size = Int.MAX_VALUE, partialWindows = true).forEach { win ->
            win.runningReduce { a, b -> a + b }.indexOfFirst { it == requiredSum }.let {
                if (it >= 0) return win.take(it + 1).minMax().sum()
            }
        }
        return 0
    }
}
