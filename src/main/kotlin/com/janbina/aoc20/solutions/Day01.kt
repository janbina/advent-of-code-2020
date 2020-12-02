package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import com.janbina.aoc20.utils.uniquePairs
import com.janbina.aoc20.utils.uniqueTriples

fun main() {
    val input = Input.getDayInputLines(1)
    Day01(input).also {
        printAndTest(it.part1(), 955584)
        printAndTest(it.part2(), 287503934)
    }
}

class Day01(inputLines: List<String>) {

    private val inputInts = inputLines.map { it.toInt() }

    fun part1(): Int {
        inputInts.uniquePairs().forEach {
            if (it.first + it.second == 2020) {
                return it.first * it.second
            }
        }
        return 0
    }

    fun part2(): Int {
        inputInts.uniqueTriples().forEach {
            if (it.first + it.second + it.third == 2020) {
                return it.first * it.second * it.third
            }
        }
        return 0
    }
}
